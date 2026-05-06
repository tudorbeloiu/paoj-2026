package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.util.DbConnection;
import com.pao.proiect.licitatii.model.enums.BidStatus;
import com.pao.proiect.licitatii.model.entities.Buyer;
import com.pao.proiect.licitatii.model.entities.User;
import com.pao.proiect.licitatii.model.products.Auction;
import com.pao.proiect.licitatii.model.transactions.Bid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BidRepository {
    private static BidRepository instance;
    private DbConnection conn;
    private static UserRepository userRepository = UserRepository.getInstance();

    private BidRepository(){
        this.conn = DbConnection.getInstance();
    }

    public static BidRepository getInstance(){
        if(instance == null){
            instance = new BidRepository();
        }
        return instance;
    }

    public void initCounter(){
        String sql_query = "SELECT MAX(CAST(REPLACE(id,'BID-','') AS INTEGER)) FROM bids;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int valCrt = resultSet.getInt(1);
                    Bid.initCounter(valCrt);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void create(Bid bid){
        String sql_string = "INSERT INTO bids VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_string)){
            preparedStatement.setString(1, bid.getId());
            preparedStatement.setString(2, bid.getBidder().getId());
            preparedStatement.setString(3, bid.getAuction().getId());
            preparedStatement.setDouble(4, bid.getAmount());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(bid.getPlacedTime()));
            preparedStatement.setString(6, bid.getStatus().name());

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void delete(String id){
        String sql_string = "DELETE FROM bids WHERE id = ?;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_string)){
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Bid> readAllByAuction(Auction auction){
        String sql_query = "SELECT * FROM bids WHERE auction_id = ?";
        List<Bid> bidList = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, auction.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String bidder_id = resultSet.getString("bidder_id");
                    double amount = resultSet.getDouble("amount");
                    Timestamp placed_time = resultSet.getTimestamp("placed_time");
                    String bidStatusString = resultSet.getString("status");

                    User bidderUser = userRepository.findById(bidder_id).orElse(null);
                    if(!(bidderUser instanceof Buyer bidder)){
                        System.out.println("Skipping bid " + id + ": bidder is not a Buyer");
                        continue;
                    }
                    BidStatus bidStatus = BidStatus.valueOf(bidStatusString);

                    bidList.add(new Bid(id, bidder, auction, amount, placed_time.toLocalDateTime(), bidStatus));

                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return bidList;
    }

    public List<String> getCountBidders(){
        String sql_query = "SELECT u.name AS name, COUNT(b.id) AS cnt " +
                            "FROM users u "+
                            "JOIN bids b " + "ON u.id = b.bidder_id " +
                            "GROUP BY u.id, u.name " +
                            "ORDER BY COUNT(b.id) DESC";

        List<String> bidders = new ArrayList<>();

        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String linie = "[" + resultSet.getString("name") + " - " + resultSet.getString("cnt") + "]";
                    bidders.add(linie);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return bidders;
    }

    public void update(Bid bid){
        String sql_query = "UPDATE bids SET status = ? WHERE id = ?;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, bid.getStatus().name());
            preparedStatement.setString(2, bid.getId());

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
