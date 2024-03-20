package pl.akademiaspecjalistowit.powtorzeniematerialu.communication;

import java.util.Set;

public class NotificationImp implements Notification {
    public void sendAll(Set<String> listUsers) {
        listUsers.forEach(user -> System.out.println(user + " will get mail about meeting"));
    }
}
