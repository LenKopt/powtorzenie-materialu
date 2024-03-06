package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetingRepository {

    private Map<Long, Meeting> meetings;

    public MeetingRepository() {
        meetings = new HashMap<>();
    }

    public void save(Meeting meeting) {
        meetings.put((long) meetings.size(), meeting);
    }

    public List<Meeting> findAll() {
        return meetings.values().stream().toList();
    }
    public List<Meeting> findAllbyEmail(String email) {
        List<Meeting> listMeetings = new ArrayList<>();
        for (Meeting entry : meetings.values()) {
            entry.
                        Integer value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
        }
        return listMeetings;
    }
}
