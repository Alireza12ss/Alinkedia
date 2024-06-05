package org.example.Controller;

import org.example.DataBaseHandler.CommentDAO;

public class CommentController {
    public static String createComment(int userId , int postId , String text , String mediaPath){
        return CommentDAO.createComment(userId , postId , text , mediaPath);
    }

    public static String getComments(int postId){
        return CommentDAO.getComments(postId).toString();
    }
}
