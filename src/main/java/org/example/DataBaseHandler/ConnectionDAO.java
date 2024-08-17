package org.example.DataBaseHandler;

import org.example.Handler.ConnectionHandler;
import org.example.Model.User;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.example.DataBaseHandler.DAO.personEmail;
import static org.example.DataBaseHandler.DAO.personId;

public class ConnectionDAO {
    public static ArrayList<User> AllConnnections(String email) {
        String sql = "SELECT * FROM connections WHERE accepted = 1 AND (receiverId = ? OR senderId = ?) ";
        return getUsers(email, sql);
    }

    public static ArrayList<User> requestedConnections(String email) {
        String sql = "SELECT * FROM connections WHERE accepted = 0 AND (receiverId = ? OR senderId = ?) ";
        return getUsers(email, sql);
    }

    @Nullable
    private static ArrayList<User> getUsers(String email, String sql) {
        try {
            ArrayList<User> users = new ArrayList<>();
            Connection connection = DAO.CreateConnection();

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(email));
            statement.setInt(2 , personId(email));
            ResultSet set = statement.executeQuery();
            while (set.next()){
                if ((set.getInt("senderId") == personId(email)) && UserDAO.getUniqueUser(personEmail(set.getInt("receiverId"))) != null){
                    users.add(UserDAO.getUniqueUser(personEmail(set.getInt("receiverId"))));
                }else if ((set.getInt("receiverId") == personId(email)) && UserDAO.getUniqueUser(personEmail(set.getInt("senderId"))) != null){
                    users.add(UserDAO.getUniqueUser(personEmail(set.getInt("senderId"))));
                }
            }

            return users;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sendRequest(String senderEmail , String receiverEmail , String description){
        if (isConnectedT(receiverEmail , senderEmail)){
            return deleteConnection(receiverEmail , senderEmail);
        }else{
            return sendRequestTo(senderEmail , receiverEmail , description);
        }
    }

    public static String sendRequestTo(String senderEmail , String receiverEmail , String description) {
        String sql = "INSERT INTO connections (senderId , receiverId , accepted , description) VALUES (? , ? , 0 , ?)";
        try {
            Connection connection = DAO.CreateConnection();

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(senderEmail));
            statement.setInt(2 , personId(receiverEmail));
            statement.setString(3 , description);
            statement.executeUpdate();
            ConnectionHandler.setResponseCode(200);
            return "Request send successfully";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String acceptRequest(String receiverEmail , String senderEmail){
        String sql = "UPDATE connections SET accepted = 1 where (receiverId = ? AND senderId = ?) OR (receiverId = ? AND senderId = ?)";
        try {
            Connection connection = DAO.CreateConnection();

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(senderEmail));
            statement.setInt(2 , personId(receiverEmail));
            statement.setInt(4 , personId(senderEmail));
            statement.setInt(3 , personId(receiverEmail));
            statement.executeUpdate();

            return "Request accepted successfully";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String deleteConnection(String receiverEmail , String senderEmail) {
        String sql = "delete from connections where (receiverId = ? AND senderId = ?) OR (receiverId = ? AND senderId = ?)";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(receiverEmail));
            statement.setInt(2 , personId(senderEmail));
            statement.executeUpdate();
            connection.close();
            ConnectionHandler.setResponseCode(201);
            return "connect deleted!";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isConnected(String senderEmail , String receiverEmail ){
        if (isConnectedT(receiverEmail , senderEmail)){
            ConnectionHandler.setResponseCode(200);
            return true;
        }else if (isConnectedf(receiverEmail , senderEmail)){
            ConnectionHandler.setResponseCode(201);
            return true;
        }
        ConnectionHandler.setResponseCode(203);
        return false;
    }
    public static boolean isConnectedT(String receiverEmail , String senderEmail){
        String sql = "select * from connections where (receiverId = ? AND senderId = ?) OR (receiverId = ? AND senderId = ?) AND accepted = 1";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(receiverEmail));
            statement.setInt(2 , personId(senderEmail));
            return statement.executeQuery().next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isConnectedf(String receiverEmail , String senderEmail){
        String sql = "select * from connections where (receiverId = ? AND senderId = ?) OR (receiverId = ? AND senderId = ?) AND accepted = 0";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(receiverEmail));
            statement.setInt(2 , personId(senderEmail));
            return statement.executeQuery().next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
