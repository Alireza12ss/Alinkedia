package org.example.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.PostController;
import org.example.Controller.UserController;
import org.json.JSONObject;

import java.io.*;

public class PostHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PostController postController = new PostController();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        switch (method){
            case "GET" :
                break;
            case "POST" :
                if (pathSplit.length == 2){
                    JSONObject jsonObject = ProfileHandler.createJsonObject(exchange);

                    response = postController.createPost((String) jsonObject.get("userEmail")
                            , (String)jsonObject.get("txt") );
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
