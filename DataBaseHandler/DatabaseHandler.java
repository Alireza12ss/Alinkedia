package DataBaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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
}
