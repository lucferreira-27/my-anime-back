package com.lucferreira.myanimeback.service;

import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;
import com.lucferreira.myanimeback.model.snapshot.Timestamp;
import com.lucferreira.myanimeback.repository.ResponseSnapshotRepository;
import com.lucferreira.myanimeback.service.wayback.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return localSnapshotList;
    }

    private List<ResponseSnapshot> getArchiveSnapshotList(Media media) {
        List<ResponseSnapshot> responseSnapshots = waybackMachineClient.getSnapshotList(media);
        List<ResponseSnapshot> filteredResponseSnapshots = new ArrayList<>();

        for (int i = 0; i < responseSnapshots.size() - 1; i++) {
            ResponseSnapshot currentSnapshot = responseSnapshots.get(i);
            String status = currentSnapshot.getSnapshotStatus();

            if (!status.startsWith("2") && !status.startsWith("3")) {
                continue;
            }

            ResponseSnapshot nextSnapshot = responseSnapshots.get(i + 1);
            if (!Timestamp.isSameDay(currentSnapshot.getTimestamp(), nextSnapshot.getTimestamp())) {
                filteredResponseSnapshots.add(currentSnapshot);
            }
        }

        return filteredResponseSnapshots;
    }

    private List<ResponseSnapshot> getLocalSnapshotList(Long malId) {
        return responseSnapshotRepository.findAllByAvailableAndMalId(true,malId);
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
