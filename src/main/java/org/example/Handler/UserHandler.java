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
                else if(pathSplit[1].equals("login")){ //url : /email/password
//                    System.out.println(JwtGenerator.decodeToken(response));
                    response = userController.login(pathSplit[2] , pathSplit[3]);
                    if (JwtGenerator.tokenIsValid(response)) {
                        Headers headers = exchange.getResponseHeaders();
                        headers.set("Authorization", "Bearer " + response);
                    }
                }else if (pathSplit[1].equals("search")){
                    try {
                        response = userController.searchUser(pathSplit[2] , pathSplit[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "POST" :
                JSONObject jsonObject = createJsonObject(exchange);

                response = userController.CreateUser((String) jsonObject.get("firstName")
                        , (String)jsonObject.get("lastName") , (String)jsonObject.get("email")
                        , (String)jsonObject.get("password"));
                break;
//            case "PATCH" :
//                //get json
//                InputStream requestBodyp = exchange.getRequestBody();
//                BufferedReader readerp = new BufferedReader(new InputStreamReader(requestBodyp));
//                StringBuilder bodyp = new StringBuilder();
//                String linep;
//                while ((linep = readerp.readLine()) != null) {
//                    bodyp.append(linep);
//                }
//                requestBodyp.close();
//
//                JSONObject jsonObjectp = new JSONObject(bodyp.toString());
//                break;
//            case "DELETE":
//                if (pathSplit.length == 2) {
//                    userController.deleteAllUsers();
//                    response = "All users deleted";
//                } else {
//                    // Extract the user ID from the path
//                    String userId = pathSplit[pathSplit.length - 1];
//                    userController.deleteUser(userId);
//                    response = "user deleted";
//                }
//                break;
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
    static JSONObject createJsonObject(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        requestBody.close();

        JSONObject jsonObject = new JSONObject(body.toString());
        return jsonObject;
    }
}
