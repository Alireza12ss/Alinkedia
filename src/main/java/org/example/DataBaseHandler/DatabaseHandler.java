package org.example.DataBaseHandler;

import java.sql.*;

public class DatabaseHandler {
    public static Connection CreateConnection(){
        String url = "jdbc:mysql://localhost:3306/projectdb";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url , username , password);


            return connection;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int personId(String email){
        String sql = "Select * From users WHERE email = ?";
        try {
            Connection connection = DatabaseHandler.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , email);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
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

    public static String personEmail(int id){
        String sql = "Select * From users WHERE id = ?";
        try {
            Connection connection = DatabaseHandler.CreateConnection();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , id);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                return set.getString("email");
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
