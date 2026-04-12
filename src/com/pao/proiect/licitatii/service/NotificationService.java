package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.transactions.Notification;
import com.pao.proiect.licitatii.model.entities.User;

import java.util.TreeSet;

public class NotificationService {
    private static NotificationService instance;

    private NotificationService(){}

    public static NotificationService getInstance(){
        if(instance == null){
            instance = new NotificationService();
        }
        return instance;
    }

    public void addNotification(User user, String message){
        user.addNotification(new Notification(message));
    }

    public void markRead(User user, String notificationId){
        user.markNotificationRead(notificationId);
    }
    public TreeSet<Notification> unreadNotifications(User user){
        TreeSet<Notification> unreadNotifs = new TreeSet<>();
        for(Notification notif: user.getNotifications()){
            if(!notif.getIsRead()){
                unreadNotifs.add(notif);
            }
        }
        return unreadNotifs;
    }

}
