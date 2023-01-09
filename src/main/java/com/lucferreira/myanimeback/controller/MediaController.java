package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.model.media.MediaDto;
import com.lucferreira.myanimeback.model.media.MediaForm;
import com.lucferreira.myanimeback.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class MediaController {
    @Autowired
    private MediaService mediaService;

    @GetMapping("/{type:animes|mangas}")
    public ResponseEntity<List<MediaDto>> list(@PathVariable String type) {
        List<MediaDto> medias = mediaService.listMediaByType(type);
        return ResponseEntity.ok(medias);
    }

    @GetMapping("/{type:animes|mangas}/{id}")
    public ResponseEntity<MediaDto> getMedia(@PathVariable String type, @PathVariable Long id) {
        MediaDto media = mediaService.getMedia(type, id);
        return ResponseEntity.ok(media);
    }

    @PutMapping("/{type:animes|mangas}/{id}")
    public ResponseEntity<MediaDto> updateMedia(@PathVariable String type,
                                                @PathVariable Long id,
                                                @RequestBody MediaForm mediaForm) {
        MediaDto media = mediaService.updateMedia(mediaForm,id);
        return ResponseEntity.ok(media);
    }

    @DeleteMapping("/{type:animes|mangas}/{id}")
    public ResponseEntity<MediaDto> deleteMedia(@PathVariable String type,
                                                @PathVariable Long id) {
        MediaDto media = mediaService.getMedia(type, id);
        mediaService.deleteMedia(type, id);
        return ResponseEntity.ok(media);
    }

    @PostMapping("/{type:animes|mangas}")
    public ResponseEntity<MediaDto> createMedia(@PathVariable String type,@RequestBody MediaForm mediaForm) throws URISyntaxException {
        MediaDto mediaDto = mediaService.createMedia(mediaForm);
        var createdUri = new URI(String.format("/%s/",type) + mediaDto.getId());
        return ResponseEntity.created(createdUri).body(mediaDto);
    }
}
