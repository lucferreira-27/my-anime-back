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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final WaybackService waybackService;
    private final ResponseSnapshotRepository responseSnapshotRepository;
    private final MediaService mediaService;
    private final RecordCreationService recordCreationService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5); // Adjust the pool size as needed
    private static final Logger logger = LoggerFactory.getLogger(RecordService.class);

    @Autowired
    public RecordService(RecordRepository recordRepository,
            RecordCreationService recordCreationService,
            WaybackService waybackService,
            MediaService mediaService,
            ResponseSnapshotRepository responseSnapshotRepository) {
        this.recordRepository = recordRepository;
        this.waybackService = waybackService;
        this.mediaService = mediaService;
        this.recordCreationService = recordCreationService;
        this.responseSnapshotRepository = responseSnapshotRepository;
    }

    private MediaRecord getOrCreateMediaRecord(Media media, ResponseSnapshot snapshot) throws JikanQueryException {
        logger.debug("Checking if record already exists for media: {}", media.getName());
        synchronized (recordRepository) {
            Optional<MediaRecord> foundRecord = recordRepository.findByArchiveUrl(snapshot.getUrl());
            if (foundRecord.isPresent()) {
                logger.debug("Record already exists for media: {}", media.getName());
                return foundRecord.get();
            }
        }
        logger.debug("Creating new record for media: {}", media.getName());
        MediaRecord mediaRecord = recordCreationService.getMediaRecord(snapshot.getUrl());
        mediaRecord.setMedia(media);
        saveMediaRecord(mediaRecord);
        snapshot.setAvailable(true);
        snapshot.setNumberOfRequests(snapshot.getNumberOfRequests() + 1);
        return mediaRecord;
    }

    private void createRecordsAsync(Media media, List<ResponseSnapshot> responseSnapshots) {

        logger.info("Starting async record creation for media: {} found {} snapshots", media.getName(),
                responseSnapshots.size());

        List<CompletableFuture<MediaRecord>> futures = responseSnapshots.stream()
                .filter(snapshot -> snapshot.getSnapshotStatus().startsWith("2")
                        || snapshot.getSnapshotStatus().startsWith("3"))
                .map(snapshot -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return getOrCreateMediaRecord(media, snapshot);
                    } catch (Exception e) {
                        snapshot.setAvailable(false);
                        snapshot.setNumberOfRequests(snapshot.getNumberOfRequests() + 1);
                        logger.error(
                                "Exception occurred while creating MediaRecord for Media '{}' using Snapshot URL '{}': {}",
                                media.getName(), snapshot.getUrl(), e.getMessage());
                        return null; // or handle the exception as needed
                    } finally {
                        updateSnapshot(snapshot);
                    }
                }, executorService))
                .collect(Collectors.toList());
        /*
         * CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
         * .thenRun(() -> handleSnapshotsErrors(media));
         */

    }

    private void updateSnapshot(ResponseSnapshot responseSnapshot) {
        logger.debug("Updating snapshot: {}", responseSnapshot.getUrl());
        responseSnapshotRepository.save(responseSnapshot);
    }

    private void handleSnapshotsErrors(Media media) {
        List<ResponseSnapshot> responseSnapshots = responseSnapshotRepository.findByAvailableFalse();
        if (responseSnapshots.size() > 0) {
            logger.warn("Handling snapshot errors {} for media: {}", responseSnapshots.size(), media.getName());
            createRecordsAsync(media, responseSnapshots);
        }
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
                    return status && !notExists;
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
        List<MediaRecord> mediaRecords = recordRepository.findAllByMedia(media);
        List<ResponseSnapshot> responseSnapshots = waybackService.getSnapshotList(media);
        List<ResponseSnapshot> filteredResponseSnapshots = filterNotExistingArchiveUrls(responseSnapshots);
        createRecordsAsync(media, filteredResponseSnapshots);

        archiveResponse.setFirst(responseSnapshots.get(0));
        archiveResponse.setLast(responseSnapshots.get(responseSnapshots.size() - 1));
        archiveResponse.setTotal(responseSnapshots.size());
        archiveResponse.setComplete(responseSnapshots.size() == mediaRecords.size());
        archiveResponse.setTotalAvailable(mediaRecords.size());

        return archiveResponse;
    }

    public List<TopListRecord> createTopListRecords(String archiveUrl) {
        return List.of(recordCreationService.getTopListRecord(archiveUrl));
    }
}
