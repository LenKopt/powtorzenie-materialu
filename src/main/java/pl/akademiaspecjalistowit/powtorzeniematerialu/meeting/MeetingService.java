package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import java.util.List;
import java.util.Set;

public interface MeetingService {
    public Meeting createNewMeeting(String meetingName, String meetingDateTimeString, Set<String> participantEmail,
                                    String meetingDuration);


    public List<Meeting> getAllMeetings();
    public MeetingRepository getMeetingRepository();
}
