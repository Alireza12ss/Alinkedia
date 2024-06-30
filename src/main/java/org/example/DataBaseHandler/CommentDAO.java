package org.example.DataBaseHandler;

import org.example.Model.Comment;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CommentDAO {

    public static String createComment(int userId, int postId, String text , String mediaPath) {
        String sql = "INSERT INTO comments (userId , postId , text , date , time , mediaPath) VALUES (? , ? , ? , ? , ? , ?)";
        try {
            Connection connection = DAO.CreateConnection();

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , userId);
            statement.setInt(2 , postId);
            statement.setString(3 , text);
            statement.setTime(5 , Time.valueOf(LocalTime.now()));
            statement.setDate(4, Date.valueOf(LocalDate.now()));
            statement.setString(6 , mediaPath);
            statement.executeUpdate();

            return "your comment send!";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Comment> getComments(int postId){
        String sql = "SELECT * FROM comments where postId = ?";
        try {
            ArrayList<Comment> comments = new ArrayList<>();
            Connection connection = DAO.CreateConnection();

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , postId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Comment comment = new Comment(set.getInt("postId") , set.getInt("userId")
                        , set.getInt("commentId") , set.getString("text")
                        , set.getDate("date") , set.getTime("time") , set.getString("mediaPath"));
                comments.add(comment);
            }
            return comments;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
