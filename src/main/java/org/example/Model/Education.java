package org.example.Model;

import java.sql.Date;

public class Education {
    private String schoolName;
    private String fieldOfStudy;
    private Date startDate;
    private Date endDate;
    private double grade;
    private String activitiesAndSocieties;
    private String profileChanges;
    private String descriptions;

    public Education(String schoolName, String fieldOfStudy, Date startDate, Date endDate,
                     double grade, String activitiesAndSocieties, String descriptions) {
        this.schoolName = schoolName;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
        this.activitiesAndSocieties = activitiesAndSocieties;
        this.descriptions = descriptions;
    }

    @Override
    public String toString(){
        return schoolName + "\n" +
                fieldOfStudy + "\n"
                +startDate + " - " + endDate
                +"\n" + descriptions;
    }

}
