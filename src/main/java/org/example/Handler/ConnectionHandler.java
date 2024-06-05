package org.example.Handler;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.ConnectionController;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class ConnectionHandler implements HttpHandler {
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
                if (pathSplit.length == 2){
                    response = ConnectionController.Allconnections(Email);
                }else if (pathSplit[2].equals("requested")){
                    response = ConnectionController.requestedConnections(Email);
                }
                break;
            case "POST" :
                if (pathSplit.length == 2) {
                    JSONObject jsonObject = createJsonObject(exchange);
                    response = ConnectionController.sendRequest(Email, jsonObject.getString("receiverEmail"), jsonObject.getString("description"));
                }else {
                    response = ConnectionController.acceptRequest(Email , pathSplit[3]);
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
