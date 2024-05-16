package User;

import java.util.ArrayList;

public class User { // images hasn't been considered
    private String id;
    private String password;
    private String firstName;
    private String lastName;
    private String additionalName;
    private String description;
    private Job job;
    private Education education;
    private String country;
    private String city;
    private String profession;
    private ConnectionInfo connectionInfo;
    // + opentowork or hiring or ...
    private ArrayList<Job> experience;
    private ArrayList<Education> studies;
    private ArrayList<Certificate> certificates;
    // + skills
    private ArrayList<Organization> organizations;
}
