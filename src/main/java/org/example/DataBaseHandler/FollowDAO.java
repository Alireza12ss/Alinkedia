package org.example.DataBaseHandler;

import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import static org.example.DataBaseHandler.DAO.personEmail;
import static org.example.DataBaseHandler.DAO.personId;

public class FollowDAO {

    public static String follow(String followerEmail, String followingEmail) {
        String sql = "INSERT INTO follows (followerId , followingId) values (? , ?)";
        try {
            Connection connection = DAO.CreateConnection();

            assert connection != null;
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

    public static ArrayList<User> getFollowers(String email) {
        String sql = "SELECT * FROM follows WHERE followingId = ?";
        try {
            ArrayList<User> users = new ArrayList<>();
            Connection connection = DAO.CreateConnection();

            assert connection != null;
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

    public static HashSet<User> getFollowing(String email) {
        String sql = "SELECT * FROM follows WHERE followerId = ?";
        try {
            HashSet<User> users = new HashSet<>();
            Connection connection = DAO.CreateConnection();

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(email));
            ResultSet set = statement.executeQuery();
            while (set.next()){
                User us = UserDAO.getUniqueUser(personEmail(set.getInt("followingId")));
                if (us != null ) {
                    users.add(UserDAO.getUniqueUser(personEmail(set.getInt("followingId"))));
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

}
