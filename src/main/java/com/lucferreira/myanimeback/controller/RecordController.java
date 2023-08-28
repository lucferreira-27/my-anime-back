package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.model.TopList;
import com.lucferreira.myanimeback.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wayback/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @Operation(summary = "Get a media record by its URL")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved media record",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Record.class)))
    @GetMapping("/media")
    public ResponseEntity<Record> getMediaRecord(
            @Parameter(description = "URL of the media record to retrieve \n" +
                    "format: https://web.archive.org/web/{timestamp}*/{myanime_url}",
                    example = "https://web.archive.org/web/20200407163618/https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood",
                    required = true)
            @RequestParam(value = "url", required = true) String url) {
        Record record = recordService.getMediaRecord(url);
        return ResponseEntity.ok(record);
    }

    @Operation(summary = "Get a top list record by its URL")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved top list record",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TopList.class)))
    @GetMapping("/toplist")
    public ResponseEntity<TopList> getTopListRecord(
            @Parameter(description = "URL of the top list record to retrieve \n" +
                    "format: https://web.archive.org/web/{timestamp}*/https://myanimelist.net/{topanime|topmanga}",
                    example = "https://web.archive.org/web/20200407163618/https://myanimelist.net/topanime.php",
                    required = true)
            @RequestParam(value = "url", required = true) String url) {
        TopList record = recordService.getTopListRecord(url);
        return ResponseEntity.ok(record);
    }
}
