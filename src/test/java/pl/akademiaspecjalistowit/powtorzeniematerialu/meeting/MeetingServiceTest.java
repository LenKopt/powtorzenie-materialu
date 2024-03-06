package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeetingServiceTest {

    private MeetingService meetingService;

    @BeforeEach
    void setUp() {
        meetingService = new MeetingService();
    }

    @Test
    void should_create_meeting_correctly() {
        // GIVEN
        String meetingName = "Test Meeting";
        String meetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = new HashSet<>();
        participantEmails.add("test@example.com");
        String meetingDuration = "02:00";

        // WHEN
        Meeting result =
                meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmails, meetingDuration);

        // THEN
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        assertThat(allMeetings).contains(result);
    }

    @Test
    void making_overlapping_meetings_for_different_participants_is_possible() {
        // GIVEN
        String meetingName = "Test Meeting";
        String meetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = Set.of("test123@example.com");
        String meetingDuration = "02:00";
        Meeting existingMeeting =
                meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmails, meetingDuration);

        String overlappingMeetingName = "Test Meeting";
        String overlappingMeetingDateTimeString = "01:01:2024 12:10";
        Set<String> overlappingParticipantEmails = Set.of("test1234@example.com");
        String OverlappingMeetingDuration = "01:00";

        // WHEN
        Meeting overlappingMeeting = meetingService
                .createNewMeeting(overlappingMeetingName,
                        overlappingMeetingDateTimeString,
                        overlappingParticipantEmails,
                        OverlappingMeetingDuration);

        // THEN
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        assertThat(allMeetings).hasSize(2);
    }

    @Test
    void making_overlapping_meetings_for_these_same_participants_is_possible() {
        // GIVEN
        String meetingName = "Test Meeting";
        String meetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = Set.of("test123@example.com");
        String meetingDuration = "02:00";
        Meeting existingMeeting =
                meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmails, meetingDuration);

        String overlappingMeetingName = "Test Meeting";
        String overlappingMeetingDateTimeString = "01:01:2024 12:10";
        Set<String> overlappingParticipantEmails = Set.of("test123@example.com");
        String OverlappingMeetingDuration = "01:00";

        // WHEN
        Meeting overlappingMeeting = meetingService
                .createNewMeeting(overlappingMeetingName,
                        overlappingMeetingDateTimeString,
                        overlappingParticipantEmails,
                        OverlappingMeetingDuration);

        // THEN
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        assertThat(allMeetings).hasSize(2);
    }

    @Test
    void searching_meetings_by_email_with_successful_result() {
        // GIVEN
        String firstMeetingName = "Test Meeting";
        String firstMeetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = Set.of("test123@example.com");
        String firstMeetingDuration = "02:00";
        Meeting firstMeeting =
                meetingService.createNewMeeting(firstMeetingName, firstMeetingDateTimeString, participantEmails, firstMeetingDuration);

        String secondMeetingName = "Test Meeting";
        String secondMeetingDateTimeString = "01:01:2024 12:10";
        Set<String> secondParticipantEmails = Set.of("test1234@example.com");
        String secondMeetingDuration = "01:00";
        Meeting secondMeeting = meetingService
                .createNewMeeting(secondMeetingName, secondMeetingDateTimeString, secondParticipantEmails, secondMeetingDuration);

        String thirdMeetingName = "Test Meeting";
        String thirdMeetingDateTimeString = "01:01:2024 12:10";
        Set<String> thirdParticipantEmails = Set.of("test1234@example.com", "test123@example.com");
        String thirdMeetingDuration = "01:00";
        Meeting thirdMeeting = meetingService
                .createNewMeeting(thirdMeetingName, thirdMeetingDateTimeString, thirdParticipantEmails, thirdMeetingDuration);

        // WHEN
        List<Meeting> listFoundedMeetingByEmail = meetingService.getAllMeetingsByEmail("test123@example.com");
        // THEN
        assertThat(listFoundedMeetingByEmail).hasSize(2);
    }

    @Test
    void searching_meetings_by_email_with_result_zero() {
        // GIVEN
        String firstMeetingName = "Test Meeting";
        String firstMeetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = Set.of("test123@example.com");
        String firstMeetingDuration = "02:00";
        Meeting firstMeeting =
                meetingService.createNewMeeting(firstMeetingName, firstMeetingDateTimeString, participantEmails, firstMeetingDuration);

        String secondMeetingName = "Test Meeting";
        String secondMeetingDateTimeString = "01:01:2024 12:10";
        Set<String> secondParticipantEmails = Set.of("test1234@example.com");
        String secondMeetingDuration = "01:00";
        Meeting secondMeeting = meetingService
                .createNewMeeting(secondMeetingName, secondMeetingDateTimeString, secondParticipantEmails, secondMeetingDuration);

        String thirdMeetingName = "Test Meeting";
        String thirdMeetingDateTimeString = "01:01:2024 12:10";
        Set<String> thirdParticipantEmails = Set.of("test1234@example.com", "test123@example.com");
        String thirdMeetingDuration = "01:00";
        Meeting thirdMeeting = meetingService
                .createNewMeeting(thirdMeetingName, thirdMeetingDateTimeString, thirdParticipantEmails, thirdMeetingDuration);

        // WHEN
        List<Meeting> listFoundedMeetingByEmail = meetingService.getAllMeetingsByEmail("test125@example.com");
        // THEN
        assertThat(listFoundedMeetingByEmail).hasSize(0);
    }
}