package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.util.DbConnection;
import com.pao.proiect.licitatii.model.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<User, String> {
    private static UserRepository instance;
    private DbConnection conn;

    private UserRepository(){
        this.conn = DbConnection.getInstance();
    }

    public final static UserRepository getInstance(){
        if(instance == null){
            instance = new UserRepository();
        }
        return instance;
    }

    @Override
    public void initCounter(){
        String sql_query = "SELECT MAX(CAST(REPLACE(id,'USER-','') AS INTEGER)) FROM users;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int valCrt = resultSet.getInt(1);
                    User.initCounter(valCrt);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void create(User user){
        String sql_query = "INSERT INTO users (id, name, email, password, user_type, balance, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole());

            if(user instanceof Buyer buyer){
                preparedStatement.setDouble(6, buyer.getBalance());
            }
            else{
                preparedStatement.setDouble(6,0);
            }

            preparedStatement.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<User> readAll(){
        String sql_query = "SELECT * FROM users;";
        List<User> userList = new ArrayList<>();

        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String user_type = resultSet.getString("user_type");
                    Timestamp timestamp = resultSet.getTimestamp("created_at");

                    if(user_type.equals("Seller")){
                        userList.add(new Seller(id, name,email, password, timestamp.toLocalDateTime()));
                    }
                    else{
                        userList.add(new Buyer(id, name, email, password, resultSet.getDouble("balance"), timestamp.toLocalDateTime()));
                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return userList;
    }

    @Override
    public Optional<User> findById(String id){
        String sql_query = "SELECT * FROM users WHERE id = ?;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String user_type = resultSet.getString("user_type");
                    Timestamp timestamp = resultSet.getTimestamp("created_at");

                    if(user_type.equals("Seller")){
                        return Optional.of(new Seller(id, name,email, password, timestamp.toLocalDateTime()));
                    }
                    else{
                        return Optional.of(new Buyer(id, name, email, password, resultSet.getDouble("balance"), timestamp.toLocalDateTime()));
                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(User user){
        String sql_query = "UPDATE users SET name = ?, email = ?, password = ?, balance = ? WHERE id = ?;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(5, user.getId());

            if(user instanceof Buyer buyer){
                preparedStatement.setDouble(4, buyer.getBalance());
            }
            else{
                preparedStatement.setDouble(4, 0);
            }

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(String id){
        String sql_query = "DELETE FROM users WHERE id = ?";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, id);

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}






