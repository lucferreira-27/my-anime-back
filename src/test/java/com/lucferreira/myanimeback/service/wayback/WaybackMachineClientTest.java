package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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
    void getSnapshotList_ReturnsSnapshots_WhenSnapshotsFound() throws WaybackException {
        // Arrange
        String url = "https://example.com";
        WaybackMachineClient waybackMachineClientSpy = spy(waybackMachineClient);
        doReturn(Optional.of(createSampleResponseSnapshots())).when(snapshotFetcherSpy).getTimeMap(anyString(),1L);

        // Act
        List<ResponseSnapshot> result = waybackMachineClientSpy.getSnapshotList(new Media());

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getSnapshotList_ThrowsWaybackException_WhenNoSnapshotsFound() {
        // Arrange
        String url = "https://example.com";
        WaybackMachineClient waybackMachineClientSpy = spy(waybackMachineClient);
        doReturn(Optional.empty()).when(snapshotFetcherSpy).getTimeMap(anyString(), 1L);

        // Act and Assert
        assertThrows(WaybackException.class, () -> waybackMachineClientSpy.getSnapshotList(new Media()));
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
        doReturn(Optional.of(createSampleResponseSnapshots())).when(snapshotFetcherSpy).getTimeMap(anyString(),1L);

        // Act
        List<ResponseSnapshot> result = waybackMachineClientSpy.getSnapshotsInRange(new Media(), beginTimestamp, Optional.of(endTimestamp));

        // Assert
        assertEquals(expectedSize, result.size());
    }


    @Test
    void getSnapshotsInRange_ThrowsWaybackException_WhenNoSnapshotsFound() {
        // Arrange
        String url = "https://example.com";
        WaybackMachineClient waybackMachineClientSpy = spy(waybackMachineClient);
        doReturn(Optional.empty()).when(snapshotFetcherSpy).getTimeMap(anyString(),1L);

        // Act and Assert
        assertThrows(WaybackException.class, () -> waybackMachineClientSpy.getSnapshotsInRange(new Media(), "20220101", Optional.of("20220401")));
    }
    @ParameterizedTest
    @CsvSource({
            "19700101, 19720101",
    })
    void getSnapshotsInRange_ThrowsWaybackException_WhenFilteredSnapshotsEmpty(String beginTimestamp, String endTimestamp) throws WaybackException {
        // Arrange
        String url = "https://example.com";
        WaybackMachineClient waybackMachineClientSpy = spy(waybackMachineClient);
        doReturn(Optional.of(createSampleResponseSnapshots())).when(snapshotFetcherSpy).getTimeMap(anyString(),1L);
        // Act and Assert
        WaybackException exception = assertThrows(WaybackException.class, () -> waybackMachineClientSpy.getSnapshotsInRange(new Media(), beginTimestamp, Optional.of(endTimestamp)));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("No snapshots found for the URL within the specified time range: " + url));
    }



    private List<ResponseSnapshot> createSampleResponseSnapshots() {
        List<ResponseSnapshot> responseSnapshots = new ArrayList<>();
        responseSnapshots.add(new ResponseSnapshot("https://web.archive.org/web/20220115/https://example.com", "20220115000000", "200",1L));
        responseSnapshots.add(new ResponseSnapshot("https://web.archive.org/web/20220315/https://example.com", "20220315000000", "200",1L));
        return responseSnapshots;
    }
}