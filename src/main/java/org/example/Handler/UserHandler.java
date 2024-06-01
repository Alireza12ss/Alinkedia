package org.example.Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.UserController;
import org.example.JWTgenerator.JwtGenerator;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;


public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        int statusCode = 200; To Do ...
        UserController userController = new UserController();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        switch (method){
            case "GET" :
                if (pathSplit.length == 2){
                    response = userController.GetAllUser();
                }
                else { //url : /email/password
                    System.out.println(JwtGenerator.decodeToken(response));
                    response = userController.GetUniqueUser(pathSplit[2]);
                    if (JwtGenerator.tokenIsValid(response)) {
                        Headers headers = exchange.getResponseHeaders();
                        headers.set("Authorization", "Bearer " + response);
                    }
                }
                break;
            case "POST" :
                // get json
                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                requestBody.close();

                JSONObject jsonObject = new JSONObject(body.toString());

                response = userController.CreateUser((String) jsonObject.get("firstName")
                        , (String)jsonObject.get("lastName") , (String)jsonObject.get("email")
                        , (String)jsonObject.get("password"));
                break;
            case "PATCH" :
                //get json

                break;
            case "DELETE":
                if (pathSplit.length == 2) {
                    userController.deleteAllUsers();
                    response = "All users deleted";
                } else {
                    // Extract the user ID from the path
                    String userId = pathSplit[pathSplit.length - 1];
                    userController.deleteUser(userId);
                    response = "user deleted";
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

