package org.example.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.SSOController;
import org.example.Controller.UserController;
import org.example.DataBaseHandler.UserDAO;
import org.example.JWTgenerator.JwtGenerator;
import org.example.Model.User;
import org.json.JSONObject;

import java.io.*;
import java.net.http.HttpClient;
import java.sql.SQLException;
import java.util.Map;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class UserHandler implements HttpHandler {
    private static int responseCode ;
    private static String response;

    public static void setResponse(String response) {
        UserHandler.response = response;
    }

    public static String getResponse() {
        return response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public static void setResponseCode(int responseCode) {
        UserHandler.responseCode = responseCode;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        int statusCode = 200; To Do ...
        UserController userController = new UserController();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        System.out.println("here");
        switch (method){
            case "GET" :
                if (pathSplit[1].equals("callback")){
                    String query = exchange.getRequestURI().getQuery();
                    if (query.contains("code=")) {
                        String authCode = query.split("=")[1];
                        exchange.sendResponseHeaders(200, 0);
                        exchange.getResponseBody().write("Authorization successful! You can close this window.".getBytes());
                        exchange.getResponseBody().close();
                        System.out.println(authCode);
                        try {
                            String accessToken = SSOController.sendAccessTokenRequest(authCode);
                            JSONObject jsonObject = new JSONObject(SSOController.getUserInfo(accessToken));
                            if (UserDAO.searchUsers(jsonObject.getString("name") , "").size() == 0){
                                response = userController.CreateUser((String) jsonObject.get("name")
                                        , "" , (String)jsonObject.get("email")
                                        , "GitHub Login");
                            }else {
                                User user = userController.login((String)jsonObject.get("email")
                                        , "GitHub Login");
                                ObjectMapper mapper = new ObjectMapper();
                                String json = mapper.writeValueAsString(user);
                                response = json;
                                if (JwtGenerator.tokenIsValid(response)) {
                                    Headers headers = exchange.getResponseHeaders();
                                    headers.set("Authorization", "Bearer " + response);
                                }
                            }
                            System.out.println("Response is : " + response);
                            SSOController.sendTokenToClient(response);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        // Handle the authCode
                        // exchange.getHttpContext().getAttributes().put("authCode", authCode);
                        // Complete the OAuth process here
                    }
                }
                else if(pathSplit[1].equals("login")){ //url : /email/password
//                    System.out.println(JwtGenerator.decodeToken(response));
                    User user = userController.login(pathSplit[2] , pathSplit[3]);
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(user);
                    response = json;
                    if (JwtGenerator.tokenIsValid(response)) {
                        Headers headers = exchange.getResponseHeaders();
                        headers.set("Authorization", "Bearer " + response);
                    }
                }else if (pathSplit[1].equals("user")){
                    Map<String, Object> decodedt = decodeToken(pathSplit[2]);
                    assert decodedt != null;
                    String Emailt = decodedt.get("email").toString();
                    User user = null;
                    try {
                        user = userController.getUniqueUser(Emailt);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(user);
                    response = json;
                    responseCode = 200;
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
            case "PUT" :
                // email , firstName, lastName, additionalName, title, jobId , educationId, connectionInfoId, imagePathProfile, imagePathBackground,
                //                country , city, profession, birthday
                JSONObject jsonObjectn = createJsonObject(exchange);
                response = userController.updateUser((String) jsonObjectn.get("email"),(String) jsonObjectn.get("firstName"), (String)jsonObjectn.get("lastName")
                        , (String)jsonObjectn.get("additionalName") , (String)jsonObjectn.get("title")
                , (String)jsonObjectn.get("imagePathProfile"), (String)jsonObjectn.get("imagePathBackground"),
                        (String)jsonObjectn.get("country"), (String)jsonObjectn.get("city") ,
                        (String)jsonObjectn.get("profession"), (String)jsonObjectn.get("birthday"));
            default:
                break;
        }
        exchange.sendResponseHeaders(getResponseCode(), response.length());
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            System.out.println("IOException");
        }

    }
    static JSONObject createJsonObject(HttpExchange exchange) throws IOException {
        return PostHandler.createJsonObject(exchange);
    }

}

