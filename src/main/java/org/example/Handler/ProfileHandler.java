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
import java.sql.Date;
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
        Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
        switch (method){
            case "GET" :
                if (pathSplit.length == 2){
                    try {
                        response = ProfileController.showProfile(decoded.get("email").toString());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (pathSplit[2].equals("job")){
                    try {
                        response = ProfileController.showJob(decoded.get("email").toString());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (pathSplit[2].equals("education")) {
                    try {
                        response = ProfileController.showEducation(decoded.get("email").toString());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (pathSplit[2].equals("connectionInfo")) {
                    try {
                        response = ProfileController.showConnectionInfo(decoded.get("email").toString());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "POST" :
                if (pathSplit[2].equals("job")) {
                    // get json
                    JSONObject jsonObject = createJsonObject(exchange);
                    try {
                        response = ProfileController.addJob(decoded.get("email").toString() ,jsonObject.getString("title"), jsonObject.getString("employmentType"), jsonObject.getString("companyName"),
                                jsonObject.getString("location"), jsonObject.getString("locationType")
                                , jsonObject.getBoolean("activity"), Date.valueOf(jsonObject.getString("startToWork")),
                                Date.valueOf(jsonObject.getString("endToWork")), jsonObject.getString("description"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else if (pathSplit[2].equals("education")) {
                    // get json
                    JSONObject jsonObject = createJsonObject(exchange);
                    try {
                        response = ProfileController.addEducation(decoded.get("email").toString() ,jsonObject.getString("schoolName")
                                , jsonObject.getString("fieldOfStudy") , Date.valueOf(jsonObject.getString("startDate")),
                                Date.valueOf(jsonObject.getString("endDate")), jsonObject.getDouble("grade"), jsonObject.getString("activitiesAndSocieties"),
                                jsonObject.getString("description"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else if (pathSplit[2].equals("connectionInfo")) {
                    // get json
                    JSONObject jsonObject = createJsonObject(exchange);
                    try {
                        response = ProfileController.addConnectionInfo(decoded.get("email").toString() ,jsonObject.getString("userLink")
                                , jsonObject.getString("email") , jsonObject.getString("phoneNumber"),
                                jsonObject.getString("phoneType"), jsonObject.getString("address"), Date.valueOf(jsonObject.getString("birthday")),
                                jsonObject.getString("birthdayAccess") , jsonObject.getString("otherWay"));
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
