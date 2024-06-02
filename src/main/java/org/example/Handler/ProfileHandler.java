package org.example.Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ProfileController;
import org.example.Controller.UserController;
import org.example.JWTgenerator.JwtGenerator;
import org.json.JSONObject;

import java.io.*;
import java.net.http.HttpHeaders;
import java.sql.SQLException;
import java.util.Map;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class ProfileHandler implements HttpHandler {
    HttpHeaders httpHeaders;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = new UserController();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        switch (method){
            case "GET" :
                    Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
                if (pathSplit.length == 2){
                    try {
                        response = ProfileController.showProfile(decoded.get("email").toString());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else if (pathSplit[2].equals("job")){
                    try {
                        response = ProfileController.showJob(decoded.get("email").toString());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            default:
                break;
        }
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

}
