package org.example.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DataBaseHandler.LikeDAO;

import java.util.Objects;

public class LikeController extends Controller{
    public static String AllLikes(String email) {
        return Objects.requireNonNull(LikeDAO.AllLikes(email)).toString();
    }

    public static String like(String email , int postId) {
        return LikeDAO.like(email , postId);
    }

    public static String postLike(int postId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(LikeDAO.PostLikes(postId)).toString();
    }
}
