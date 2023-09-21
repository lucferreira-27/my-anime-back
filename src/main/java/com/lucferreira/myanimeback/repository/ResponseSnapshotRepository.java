package com.lucferreira.myanimeback.repository;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;
import com.lucferreira.myanimeback.model.snapshot.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseSnapshotRepository extends JpaRepository<ResponseSnapshot, Long> {
    // Retrieve a record by its ID
    Optional<ResponseSnapshot> findById(Long id);
    List<ResponseSnapshot> findByUrl(String url);
    List<ResponseSnapshot> findAllById(Long malId);
    List<ResponseSnapshot> findByTimestamp(Timestamp timestamp);
    List<ResponseSnapshot> findBySnapshotStatus(String snapshotStatus);
    List<ResponseSnapshot> findByTimestampDateBetween(Date startDate, Date endDate);
    
    
}
