package com.lucferreira.myanimeback.repository;

import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;
import com.lucferreira.myanimeback.model.snapshot.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseSnapshotRepository extends JpaRepository<ResponseSnapshot, Long> {
    // Retrieve a record by its ID
    Optional<ResponseSnapshot> findById(Long id);

    ResponseSnapshot findByUrl(String url);

    List<ResponseSnapshot> findAllByMalId(Long malId);

    List<ResponseSnapshot> findByTimestamp(Timestamp timestamp);

    List<ResponseSnapshot> findBySnapshotStatus(String snapshotStatus);

    List<ResponseSnapshot> findByTimestampDateBetween(Date startDate, Date endDate);
    List<ResponseSnapshot> findByAvailable(boolean available);

    List<ResponseSnapshot> findByAvailableAndMediaRecord_Media(boolean available, Media media);

    List<ResponseSnapshot> findAllByAvailableAndMalId(boolean b, Long malId);


}
