package com.lucferreira.myanimeback.service;

import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;
import com.lucferreira.myanimeback.repository.ResponseSnapshotRepository;
import com.lucferreira.myanimeback.service.wayback.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaybackService {

    private final WaybackMachineClient waybackMachineClient;
    private final ResponseSnapshotRepository responseSnapshotRepository;

    @Autowired
    public WaybackService(WaybackMachineClient waybackMachineClient,
            ResponseSnapshotRepository responseSnapshotRepository) {
        this.waybackMachineClient = waybackMachineClient;
        this.responseSnapshotRepository = responseSnapshotRepository;
    }

    public List<ResponseSnapshot> getSnapshotList(Media media) {
        List<ResponseSnapshot> localSnapshotList = getLocalSnapshotList(media.getMalId());
        if (localSnapshotList.size() == 0) {
            var archiveSnapshotlist = getArchiveSnapshotList(media);
            responseSnapshotRepository.saveAll(archiveSnapshotlist);
            return archiveSnapshotlist;
        }
        return getLocalSnapshotList(media.getMalId());
    }

    private List<ResponseSnapshot> getArchiveSnapshotList(Media media) {
        List<ResponseSnapshot> responseSnapshots = waybackMachineClient.getSnapshotList(media);
        List<ResponseSnapshot> filterdResponseSnapshots = responseSnapshots
                .stream()
                .filter(snapshot -> snapshot.getSnapshotStatus().startsWith("2")
                        || snapshot.getSnapshotStatus().startsWith("3"))
                .collect(Collectors.toList());
        return filterdResponseSnapshots;
    }

    private List<ResponseSnapshot> getLocalSnapshotList(Long malId) {
        return responseSnapshotRepository.findAll();
    }

    public List<ResponseSnapshot> getSnapshotByTimestamp(Media media, String timestamp) {
        String[] timestamps = timestamp.contains(",") ? timestamp.split(",") : new String[] { timestamp };
        if (timestamps.length == 0 || timestamps.length > 2) {
            throw new WaybackTimestampParseException(
                    "Invalid timestamp format. Timestamp should be a comma-separated range of 4 to 14 digits. Example: 20200305120000,20200306120000");
        }
        if (timestamps.length == 2) {
            return waybackMachineClient.getSnapshotsInRange(media, timestamps[0], Optional.of(timestamps[1]));
        }
        return waybackMachineClient.getSnapshotsInRange(media, timestamps[0], Optional.empty());
    }

}
