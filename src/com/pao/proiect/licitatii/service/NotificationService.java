package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.repository.NotificationRepository;
import com.pao.proiect.licitatii.model.transactions.Notification;
import com.pao.proiect.licitatii.model.entities.User;

import java.util.TreeSet;

public class NotificationService {
    private final AuditService auditService = AuditService.getInstance();
    private static NotificationService instance;
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    private NotificationService(){
        notificationRepository.initCounter();
    }

    public static NotificationService getInstance(){
        if(instance == null){
            instance = new NotificationService();
        }
        return instance;
    }

    public void addNotification(User user, String message){
        Notification n = new Notification(message);
        user.addNotification(n);
        notificationRepository.create(n, user.getId());
    }

    public void markRead(User user, String notificationId){
        user.markNotificationRead(notificationId);
        notificationRepository.markRead(notificationId);
        auditService.log("Mark notification as read");
    }
    public TreeSet<Notification> unreadNotifications(User user){
        TreeSet<Notification> unreadNotifs = new TreeSet<>();
        for(Notification notif: user.getNotifications()){
            if(!notif.getIsRead()){
                unreadNotifs.add(notif);
            }
        }
        auditService.log("Show unread notifications");
        return unreadNotifs;
    }

}
