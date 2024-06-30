package org.example.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ProfileController;
import org.json.JSONObject;

import java.io.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class ProfileHandler implements HttpHandler {
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
                /*
                /profile --> return main profile
                /profile/job --> return job info
                /profile/education --> return education info
                /profile/connectionInfo --> return connection info
                 */
                if (pathSplit.length == 2){
                    try {
                        response = ProfileController.showProfile(Email);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (pathSplit[2].equals("job")){
                    try {
                        response = ProfileController.showJob(Email);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (pathSplit[2].equals("education")) {
                    try {
                        response = ProfileController.showEducation(Email);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (pathSplit[2].equals("connectionInfo")) {
                    try {
                        response = ProfileController.showConnectionInfo(Email);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "POST" :
                /*
                /profile --> update main profile
                /profile/job --> update job info
                /profile/education --> update education info
                /profile/connectionInfo --> update connection info
                 */
                switch (pathSplit[2]) {
                    case "job" -> {
                        // get json
                        JSONObject jsonObject = createJsonObject(exchange);
                        try {
                            response = ProfileController.addJob(Email, jsonObject.getString("title"), jsonObject.getString("employmentType"), jsonObject.getString("companyName"),
                                    jsonObject.getString("location"), jsonObject.getString("locationType")
                                    , jsonObject.getBoolean("activity"), Date.valueOf(jsonObject.getString("startToWork")),
                                    Date.valueOf(jsonObject.getString("endToWork")), jsonObject.getString("description"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "education" -> {
                        // get json
                        JSONObject jsonObject = createJsonObject(exchange);
                        try {
                            response = ProfileController.addEducation(Email, jsonObject.getString("schoolName")
                                    , jsonObject.getString("fieldOfStudy"), Date.valueOf(jsonObject.getString("startDate")),
                                    Date.valueOf(jsonObject.getString("endDate")), jsonObject.getDouble("grade"), jsonObject.getString("activitiesAndSocieties"),
                                    jsonObject.getString("description"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "connectionInfo" -> {
                        // get json
                        JSONObject jsonObject = createJsonObject(exchange);
                        try {
                            response = ProfileController.addConnectionInfo(Email, jsonObject.getString("userLink")
                                    , jsonObject.getString("email"), jsonObject.getString("phoneNumber"),
                                    jsonObject.getString("phoneType"), jsonObject.getString("address"), Date.valueOf(jsonObject.getString("birthday")),
                                    jsonObject.getString("birthdayAccess"), jsonObject.getString("otherWay"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
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
        return PostHandler.createJsonObject(exchange);
    }

}
