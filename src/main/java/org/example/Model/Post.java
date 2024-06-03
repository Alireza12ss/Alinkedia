package org.example.Model;

public class Post {
    int userId;
    String text;

    public Post(int userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "\n" + userId + "\nText : " + text + "\n-------------\n";
    }
}
