package org.example.DataBaseHandler;

import org.example.Handler.FollowHandler;
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

    public static String follow(String followerEmail, String followingEmail) throws SQLException {
        if (!isFollowed(followerEmail , followingEmail)){
            System.out.println(true);
            return followAction(followerEmail, followingEmail);
        }else {
            System.out.println(false);
            return deleteFollow(personId(followerEmail) , personId(followingEmail));
        }
    }
    public static String followAction(String followerEmail, String followingEmail) {
        String sql = "INSERT INTO follows (followerId , followingId) values (? , ?)";
        try {
            Connection connection = DAO.CreateConnection();

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(followerEmail));
            statement.setInt(2 , personId(followingEmail));
            statement.executeUpdate();
            connection.close();
            FollowHandler.setResponseCodeFollowHandler(200);
            return "followed!";
        }catch (SQLException e){
            e.printStackTrace();
            FollowHandler.setResponseCodeFollowHandler(400);
            return null;
        } catch (Exception e) {
            FollowHandler.setResponseCodeFollowHandler(400);
            throw new RuntimeException(e);
        }
    }
    public static String deleteFollow(int followerId , int followingId) {
        String sql = "delete from follows where followerId = ? AND followingId = ?";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , followerId);
            statement.setInt(2 , followingId);
            statement.executeUpdate();
            connection.close();
            FollowHandler.setResponseCodeFollowHandler(201);
            return "follow deleted!";
        }catch (SQLException e){
            FollowHandler.setResponseCodeFollowHandler(400);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            FollowHandler.setResponseCodeFollowHandler(400);
            throw new RuntimeException(e);
        }
    }
    public static boolean isFollowed(String followerEmail, String followingEmail) throws SQLException {
        String sql = "select * from follows where followerId = ? AND followingId = ?";
            Connection connection = DAO.CreateConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(followerEmail));
            statement.setInt(2 , personId(followingEmail));
            return statement.executeQuery().next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            connection.close();
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
            connection.close();
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
            connection.close();
            return users;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
