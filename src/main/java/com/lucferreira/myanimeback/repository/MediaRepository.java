package com.lucferreira.myanimeback.repository;

import com.lucferreira.myanimeback.model.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    // Retrieve a media by its type and ID

    List<Media> findByUpdated(boolean updated);

    Optional<Media> findByTypeAndId(String type, Long id);

    List<Media> findAllByType(String type);

    Optional<Media> findByMyanimelistUrl(String myanimelistUrl);
}
