package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.entities.*;
import com.pao.proiect.licitatii.exception.EmailAlreadyExists;
import com.pao.proiect.licitatii.exception.UserNotFound;

import java.util.HashMap;

public class UserService {

    private static UserService instance;
    private HashMap<String, User> users;

    private UserService(){
        this.users = new HashMap<>();
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
    }
    public void deleteUserById(String id){
        if(!users.containsKey(id)){
            throw new UserNotFound();
        }
        users.remove(id);
    }

    public HashMap<String, User> getUsers(){
        return new HashMap<>(users);
    }

}
