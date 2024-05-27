package org.example.Http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class ClientHandler {
    HttpHandler httpHandler;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    public HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public ClientHandler(){
       httpHandler = (HttpHandler) Server.getServer();
    }

    public static void handle(HttpExchange httpExchange) throws IOException {
        objectOutputStream = (ObjectOutputStream) httpExchange.getResponseBody();
        String response = "Ok ";
        objectOutputStream.writeObject(response.getBytes());
        objectOutputStream.flush();
        objectOutputStream.close();
    }
}
