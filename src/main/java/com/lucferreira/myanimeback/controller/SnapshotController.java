package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;
import com.lucferreira.myanimeback.service.MediaService;
import com.lucferreira.myanimeback.service.WaybackService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.sandrohc.jikan.exception.JikanQueryException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wayback/snapshot")
public class SnapshotController {

    @Autowired
    private WaybackService waybackService;
    @Autowired
    private MediaService mediaService;

    @Operation(summary = "Get snapshots from Wayback Machine")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved snapshots", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseSnapshot.class)))
    @GetMapping("")
    public ResponseEntity<List<ResponseSnapshot>> getSnapshot(
            @Parameter(description = "URL of the snapshot to retrieve", example = "https://myanimelist.net/anime/21/One_Piece", required = true) @RequestParam(value = "url", required = true) String url,
            @Parameter(description = "Optional timestamp for filtering snapshots in the format 'YYYYMMdd,YYYYMMdd'", example = "202006,2023") @RequestParam(value = "timestamp") Optional<String> timestamp)
            throws WaybackException, JikanQueryException {
        Media media = mediaService.getFullMediaByUrl(url);
        if (timestamp.isPresent()) {
            String strTimestamp = timestamp.get();
            List<ResponseSnapshot> responseSnapshots = waybackService.getSnapshotByTimestamp(media, strTimestamp);
            return ResponseEntity.ok(responseSnapshots);
        }

        List<ResponseSnapshot> responseSnapshots = waybackService.getSnapshotList(media);
        return ResponseEntity.ok(responseSnapshots);
    }
}
