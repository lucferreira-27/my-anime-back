package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.model.media.MediaDto;
import com.lucferreira.myanimeback.model.media.MediaForm;
import com.lucferreira.myanimeback.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {
    @Autowired
    private MediaService mediaService;

    @Operation(summary = "List anime/manga by type")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of media",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MediaDto.class)))
    @GetMapping("/{type:animes|mangas}")
    public ResponseEntity<List<MediaDto>> list(
            @Parameter(description = "Type of media: 'animes' or 'mangas'",examples = @ExampleObject(value = "animes"), required = true)
            @PathVariable String type) {
        List<MediaDto> medias = mediaService.listMediaByType(type);
        return ResponseEntity.ok(medias);
    }

    @Operation(summary = "Get anime/manga by ID and type")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved media",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MediaDto.class)))
    @GetMapping("/{type:animes|mangas}/{id}")
    public ResponseEntity<MediaDto> getMedia(
            @Parameter(description = "Type of media: 'animes' or 'mangas'", required = true)
            @PathVariable String type,
            @Parameter(description = "ID of the media", required = true)
            @PathVariable Long id) {
        MediaDto media = mediaService.getMedia(type, id);
        return ResponseEntity.ok(media);
    }

    @Operation(summary = "Update anime/manga by ID and type")
    @ApiResponse(responseCode = "200", description = "Successfully updated media",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MediaDto.class)))
    @PutMapping("/{type:animes|mangas}/{id}")
    public ResponseEntity<MediaDto> updateMedia(
            @Parameter(description = "Type of media: 'animes' or 'mangas'", required = true)
            @PathVariable String type,
            @Parameter(description = "ID of the media", required = true)
            @PathVariable Long id,
            @RequestBody MediaForm mediaForm) {
        MediaDto media = mediaService.updateMedia(mediaForm, id);
        return ResponseEntity.ok(media);
    }

    @Operation(summary = "Delete anime/manga by ID and type")
    @ApiResponse(responseCode = "200", description = "Successfully deleted media",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MediaDto.class)))
    @DeleteMapping("/{type:animes|mangas}/{id}")
    public ResponseEntity<MediaDto> deleteMedia(
            @Parameter(description = "Type of media: 'animes' or 'mangas'", required = true)
            @PathVariable String type,
            @Parameter(description = "ID of the media", required = true)
            @PathVariable Long id) {
        MediaDto media = mediaService.getMedia(type, id);
        mediaService.deleteMedia(type, id);
        return ResponseEntity.ok(media);
    }

    @Operation(summary = "Create anime/manga")
    @ApiResponse(responseCode = "201", description = "Successfully created media",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MediaDto.class)))
    @PostMapping("/{type:animes|mangas}")
    public ResponseEntity<MediaDto> createMedia(
            @Parameter(description = "Type of media: 'animes' or 'mangas'", required = true)
            @PathVariable String type,
            @RequestBody MediaForm mediaForm) throws URISyntaxException {
        MediaDto mediaDto = mediaService.createMedia(mediaForm);
        var createdUri = new URI(String.format("/%s/", type) + mediaDto.getId());
        return ResponseEntity.created(createdUri).body(mediaDto);
    }
}
