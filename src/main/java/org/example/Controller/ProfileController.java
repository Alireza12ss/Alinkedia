package org.example.Controller;

import org.example.DataBaseHandler.ConnectionInfoDAO;
import org.example.DataBaseHandler.EducationDAO;
import org.example.DataBaseHandler.JobDAO;
import org.example.DataBaseHandler.UserDAO;

import java.sql.Date;
import java.sql.SQLException;

public class ProfileController extends Controller{
    public static String showProfile(String email) throws SQLException {
        return UserDAO.showProfile(email);
    }


    //job
    public static String showJob(String email) throws SQLException {
        return UserDAO.showJob(email);
    }

    public static String addJob(String email ,String title, String employmentType, String companyName, String location,
                                String locationType, boolean activity, Date startToWork, Date endToWork, String description) throws SQLException {
        return JobDAO.addJob(email ,title, employmentType,  companyName,location,
                locationType,activity,  startToWork, endToWork, description);

    }


    //education
    public static String showEducation(String email) throws SQLException {
        return UserDAO.showEducation(email);
    }

    public static String addEducation(String email , String schoolName, String fieldOfStudy, Date startDate, Date endDate,
                                      double grade, String activitiesAndSocieties, String descriptions) throws SQLException {
        return EducationDAO.addEducation(email , schoolName , fieldOfStudy, startDate,  endDate,grade,
                activitiesAndSocieties,descriptions);

    }


    //connection info
    public static String showConnectionInfo(String email) throws SQLException {
        return UserDAO.showConnectionInfo(email);
    }

    public static String addConnectionInfo(String userEmail ,String userLink, String email, String phoneNumber, String phoneType
            , String address, Date birthday, String birthdayAccess, String otherWay) throws SQLException {
        return ConnectionInfoDAO.addConnectionInfo(userEmail , userLink , email, phoneNumber,  phoneType,address,
                birthday,birthdayAccess , otherWay);

    }





    public String updateProfile(String additionalName , String title , String imagePathProfile , String imagePathBackground , String country , String city , String profession ){

        return additionalName;
    }

    public String deleteUser(String email) {
        return UserDAO.deleteUser(email);
    }

    public String deleteAllUsers() {
        return UserDAO.deleteAllUsers();
    }
}
