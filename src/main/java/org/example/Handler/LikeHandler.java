package org.example.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.LikeController;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class LikeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
        assert decoded != null;
        String Email = decoded.get("email").toString();
        switch (method) {
            case "GET": // /like
                if (pathSplit.length == 2){
                    response = LikeController.AllLikes(Email);
                }
                break;
            default:
                response = "wrong url!";
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
