package com.lucferreira.myanimeback.repository;
import com.lucferreira.myanimeback.model.snapshot.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimestampRepository extends JpaRepository<Timestamp, Long> {
        
}