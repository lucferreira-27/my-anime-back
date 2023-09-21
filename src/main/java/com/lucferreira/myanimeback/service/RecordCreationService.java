package com.lucferreira.myanimeback.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lucferreira.myanimeback.exception.RecordException;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.record.MediaRecord;
import com.lucferreira.myanimeback.model.record.TopListRecord;
import com.lucferreira.myanimeback.repository.RecordRepository;
import com.lucferreira.myanimeback.util.Regex;

import net.sandrohc.jikan.exception.JikanQueryException;

@Service
public class RecordCreationService {
    private final RecordRepository recordRepository;
    private final ScrapeService scrapeService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5); // Adjust the pool size as needed

    @Autowired
    public RecordCreationService(RecordRepository recordRepository,
            ScrapeService scrapeService) {
        this.recordRepository = recordRepository;
        this.scrapeService = scrapeService;
    }

    public MediaRecord getMediaRecord(String url) throws JikanQueryException {

        final String pattern = "^https:\\/\\/web\\.archive\\.org\\/web\\/" +
                "(?<date>\\d{14})\\/(https?:\\/\\/)?myanimelist\\.net(:80)?\\/" +
                "(?<category>anime|manga)\\/" +
                "(?<id>\\d+)(?:\\/" +
                "(?<title>[\\w-]+))?";
        if (Regex.match(url, pattern) == null) {
            final String errorMessage = "Invalid URL. Please provide a valid Archive MyAnimeList URL in the following format: "
                    +
                    "https://web.archive.org/web/{timestamp}*/https://myanimelist.net/{category}/{id}/{title (optional)}";
            throw new RecordException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        Optional<MediaRecord> foundRecord = recordRepository.findByArchiveUrl(url);
        if (foundRecord.isPresent()) {
            return foundRecord.get();
        }
        MediaRecord record = scrapeService.getMediaStatistics(url);
        return record;
    }

    public List<MediaRecord> createMediaRecords(List<String> urls, Media media) {

        List<CompletableFuture<MediaRecord>> futures = urls.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return getMediaRecord(url);
                    } catch (Exception e) {
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

    public TopListRecord getTopListRecord(String url) {

        final String pattern = "https:\\/\\/web\\.archive\\.org\\/web\\/(?<date>\\d{14})\\/(https?:\\/\\/)?(www\\.)?myanimelist\\.net\\/(?<category>topanime.php|topmanga.php)";
        if (Regex.match(url, pattern) == null) {
            final String errorMessage = "Invalid URL. Please provide a valid Archive MyAnimeList URL in the following format: "
                    +
                    "https://web.archive.org/web/{timestamp}*/https://myanimelist.net/{topanime|topmanga}";
            throw new RecordException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        TopListRecord record = scrapeService.getTopListStatistics(url);
        return record;
    }
}
