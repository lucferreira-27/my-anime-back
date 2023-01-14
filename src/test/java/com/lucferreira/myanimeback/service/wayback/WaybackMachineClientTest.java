package com.lucferreira.myanimeback.service.wayback;


import com.lucferreira.myanimeback.exception.WaybackException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WaybackMachineClientTest {

        @Mock
        private  RestTemplate restTemplate;


        @Test
        public void test() throws IOException {

        }

        @Test
        void getSnapshotsByYear() {
        }

        @Test
        void getSnapshotFirst_WaybackResponseContainsValidSnapshotWithTimestampReset_ValidResponseSnapshotReturned() throws WaybackException {
                Timestamp expectTimestamp = new Timestamp("20020120");
                String expectTimestampValue = expectTimestamp.getOriginalValue();
                String targetUrl = "http://example.com";
                String expectedUrl = "http://web.archive.org/web/20020120/http://example.com";
                String expectedEndPoint =  "http://archive.org/wayback/available?url=" + targetUrl + "&timestamp=0";
                WaybackResponse waybackResponse = new WaybackResponse();
                ArchivedSnapshots archivedSnapshots = new ArchivedSnapshots();
                Closest closest = new Closest();
                closest.setAvailable(true);
                closest.setTimestamp(expectTimestampValue);
                closest.setUrl(expectedUrl);
                archivedSnapshots.setClosest(closest);
                waybackResponse.setArchivedSnapshots(archivedSnapshots);

                when(restTemplate.getForEntity(expectedEndPoint, WaybackResponse.class)).thenReturn(new ResponseEntity(waybackResponse, HttpStatus.OK));

                WaybackMachineClient waybackMachineClient = new WaybackMachineClient(restTemplate);
                ResponseSnapshot responseSnapshot = waybackMachineClient.getFirstSnapshot(targetUrl);

                assertEquals(responseSnapshot.getTimestamp(),expectTimestamp);
                assertEquals(responseSnapshot.getUrl(),expectedUrl);
        }

        @SuppressWarnings("unchecked")
        @Test
        void getSnapshotByYear_WaybackResponseContainsValidSnapshot_ValidResponseSnapshotReturned() throws WaybackException {
                Timestamp expectTimestamp = new Timestamp("19700101");
                String expectTimestampValue = expectTimestamp.getOriginalValue();
                String expectedUrl = "http://example.com";
                String targetUrl = "http://example.com";

                WaybackResponse waybackResponse = new WaybackResponse();
                ArchivedSnapshots archivedSnapshots = new ArchivedSnapshots();
                Closest closest = new Closest();
                closest.setAvailable(true);
                closest.setTimestamp(expectTimestampValue);
                closest.setUrl(expectedUrl);
                archivedSnapshots.setClosest(closest);
                waybackResponse.setArchivedSnapshots(archivedSnapshots);
                String expectedEndPoint =  "http://archive.org/wayback/available?url=" + expectedUrl + "&timestamp=" + expectTimestampValue;
                when(restTemplate.getForEntity(expectedEndPoint, WaybackResponse.class)).thenReturn(new ResponseEntity(waybackResponse, HttpStatus.OK));

                WaybackMachineClient waybackMachineClient = new WaybackMachineClient(restTemplate);
                //List<ResponseSnapshot> responseSnapshots = waybackMachineClient.getSnapshotsByYear(targetUrl,2022);



        }

        @SuppressWarnings("unchecked")
        @Test
        void getSnapshot_WaybackResponseContainsValidSnapshot_ValidResponseSnapshotReturned() throws WaybackException {
                Timestamp expectTimestamp = new Timestamp("19700101");
                String expectTimestampValue = expectTimestamp.getOriginalValue();
                String expectedUrl = "http://example.com";

                WaybackResponse waybackResponse = new WaybackResponse();
                ArchivedSnapshots archivedSnapshots = new ArchivedSnapshots();
                Closest closest = new Closest();
                closest.setAvailable(true);
                closest.setTimestamp(expectTimestampValue);
                closest.setUrl(expectedUrl);
                archivedSnapshots.setClosest(closest);
                waybackResponse.setArchivedSnapshots(archivedSnapshots);
                String expectedEndPoint =  "http://archive.org/wayback/available?url=" + expectedUrl + "&timestamp=" + expectTimestampValue;
                when(restTemplate.getForEntity(expectedEndPoint, WaybackResponse.class)).thenReturn(new ResponseEntity(waybackResponse, HttpStatus.OK));

                WaybackMachineClient waybackMachineClient = new WaybackMachineClient(restTemplate);
                ResponseSnapshot responseSnapshot = waybackMachineClient.getSnapshot(expectedUrl,expectTimestampValue);

                assertEquals(responseSnapshot.getTimestamp(),expectTimestamp);
                assertEquals(responseSnapshot.getUrl(),expectedUrl);

        }

        @Test
        void testGetSnapshot() {
        }

        @Test
        void requestSnapshot() {
        }

        @Test
        void requestCalendarCaptures() {
        }

        @Test
        void getResponseSnapshot() {
        }
}
