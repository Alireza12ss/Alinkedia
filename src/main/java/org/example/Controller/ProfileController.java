package org.example.Controller;

import org.example.DataBaseHandler.UserDAO;

import java.sql.SQLException;

public class ProfileController extends Controller{
    public static String showProfile(String email) throws SQLException {
        return UserDAO.showProfile(email);
    }

    public static String showJob(String email) throws SQLException {
        return UserDAO.showJob(email);
    }
    public String updateProfile(String additionalName , String title , String imagePathProfile , String imagePathBackground , String country , String city , String profession ){

        return additionalName;
    }

    public String deleteUser(String email) {
        return UserDAO.deleteUser(email);
    }

    public String deleteAllUsers() {
        return UserDAO.deleteAllUsers();
    }
}
