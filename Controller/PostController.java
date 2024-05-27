package org.example.Controller;

import org.example.DataBaseHandler.UserDAO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class PostController extends Controller{
    public String createPost(String userEmail , String txt){
        try {
            return UserDAO.createPost(userEmail , txt);
        }catch (SQLIntegrityConstraintViolationException e){
            return "SQLIntegrityConstraintViolationException";
        }catch (SQLException e){
            return "SQLException";
        }
    }
}
