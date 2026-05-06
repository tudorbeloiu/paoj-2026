package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.util.DbConnection;
import com.pao.proiect.licitatii.model.products.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository implements Repository<Category, String> {
    private static CategoryRepository instance;

    private DbConnection conn;

    private CategoryRepository(){
        this.conn = DbConnection.getInstance();
    }

    public static CategoryRepository getInstance(){
        if(instance == null){
            instance = new CategoryRepository();
        }
        return instance;
    }

    @Override
    public void initCounter(){
        String sql_query = "SELECT MAX(CAST(REPLACE(id,'C-','') AS INTEGER)) FROM categories;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int valCrt = resultSet.getInt(1);
                    Category.initCounter(valCrt);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void create(Category category){
        String sql_string = "INSERT INTO categories VALUES (?, ?, ?);";
        try(PreparedStatement pstmt = conn.getConn().prepareStatement(sql_string)){
            pstmt.setString(1, category.getId());
            pstmt.setString(2, category.getName());
            pstmt.setString(3, category.getDescription());

            pstmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Category> readAll(){
        String sql_query = "SELECT * FROM categories";
        List<Category> categoryList = new ArrayList<>();

        try(PreparedStatement pstmt = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = pstmt.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    Category c = new Category(id, name, description);
                    categoryList.add(c);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return categoryList;
    }

    @Override
    public Optional<Category> findById(String id){
        String sql_query = "SELECT * FROM categories WHERE id=?";

        try(PreparedStatement pstmt = conn.getConn().prepareStatement(sql_query)){

            pstmt.setString(1, id);

            try(ResultSet resultSet = pstmt.executeQuery()){
                if(resultSet.next()){
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    return Optional.of(new Category(id, name, description));
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(Category category){
        String sql_query = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try(PreparedStatement pstmt = conn.getConn().prepareStatement(sql_query)){
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            pstmt.setString(3, category.getId());

            pstmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(String id){
        String sql_query = "DELETE FROM categories WHERE id = ?";
        try(PreparedStatement pstmt = conn.getConn().prepareStatement(sql_query)){
            pstmt.setString(1, id);

            pstmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
