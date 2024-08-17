package org.example.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DataBaseHandler.LikeDAO;
import org.example.DataBaseHandler.UserDAO;
import org.example.Model.User;


import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

public class UserController extends Controller{
    public String GetAllUser() {
        try {
            ArrayList<User> users = UserDAO.getAllUsers();
            assert users != null;
            return users.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User login(String email , String pass)  {
        User str;
        try {
            str = UserDAO.login(email , pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    public String CreateUser(String firstName , String lastName , String email , String pass) {
        try {
            return UserDAO.signUp(firstName , lastName , email , pass);
        }catch (SQLIntegrityConstraintViolationException e){
            return "SQLIntegrityConstraintViolationException";
        }catch (SQLException e){
            return "SQLException";
        }
    }

    public String searchUser(String firstName , String lastName) throws SQLException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(UserDAO.searchUsers(firstName , lastName)).toString();
    }

    public User getUniqueUser(String email) throws SQLException {
        return UserDAO.getUniqueUser(email);
    }

    public String updateUser(String email , String firstName, String lastName, String additionalName , String title, String imagePathProfile,
                             String imagePathBackground, String country, String city, String profession , String birthday){
        return UserDAO.updateProfile(email , firstName, lastName, additionalName, title, imagePathProfile, imagePathBackground,
                country , city, profession, birthday);
    }

}
