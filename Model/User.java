package Model;

import java.util.ArrayList;

public class User { // images hasn't been considered
    private String id;

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    private String password;
    private String firstName;
    private String lastName;
    private String additionalName;
    private String email;
    private String title;
    private String imagePathProfile;
    private String imagePathBackground;
    private Job job;
    private Education education;
    private String country;
    private String city;
    private String profession;
    private ConnectionInfo connectionInfo;
    // + opentowork or hiring or ...
    private ArrayList<Job> experience;
    private ArrayList<Education> studies;
    private ArrayList<Skill> skills;

    public User(String id, String password, String firstName, String lastName, String additionalName, String title, Job job, Education education, String country, String city, String profession, ConnectionInfo connectionInfo) {
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.title = title;
        this.job = job;
        this.education = education;
        this.country = country;
        this.city = city;
        this.profession = profession;
        this.connectionInfo = connectionInfo;
    }
}
