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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wayback/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @Operation(summary = "Post archive  urls of media to get the records")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved media records",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Record.class)))
    @PostMapping("/media")
    public ResponseEntity<List<Record>> getMediaRecords(
            @Parameter(description = "List of URLs of media records to retrieve \n" +
                    "format: [https://web.archive.org/web/{timestamp}*/{myanime_url}, ...]",
                    example = "[\n" +
                            "  \"https://web.archive.org/web/20200407163618/https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood\",\n" +
                            "  \"https://web.archive.org/web/20200407163618/https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood\",\n" +
                            "  \"https://web.archive.org/web/20200407163618/https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood\"\n" +
                            "]",
                    required = true,
                    schema = @Schema(implementation = List.class, defaultValue = "[\"https://web.archive.org/web/20200407163618/https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood\"]"))
            @RequestBody List<String> urls) {
        List<Record> records = recordService.getMediaRecords(urls);
        return ResponseEntity.ok(records);
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
