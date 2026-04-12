package com.pao.proiect.licitatii.model.transactions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification implements Comparable<Notification> {
    private static int counterId = 0;
    private final String id;

    private LocalDateTime notifTime;
    private String message;

    private boolean isRead;

    public Notification(String message){
        this.id = "N-" + (++counterId);

        this.notifTime = LocalDateTime.now();
        this.message = message;
        this.isRead = false;
    }

    public String getId(){
        return id;
    }
    public boolean getIsRead(){
        return isRead;
    }
    public LocalDateTime getNotifTime(){
        return notifTime;
    }
    public String getMessage(){
        return message;
    }

    public void setIsRead(boolean read){
        this.isRead = read;
    }

    @Override
    public int compareTo(Notification other){
        return -this.getNotifTime().compareTo(other.getNotifTime());
    }

    @Override
    public String toString(){
        return "[" + id + "] | " + notifTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " | " + "\"" +message + "\"";
    }

}
