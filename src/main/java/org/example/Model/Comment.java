package org.example.Model;

import org.example.DataBaseHandler.UserDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class Comment {
    int postId;
    int userId;
    int commentId;
    String text;
    Date date;
    String mediaPath;
    Time time;

    public Comment(int postId, int userId, int commentId, String text, Date date, Time time , String mediaPath) {
        this.postId = postId;
        this.mediaPath = mediaPath;
        this.userId = userId;
        this.commentId = commentId;
        this.text = text;
        this.date = date;
        this.time = time;
    }


}
