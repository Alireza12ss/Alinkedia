package org.example.Controller;

import org.example.DataBaseHandler.PostDAO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

public class PostController extends Controller{
    public String createPost(String userEmail , String txt , String mediaPath){
        try {
            return PostDAO.createPost(userEmail , txt , mediaPath);
        }catch (SQLIntegrityConstraintViolationException e){
            return "SQLIntegrityConstraintViolationException";
        }catch (SQLException e){
            return "SQLException";
        }
    }

    public String getPosts(String email) {
        return Objects.requireNonNull(PostDAO.getPosts(email)).toString();
    }

    public String getPost(int postId){
        return Objects.requireNonNull(PostDAO.getPost(postId)).toString();
    }
}
