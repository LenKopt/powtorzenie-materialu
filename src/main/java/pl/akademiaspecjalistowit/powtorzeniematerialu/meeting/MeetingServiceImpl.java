package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import java.util.List;
import java.util.Set;

public class MeetingServiceImpl implements MeetingService {

    private MeetingRepository meetingRepository;
    private static MeetingServiceImpl meetingService;

    private MeetingServiceImpl() {
        meetingRepository = MeetingRepository.getInstance();
    }

    public static MeetingServiceImpl getInstance() {
        if (meetingService == null) {
            synchronized (MeetingServiceImpl.class) {
                if (meetingService == null) {
                    meetingService = new MeetingServiceImpl();
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

    public MeetingRepository getMeetingRepository() {
        return meetingRepository;
    }
    //    public List<Meeting> getAllMeetingsByEmail(String email) {
//        return meetingRepository.findAllbyEmail(email);
//    }
//
//    public Meeting removeMeeting(Long id) {
//        return meetingRepository.remove(id);
//    }
}


