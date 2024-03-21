package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import pl.akademiaspecjalistowit.powtorzeniematerialu.app.MeetingApp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MeetingService {

    private MeetingRepository meetingRepository;
    private static MeetingService meetingService;

    private MeetingService() {
        meetingRepository = MeetingRepository.getInstance();
    }

    public static MeetingService getInstance() {
        if (meetingService == null) {
            synchronized (MeetingService.class) {
                if (meetingService == null) {
                    meetingService = new MeetingService();
                }
            }
        }
        return meetingService;
    }

    public Meeting createNewMeeting(String meetingName, String meetingDateTimeString, Set<String> participantEmail,
                                    String meetingDuration) {

        Meeting meeting = new Meeting(meetingName, meetingDateTimeString, participantEmail, meetingDuration);
        boolean meetingsOverlap = participantHasMeeting(meeting);
        if (meetingsOverlap) {
            throw new MeetingException("Ten email już jest w jednym ze spotkań. Nie ma możliwości dodać takie spotkanie.");
        }
        meetingRepository.save(meeting);
        return meeting;
    }


    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    private boolean participantHasMeeting(Meeting meeting) {
        List<Meeting> allMeetings = getAllMeetings();
        for (int i = 0; i < allMeetings.size(); i++) {
            Meeting otherMeeting = allMeetings.get(i);
            Set<String> checkedListParticipantEmails = otherMeeting.getParticipantEmail();
            for (int j = 0; j < checkedListParticipantEmails.size(); j++) {
                if (meeting.duplicateExist(otherMeeting)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Meeting> getAllMeetingsByEmail(String email) {
        return meetingRepository.findAllbyEmail(email);
    }

    public Meeting removeMeeting(Long id) {
        return meetingRepository.remove(id);
    }
}


