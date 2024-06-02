package org.example.DataBaseHandler;

import org.example.Model.Education;

import java.sql.*;

public class EducationDAO {
    public static Education getEducation(int educationId){
        String sql = "Select * From educations where id = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , educationId);
            ResultSet set = statement.executeQuery();
            Education edu = null;
            if (set.next()) {
                edu = new Education(set.getString("schoolName") ,
                        set.getString("fieldOfStudy") ,
                        set.getDate("startDate") ,
                        set.getDate("endDate"),
                        set.getDouble("grade") ,
                        set.getString("activitiesAndSocieties") ,
                        set.getString("descriptions"));
                return edu;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String addEducation(String email , String schoolName, String fieldOfStudy, Date startDate, Date endDate,
                                double grade, String activitiesAndSocieties, String descriptions) throws SQLException {
        Connection connection = DatabaseHandler.CreateConnection();

        String sql = "INSERT INTO educations (schoolName , fieldOfStudy, startDate,  endDate,grade,\n" +
                "                activitiesAndSocieties,descriptions) VALUES (? , ? , ? , ? , ? ,? , ?) ";

        PreparedStatement pstmt = connection.prepareStatement(sql);


        pstmt.setString(1 , schoolName);
        pstmt.setString(2 , fieldOfStudy);
        pstmt.setDate(3 , startDate);
        pstmt.setDate(4 , endDate);
        pstmt.setDouble(5 , grade);
        pstmt.setString(6 , activitiesAndSocieties);
        pstmt.setString(7 , descriptions);

        pstmt.executeUpdate();
        UserDAO.updateEducationId(email , getId(schoolName , fieldOfStudy));
        return "education added";
    }

    private static int getId(String schoolName , String fieldOfStudy){
        String sql = "Select * From educations where schoolName = ? AND fieldOfStudy = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , schoolName);
            statement.setString(2 , fieldOfStudy);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                return set.getInt("id");
            }
            return 0;
        }catch (SQLException e){
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
