package Controller;

import DataBaseHandler.DatabaseHandler;
import DataBaseHandler.Insert;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserController extends Controller{
    public static void GetUser(HttpExchange exchange) throws IOException {

        String response = "here is all Users";
        exchange.sendResponseHeaders(200 , response.length());
        try (OutputStream outputStream = exchange.getResponseBody()){
            outputStream.write(response.getBytes());
        }catch (IOException e){
            System.out.println("IOException");
        }
    }
    public static void CreateUser(HttpExchange exchange) throws IOException{
        try {
            Insert.insertNameAndEmail(DatabaseHandler.CreateStatement() , "Pouria" , "fahimi" , "kiki", "porito@aut.ac.ir");
        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("SQLIntegrityConstraintViolationException");
        }catch (SQLException e){
            System.out.println("SQLException");
        }

        String response = "User created Successfully";
        exchange.sendResponseHeaders(200 , response.length());

        try (OutputStream outputStream = exchange.getResponseBody()){
            outputStream.write(response.getBytes());
        }

    }
}
