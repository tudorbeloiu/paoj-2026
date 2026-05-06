package com.pao.proiect.licitatii.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;

public class DbConnection {
    private Connection conn;

    private static DbConnection instance;

    private DbConnection(){
        try{
            this.conn = createConnection();
        }
        catch(IOException | SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public Connection getConn(){
        try{
            if(this.conn == null || this.conn.isClosed()){

                this.conn = createConnection();
            }
        }
        catch(IOException | SQLException e){
            System.out.println(e.getMessage());
        }
        return this.conn;
    }

    public static DbConnection getInstance(){
        if(instance == null){
            instance = new DbConnection();
        }
        return instance;
    }

    public void closeConnection(){
        try{
            if(this.conn != null && !this.conn.isClosed()){
                this.conn.close();
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Connection createConnection() throws IOException, SQLException{
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("resources/db.properties")));

        String URL = props.getProperty("db.url");
        String USER = props.getProperty("db.user");
        String PASSWORD = props.getProperty("db.password");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
