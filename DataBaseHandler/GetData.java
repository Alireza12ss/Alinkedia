package DataBaseHandler;

import Model.User;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetData extends DatabaseHandler {
    static String sql = "Select * From users ";
    public static void getData(Connection connection) throws SQLException {
        try {
            Statement statement = connection.prepareStatement(sql);

            ResultSet set = statement.executeQuery(sql);
            int index = 0 ;
            while (set.next()){
                String str = set.getString(4);
                System.out.println(str + "   hi");
                index++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        


    }
}
