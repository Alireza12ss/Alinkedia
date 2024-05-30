package org.example.Controller;


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

    public String GetUniqueUser(String email)  {
        String str;
        try {
            str = UserDAO.getUniqueUser(email);
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


    public String deleteUser(String userId) {
        return UserDAO.deleteUser();
    }

    public String deleteAllUsers() {
        return UserDAO.deleteAllUsers();
    }
}
