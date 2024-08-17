package org.example.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.FeedController;
import org.example.DataBaseHandler.PostDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class FeedHandler implements HttpHandler {
    private static int responseCodeFeedHandler ;
    private static String responseFeedHandler;

    public static void setResponseFeedHandler(String response) {
        FeedHandler.responseFeedHandler = response;
    }

    public static String getResponse() {
        return responseFeedHandler;
    }

    public int getResponseCodeFeedHandler() {
        return responseCodeFeedHandler;
    }

    public static void setResponseCodeFeedHandler(int responseCode) {
        FeedHandler.responseCodeFeedHandler = responseCode;
    }

    public static String findHashtags(String hashtag) {
        return Objects.requireNonNull(PostDAO.findHashtags(hashtag)).toString();
    }
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
        exchange.sendResponseHeaders(getResponseCodeFeedHandler(), response.length());
        try (
                OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(response.getBytes());
        }catch (IOException e) {
            System.out.println("IOException");
        }
    }
}
