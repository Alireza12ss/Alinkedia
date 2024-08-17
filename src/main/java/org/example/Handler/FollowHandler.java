package org.example.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.FollowController;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class FollowHandler implements HttpHandler {
    private static int responseCodeFollowHandler ;
    private static String responseFollowHandler;

    public static void setResponseFollowHandler(String response) {
        FollowHandler.responseFollowHandler = response;
    }

    public static String getResponse() {
        return responseFollowHandler;
    }

    public int getResponseCodeFollowHandler() {
        return responseCodeFollowHandler;
    }

    public static void setResponseCodeFollowHandler(int responseCode) {
        FollowHandler.responseCodeFollowHandler = responseCode;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
        assert decoded != null;
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
                try {
                    response = FollowController.follow(Email , pathSplit[2]);
                } catch (SQLException e) {
                    setResponseCodeFollowHandler(400);
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }
        System.out.println(response);
        exchange.sendResponseHeaders(getResponseCodeFollowHandler(), response.length());
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
}
