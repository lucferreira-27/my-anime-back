package com.lucferreira.myanimeback.repository;

import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.record.MediaRecord;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<MediaRecord, Long> {
    // Retrieve a record by its ID
    Optional<MediaRecord> findById(Long id);

    // Retrieve a record by its media
    Optional<MediaRecord> findByMedia(Media media);

    // Retrieve a record by its archive URL
    Optional<MediaRecord> findByArchiveUrl(String archiveUrl);

    // Retrieve a record by its archive date
    Optional<MediaRecord> findByArchiveDate(Date archiveDate);

    List<MediaRecord> findAllByMedia(Media media);

    boolean existsByArchiveUrl(String url);

    

}
