package pl.akademiaspecjalistowit.powtorzeniematerialu.app;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import pl.akademiaspecjalistowit.powtorzeniematerialu.communication.NotificationImp;
import pl.akademiaspecjalistowit.powtorzeniematerialu.meeting.Meeting;
import pl.akademiaspecjalistowit.powtorzeniematerialu.meeting.MeetingException;
import pl.akademiaspecjalistowit.powtorzeniematerialu.meeting.MeetingService;
import pl.akademiaspecjalistowit.powtorzeniematerialu.meeting.MeetingServiceImpl;

public class MeetingApp {

    private MeetingService meetingService;


    public MeetingApp() {
        this.meetingService = new MeetingServiceWithNotification(MeetingServiceImpl.getInstance(), new NotificationImp());
    }

    public void run() {
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        try {
            while (!exit) {
                exit = printMenu(scanner);
            }
        } catch (Exception e) {
            System.out.println("Wystąpił problem, przepraszamy: " + e);
            run();
        }

    }

    private boolean printMenu(Scanner scanner) {

        System.out.println("\n Dostępne komendy: ");
        System.out.println("1) Nowe spotkanie");
        System.out.println("2) Pokaż wszystkie spotkania");
        System.out.println("3) Usuń spotkanie");
        System.out.println("4) Pokaż spotkania zgodnie z podanym email");
        System.out.print("Wpisz komendę: ");
        String command = scanner.nextLine();

        switch (command) {
            case "1":
                createMeeting(scanner);
                break;
            case "2":
                showMeetings();
                break;
            case "3":
                deleteMeeting(scanner);
                break;
            case "4":
                showMeetingsByEmail(scanner);
                break;
            case "exit":
                System.out.println("Zamykanie aplikacji...");
                return true;
            default:
                System.out.println("Nieznana komenda. Spróbuj ponownie.");
                break;
        }
        return false;
    }

    private void showMeetings() {
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        if (allMeetings.isEmpty()) {
            System.out.println("Brak spotkań");
            return;
        }
        for (int i = 0; i < allMeetings.size(); i++) {
            System.out.println("№ " + (i + 1) + " - " + allMeetings.get(i));
        }
    }

    private void showMeetingsByEmail(Scanner scanner) {
        System.out.println("Podaj email: ");
        String email = scanner.nextLine();

        List<Meeting> allMeetings = meetingService.getMeetingRepository().findAllbyEmail(email);
        if (allMeetings.isEmpty()) {
            System.out.println("Brak spotkań");
            return;
        }
        for (Meeting meeting : allMeetings) {
            System.out.println(meeting);
        }
    }

    private void createMeeting(Scanner scanner) {
        System.out.println("Tworzenie nowego spotkania...");
        System.out.println("Podaj nazwę spotkania: ");
        String meetingName = scanner.nextLine();

        System.out.println("Podaj datę i godzinę spotkania (w formacie DD:MM:RRRR HH:MM): ");
        String meetingDateTimeString = scanner.nextLine();

        System.out.println("Podaj długość trwania spotkania (w formacie HH:MM) ");
        String meetingDuration = scanner.nextLine();

        Set<String> participantEmail = new HashSet<>();
        boolean stop = false;
        while (!stop) {
            System.out.println("Podaj email uczestnika do zaprosznia: ");
            participantEmail.add(scanner.nextLine());
            System.out.println("Chcesz dodać więcej uczestników?: (T/N)");
            String decission = scanner.nextLine();
            if (decission.toUpperCase().equals("N")) {
                stop = true;
            }
        }

        Meeting newMeeting =
                meetingService.createNewMeeting(meetingName, meetingDateTimeString, participantEmail, meetingDuration);

        System.out.println("Spotkanie " + newMeeting + " zostało utworzone.");
    }

    private void deleteMeeting(Scanner scanner) {
        showMeetings();
        if (meetingService.getAllMeetings().size() == 0) {
            return;
        }
        System.out.println("Podaj numer spotkania zgodnie z listą: ");
        String meetingId = scanner.nextLine();
        try {
            meetingService.getMeetingRepository().remove(Long.parseLong(meetingId) - 1);
            System.out.println("Spotkanie z numerem " + meetingId + " zostało usunięte!");
        } catch (MeetingException e) {
            System.out.println(e.getMessage());
        }
    }

}
