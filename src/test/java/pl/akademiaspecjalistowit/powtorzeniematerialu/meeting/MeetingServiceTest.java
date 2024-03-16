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
    void making_overlapping_meetings_for_these_same_participants_is_inpossible() {
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
        try {
            Meeting overlappingMeeting = meetingService
                    .createNewMeeting(overlappingMeetingName,
                            overlappingMeetingDateTimeString,
                            overlappingParticipantEmails,
                            OverlappingMeetingDuration);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isNotEqualTo("");
        }

        // THEN
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        assertThat(allMeetings).hasSize(1);
    }

    private Meeting prepareValidMeetingDurationTwoHours() {
        String meetingName = "Test Meeting";
        String meetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = new HashSet<>();
        participantEmails.add("test@example.com");
        String meetingDuration = "02:00";
        return meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmails, meetingDuration);
    }

    @Test
    void making_overlapping_meetings_for_these_same_participants_is_inpossible_ending_meeting_later_start_previous() {
        // GIVEN
        prepareValidMeetingDurationTwoHours();
        String meetingNameSecond = "Test Meeting 2";
        String meetingDateTimeStringSecond = "01:01:2024 11:30";
        String meetingDurationSecond = "01:00";
        Set<String> participantEmails = new HashSet<>();
        participantEmails.add("test@example.com");
        // WHEN
        try {
            Meeting second =
                    meetingService.createNewMeeting(meetingNameSecond, meetingDateTimeStringSecond, participantEmails, meetingDurationSecond);
        } catch (RuntimeException e)
        // THEN
        {
            assertThat(e.getMessage()).isNotEqualTo("");
        }
    }

    @Test
    void making_overlapping_meetings_for_these_same_participants_is_inpossible_starting_meeting_before_start_previous_ending_after_previous() {
        // GIVEN
        prepareValidMeetingDurationTwoHours();
        Set<String> participantEmails = new HashSet<>();
        participantEmails.add("test@example.com");
        String meetingNameSecond = "Test Meeting 2";
        String meetingDateTimeStringSecond = "01:01:2024 11:00";
        String meetingDurationSecond = "03:00";
        // WHEN
        try {
            Meeting second =
                    meetingService.createNewMeeting(meetingNameSecond, meetingDateTimeStringSecond, participantEmails, meetingDurationSecond);
        } catch (RuntimeException e)
        // THEN
        {
            assertThat(e.getMessage()).isNotEqualTo("");
        }
    }

    @Test
    void making_overlapping_meetings_for_these_same_participants_is_possible_after_previous_meeting() {
        // GIVEN
        prepareValidMeetingDurationOneHour();
        Set<String> participantEmails = new HashSet<>();
        participantEmails.add("test@example.com");
        String meetingNameSecond = "Test Meeting 2";
        String meetingDateTimeStringSecond = "01:01:2024 13:10";
        String meetingDurationSecond = "03:00";
        // WHEN
        Meeting second =
                meetingService.createNewMeeting(meetingNameSecond, meetingDateTimeStringSecond, participantEmails, meetingDurationSecond);
        // THEN
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        assertThat(allMeetings).hasSize(2);
    }

    private Meeting prepareValidMeetingDurationOneHour() {
        String meetingName = "Test Meeting";
        String meetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = new HashSet<>();
        participantEmails.add("test@example.com");
        String meetingDuration = "01:00";
        return meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmails, meetingDuration);
    }

    @Test
    void making_overlapping_meetings_for_these_same_participants_is_possible_before_previous_meeting() {
        // GIVEN
        prepareValidMeetingDurationTwoHours();
        Set<String> participantEmails = new HashSet<>();
        participantEmails.add("test@example.com");
        String meetingNameSecond = "Test Meeting 2";
        String meetingDateTimeStringSecond = "01:01:2024 10:00";
        String meetingDurationSecond = "01:00";
        // WHEN
        Meeting second =
                meetingService.createNewMeeting(meetingNameSecond, meetingDateTimeStringSecond, participantEmails, meetingDurationSecond);
        // THEN
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        assertThat(allMeetings).hasSize(2);
    }

    @Test
    void deletion_meeting_successful() {
        // GIVEN
        String meetingName = "Test Meeting";
        String meetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = Set.of("test123@example.com");
        String meetingDuration = "02:00";
        Meeting firstMeeting =
                meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmails, meetingDuration);

        String overlappingMeetingName = "Test Meeting";
        String overlappingMeetingDateTimeString = "01:01:2024 12:10";
        Set<String> overlappingParticipantEmails = Set.of("test1234@example.com");
        String OverlappingMeetingDuration = "01:00";
        Meeting secondMeeting = meetingService
                .createNewMeeting(overlappingMeetingName,
                        overlappingMeetingDateTimeString,
                        overlappingParticipantEmails,
                        OverlappingMeetingDuration);

        // WHEN
        Meeting remotedMeeting = meetingService.removeMeeting(1l);

        // THEN
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        assertThat(allMeetings).hasSize(1);
        assertThat(allMeetings.get(0)).isEqualTo(firstMeeting);
    }

    @Test
    void deletion_meeting_failure() {
        // GIVEN
        String meetingName = "Test Meeting";
        String meetingDateTimeString = "01:01:2024 12:00";
        Set<String> participantEmails = Set.of("test123@example.com");
        String meetingDuration = "02:00";
        Meeting firstMeeting =
                meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmails, meetingDuration);

        // WHEN
        try {
            Meeting remotedMeeting = meetingService.removeMeeting(1l);
        } catch (MeetingException e) {
            // THEN
            List<Meeting> allMeetings = meetingService.getAllMeetings();
            assertThat(allMeetings).hasSize(1);
            assertThat(allMeetings.get(0)).isEqualTo(firstMeeting);
            assertThat(e.getMessage()).isEqualTo("Coś poszło nie tak...");
        }
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
        String thirdMeetingDateTimeString = "01:01:2024 14:10";
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
        String thirdMeetingDateTimeString = "01:01:2024 14:10";
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