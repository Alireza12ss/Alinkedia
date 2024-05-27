package org.example.Controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ErrorController {
    public static void MethodNotSupported(HttpExchange exchange) throws IOException {
        String respone = " this method is not supported";
        exchange.sendResponseHeaders(400 , respone.length());
        try (OutputStream stream = exchange.getResponseBody()){
            stream.write(respone.getBytes());
        }
    }
}
