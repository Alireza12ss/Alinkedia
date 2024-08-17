package org.example.Model;

import org.example.DataBaseHandler.UserDAO;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Post {
    int postId;
    int likes;
    int comments;
    int userId;
    String text;
    String date;
    String mediaPath;

    public int getPostId() {
        return postId;
    }

    public int getComments() {
        return comments;
    }

    public String getDate() {
        return date;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public String getTime() {
        return time;
    }

    String time;

    public Post(int postId, int likes, int comments,  int userId, String text, String date, String time , String mediaPath) {
        this.postId = postId;
        this.mediaPath = mediaPath;
        this.comments = comments;
        this.likes = likes;
        this.userId = userId;
        this.text = text;
        this.date = date;
        this.time = time;
    }

    public int getLikes() {
        return likes;
    }

    public int getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }



}
