package org.example.DataBaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostDAO {
    public static String createPost( String userEmail , String txt) throws SQLException {
        try {
            Connection connection = DatabaseHandler.CreateConnection();

            String sql = "INSERT INTO posts (userEmail , txt ) VALUES (? , ?) ";

            PreparedStatement stmt = connection.prepareStatement(sql);


            stmt.setString(1 , userEmail);
            stmt.setString(2 , txt);

            stmt.executeUpdate();

            return "Post created";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Exception !";
        }

    }
}
