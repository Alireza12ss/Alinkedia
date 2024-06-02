package org.example.DataBaseHandler;

import org.example.Model.Job;
import org.example.Model.User;

import java.sql.*;

public class JobDAO {
    public static Job getJob(int jobId){
        String sql = "Select * From jobs where id = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , jobId);
            ResultSet set = statement.executeQuery();
            Job job = null;
            if (set.next()) {
                job = new Job(set.getString("title") ,
                        set.getString("employmentType") ,
                        set.getString("companyName") ,
                        set.getString("location") ,
                        set.getString("locationType"),
                        set.getBoolean("activity"),
                        set.getDate("startToWork") ,
                        set.getDate("endToWork"),
                        set.getString("description"));
                return job;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String addJob(String email ,String title, String employmentType, String companyName, String location, String locationType ,
                                boolean activity, Date startToWork, Date endToWork, String description) throws SQLException {
        Connection connection = DatabaseHandler.CreateConnection();

        String sql = "INSERT INTO jobs (title, employmentType,  companyName,location,\n" +
                "                locationType,activity,  startToWork, endToWork, description) VALUES (? , ? , ? , ? , ? ,? , ? , ? , ?) ";

        PreparedStatement pstmt = connection.prepareStatement(sql);


        pstmt.setString(1 , title);
        pstmt.setString(2 , employmentType);
        pstmt.setString(3 , companyName);
        pstmt.setString(4 , location);
        pstmt.setString(5 , locationType);
        pstmt.setBoolean(6 , activity);
        pstmt.setDate(7 , startToWork);
        pstmt.setDate(8 , endToWork);
        pstmt.setString(9 , description);

        pstmt.executeUpdate();
        UserDAO.updateJobId(email , getId(title , companyName));
        return "position added";
    }

    private static int getId(String title , String companyName){
        String sql = "Select * From jobs where title = ? AND companyName = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , title);
            statement.setString(2 , companyName);
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
