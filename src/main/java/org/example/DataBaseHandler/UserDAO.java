package org.example.DataBaseHandler;

import org.example.JWTgenerator.JwtGenerator;
import org.example.Model.User;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDAO extends DatabaseHandler{
    public static String signUp( String firstName , String lastName , String additionalName , String email , String pass) throws SQLException {
        try {
            Connection connection = DatabaseHandler.CreateConnection();

            String sql = "INSERT INTO users (firstName , lastName , additionalName , email , password) VALUES (? , ? , ? , ? , ?) ";

            PreparedStatement pstmt = connection.prepareStatement(sql);


            pstmt.setString(1 , firstName);
            pstmt.setString(2 , lastName);
            pstmt.setString(3 , additionalName);
            //check email
            if (DataCheck.CheckEmail(email)){
                pstmt.setString(4 , email);
            }else {
                pstmt.close();
                return "invalid email";
            }
            //check password
            if (DataCheck.CheckPass(pass)){
                pstmt.setString(5 , pass);
            }else {
                pstmt.close();
                return "invalid pass\n" + pass +
                        "your password should contain at least 8 digits or letters";
            }

            pstmt.executeUpdate();

            return "User sign up";

        }catch (SQLIntegrityConstraintViolationException e){
            e.toString();
            return "Primary key constraint violated";
        }catch (SQLException e) {
            e.printStackTrace();
            return "Exception !";
        }

    }

    public static String createPost( String userEmail , String txt) throws SQLException {
        try {
            Connection connection = DatabaseHandler.CreateConnection();

            String sql = "INSERT INTO posts (userEmail , txt ) VALUES (? , ?) ";

            PreparedStatement pstmt = connection.prepareStatement(sql);


            pstmt.setString(1 , userEmail);
            pstmt.setString(2 , txt);

            pstmt.executeUpdate();

            return "Post created";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Exception !";
        }

    }

    public static ArrayList<User> getAllUsers() throws SQLException {
        String sql = "Select * From users";
        try {
            ArrayList<User> users = new ArrayList<>();

            Connection connection = DatabaseHandler.CreateConnection();

            Statement statement = connection.prepareStatement(sql);

            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                User user = new User(set.getString("firstname") ,
                        set.getString("lastname") ,
                        set.getString("additionalname") ,
                        set.getString("email") ,
                        set.getString("password"));

                users.add(user);
            }

            return users;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static String  getUniqueUser(String email) throws SQLException {
        String sql = "Select * From users where email = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                User user = new User(set.getString("firstName"),
                        set.getString("lastName"),
                        set.getString("additionalName"),
                        set.getString("email"),
                        set.getString("password"));
                break;
            }
            HashMap<String, Object> claims = new HashMap<>();


            claims.put("email", email);

            JwtGenerator generator = new JwtGenerator();

            String token = generator.createToken(claims , 60);

            return token;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
