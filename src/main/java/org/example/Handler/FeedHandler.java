package org.example.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.FeedController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class FeedHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        FeedController feedController = new FeedController();
        String method = exchange.getRequestMethod();
        String response;
        Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
        assert decoded != null;
        String Email = decoded.get("email").toString();
        switch (method){
            case "GET" :
                response = feedController.feed(Email);
                break;
            default:
                response = "wrong url";
        }
        exchange.sendResponseHeaders(200, response.length());
        try (
                OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(response.getBytes());
        }catch (IOException e) {
            System.out.println("IOException");
        }
    }
}
