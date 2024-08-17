package org.example.Controller;

import org.example.DataBaseHandler.FollowDAO;

import java.sql.SQLException;
import java.util.Objects;

public class FollowController extends Controller{
    public static String follow(String followerEmail, String followingEmail) throws SQLException {
        return FollowDAO.follow(followerEmail , followingEmail);
    }

    public static String getFollowers(String email) {
        return Objects.requireNonNull(FollowDAO.getFollowers(email)).toString();
    }

    public static String getFollowing(String email) {
        return Objects.requireNonNull(FollowDAO.getFollowing(email)).toString();
    }

}
