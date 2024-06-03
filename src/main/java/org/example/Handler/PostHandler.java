package org.example.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.PostController;
import org.example.Controller.UserController;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class PostHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PostController postController = new PostController();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
        String Email = decoded.get("email").toString();
        switch (method){
            case "GET" :
                response = postController.getPosts(Email);
                break;
            case "POST" :
                if (pathSplit.length == 2){
                    JSONObject jsonObject = ProfileHandler.createJsonObject(exchange);
                    response = postController.createPost(Email , jsonObject.getString("text"));
                }
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
