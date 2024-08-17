package org.example.DataBaseHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Handler.UserHandler;
import org.example.JWTgenerator.JwtGenerator;
import org.example.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UserDAO extends DAO {
    public static String signUp( String firstName , String lastName ,  String email , String pass) throws SQLException {
        try {
            Connection connection = DAO.CreateConnection();

            String sql = "INSERT INTO users (firstName , lastName , email , password) VALUES (? , ? , ? , ?) ";

            assert connection != null;
            PreparedStatement pstmt = connection.prepareStatement(sql);


            pstmt.setString(1 , firstName);
            pstmt.setString(2 , lastName);
            //check email
            if (DataCheck.CheckEmail(email)){
                pstmt.setString(3 , email);
            }else {
                pstmt.close();
                UserHandler.setResponseCode(406);
                return "invalid email";
            }
            //check password
            if (pass.equals("GitHub Login")){
                pstmt.setString(4 , "GitHub Login");
            }
            else if (DataCheck.CheckPass(pass)){
                String encryptPass = DataCheck.encrypt(pass);
                pstmt.setString(4 , encryptPass);
            }else {
                UserHandler.setResponseCode(406);
                pstmt.close();
                return "invalid pass\n" + pass +
                        "your password should contain at least 8 digits or letters";
            }

            if (DataCheck.uniqueEmail(email)) {
                pstmt.executeUpdate();
                UserHandler.setResponseCode(201);
                HashMap<String, Object> claims = new HashMap<>();

                claims.put("email", email);

                JwtGenerator generator = new JwtGenerator();
                UserHandler.setResponseCode(202);
                User user = getUniqueUser(email);
                user.setToken(generator.createToken(claims, 60));
                System.out.println(user.getToken());
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(user);
                return json;
            }else {
                UserHandler.setResponseCode(403);
                return "this email already have account";
            }
        }catch (SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            return "Primary key constraint violated";
        }catch (SQLException e) {
            e.printStackTrace();
            return "Exception !";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static User login(String email , String pass) throws SQLException {
        String sql = "Select * From users where email = ?";

        try {
            Connection connection = DAO.CreateConnection();
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                if (!pass.equals("GitHub Login") && !Objects.requireNonNull(set.getString("password").equals(DataCheck.encrypt(pass)))){
                    UserHandler.setResponseCode(406);
                    return null;
                }

                HashMap<String, Object> claims = new HashMap<>();

                claims.put("email", email);

                JwtGenerator generator = new JwtGenerator();
                UserHandler.setResponseCode(202);
                User user = getUniqueUser(email);
                user.setToken(generator.createToken(claims, 60));
                System.out.println(user.getToken());
                connection.close();
                return user;
            }else {
                UserHandler.setResponseCode(404);
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    //Profile
    public static String showProfile(String email) throws SQLException {
        User user = getUniqueUser(email);
        assert user != null;
        return user.toString();
    }

    public static String updateProfile(String email , String firstName, String lastName, String additionalName , String title, String imagePathProfile,
                                       String imagePathBackground, String country, String city, String profession  , String birthday){

        String sql = "UPDATE users SET firstName = ?, lastName = ?, additionalName = ?, title = ?, imagePathProfile = ?, imagePathBackground = ?, country = ?, city = ?, profession = ?, birthday = ? WHERE email = ?";

        try {
            Connection connection = DAO.CreateConnection();
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, additionalName);
            statement.setString(4, title);
            statement.setString(5, imagePathProfile);
            statement.setString(6, imagePathBackground);
            statement.setString(7, country);
            statement.setString(8, city);
            statement.setString(9, profession);
            statement.setDate(10, birthday.equals("") ? null : Date.valueOf(birthday));
            statement.setString(11, email);

            statement.executeUpdate();

            return "User Updated";
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    //job
    public static String showJob(String email) throws SQLException {
        User user = getUniqueUser(email);
        assert user != null;
        return Objects.requireNonNull(JobDAO.getJob(user.getJobId())).toString();
    }

    public static void updateJobId(String email, int jobId){
        String sql = "UPDATE users SET jobId = ? where email = ?";

        try {
            Connection connection = DAO.CreateConnection();
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , jobId);
            statement.setString(2 , email);
            statement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //connectionInfo
    public static String showConnectionInfo(String email) throws SQLException {
        User user = getUniqueUser(email);
        assert user != null;
        return Objects.requireNonNull(ConnectionInfoDAO.getConnectionInfo(user.getConnectionInfoId())).toString();
    }

    public static void updateConnectionInfoId(String email, int ConnectionInfo){
        String sql = "UPDATE users SET ConnectionInfoId = ? where email = ?";

        try {
            Connection connection = DAO.CreateConnection();
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , ConnectionInfo);
            statement.setString(2 , email);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //eduction
    public static String showEducation(String email) throws SQLException {
        User user = getUniqueUser(email);
        assert user != null;
        return Objects.requireNonNull(EducationDAO.getEducation(user.getEducationId())).toString();
    }

    public static void updateEducationId(String email, int EducationId){
        String sql = "UPDATE users SET educationId = ? where email = ?";

        try {
            Connection connection = DAO.CreateConnection();
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , EducationId);
            statement.setString(2 , email);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    //search
    public static ArrayList<User> searchUsers(String firstName , String lastName) {
        String first = "%".concat(firstName);
        String last = "%".concat(lastName);
        String sql = "Select * From users WHERE firstName LIKE ? or lastName LIKE ?";
        try {
            ArrayList<User> users = new ArrayList<>();

            Connection connection = DAO.CreateConnection();

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , first.concat("%"));
            statement.setString(2 , last.concat("%"));
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                String decryptedPass = DataCheck.encrypt(set.getString("password"));
                User user = new User(set.getString("firstname") ,
                        set.getString("lastname") ,
                        set.getString("email") ,
                        decryptedPass);

                users.add(user);
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





    public static ArrayList<User> getAllUsers() throws SQLException {
        String sql = "Select * From users";
        try {
            ArrayList<User> users = new ArrayList<>();

            Connection connection = DAO.CreateConnection();

            assert connection != null;
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
            connection.close();

            return users;
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
            Connection connection = DAO.CreateConnection();
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            ResultSet set = statement.executeQuery();
            User user;
            if (set.next()) {
                user = new User(set.getString("firstName"),
                        set.getString("lastName"),
                        set.getString("additionalName"),
                        set.getString("email"),
                        set.getString("title"),
                        set.getInt("jobId"),
                        set.getInt("educationId"),
                        set.getInt("connectionInfoId"),
                        set.getString("imagePathProfile"),
                        set.getString("imagePathBackground"),
                        set.getString("country"),
                        set.getString("city"),
                        set.getString("profession"),
                        set.getString("password"));
                connection.close();
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

            Connection connection = DAO.CreateConnection();

            assert connection != null;
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

            Connection connection = DAO.CreateConnection();

            assert connection != null;
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
