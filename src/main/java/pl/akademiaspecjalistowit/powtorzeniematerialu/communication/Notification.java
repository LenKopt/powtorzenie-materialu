package pl.akademiaspecjalistowit.powtorzeniematerialu.communication;

import java.util.Set;

public interface Notification {
    public void sendAll(Set<String> listUsers);
}
