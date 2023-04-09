package com.lucferreira.myanimeback.service.wayback;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WaybackMachineClientTest {

        @Mock
        private  RestTemplate restTemplate;


        @Test
        void testGetTimeMap() throws WaybackException, JsonProcessingException {
                // prepare test data
                String url = "http://myanimelist.net/anime/21/One_Piece";
                Object[] array1 = new Object[] {
                        "urlkey", "timestamp", "original", "mimetype", "statuscode",
                        "digest", "redirect", "robotflags", "length", "offset", "filename"
                };
                Object[] array2 = new Object[] {
                        "net,myanimelist)/anime/21/one_piece", "20080904102954",
                        "http://myanimelist.net:80/anime/21/One_Piece", "text/html", "200",
                        "CQV3LVIQC4IFVISYOHTYNOHGHGLC6EPT", "-", "-", "27890", "17458671",
                        "52_5_20080904100919_crawl101-c/52_5_20080904102851_crawl104.arc.gz"
                };
                Object[] array3 = new Object[] {
                        "net,myanimelist)/anime/21/one_piece", "20080908015513",
                        "http://myanimelist.net:80/anime/21/One_Piece", "text/html", "200",
                        "7Q62AD3ECS7WB5UG3UUDXMEKU5KEBES4", "-", "-", "27907", "20685994",
                        "51_5_20080907235419_crawl105-c/51_5_20080908015349_crawl104.arc.gz"
                };
                Object[] array4 = new Object[] {
                        "net,myanimelist)/anime/21/one_piece", "20080909090825",
                        "http://myanimelist.net:80/anime/21/One_Piece", "text/html", "200",
                        "R34LGIGB6DHX3IHCLQPL4CQ42QI2IJV7", "-", "-", "28067", "5223970",
                        "51_5_20080909071035_crawl108-c/51_5_20080909090814_crawl104.arc.gz"
                };
                Object[][] snapshotsArray = new Object[][] { array1, array2, array3, array4 };
                ResponseEntity<Object[]> response = new ResponseEntity<>(snapshotsArray, HttpStatus.OK);
                when(restTemplate.getForEntity(anyString(), eq(Object[].class))).thenReturn(response);
                WaybackMachineClient waybackMachineClient = new WaybackMachineClient(restTemplate);

                // call the method being tested
                Optional<List<ResponseSnapshot>> optionalSnapshots = waybackMachineClient.getTimeMap(url);
                List<ResponseSnapshot> actualSnapshots = optionalSnapshots.get();
                // assert the result
                assertEquals(3, actualSnapshots.size());
                assertEquals("20080904", actualSnapshots.get(0).getTimestamp().getOriginalValue());
                assertEquals("http://myanimelist.net:80/anime/21/One_Piece", actualSnapshots.get(0).getUrl());
                assertEquals("20080908", actualSnapshots.get(1).getTimestamp().getOriginalValue());
                assertEquals("http://myanimelist.net:80/anime/21/One_Piece", actualSnapshots.get(1).getUrl());
                assertEquals("20080909", actualSnapshots.get(2).getTimestamp().getOriginalValue());
                assertEquals("http://myanimelist.net:80/anime/21/One_Piece", actualSnapshots.get(2).getUrl());
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


        /*
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
        */
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
