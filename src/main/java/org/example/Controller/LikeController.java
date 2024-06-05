package org.example.Controller;

import org.example.DataBaseHandler.LikeDAO;

import java.util.Objects;

public class LikeController extends Controller{
    public static String AllLikes(String email) {
        return Objects.requireNonNull(LikeDAO.AllLikes(email)).toString();
    }

    public static String like(String email , int postId) {
        return LikeDAO.like(email , postId);
    }

    public static String postLike(int postId) {
        return Objects.requireNonNull(LikeDAO.PostLikes(postId)).toString();
    }
}
