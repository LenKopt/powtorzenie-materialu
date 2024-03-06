package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import java.util.*;

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
        for (Meeting actualMeeting : meetings.values()) {
            Set<String> actualSetEmails = actualMeeting.getParticipantEmail();
            if (actualSetEmails.contains(email)) {
                listMeetings.add(actualMeeting);
            }
        }
        return listMeetings;
    }
}
