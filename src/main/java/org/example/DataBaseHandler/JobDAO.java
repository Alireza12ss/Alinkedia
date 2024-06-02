package org.example.DataBaseHandler;

import org.example.Model.Job;
import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
