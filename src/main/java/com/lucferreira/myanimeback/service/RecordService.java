package com.lucferreira.myanimeback.service;

import com.lucferreira.myanimeback.model.ArchiveRequest;
import com.lucferreira.myanimeback.model.ArchiveResponse;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.record.MediaRecord;
import com.lucferreira.myanimeback.model.record.TopListRecord;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;
import com.lucferreira.myanimeback.repository.MediaRepository;
import com.lucferreira.myanimeback.repository.RecordRepository;
import com.lucferreira.myanimeback.repository.ResponseSnapshotRepository;
import net.sandrohc.jikan.exception.JikanQueryException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final WaybackService waybackService;
    private final MediaRepository mediaRepository;
    private final ResponseSnapshotRepository responseSnapshotRepository;
    private final MediaService mediaService;
    private final RecordCreationService recordCreationService;
    private final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    private static final Logger logger = LoggerFactory.getLogger(RecordService.class);
    private final Set<String> mediaBeingProcessed = ConcurrentHashMap.newKeySet(); // Add this line

    @Autowired
    public RecordService(RecordRepository recordRepository,
            RecordCreationService recordCreationService,
            WaybackService waybackService,
            MediaService mediaService,
            ResponseSnapshotRepository responseSnapshotRepository,
            MediaRepository mediaRepository) {
        this.recordRepository = recordRepository;
        this.waybackService = waybackService;
        this.mediaService = mediaService;
        this.recordCreationService = recordCreationService;
        this.responseSnapshotRepository = responseSnapshotRepository;
        this.mediaRepository = mediaRepository;
    }

    private MediaRecord getOrCreateMediaRecord(Media media, ResponseSnapshot snapshot) throws JikanQueryException {
        logger.debug("Checking if record already exists for media: {}", media.getName());
        MediaRecord record = null;
        synchronized (recordRepository) {
            Optional<MediaRecord> foundRecord = recordRepository.findByArchiveUrl(snapshot.getUrl());
            if (foundRecord.isPresent()) {
                record = foundRecord.get();
                if (record.getArchiveDate() != null) {
                    logger.debug("Record already exists for media: {}", media.getName());
                    return foundRecord.get();
                }

            }
        }
        logger.debug("Creating new record for media: {}", media.getName());
        MediaRecord mediaRecord = recordCreationService.getMediaRecord(snapshot.getUrl());
        if (record != null) {
            mediaRecord.setId(record.getId());
        }
        responseSnapshotRepository.save(snapshot); // Save the snapshot first
        mediaRecord.setMedia(media);
        mediaRecord.setResponseSnapshot(snapshot);
        snapshot.setMediaRecord(mediaRecord);
        snapshot.setAvailable(true);
        snapshot.setNumberOfRequests(snapshot.getNumberOfRequests() + 1);
        saveMediaRecord(mediaRecord);

        return mediaRecord;
    }

    private void createRecordsAsync(Media media, List<ResponseSnapshot> responseSnapshots) {
        String mediaName = media.getName();
        if (executorService.getActiveCount() == 0) {
            mediaBeingProcessed.clear();
        }
        if (!mediaBeingProcessed.add(mediaName)) {
            logger.info("Skipping record creation for media: {} as it is already being processed THREADS: [{}]",
                    mediaName, executorService.getActiveCount());
            return;
        }
        logger.info("Starting async record creation for media: {} found {} snapshots", media.getName(),
                responseSnapshots.size());
        media.setBusy(true);
        mediaRepository.save(media);

        List<CompletableFuture<MediaRecord>> futures = responseSnapshots.stream()
                .filter(snapshot -> snapshot.getSnapshotStatus().startsWith("2")
                        || snapshot.getSnapshotStatus().startsWith("3"))
                .map(snapshot -> CompletableFuture.supplyAsync(() -> {
                    logger.info("Create Media Record");

                    try {
                        return getOrCreateMediaRecord(media, snapshot);
                    } catch (Exception e) {

                        MediaRecord mediaRecord = new MediaRecord();
                        mediaRecord.setMedia(media);
                        mediaRecord.setArchiveUrl(snapshot.getUrl());
                        mediaRecord.setResponseSnapshot(snapshot);
                        snapshot.setMediaRecord(mediaRecord);
                        snapshot.setAvailable(false);
                        snapshot.setNumberOfRequests(snapshot.getNumberOfRequests() + 1);
                        saveMediaRecord(mediaRecord);
                        logger.error(
                                "Exception occurred while creating MediaRecord for Media '{}' using Snapshot URL '{}': {}",
                                media.getName(), snapshot.getUrl(), e.getMessage());
                        return null; // or handle the exception as needed
                    } finally {
                        updateSnapshot(snapshot);
                    }
                }, executorService))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    logger.info("Complete Future");
                    media.setBusy(false);
                    mediaBeingProcessed.remove(mediaName);
                });

    }

    @Scheduled(fixedRate = 300000) // Runs every 5 minutes (300,000 milliseconds)
    public void checkForErrors() {
        List<Media> medias = mediaRepository.findByUpdated(false);
        for (Media media : medias) {
            var mediaName = media.getName();
            if (!mediaBeingProcessed.add(mediaName)) {
                continue;
            }
            List<ResponseSnapshot> responseSnapshotsAvList = responseSnapshotRepository
                    .findByAvailableAndMediaRecord_Media(false, media);
            logger.info("Handling snapshot errors {} for media: {}", responseSnapshotsAvList.size(),
                    media.getName());
            var filterdResponseSnapshotsAvList = responseSnapshotsAvList.stream()
                    .filter(snapshot -> (snapshot.getSnapshotStatus().startsWith("2")
                            || snapshot.getSnapshotStatus().startsWith("3")) && snapshot.getNumberOfRequests() < 3)
                    .collect(Collectors.toList());
            createRecordsAsync(media, filterdResponseSnapshotsAvList);
        }

    }

    private void updateSnapshot(ResponseSnapshot responseSnapshot) {
        logger.debug("Updating snapshot: {}", responseSnapshot.getUrl());
        responseSnapshotRepository.save(responseSnapshot);
    }


    private List<MediaRecord> createRecordsSync(List<String> urls, Media media) {
        logger.info("Starting sync record creation for media: {}", media.getName());

        var futures = urls.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Optional<MediaRecord> foundRecord = recordRepository.findByArchiveUrl(url);
                        if (foundRecord.isPresent()) {
                            return foundRecord.get();
                        }
                        MediaRecord mediaRecord = recordCreationService.getMediaRecord(url);
                        mediaRecord.setMedia(media);
                        saveMediaRecord(mediaRecord);
                        return mediaRecord;
                    } catch (Exception e) {
                        logger.error("Exception occurred in CompletableFuture: {}", e.getMessage());
                        return null;
                    }
                }, executorService))
                .collect(Collectors.toList());

        List<MediaRecord> records = futures.stream()
                .map(CompletableFuture::join)
                .filter(record -> record != null) // Filter out failed tasks
                .collect(Collectors.toList());

        return records;

    }

    public List<MediaRecord> listRecords() {
        return recordRepository.findAll();
    }

    private void saveMediaRecord(MediaRecord mediaRecord) {
        logger.debug("Saving media record: {}", mediaRecord.getArchiveUrl());
        recordRepository.save(mediaRecord);
    }

    public List<MediaRecord> createMediaRecords(List<String> urls, String malUrl) throws JikanQueryException {

        Media media = mediaService.getFullMediaByUrl(malUrl);

        List<MediaRecord> records = createRecordsSync(urls, media);

        createMediaRecords(new ArchiveRequest(malUrl));
        return records;
    }

    private List<ResponseSnapshot> filterNotExistingArchiveUrls(List<ResponseSnapshot> responseSnapshots) {
        return responseSnapshots.stream()
                .filter(snapshot -> {
                    var status = snapshot.getSnapshotStatus().startsWith("2")
                            || snapshot.getSnapshotStatus().startsWith("3");
                    var notExists = recordRepository.existsByArchiveUrl(snapshot.getUrl());
                    var isAboveLimitOfTotalRequests = snapshot.getNumberOfRequests() > 3;
                    return status && !notExists && !isAboveLimitOfTotalRequests;
                })
                .collect(Collectors.toList());
    }

    public ArchiveResponse createMediaRecords(ArchiveRequest archiveRequest) throws JikanQueryException {
        ArchiveResponse archiveResponse = new ArchiveResponse();
        var malUrl = archiveRequest.getUrl();
        boolean isNotNewMedia = mediaService.mediaExistByUrl(malUrl);
        archiveResponse.setNewMedia(!isNotNewMedia);

        Media media = mediaService.getFullMediaByUrl(malUrl);
        archiveResponse.setMediaId(media.getId());
        List<MediaRecord> mediaRecords = recordRepository.findAllByMediaAndResponseSnapshot_Available(media,true);
        List<ResponseSnapshot> responseSnapshots = waybackService.getSnapshotList(media);
        List<ResponseSnapshot> filteredResponseSnapshots = filterNotExistingArchiveUrls(responseSnapshots);
        createRecordsAsync(media, filteredResponseSnapshots);

        archiveResponse.setFirst(responseSnapshots.get(0));
        archiveResponse.setLast(responseSnapshots.get(responseSnapshots.size() - 1));
        archiveResponse.setTotal(responseSnapshots.size());
        archiveResponse.setComplete(responseSnapshots.size() == mediaRecords.size());
        archiveResponse.setTotalAvailable(mediaRecords.size());
        List<ResponseSnapshot> unavailableSnapshtos = responseSnapshotRepository
                .findByAvailableAndMediaRecord_Media(false, media)
                .stream()
                .filter(snapshot -> snapshot.getNumberOfRequests() >= 3)
                .collect(Collectors.toList());
        archiveResponse.setTotalUnavailable(unavailableSnapshtos.size());
        archiveResponse.setTotalUnavailable(unavailableSnapshtos.size());

        return archiveResponse;
    }

    public List<TopListRecord> createTopListRecords(String archiveUrl) {
        return List.of(recordCreationService.getTopListRecord(archiveUrl));
    }
}
