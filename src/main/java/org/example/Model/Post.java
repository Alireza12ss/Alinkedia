package org.example.Model;

public class Post {
    String userEmail;
    String txt;

    public Post(String userEmail, String txt) {
        this.userEmail = userEmail;
        this.txt = txt;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
