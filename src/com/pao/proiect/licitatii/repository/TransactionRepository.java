package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.util.DbConnection;
import com.pao.proiect.licitatii.model.entities.Buyer;
import com.pao.proiect.licitatii.model.entities.Seller;
import com.pao.proiect.licitatii.model.entities.User;
import com.pao.proiect.licitatii.model.products.Auction;
import com.pao.proiect.licitatii.model.transactions.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private static TransactionRepository instance;
    private DbConnection conn;

    private static final UserRepository userCRUD = UserRepository.getInstance();
    private static AuctionRepository auctionRepository = AuctionRepository.getInstance();

    private TransactionRepository(){
        this.conn = DbConnection.getInstance();
    }

    public static TransactionRepository getInstance(){
        if(instance == null){
            instance = new TransactionRepository();
        }
        return instance;
    }

    public void initCounter(){
        String sql_query = "SELECT MAX(CAST(REPLACE(id,'T-','') AS INTEGER)) FROM transactions;";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int valCrt = resultSet.getInt(1);
                    Transaction.initCounter(valCrt);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void create(Transaction transaction){
        String sql_query = "INSERT INTO transactions VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement preparedStatement = conn.getConn().prepareStatement(sql_query)){
            preparedStatement.setString(1, transaction.getId());
            preparedStatement.setString(2, transaction.getEndedAuction().getId());
            preparedStatement.setString(3, transaction.getAuctionWinner().getId());
            preparedStatement.setString(4, transaction.getAuctionSeller().getId());
            preparedStatement.setDouble(5, transaction.getFinalAmount());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(transaction.getTransactionTime()));

            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Transaction> readAll(){
        String sql_query = "SELECT * FROM transactions;";
        List<Transaction> transactionList = new ArrayList<>();
        try(PreparedStatement pstmt = conn.getConn().prepareStatement(sql_query)){
            try(ResultSet resultSet = pstmt.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String auction_id = resultSet.getString("auction_id");
                    String winner_id = resultSet.getString("winner_id");
                    String seller_id = resultSet.getString("seller_id");
                    double final_amount = resultSet.getDouble("final_amount");
                    Timestamp transaction_time = resultSet.getTimestamp("transaction_time");

                    User winnerUser = userCRUD.findById(winner_id).orElse(null);
                    User sellerUser = userCRUD.findById(seller_id).orElse(null);
                    if(!(winnerUser instanceof Buyer winner)){
                        System.out.println("Skipping transaction " + id + ": winner is not a Buyer");
                        continue;
                    }
                    if(!(sellerUser instanceof Seller seller)){
                        System.out.println("Skipping transaction " + id + ": seller is not a Seller");
                        continue;
                    }
                    Auction auction = auctionRepository.findById(auction_id).orElse(null);

                    transactionList.add(new Transaction(id, auction, winner, seller, final_amount, transaction_time.toLocalDateTime()));
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return transactionList;
    }

    public List<Transaction> readAllByUser(User user){
        String sql_query = "SELECT * FROM transactions WHERE winner_id = ? OR seller_id = ?;";
        List<Transaction> transactionList = new ArrayList<>();
        try(PreparedStatement pstmt = conn.getConn().prepareStatement(sql_query)){
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getId());
            try(ResultSet resultSet = pstmt.executeQuery()){
                while(resultSet.next()){
                    String id = resultSet.getString("id");
                    String auction_id = resultSet.getString("auction_id");
                    String winner_id = resultSet.getString("winner_id");
                    String seller_id = resultSet.getString("seller_id");
                    double final_amount = resultSet.getDouble("final_amount");
                    Timestamp transaction_time = resultSet.getTimestamp("transaction_time");

                    User winnerUser = userCRUD.findById(winner_id).orElse(null);
                    User sellerUser = userCRUD.findById(seller_id).orElse(null);
                    if(!(winnerUser instanceof Buyer winner)){
                        System.out.println("Skipping transaction " + id + ": winner is not a Buyer");
                        continue;
                    }
                    if(!(sellerUser instanceof Seller seller)){
                        System.out.println("Skipping transaction " + id + ": seller is not a Seller");
                        continue;
                    }
                    Auction auction = auctionRepository.findById(auction_id).orElse(null);

                    transactionList.add(new Transaction(id, auction, winner, seller, final_amount, transaction_time.toLocalDateTime()));
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return transactionList;
    }


}
