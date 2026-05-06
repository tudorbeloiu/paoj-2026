package com.pao.proiect.licitatii.model.entities;

import java.time.LocalDateTime;
import java.util.TreeSet;

import com.pao.proiect.licitatii.model.transactions.Notification;

import java.util.Objects;

public abstract class User {

    private static int contorId = 0;

    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    private TreeSet<Notification> notifications;

    public User(String name, String email, String password){
        this.id = "USER-" + (++contorId);
        this.name = name;
        this.email = email;
        this.password = password;

        this.createdAt = LocalDateTime.now();

        this.notifications = new TreeSet<>();
    }
    public User(String id, String name, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.notifications = new TreeSet<>();
    }

    public static void initCounter(int max){
        contorId = max;
    }

    public abstract String getRole();

    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    public void markNotificationRead(String notificationId){
        for(Notification notif: notifications){
            if(notif.getId().equals(notificationId)){
                notif.setIsRead(true);
                break;
            }
        }
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public TreeSet<Notification> getNotifications(){
        return new TreeSet<>(notifications);
    }



    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }


    @Override
    public String toString(){
        return "[" + getRole() + "]  " + name + " | " + email + " | " + id;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        User u = (User) o;
        return Objects.equals(email, u.email);
    }
    @Override
    public int hashCode(){
        return Objects.hash(email);
    }

}
