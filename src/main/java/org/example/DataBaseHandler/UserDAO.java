package org.example.DataBaseHandler;

import org.example.JWTgenerator.JwtGenerator;
import org.example.Model.User;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDAO extends DatabaseHandler{
    public static String signUp( String firstName , String lastName ,  String email , String pass) throws SQLException {
        try {
            Connection connection = DatabaseHandler.CreateConnection();

            String sql = "INSERT INTO users (firstName , lastName , email , password) VALUES (? , ? , ? , ?) ";

            PreparedStatement pstmt = connection.prepareStatement(sql);


            pstmt.setString(1 , firstName);
            pstmt.setString(2 , lastName);
            //check email
            if (DataCheck.CheckEmail(email)){
                pstmt.setString(4 , email);
            }else {
                pstmt.close();
                return "invalid email";
            }
            //check password
            if (DataCheck.CheckPass(pass)){
                String encryptPass = DataCheck.encrypt(pass);
                pstmt.setString(4 , encryptPass);
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String showProfile(String email) throws SQLException {
        User user = getUniqueUser(email);
        return user.showProfile();
    }

    public static String showJob(String email) throws SQLException {
        User user = getUniqueUser(email);
        return JobDAO.getJob(user.getJobId()).toString();
    }

    public static String addJob(String email ,String title, String employmentType,  String companyName, String location,
                                String locationType, boolean activity, Date startToWork, Date endToWork, String description) throws SQLException {
        return JobDAO.addJob(email , title, employmentType,  companyName,location,
                locationType,activity,  startToWork, endToWork, description);

    }

    public static void updateJobId(String email, int jobId){
        String sql = "UPDATE users SET jobId = ? where email = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , jobId);
            statement.setString(2 , email);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String updateProfile(String email , String additionalName , String title , String imagePathProfile ,
                                String imagePathBackground , String country , String city , String profession ){

        String sql = "UPDATE users SET additionalName = ? , title = ? ,imagePathProfile = ?" +
                " ,imagePathBackground = ? ,country = ? ,city = ? ,profession = ?  WHERE email = ?;";

        try {
            Connection connection = DatabaseHandler.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,additionalName);
            statement.setString(2,title);
            statement.setString(3,imagePathProfile);
            statement.setString(4,imagePathBackground);
            statement.setString(5,country);
            statement.setString(6,city);
            statement.setString(7,profession);
            statement.setString(8,email);

            statement.executeQuery(sql);

            return "User Updated";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                String decryptedPass = DataCheck.encrypt(set.getString("password"));
                User user = new User(set.getString("firstname") ,
                        set.getString("lastname") ,
                        set.getString("email") ,
                        decryptedPass);

                users.add(user);
            }

            return users;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String login(String email) throws SQLException {
        String sql = "Select * From users where email = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                HashMap<String, Object> claims = new HashMap<>();

                claims.put("email", email);

                JwtGenerator generator = new JwtGenerator();

                String token = generator.createToken(claims, 60);

                return token;
            }else {
                return "User not found";
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static User getUniqueUser(String email) throws SQLException {
        String sql = "Select * From users where email = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            ResultSet set = statement.executeQuery();
            User user = null;
            if (set.next()) {
                user = new User(set.getString("firstName"),
                        set.getString("lastName"),
                        set.getString("additionalName"),
                        set.getString("title"),
                        set.getInt("jobId"),
                        set.getString("imagePathProfile"),
                        set.getString("imagePathBackground"),
                        set.getString("country"),
                        set.getString("city"),
                        set.getString("profession"));
                return user;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String deleteAllUsers() {
        String sql = "delete From users";
        try {
            ArrayList<User> users = new ArrayList<>();

            Connection connection = DatabaseHandler.CreateConnection();

            Statement statement = connection.prepareStatement(sql);

            statement.executeQuery(sql);

            return "All users deleted";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String deleteUser(String email) {
        String sql = "delete From users where email = ?";
        try {

            Connection connection = DatabaseHandler.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            statement.executeQuery(sql);

            return "user removed";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
