package org.example.DataBaseHandler;

import org.example.Model.Post;
import org.example.Model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class FeedDAO {
    public static ArrayList<Post> feed(int id){
        HashSet<User> users = followingAndConnections(DAO.personEmail(id));
        ArrayList<Post> posts = new ArrayList<>();
        for(User user : users){
            posts.addAll(PostDAO.getPosts(user.getEmail()));
        }
        return posts;
    }

    public static HashSet<User> followingAndConnections(String email){
        HashSet<User> res = FollowDAO.getFollowing(email);
        assert res != null;
        res.addAll(Objects.requireNonNull(ConnectionDAO.AllConnnections(email)));
        return res;
    }
}
