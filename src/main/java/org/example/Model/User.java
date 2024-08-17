package org.example.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

public class User { // images hasn't been considered
    private String id;
    private String token;

    public User( String firstName, String lastName,  String email , String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getAdditionalName() {
        return additionalName;
    }
    private String birthDay;

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePathProfile() {
        return imagePathProfile;
    }

    public String getImagePathBackground() {
        return imagePathBackground;
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

    public ArrayList<Job> getExperience() {
        return experience;
    }

    public ArrayList<Education> getStudies() {
        return studies;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public User(String firstName, String lastName, String additionalName, String email , String title, int jobId , int educationId , int connectionInfoId , String imagePathProfile,
                String imagePathBackground, String country, String city, String profession , String password) {
        this.firstName = firstName;
        this.password = password;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.title = title;
        this.connectionInfoId = connectionInfoId;
        this.jobId = jobId;
        this.email = email;
        this.educationId = educationId;
        this.imagePathProfile = imagePathProfile;
        this.imagePathBackground = imagePathBackground;
        this.country = country;
        this.city = city;
        this.profession = profession;
    }

    public User(String firstName, String lastName, String additionalName, String email , String title, int jobId , int educationId , int connectionInfoId , String imagePathProfile,
                String imagePathBackground, String country, String city, String profession , String password , String birthday) {
        this.firstName = firstName;
        this.password = password;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.birthDay = birthday;
        this.title = title;
        this.connectionInfoId = connectionInfoId;
        this.jobId = jobId;
        this.email = email;
        this.educationId = educationId;
        this.imagePathProfile = imagePathProfile;
        this.imagePathBackground = imagePathBackground;
        this.country = country;
        this.city = city;
        this.profession = profession;
    }
    public void setToken(String token){
        this.token = token;
    }


    private String password;
    private String firstName;
    private String lastName;
    private String additionalName;
    private String email;

    public int getEducationId() {
        return educationId;
    }

    public int getConnectionInfoId() {
        return connectionInfoId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    private String title;

    private String imagePathProfile;
    private String imagePathBackground;
    private int jobId;
    private int educationId;
    private int connectionInfoId;
    private String country;

    public String getEmail() {
        return email;
    }

    private String city;
    private String profession;
    // + open to work or hiring or ...
    private ArrayList<Job> experience;
    private ArrayList<Education> studies;
    private ArrayList<Skill> skills;


    public int getJobId() {
        return jobId;
    }


    public String getToken() {
        return this.token;
    }
}
