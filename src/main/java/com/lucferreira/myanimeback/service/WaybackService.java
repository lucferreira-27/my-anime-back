package com.lucferreira.myanimeback.service;

import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.service.wayback.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WaybackService {

    final WaybackMachineClient waybackMachineClient;

    @Autowired
    public WaybackService(WaybackMachineClient waybackMachineClient) {
        this.waybackMachineClient = waybackMachineClient;
    }

    public List<ResponseSnapshot> getSnapshotList(String url){

        return waybackMachineClient.getSnapshotList(url);
    }
    public List<ResponseSnapshot> getSnapshotByTimestamp(String url, String timestamp){
        String [] timestamps = timestamp.contains(",") ? timestamp.split(",") : new String[]{timestamp};
        if(timestamps.length == 0 || timestamps.length > 2){
            throw new WaybackTimestampParseException("Invalid timestamp format. Timestamp should be a comma-separated range of 4 to 14 digits. Example: 20200305120000,20200306120000");
        }
        if(timestamps.length == 2){
            return waybackMachineClient.getSnapshotsInRange(url, timestamps[0], Optional.of(timestamps[1]));
        }
        return waybackMachineClient.getSnapshotsInRange(url,timestamps[0], Optional.empty());
    }

}
