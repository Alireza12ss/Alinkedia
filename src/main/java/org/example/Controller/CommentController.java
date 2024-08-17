package org.example.Controller;

import org.example.DataBaseHandler.CommentDAO;

import java.util.Objects;

public class CommentController {
    public static String createComment(int userId , int postId , String text , String mediaPath){
        return CommentDAO.createComment(userId , postId , text , mediaPath);
    }

    public static String getComments(int postId){
        return Objects.requireNonNull(CommentDAO.getComments(postId)).toString();
    }
}
