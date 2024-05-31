package org.example.Http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.example.Controller.ErrorController;
import org.example.Handler.PostHandler;
import org.example.Handler.UserHandler;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {
    private static HttpServer server;

    public Server(int port) throws IOException {
        createServer(port);
    }


    /**
     * create server on the localhost IP and entered port number
     * @param port
     * @throws IOException
     */
    private void createServer(int port) throws IOException {
        InetAddress localAddress = InetAddress.getByName("127.0.0.1");
        this.server = HttpServer.create(new InetSocketAddress(localAddress, port), 0);

        server.createContext("/signup", new UserHandler());
        server.createContext("/login", new UserHandler());
        server.createContext("/post" , new PostHandler());

        server.start();
    }

    public static HttpServer getServer() {
        return server;
    }

    public void get(String path,HttpHandler handler) {
        server.createContext(path, (exchange) -> {
            if (exchange.getRequestMethod().equals("GET")) {
                handler.handle(exchange);
            } else {
                ErrorController.MethodNotSupported(exchange);
            }
        });
    }

    public void post(String path,HttpHandler handler) {
        server.createContext(path, (exchange) -> {
            if (exchange.getRequestMethod().equals("POST")) {
                handler.handle(exchange);
            } else {
                ErrorController.MethodNotSupported(exchange);
            }
        });

    }

//    public void sendMessage(){
//        server.
//    }

}