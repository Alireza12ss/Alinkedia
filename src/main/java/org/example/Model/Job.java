package org.example.Model;

import java.util.ArrayList;
import java.sql.Date;

public class Job {
    private String title;
    private String employmentType;
    private String companyName;
    private String location;
    private String locationType;
    private boolean activity;
    // date with date picker
    private Date startToWork;
    private Date endToWork;
    private String description;
    private ArrayList<Skill> skills;

    public Job(String title, String employmentType,  String companyName, String location,
               String locationType, boolean activity, Date startToWork, Date endToWork, String description) {
        this.title = title;
        this.employmentType = employmentType;
        this.location = location;
        this.companyName = companyName;
        this.locationType = locationType;
        this.activity = activity;
        this.startToWork = startToWork;
        this.endToWork = endToWork;
        this.description = description;
    }

    @Override
    public String toString(){
        return title + "\n" +
                companyName + " . " + employmentType
                + "\n" + startToWork + " - " + endToWork +
                " \n" + location;
    }
}
