package org.example.DataBaseHandler;

import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FollowDAO {

    public static String follow(String followerEmail, String followingEmail) {
        String sql = "INSERT INTO follow (followerId , followingId) values (? , ?)";
        try {
            Connection connection = DatabaseHandler.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(followerEmail));
            statement.setInt(2 , personId(followingEmail));
            statement.executeUpdate();

            return "followed!";
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
            Connection connection = DatabaseHandler.CreateConnection();

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
            Connection connection = DatabaseHandler.CreateConnection();

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

    public static ArrayList<User> getFollowers(String email) {
        String sql = "SELECT * FROM follow WHERE followingId = ?";
        try {
            ArrayList<User> users = new ArrayList<>();
            Connection connection = DatabaseHandler.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(email));
            ResultSet set = statement.executeQuery();
            while (set.next()){
                users.add(UserDAO.getUniqueUser(personEmail(set.getInt("followerId"))));
            }

            return users;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<User> getFollowing(String email) {
        String sql = "SELECT * FROM follow WHERE followerId = ?";
        try {
            ArrayList<User> users = new ArrayList<>();
            Connection connection = DatabaseHandler.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(email));
            ResultSet set = statement.executeQuery();
            while (set.next()){
                User us = UserDAO.getUniqueUser(personEmail(set.getInt("followingId")));
                if (us != null )
                users.add(UserDAO.getUniqueUser(personEmail(set.getInt("followingId"))));
            }

            return users;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
