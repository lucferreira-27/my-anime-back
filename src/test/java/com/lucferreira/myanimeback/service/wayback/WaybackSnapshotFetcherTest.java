package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.exception.WaybackUnavailableException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WaybackMachineClientTest {
    private RestTemplate restTemplate;
    private WaybackMachineClient waybackMachineClient;
    private WaybackSnapshotFetcher snapshotFetcherSpy;
    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        snapshotFetcherSpy = spy(new WaybackSnapshotFetcher(restTemplate));
        waybackMachineClient = new WaybackMachineClient(restTemplate, snapshotFetcherSpy);
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
    void getSnapshotList_ReturnsSnapshots_WhenSnapshotsFound() throws WaybackException {
        // Arrange
        String url = "https://example.com";
        WaybackMachineClient waybackMachineClientSpy = spy(waybackMachineClient);
        doReturn(Optional.of(createSampleResponseSnapshots())).when(snapshotFetcherSpy).getTimeMap(anyString());

        // Act
        List<ResponseSnapshot> result = waybackMachineClientSpy.getSnapshotList(url);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getSnapshotList_ThrowsWaybackException_WhenNoSnapshotsFound() {
        // Arrange
        String url = "https://example.com";
        WaybackMachineClient waybackMachineClientSpy = spy(waybackMachineClient);
        doReturn(Optional.empty()).when(snapshotFetcherSpy).getTimeMap(anyString());

        // Act and Assert
        assertThrows(WaybackException.class, () -> waybackMachineClientSpy.getSnapshotList(url));
    }

    @ParameterizedTest
    @CsvSource({
            "20220101, 20220401, 2",
            "20220101, 20220301, 1"
    })
    void getSnapshotsInRange_ReturnsFilteredSnapshots(String beginTimestamp, String endTimestamp, int expectedSize) throws WaybackException {
        // Arrange
        String url = "https://example.com";
        WaybackMachineClient waybackMachineClientSpy = spy(waybackMachineClient);
        doReturn(Optional.of(createSampleResponseSnapshots())).when(snapshotFetcherSpy).getTimeMap(anyString());

        // Act
        List<ResponseSnapshot> result = waybackMachineClientSpy.getSnapshotsInRange(url, beginTimestamp, Optional.of(endTimestamp));

        // Assert
        assertEquals(expectedSize, result.size());
    }


    @Test
    void getSnapshotsInRange_ThrowsWaybackException_WhenNoSnapshotsFound() {
        // Arrange
        String url = "https://example.com";
        WaybackMachineClient waybackMachineClientSpy = spy(waybackMachineClient);
        doReturn(Optional.empty()).when(snapshotFetcherSpy).getTimeMap(anyString());

        // Act and Assert
        assertThrows(WaybackException.class, () -> waybackMachineClientSpy.getSnapshotsInRange(url, "20220101", Optional.of("20220401")));
    }


    // Helper methods
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

    private List<ResponseSnapshot> createSampleResponseSnapshots() {
        List<ResponseSnapshot> responseSnapshots = new ArrayList<>();
        responseSnapshots.add(new ResponseSnapshot("https://web.archive.org/web/20220115/https://example.com", "20220115000000", "200"));
        responseSnapshots.add(new ResponseSnapshot("https://web.archive.org/web/20220315/https://example.com", "20220315000000", "200"));
        return responseSnapshots;
    }
}