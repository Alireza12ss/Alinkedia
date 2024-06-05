package org.example.DataBaseHandler;

import org.example.Model.Post;
import org.example.Model.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostDAO {
    public static String createPost( String email , String text , String mediaPath) throws SQLException {
        try {
            Connection connection = DAO.CreateConnection();

            String sql = "INSERT INTO posts (userId , text , date , time , mediaPath) VALUES (? , ? , ? , ? , ? ) ";

            PreparedStatement stmt = connection.prepareStatement(sql);


            stmt.setInt(1 , DAO.personId(email));
            stmt.setString(2 , text);
            stmt.setDate(3 , Date.valueOf(LocalDate.now()));
            stmt.setTime(4 , Time.valueOf(LocalTime.now()));
            stmt.setString(5 , mediaPath);

            stmt.executeUpdate();

            return "Post created";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Exception !";
        }

    }

    public static ArrayList<Post> getPosts(String email) {
        try {
            ArrayList<Post> posts = new ArrayList<>();

            Connection connection = DAO.CreateConnection();

            String sql = "SELECT * FROM posts where userId = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1 , DAO.personId(email));

            ResultSet set = stmt.executeQuery();

            while (set.next()){
                Post post = new Post(set.getInt("postId") , LikeDAO.PostLikes(set.getInt("postId")).size() , CommentDAO.getComments(set.getInt("postId")).size()
                        , set.getInt("userId") , set.getString("text")
                        , set.getDate("date") ,set.getTime("time") , set.getString("mediaPath")) ;
                posts.add(post);
            }

            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Post getPost(int id) {
        String sql = "Select * From posts where postId = ?";

        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return new Post(set.getInt("postId") , LikeDAO.PostLikes(set.getInt("postId")).size() , CommentDAO.getComments(set.getInt("postId")).size()
                        , set.getInt("userId") , set.getString("text")
                        , set.getDate("date") ,set.getTime("time") , set.getString("mediaPath")) ;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Post> findHashtags(String hashtag) {
        String sql = "Select * From posts";
        ArrayList<Post> posts = new ArrayList<>();
        try {
            Connection connection = DAO.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next() && extractHashtags(set.getString("text")).contains("#".concat(hashtag))) {
                posts.add(new Post(set.getInt("postId") , LikeDAO.PostLikes(set.getInt("postId")).size() , CommentDAO.getComments(set.getInt("postId")).size()
                        , set.getInt("userId") , set.getString("text")
                        , set.getDate("date") ,set.getTime("time") , set.getString("mediaPath"))) ;

            }
            return posts;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> extractHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(text);

        while(matcher.find()) {
            hashtags.add(matcher.group());
        }
        return hashtags;
    }
}
