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
        void getFirstSnapshot() throws WaybackException {
                String expectedUrl = "http://example.com";
                WaybackMachineClient waybackMachineClient = new WaybackMachineClient(restTemplate);
                waybackMachineClient.getFirstSnapshot(expectedUrl);

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
