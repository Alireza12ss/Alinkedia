package org.example.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.DataBaseHandler.PostDAO;
import org.example.Model.Post;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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

    public String getPosts(String email){ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(PostDAO.getPosts(email)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPost(int postId){
        return Objects.requireNonNull(PostDAO.getPost(postId)).toString();
    }
}
