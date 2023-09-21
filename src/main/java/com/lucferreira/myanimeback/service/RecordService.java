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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final WaybackService waybackService;
    private final MediaService mediaService;
    private final RecordCreationService recordCreationService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5); // Adjust the pool size as needed

    @Autowired
    public RecordService(RecordRepository recordRepository,
            RecordCreationService recordCreationService,
            WaybackService waybackService,
            MediaService mediaService) {
        this.recordRepository = recordRepository;
        this.waybackService = waybackService;
        this.mediaService = mediaService;
        this.recordCreationService = recordCreationService;
    }

    private void createRecordsAsync(Media media, List<ResponseSnapshot> responseSnapshots) {
        responseSnapshots.stream()
                .filter(snapshot -> snapshot.getSnapshotStatus().startsWith("2")
                        || snapshot.getSnapshotStatus().startsWith("3"))
                .map(snapshot -> CompletableFuture.supplyAsync(() -> {
                    try {
                        MediaRecord mediaRecord = recordCreationService.getMediaRecord(snapshot.getUrl());
                        mediaRecord.setMedia(media);
                        saveMediaRecord(mediaRecord);
                        return mediaRecord;
                    } catch (Exception e) {
                        // Handle exceptions within this CompletableFuture
                        e.printStackTrace();
                        return null; // or handle the exception as needed
                    }
                }, executorService))
                .collect(Collectors.toList());

    }

    private List<MediaRecord> createRecordsSync(List<String> urls, Media media) {

        var futures = urls.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> {
                    try {
                        MediaRecord mediaRecord = recordCreationService.getMediaRecord(url);
                        mediaRecord.setMedia(media);
                        saveMediaRecord(mediaRecord);
                        return mediaRecord;
                    } catch (Exception e) {
                        // Handle exceptions within this CompletableFuture
                        e.printStackTrace();
                        return null; // or handle the exception as needed
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

        recordRepository.save(mediaRecord);
    }

    public List<MediaRecord> createMediaRecords(List<String> urls, String malUrl) throws JikanQueryException {

        Media media = mediaService.getFullMediaByUrl(malUrl);

        List<MediaRecord> records = createRecordsSync(urls, media);
        return records;
    }

    public ArchiveResponse createMediaRecords(ArchiveRequest archiveRequest) throws JikanQueryException {
        ArchiveResponse archiveResponse = new ArchiveResponse();
        var malUrl = archiveRequest.getUrl();
        System.out.println(malUrl);
        boolean isNotNewMedia = mediaService.mediaExistByUrl(malUrl);
        archiveResponse.setNewMedia(!isNotNewMedia);

        Media media = mediaService.getFullMediaByUrl(malUrl);
        archiveResponse.setMediaId(media.getId());
        System.out.println(media.getName());
        List<MediaRecord> mediaRecords = recordRepository.findAllByMedia(media);
        List<ResponseSnapshot> responseSnapshots = waybackService.getSnapshotList(media);

        createRecordsAsync(media, responseSnapshots);

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
