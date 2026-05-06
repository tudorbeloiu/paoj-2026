package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.repository.NotificationRepository;
import com.pao.proiect.licitatii.model.entities.*;
import com.pao.proiect.licitatii.exception.EmailAlreadyExists;
import com.pao.proiect.licitatii.exception.UserNotFound;

import com.pao.proiect.licitatii.repository.UserRepository;
import com.pao.proiect.licitatii.model.transactions.Notification;

import java.util.HashMap;

public class UserService {
    private static final AuditService auditService = AuditService.getInstance();
    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    private static UserService instance;
    private HashMap<String, User> users;

    private UserService(){
        userRepository.initCounter();
        notificationRepository.initCounter();

        this.users = new HashMap<>();
        for(User user: userRepository.readAll()){
            for(Notification n: notificationRepository.readAllByUser(user.getId())){
                user.addNotification(n);
            }
            users.put(user.getId(), user);
        }
    }

    public static UserService getInstance(){
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }
    public void addUser(User user){
        for(User registeredUser: users.values()){
            if(user.getEmail().equals(registeredUser.getEmail())){
                throw new EmailAlreadyExists(user.getEmail());
            }
        }
        users.put(user.getId(), user);
        userRepository.create(user);
        if(user instanceof Buyer){
            auditService.log("Add buyer");
        }
        else if(user instanceof Seller){
            auditService.log("Add seller");
        }
    }

    public User findUserById(String id){
        User user = users.get(id);
        if(user == null){
            throw new UserNotFound();
        }
        return user;
    }
    public User findUserByEmail(String email){
        for(User user: users.values()){
            if(user.getEmail().equals(email)){
                auditService.log("Find user by email");
                return user;
            }
        }
        throw new UserNotFound();
    }
    public void printAllUsers(){
        if(users.isEmpty()){
            System.out.println("There are no users at this time");
            return;
        }
        for(User user: users.values()){
            System.out.println(user);
        }
        auditService.log("Print all users");
    }
    public void deleteUserById(String id){
        if(!users.containsKey(id)){
            throw new UserNotFound();
        }
        users.remove(id);
        userRepository.delete(id);
        auditService.log("Delete user by id");
    }

    public void updateBuyer(String id, String name, String email, String password, double balance){
        User user = findUserById(id);
        if(!(user instanceof Buyer buyer)){
            throw new UserNotFound();
        }

        for(User existing : users.values()){
            if(!existing.getId().equals(id) && existing.getEmail().equals(email)){
                throw new EmailAlreadyExists(email);
            }
        }

        buyer.setName(name);
        buyer.setEmail(email);
        buyer.setPassword(password);
        buyer.setBalance(balance);

        userRepository.update(buyer);
        auditService.log("Update buyer");
    }

    public void updateSeller(String id, String name, String email, String password){
        User user = findUserById(id);
        if(!(user instanceof Seller seller)){
            throw new UserNotFound();
        }

        for(User existing : users.values()){
            if(!existing.getId().equals(id) && existing.getEmail().equals(email)){
                throw new EmailAlreadyExists(email);
            }
        }

        seller.setName(name);
        seller.setEmail(email);
        seller.setPassword(password);

        userRepository.update(seller);
        auditService.log("Update seller");
    }

    public HashMap<String, User> getUsers(){
        return new HashMap<>(users);
    }

}
