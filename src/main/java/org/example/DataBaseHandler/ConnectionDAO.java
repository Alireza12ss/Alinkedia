package org.example.DataBaseHandler;

import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionDAO {
    public static ArrayList<User> Allconnnections(String email) {
        String sql = "SELECT * FROM connections WHERE accepted = 1 AND (receiverId = ? OR senderId = ?) ";
        try {
            ArrayList<User> users = new ArrayList<>();
            Connection connection = DAO.CreateConnection();

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

    public static ArrayList<User> requestedConnections(String email) {
        String sql = "SELECT * FROM connections WHERE accepted = 0 AND (receiverId = ? OR senderId = ?) ";
        try {
            ArrayList<User> users = new ArrayList<>();
            Connection connection = DAO.CreateConnection();

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

    public static String sendRequest(String senderEmail , String receiverEmail , String description) {
        String sql = "INSERT INTO connections (senderId , receiverId , accepted , description) VALUES (? , ? , 0 , ?)";
        try {
            Connection connection = DAO.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(senderEmail));
            statement.setInt(2 , personId(receiverEmail));
            statement.setString(3 , description);
            statement.executeUpdate();

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

    private static int personId(String email){
        String sql = "Select * From users WHERE email = ?";
        try {
            Connection connection = DAO.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                return set.getInt("id");
            }
            return 0;
        }catch (SQLException e){
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String personEmail(int id){
        String sql = "Select * From users WHERE id = ?";
        try {
            Connection connection = DAO.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , id);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                return set.getString("email");
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
