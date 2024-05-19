package DataBaseHandler;

import java.sql.*;

public class Insert extends DatabaseHandler {
    public static void insertNameAndEmail(Connection connection , String firstName , String lastName , String additionalName , String email ) throws SQLException {
        try {
            String sql = "INSERT INTO users (firstName , lastName , additionalName , email ) VALUES (? , ? , ? , ?) ";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1 , firstName);
            pstmt.setString(2 , lastName);
            pstmt.setString(3 , additionalName);
            pstmt.setString(4 , email);

            pstmt.executeUpdate();

            System.out.println("finish");

        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Primary key constraint violated");
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
