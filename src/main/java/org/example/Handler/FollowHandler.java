package org.example.Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.FollowController;
import org.example.JWTgenerator.JwtGenerator;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Handler;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class FollowHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
        String Email = decoded.get("email").toString();
        switch (method){
            case "GET" :
                if (pathSplit[2].equals("followers")){
                    response = FollowController.getFollowers(Email);
                }else if (pathSplit[2].equals("following")){
                    response = FollowController.getFollowing(Email);
                }
                break;
            case "POST" :
                response = FollowController.follow(Email , pathSplit[2]);
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
