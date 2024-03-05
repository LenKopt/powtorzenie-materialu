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

        Meeting meeting = new Meeting(meetingName, meetingDateTimeString, participantEmail, meetingDuration);
        boolean meetingsOverlap = checkEmail(participantEmail, meetingDateTimeString, meetingDuration, meeting);
        if (meetingsOverlap) {
            throw new MeetingException("Ten email już jest w jednym ze spotkań. Nie ma możliwości dodać takie spotkanie.");
        }
        meetingRepository.save(meeting);
        return meeting;
    }


    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    private boolean checkEmail(Set<String> newEmail, String newMeetingDateTimeString, String newMeetingDurationString, Meeting meeting) {
        List<Meeting> allMeetings = getAllMeetings();
        for (int i = 0; i < allMeetings.size(); i++) {
            Meeting nextMeeting = allMeetings.get(i);
            Set<String> checkedListParticipantEmails = nextMeeting.getParticipantEmail();
            for (int j = 0; j < checkedListParticipantEmails.size(); j++) {
                LocalDateTime startNewMeeting = Meeting.parseStringToDate(newMeetingDateTimeString);
                Duration newMeetingDuration = Meeting.parseDurationFromString(newMeetingDurationString);
                LocalDateTime endNewMeeting = startNewMeeting.plus(newMeetingDuration);
                if (meeting.checkEmailSetsForDuplicates(nextMeeting)) {
                    if ((startNewMeeting.compareTo(nextMeeting.getDateAndTime()) == -1 &&
                            endNewMeeting.compareTo(nextMeeting.getDateAndTime()) == -1) ||
                            (startNewMeeting.compareTo(nextMeeting.getDateAndTime().plus(nextMeeting.getMeetingDuration())) == 1 &&
                                    endNewMeeting.compareTo(nextMeeting.getDateAndTime().plus(nextMeeting.getMeetingDuration())) == 1)) {
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}


