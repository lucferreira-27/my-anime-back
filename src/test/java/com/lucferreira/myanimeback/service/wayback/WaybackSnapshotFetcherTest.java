package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.exception.WaybackUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WaybackSnapshotFetcherTest {
    private RestTemplate restTemplate;
    private WaybackSnapshotFetcher snapshotFetcherSpy;
    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        snapshotFetcherSpy = spy(new WaybackSnapshotFetcher(restTemplate));
    }

    @Test
    void getTimeMap_ReturnsOptionalEmpty_WhenNoSnapshotsFound() throws WaybackException {
        // Arrange
        String url = "https://example.com";
        String endpoint = "https://web.archive.org/web/timemap/json?url=" + url;

        when(restTemplate.getForEntity(endpoint, ArrayList.class)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // Act and Assert
        assertThrows(WaybackUnavailableException.class, () -> snapshotFetcherSpy.getTimeMap(url));
    }

    @Test
    void getTimeMap_ReturnsOptionalWithSnapshots_WhenSnapshotsFound() throws WaybackException {
        // Arrange
        String url = "https://example.com";
        String endpoint = "https://web.archive.org/web/timemap/json?url=" + url;
        ArrayList<ArrayList<String>> responseBody = createSampleResponseBody();
        when(restTemplate.getForEntity(endpoint, ArrayList.class)).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        // Act
        Optional<List<ResponseSnapshot>> result = snapshotFetcherSpy.getTimeMap(url);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }
    @Test
    void getTimeMap_ReturnsClosestSnapshot_WhenTargetTimestampProvided() throws WaybackException {
        // Arrange
        String url = "https://example.com";
        String targetTimestamp = "20220215000000";
        doReturn(Optional.of(createSampleResponseSnapshots())).when(snapshotFetcherSpy).getTimeMap(url);

        // Act
        Optional<ResponseSnapshot> result = snapshotFetcherSpy.getTimeMap(url, targetTimestamp);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("20220115000000", result.get().getTimestamp().getOriginalValue());
    }

    // Helper methods
    private List<ResponseSnapshot> createSampleResponseSnapshots() {
        List<ResponseSnapshot> responseSnapshots = new ArrayList<>();
        responseSnapshots.add(new ResponseSnapshot("https://web.archive.org/web/20220115/https://example.com", "20220115000000", "200"));
        responseSnapshots.add(new ResponseSnapshot("https://web.archive.org/web/20220315/https://example.com", "20220415000000", "200"));
        return responseSnapshots;
    }
    private ArrayList<ArrayList<String>> createSampleResponseBody() {
        ArrayList<ArrayList<String>> body = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>(Arrays.asList("timestamp", "original", "statuscode"));
        ArrayList<String> snapshot1 = new ArrayList<>(Arrays.asList("20220115", "https://example.com", "200"));
        ArrayList<String> snapshot2 = new ArrayList<>(Arrays.asList("20220315", "https://example.com", "200"));
        body.add(keys);
        body.add(snapshot1);
        body.add(snapshot2);
        return body;
    }

}