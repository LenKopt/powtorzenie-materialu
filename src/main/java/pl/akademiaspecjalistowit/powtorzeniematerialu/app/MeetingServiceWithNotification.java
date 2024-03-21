package pl.akademiaspecjalistowit.powtorzeniematerialu.app;

import pl.akademiaspecjalistowit.powtorzeniematerialu.communication.Notification;
import pl.akademiaspecjalistowit.powtorzeniematerialu.meeting.Meeting;
import pl.akademiaspecjalistowit.powtorzeniematerialu.meeting.MeetingRepository;
import pl.akademiaspecjalistowit.powtorzeniematerialu.meeting.MeetingService;
import pl.akademiaspecjalistowit.powtorzeniematerialu.meeting.MeetingServiceImpl;

import java.util.List;
import java.util.Set;

public class MeetingServiceWithNotification implements MeetingService {
    private MeetingServiceImpl meetingService;
    private Notification notificationService;

    public MeetingServiceWithNotification(MeetingServiceImpl meetingService, Notification notificationService) {
        this.meetingService = meetingService;
        this.notificationService = notificationService;
    }

    @Override
    public Meeting createNewMeeting(String meetingName, String meetingDateTimeString, Set<String> participantEmail, String meetingDuration) {
        Meeting newMeeting = meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmail, meetingDuration);
        notificationService.sendAll(newMeeting.getParticipantEmail());
        return newMeeting;
    }

    @Override
    public List<Meeting> getAllMeetings() {
        return meetingService.getAllMeetings();
    }

    @Override
    public MeetingRepository getMeetingRepository() {
        return meetingService.getMeetingRepository();
    }
}
