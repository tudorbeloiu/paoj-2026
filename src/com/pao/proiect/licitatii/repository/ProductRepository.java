package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.util.DbConnection;
import com.pao.proiect.licitatii.model.products.Category;
import com.pao.proiect.licitatii.model.products.DigitalProduct;
import com.pao.proiect.licitatii.model.products.PhysicalProduct;
import com.pao.proiect.licitatii.model.products.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements Repository<Product, String> {
    private static ProductRepository instance;
    private final CategoryRepository categoryRepository = CategoryRepository.getInstance();
    private DbConnection conn;

    private ProductRepository(){
        this.conn = DbConnection.getInstance();
    }

    public static final ProductRepository getInstance(){
        if(instance == null){
            instance = new ProductRepository();
        }
        return instance;
    }

    @Override
    public void initCounter(){
        String sql_query = "SELECT MAX(CAST(REPLACE(id,'P-','') AS INTEGER)) FROM products;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int valCrt = resultSet.getInt(1);
                    Product.initCounter(valCrt);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void create(Product product){
        String sql_query_1 = "INSERT INTO products VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_1)) {
            preparedStatement.setString(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setString(5, product.getCategory().getId());
            if(product instanceof PhysicalProduct){
                preparedStatement.setString(6, "PHYSICAL");
            }
            else{
                preparedStatement.setString(6, "DIGITAL");
            }

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }


        if(product instanceof PhysicalProduct physicalProduct){
            String sql2 = "INSERT INTO physical_products (id, year, weight) VALUES (?, ?, ?)";
            try(PreparedStatement preparedStatement1 = conn.getConn().prepareStatement(sql2)) {
                preparedStatement1.setString(1, product.getId());
                preparedStatement1.setInt(2, physicalProduct.getYear());
                preparedStatement1.setDouble(3, physicalProduct.getWeight());

                preparedStatement1.executeUpdate();
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        else if(product instanceof DigitalProduct digitalProduct){
            String sql2 = "INSERT INTO digital_products (id, format, license_key) VALUES (?, ?, ?)";
            try(PreparedStatement preparedStatement1 = conn.getConn().prepareStatement(sql2)) {
                preparedStatement1.setString(1, product.getId());
                preparedStatement1.setString(2, digitalProduct.getFormat());
                preparedStatement1.setString(3, digitalProduct.getLicenseKey());

                preparedStatement1.executeUpdate();
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    @Override
    public List<Product> readAll(){
        List<Product> products = new ArrayList<>();
        String sql_query_1 = "SELECT p.*, pp.year, pp.weight " +
                            "FROM products p " +
                            "JOIN physical_products pp ON p.id = pp.id";

        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_1)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String name  = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    String categoryId  = resultSet.getString("category_id");
                    int year = resultSet.getInt("year");
                    double weight = resultSet.getDouble("weight");

                    Category category = categoryRepository.findById(categoryId).orElse(null);
                    products.add(new PhysicalProduct(id, name, description, price, category, year, weight));
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        String sql_query_2 = "SELECT p.*, dp.format, dp.license_key " +
                            "FROM products p " +
                            "JOIN digital_products dp ON p.id = dp.id;";

        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_2)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String name  = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    String categoryId  = resultSet.getString("category_id");
                    String format = resultSet.getString("format");
                    String licenseKey  = resultSet.getString("license_key");

                    Category category = categoryRepository.findById(categoryId).orElse(null);
                    products.add(new DigitalProduct(id, name, description, price, category, format, licenseKey));
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return products;
    }

    @Override
    public Optional<Product> findById(String id){
        String sql_query_type = "SELECT product_type FROM products WHERE id = ?;";
        String product_type = null;
        try(PreparedStatement pstmt_type = conn.getConn().prepareStatement(sql_query_type)){
            pstmt_type.setString(1, id);
            try(ResultSet resultSet = pstmt_type.executeQuery()){
                if(resultSet.next()){
                    product_type = resultSet.getString("product_type");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        if(product_type == null){
            return Optional.empty();
        }

        if("PHYSICAL".equals(product_type)){
            String sql_query_1 = "SELECT p.*, pp.year, pp.weight " +
                    "FROM products p JOIN physical_products pp ON p.id = pp.id " +
                    "WHERE p.id = ?";

            try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_1)){
                preparedStatement.setString(1, id);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        String name  = resultSet.getString("name");
                        String description = resultSet.getString("description");
                        double price = resultSet.getDouble("price");
                        String categoryId  = resultSet.getString("category_id");
                        int year = resultSet.getInt("year");
                        double weight = resultSet.getDouble("weight");

                        Category category = categoryRepository.findById(categoryId).orElse(null);
                        return Optional.of(new PhysicalProduct(id, name, description, price, category, year, weight));
                    }
                }
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        else if("DIGITAL".equals(product_type)){
            String sql_query_1 = "SELECT p.*, dp.format, dp.license_key " +
                    "FROM products p JOIN digital_products dp ON p.id = dp.id " +
                    "WHERE p.id = ?";

            try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_1)){
                preparedStatement.setString(1, id);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        String name  = resultSet.getString("name");
                        String description = resultSet.getString("description");
                        double price = resultSet.getDouble("price");
                        String categoryId  = resultSet.getString("category_id");
                        String format = resultSet.getString("format");
                        String licenseKey  = resultSet.getString("license_key");

                        Category category = categoryRepository.findById(categoryId).orElse(null);
                        return Optional.of(new DigitalProduct(id, name, description, price, category, format, licenseKey));
                    }
                }
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Product product){
        String sql_query = "UPDATE products SET name=?, description=?, price=?, category_id=? WHERE id=?;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setString(4, product.getCategory().getId());
            preparedStatement.setString(5, product.getId());

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        if(product instanceof PhysicalProduct physicalProduct){
            String sql_query_1 = "UPDATE physical_products SET year=?, weight=? WHERE id=?;";
            try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_1)){
                preparedStatement.setInt(1, physicalProduct.getYear());
                preparedStatement.setDouble(2, physicalProduct.getWeight());
                preparedStatement.setString(3, physicalProduct.getId());

                preparedStatement.executeUpdate();
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        else if(product instanceof  DigitalProduct digitalProduct){
            String sql_query_1 = "UPDATE digital_products SET format=?, license_key=? WHERE id=?;";
            try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_1)){
                preparedStatement.setString(1, digitalProduct.getFormat());
                preparedStatement.setString(2, digitalProduct.getLicenseKey());
                preparedStatement.setString(3, digitalProduct.getId());

                preparedStatement.executeUpdate();
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }


    @Override
    public void delete(String id){
        String sql_query_type = "SELECT product_type FROM products WHERE id = ?;";
        String product_type = null;
        try(PreparedStatement pstmt_type = conn.getConn().prepareStatement(sql_query_type)){
            pstmt_type.setString(1, id);
            try(ResultSet resultSet = pstmt_type.executeQuery()){
                if(resultSet.next()){
                    product_type = resultSet.getString("product_type");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        if(product_type == null){
            return;
        }

        String sql_query_1;
        if("PHYSICAL".equals(product_type)){
            sql_query_1 = "DELETE FROM physical_products WHERE id = ?;";
        }
        else if("DIGITAL".equals(product_type)){
            sql_query_1 = "DELETE FROM digital_products WHERE id = ?;";
        }
        else{
            return;
        }

        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_1)){
            preparedStatement.setString(1, id);

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        String sql_query_products = "DELETE FROM products WHERE id = ?";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query_products)){
            preparedStatement.setString(1, id);

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
