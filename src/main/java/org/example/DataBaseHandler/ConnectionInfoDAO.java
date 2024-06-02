package org.example.DataBaseHandler;

import org.example.Model.ConnectionInfo;
import org.example.Model.Education;

import java.sql.*;

public class ConnectionInfoDAO {
    public static ConnectionInfo getConnectionInfo(int educationId){
        String sql = "Select * From connectionInfos where id = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , educationId);
            ResultSet set = statement.executeQuery();
            ConnectionInfo con = null;
            if (set.next()) {
                con = new ConnectionInfo(set.getString("userLink") ,
                        set.getString("email") ,
                        set.getString("phoneNumber") ,
                        set.getString("phoneType") ,
                        set.getString("address") ,
                        set.getDate("birthday") ,
                        set.getString("birthdayAccess") ,
                        set.getString("otherWay"));
                return con;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String addConnectionInfo(String userEmail , String userLink, String email, String phoneNumber, String phoneType
            , String address, Date birthday, String birthdayAccess, String otherWay) throws SQLException {
        Connection connection = DatabaseHandler.CreateConnection();

        String sql = "INSERT INTO connectionInfos (userLink , email, phoneNumber,  phoneType,address,\n" +
                "                birthday,birthdayAccess , otherWay) VALUES (? , ? , ? , ? , ? , ? ,? , ?) ";

        PreparedStatement pstmt = connection.prepareStatement(sql);


        pstmt.setString(1 , userLink);
        pstmt.setString(2 , email);
        pstmt.setString(3 , phoneNumber);
        pstmt.setString(4 , phoneType);
        pstmt.setString(5 , address);
        pstmt.setDate(6 , birthday);
        pstmt.setString(7 , birthdayAccess);
        pstmt.setString(8 , otherWay);

        pstmt.executeUpdate();
        UserDAO.updateConnectionInfoId(userEmail , getId(email , userLink));
        return "Connection info added";
    }

    private static int getId(String email , String userLink){
        String sql = "Select * From connectionInfos where email = ? AND userLink = ?";

        try {
            Connection connection = DatabaseHandler.CreateConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            statement.setString(2 , userLink);
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
