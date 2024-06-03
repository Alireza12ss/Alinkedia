package org.example.DataBaseHandler;

import org.example.Model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostDAO {
    public static String createPost( String email , String text) throws SQLException {
        try {
            Connection connection = DatabaseHandler.CreateConnection();

            String sql = "INSERT INTO posts (userId , text ) VALUES (? , ?) ";

            PreparedStatement stmt = connection.prepareStatement(sql);


            stmt.setInt(1 , DatabaseHandler.personId(email));
            stmt.setString(2 , text);

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

            Connection connection = DatabaseHandler.CreateConnection();

            String sql = "SELECT * FROM posts where userId = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);


            stmt.setInt(1 , DatabaseHandler.personId(email));

            ResultSet set = stmt.executeQuery();

            while (set.next()){
                Post post = new Post(set.getInt("userId") , set.getString("text"));
                posts.add(post);
            }

            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
