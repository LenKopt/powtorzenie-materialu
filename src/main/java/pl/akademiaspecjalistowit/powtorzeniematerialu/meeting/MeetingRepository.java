package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import java.util.*;

public class MeetingRepository {

    private Map<Long, Meeting> meetings;
    private static MeetingRepository meetingRepository;

    private MeetingRepository() {
        meetings = new HashMap<>();
    }

    public static MeetingRepository getInstance() {
        if (meetingRepository == null) {
            synchronized (MeetingRepository.class) {
                if (meetingRepository == null) {
                    meetingRepository = new MeetingRepository();
                }
            }
        }
        return meetingRepository;
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

    public Meeting remove(Long id) {
        try {
            return meetings.remove(id);
        } catch (RuntimeException e) {
            throw new MeetingException("Coś poszło nie tak...");
        }
    }

    public Map<Long, Meeting> getMeetings() {
        return meetings;
    }
}
