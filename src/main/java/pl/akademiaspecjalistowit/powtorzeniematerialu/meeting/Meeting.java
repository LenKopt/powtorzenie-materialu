package pl.akademiaspecjalistowit.powtorzeniematerialu.meeting;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Meeting {

    private final UUID meetingId;
    private final String name;
    private final LocalDateTime dateAndTime;
    private final Set<String> participantEmail;
    private final Duration meetingDuration;

    public Meeting(String meetingName,
                   String meetingDateTimeString,
                   Set<String> participantEmail,
                   String meetingDuration) {


        this.dateAndTime = parseStringToDate(meetingDateTimeString);
        this.meetingDuration = parseDurationFromString(meetingDuration);
        this.participantEmail = participantEmail;
        this.name = meetingName;
        this.meetingId = UUID.randomUUID();

    }

    private static LocalDateTime parseStringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm");
        try {
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new MeetingException("Podana data ma niewłaściwy format");
        }
    }

    private static Duration parseDurationFromString(String durationString) {
        String[] parts = durationString.split(":");
        if (parts.length != 2) {
            throw new MeetingException("Nieprawidłowy format. Oczekiwano formatu HH:MM.");
        }
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);

        return Duration.ofHours(hours).plus(Duration.ofMinutes(minutes));
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "meetingId=" + meetingId +
                ", name='" + name + '\'' +
                ", dateAndTime=" + dateAndTime +
                ", participantEmail=" + participantEmail +
                ", meetingDuration=" + meetingDuration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Meeting meeting = (Meeting) o;
        return Objects.equals(meetingId, meeting.meetingId) && Objects.equals(name, meeting.name) &&
                Objects.equals(dateAndTime, meeting.dateAndTime) &&
                Objects.equals(participantEmail, meeting.participantEmail) &&
                Objects.equals(meetingDuration, meeting.meetingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId, name, dateAndTime, participantEmail, meetingDuration);
    }

    public Set<String> getParticipantEmail() {
        return Set.copyOf(participantEmail);
    }

    private boolean overlapping_meetings(Meeting other) {
        for (String email : this.getParticipantEmail()) {
            LocalDateTime startThisMeeting = this.getDateAndTime();
            LocalDateTime endThisMeeting = this.getDateAndTime().plus(this.getMeetingDuration());
            LocalDateTime startOtherMeeting = other.getDateAndTime();
            LocalDateTime endOtherMeeting = other.getDateAndTime().plus(other.getMeetingDuration());
            if (other.getParticipantEmail().contains(email)) {
                if (startOtherMeeting.isAfter(endThisMeeting)) {
                    return false;
                }
                if (startThisMeeting.isAfter(endOtherMeeting)) {
                    return false;
                }
//                if ((this.getDateAndTime().compareTo(other.getDateAndTime()) == -1 &&
//                        endThisMeeting.compareTo(other.getDateAndTime()) == -1) ||
//                        (this.getDateAndTime().compareTo(other.getDateAndTime()) == 1) &&
//                                endThisMeeting.compareTo(other.getDateAndTime().plus(other.getMeetingDuration())) == 1)
//                    ;
//                return false;
            }
        }
        return true;
    }

    public boolean duplicateExist(Meeting other) {
        return overlapping_meetings(other) && emailHasMeetingOnThisTime(other);
    }

    public Duration getMeetingDuration() {
        return meetingDuration;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    private boolean emailHasMeetingOnThisTime(Meeting other) {
        for (String email : this.getParticipantEmail()) {
            if (other.getParticipantEmail().contains(email)) {
                return true;
            }
        }
        return false;
    }
}
