package org.example.DataBaseHandler;

import org.example.Model.Post;
import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.example.DataBaseHandler.DAO.personEmail;
import static org.example.DataBaseHandler.DAO.personId;

public class LikeDAO {
    public static ArrayList<Post> AllLikes(String email) {
        ArrayList<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM likes WHERE likerId = ?";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(email));
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                posts.add(PostDAO.getPost(set.getInt("postId")));
            }
            return posts;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<User> PostLikes(int postId) {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM likes WHERE postId = ?";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , postId);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                users.add(UserDAO.getUniqueUser(personEmail(set.getInt("likerId"))));
            }
            return users;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String like(String email , int postId){
        if (isLiked(email , postId)){
            return deleteLike(email , postId);
        }
        return saveLike(email , postId);
    }
    public static String saveLike(String email , int postId) {
        String sql = "INSERT INTO likes(likerId , postId) values (? , ?)";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(email));
            statement.setInt(2 , postId);
            statement.executeUpdate();
            return "liked!";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String deleteLike(String email , int postId) {
        String sql = "delete from likes where likerId = ? AND postId = ?";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(email));
            statement.setInt(2 , postId);
            statement.executeUpdate();
            return "like deleted!";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isLiked(String email , int postId){
        String sql = "select * from likes where likerId = ? AND postId = ?";
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , personId(email));
            statement.setInt(2 , postId);
            return statement.executeQuery().next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}