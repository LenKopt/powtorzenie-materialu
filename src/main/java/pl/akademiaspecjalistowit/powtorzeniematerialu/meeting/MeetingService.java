package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MeetingService {

    private MeetingRepository meetingRepository;

    public MeetingService() {
        meetingRepository = new MeetingRepository();
    }

    public Meeting createNewMeeting(String meetingName, String meetingDateTimeString, Set<String> participantEmail,
                                    String meetingDuration) {
        boolean inAllMeetings = checkEmail(participantEmail, meetingDateTimeString, meetingDuration);
        if (inAllMeetings) {
            throw new MeetingException("Ten email już jest w jednym ze spotkań. Nie ma możliwości dodać takie spotkanie.");
        }
        Meeting meeting = new Meeting(meetingName, meetingDateTimeString, participantEmail, meetingDuration);
        meetingRepository.save(meeting);
        return meeting;
    }


    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    private boolean checkEmail(Set<String> emailNextPeople, String meetingDateTimeString, String meetingDuration) {
        List<Meeting> allMeetings = getAllMeetings();
        for (int i = 0; i < allMeetings.size(); i++) {
            Meeting nextMeeting = allMeetings.get(i);
            Set<String> nextMeetingParticipants = nextMeeting.getParticipantEmail();
            for (int j = 0; j < nextMeetingParticipants.size(); j++) {
                LocalDateTime timeOfMeeting = nextMeeting.parseStringToDate(meetingDateTimeString);
                Duration durationOfMeeting = Meeting.parseDurationFromString(meetingDuration);
                if (nextMeetingParticipants.containsAll(emailNextPeople) &&
                        (timeOfMeeting.compareTo(nextMeeting.getDateAndTime()) == 1 ||
                                timeOfMeeting.compareTo(nextMeeting.getDateAndTime()) == 0) &&
                        (timeOfMeeting.compareTo(nextMeeting.getDateAndTime().plus(nextMeeting.getMeetingDuration())) == -1)
                ) {
                    return true;
                }
            }
        }
        return false;
    }
}


