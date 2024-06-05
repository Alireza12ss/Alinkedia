package org.example.Model;

import org.example.DataBaseHandler.UserDAO;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

public class Post {
    int postId;
    int likes;
    int comments;
    int userId;
    String text;
    Date date;
    String mediaPath;
    Time time;

    public Post(int postId, int likes, int comments,  int userId, String text, Date date, Time time , String mediaPath) {
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

    private static String getName(int userId) throws SQLException {
        User user = UserDAO.getUniqueUser(UserDAO.personEmail(userId));
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public String toString() {
        try {
            return mediaPath + "\n" + getName(userId) + "\nText : " + text + "\n" + likes + " like -  " + comments + "comment" +
            "\n" +date + " - " + time + "\n-------------\n";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
