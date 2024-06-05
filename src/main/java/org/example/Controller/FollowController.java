package org.example.Controller;

import org.example.DataBaseHandler.FollowDAO;

public class FollowController extends Controller{
    public static String follow(String followerEmail, String followingEmail){
        return FollowDAO.follow(followerEmail , followingEmail);
    }

    public static String getFollowers(String email) {
        return FollowDAO.getFollowers(email).toString();
    }

    public static String getFollowing(String email) {
        return FollowDAO.getFollowing(email).toString();
    }

}
