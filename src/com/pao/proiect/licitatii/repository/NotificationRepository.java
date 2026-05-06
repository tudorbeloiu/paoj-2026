package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.util.DbConnection;
import com.pao.proiect.licitatii.model.transactions.Notification;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {
    private static NotificationRepository instance;
    private DbConnection conn;

    private NotificationRepository(){
        this.conn = DbConnection.getInstance();
    }

    public static NotificationRepository getInstance(){
        if(instance == null){
            instance = new NotificationRepository();
        }
        return instance;
    }

    public void initCounter(){
        String sql_query = "SELECT MAX(CAST(REPLACE(id,'N-','') AS INTEGER)) FROM notifications;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int valCrt = resultSet.getInt(1);
                    Notification.initCounter(valCrt);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void create(Notification notification, String userId){
        String sql_query = "INSERT INTO notifications VALUES (?, ?, ?, ?, ?);";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, notification.getId());
            preparedStatement.setString(2, userId);
            preparedStatement.setString(3, notification.getMessage());
            preparedStatement.setBoolean(4, notification.getIsRead());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(notification.getNotifTime()));
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void delete(String id){
        String sql_query = "DELETE FROM notifications WHERE id = ?;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void markRead(String id){
        String sql_query = "UPDATE notifications SET is_read = true WHERE id = ?;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Notification> readAllByUser(String userId){
        List<Notification> notificationList = new ArrayList<>();
        String sql_query = "SELECT * FROM notifications WHERE user_id = ?";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String message = resultSet.getString("message");
                    boolean isRead = resultSet.getBoolean("is_read");
                    Timestamp time = resultSet.getTimestamp("created_at");

                    notificationList.add(new Notification(id, message, time.toLocalDateTime(), isRead));
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return notificationList;
    }
}
