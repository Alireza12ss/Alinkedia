package org.example.Model;

import java.util.ArrayList;

public class User { // images hasn't been considered
//    private String id;
    private String firstName;
    private String lastName;
    private String additionalName;
    private String email;
    private String title;
    private String token;
    private String imagePathProfile;
    private String imagePathBackground;
    private int jobId;
    private int educationId;
    private int connectionInfoId;

//    public String getId() {
//        return id;
//    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getToken() {
        return token;
    }

    public String getImagePathProfile() {
        return imagePathProfile;
    }

    public String getImagePathBackground() {
        return imagePathBackground;
    }

    public int getJobId() {
        return jobId;
    }

    public int getEducationId() {
        return educationId;
    }

    public int getConnectionInfoId() {
        return connectionInfoId;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getProfession() {
        return profession;
    }

    public User(String firstName, String lastName, String additionalName,
                String email, String title, String token, String imagePathProfile, String imagePathBackground,
                int jobId, int educationId, int connectionInfoId, String country, String city, String profession) {
//        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.email = email;
        this.title = title;
        this.token = token;
        this.imagePathProfile = imagePathProfile;
        this.imagePathBackground = imagePathBackground;
        this.jobId = jobId;
        this.educationId = educationId;
        this.connectionInfoId = connectionInfoId;
        this.country = country;
        this.city = city;
        this.profession = profession;
    }

    private String country;
    private String city;
    private String profession;



}
