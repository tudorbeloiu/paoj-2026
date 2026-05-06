package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.util.DbConnection;
import com.pao.proiect.licitatii.model.enums.AuctionStatus;
import com.pao.proiect.licitatii.model.enums.AuctionType;
import com.pao.proiect.licitatii.model.entities.Buyer;
import com.pao.proiect.licitatii.model.entities.Seller;
import com.pao.proiect.licitatii.model.entities.User;
import com.pao.proiect.licitatii.model.products.Auction;
import com.pao.proiect.licitatii.model.products.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuctionRepository implements Repository<Auction, String> {
    private static AuctionRepository instance;
    private DbConnection conn;

    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final ProductRepository productRepository = ProductRepository.getInstance();

    private AuctionRepository(){
        this.conn = DbConnection.getInstance();
    }

    public static final AuctionRepository getInstance(){
        if(instance == null){
            instance = new AuctionRepository();
        }
        return instance;
    }
    @Override
    public void initCounter(){
        String sql_query = "SELECT MAX(CAST(REPLACE(id,'A-','') AS INTEGER)) FROM auctions;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int valCrt = resultSet.getInt(1);
                    Auction.initCounter(valCrt);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void create(Auction auction){
        String sql_query = "INSERT INTO auctions (id, seller_id, product_id, starting_price, " +
                "current_price, starting_time, ending_time, auction_status, auction_type, winner_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, auction.getId());
            preparedStatement.setString(2, auction.getSeller().getId());
            preparedStatement.setString(3, auction.getProduct().getId());
            preparedStatement.setDouble(4, auction.getStartingPrice());
            preparedStatement.setDouble(5, auction.getCurrentPrice());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(auction.getStartingTime()));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(auction.getEndingTime()));
            preparedStatement.setString(8, auction.getAuctionStatus().name());
            preparedStatement.setString(9, auction.getAuctionType().name());

            if(auction.getWinner() != null){
                preparedStatement.setString(10, auction.getWinner().getId());
            }
            else{
                preparedStatement.setString(10, null);
            }

            preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Auction> readAll(){
        List<Auction> auctionList = new ArrayList<>();

        String sql_query = "SELECT * FROM auctions;";
        try(PreparedStatement pstmt = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = pstmt.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String seller_id = resultSet.getString("seller_id");
                    String product_id = resultSet.getString("product_id");
                    double starting_price = resultSet.getDouble("starting_price");
                    double current_price = resultSet.getDouble("current_price");
                    Timestamp starting_time = resultSet.getTimestamp("starting_time");
                    Timestamp ending_time = resultSet.getTimestamp("ending_time");
                    String auction_status_string = resultSet.getString("auction_status");
                    String auction_type_string = resultSet.getString("auction_type");
                    String winner_id = resultSet.getString("winner_id");

                    User seller = userRepository.findById(seller_id).orElse(null);
                    Product product = productRepository.findById(product_id).orElse(null);

                    if(!(seller instanceof Seller seller1)){
                        System.out.println("Skipping auction " + id + ": seller not found or invalid");
                        continue;
                    }
                    if(product == null){
                        System.out.println("Skipping auction " + id + ": product not found");
                        continue;
                    }

                    AuctionStatus auctionStatus = AuctionStatus.valueOf(auction_status_string);
                    AuctionType auctionType =  AuctionType.valueOf(auction_type_string);

                    Buyer winner = null;
                    if(winner_id != null){
                        User winnerUser = userRepository.findById(winner_id).orElse(null);
                        if(winnerUser instanceof Buyer buyer){
                            winner = buyer;
                        }
                    }

                    Auction auction = new Auction(id, seller1, product, starting_price, current_price, starting_time.toLocalDateTime(), ending_time.toLocalDateTime(), auctionType, auctionStatus);
                    if(winner != null){
                        auction.setWinner(winner);
                    }
                    auctionList.add(auction);

                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return auctionList;

    }

    public Optional<Auction> findById(String id){
        String sql_query = "SELECT * FROM auctions WHERE id = ?;";
        try(PreparedStatement pstms = conn.getConn().prepareStatement(sql_query)){
            pstms.setString(1, id);
            try(ResultSet resultSet = pstms.executeQuery()){
                if(resultSet.next()){
                    String seller_id = resultSet.getString("seller_id");
                    String product_id = resultSet.getString("product_id");
                    double starting_price = resultSet.getDouble("starting_price");
                    double current_price = resultSet.getDouble("current_price");
                    Timestamp starting_time = resultSet.getTimestamp("starting_time");
                    Timestamp ending_time = resultSet.getTimestamp("ending_time");
                    String auction_status_string = resultSet.getString("auction_status");
                    String auction_type_string = resultSet.getString("auction_type");
                    String winner_id = resultSet.getString("winner_id");

                    User seller = userRepository.findById(seller_id).orElse(null);
                    Product product = productRepository.findById(product_id).orElse(null);

                    if(!(seller instanceof Seller seller1) || product == null){
                        return Optional.empty();
                    }

                    AuctionStatus auctionStatus = AuctionStatus.valueOf(auction_status_string);
                    AuctionType auctionType =  AuctionType.valueOf(auction_type_string);

                    Buyer winner = null;
                    if(winner_id != null){
                        User winnerUser = userRepository.findById(winner_id).orElse(null);
                        if(winnerUser instanceof Buyer buyer){
                            winner = buyer;
                        }
                    }

                    Auction auction = new Auction(id, seller1, product, starting_price, current_price, starting_time.toLocalDateTime(), ending_time.toLocalDateTime(), auctionType, auctionStatus);
                    if(winner != null){
                        auction.setWinner(winner);
                    }
                    return Optional.of(auction);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(Auction auction){
        String sql_query = "UPDATE auctions SET current_price=?, auction_status=?, winner_id=? WHERE id=?;";
        try(PreparedStatement pstms = conn.getConn().prepareStatement(sql_query)){
            pstms.setDouble(1, auction.getCurrentPrice());
            pstms.setString(2, auction.getAuctionStatus().name());
            if(auction.getWinner() != null){
                pstms.setString(3, auction.getWinner().getId());
            }
            else{
                pstms.setString(3, null);
            }
            pstms.setString(4, auction.getId());

            pstms.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(String id){
        String sql_query = "DELETE FROM auctions WHERE id = ?;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<String> getActiveAuctionsProductCategory(){
        List<String> listAPC = new ArrayList<>();

        String sql_query = "SELECT a.id AS id, a.current_price AS current_price, p.name AS name, c.name AS category " +
                            "FROM auctions a " +
                            "JOIN products p " + "ON a.product_id = p.id " +
                            "JOIN categories c " + "ON p.category_id = c.id " +
                            "WHERE a.auction_status = 'ACTIVE'";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String linie = "[" + resultSet.getString("id") + "] " +
                            resultSet.getString("name") + " | Category: " + resultSet.getString("category") +
                            " | Current price: " + resultSet.getDouble("current_price");
                    listAPC.add(linie);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return listAPC;
    }

}
