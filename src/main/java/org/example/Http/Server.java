package org.example.Http;

import com.sun.net.httpserver.HttpServer;
import org.example.Handler.PostHandler;
import org.example.Handler.ProfileHandler;
import org.example.Handler.UserHandler;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {
    private static HttpServer server;

    public Server(int port) throws IOException {
        createServer(port);
    }

    private void createServer(int port){
        try {
            InetAddress localAddress = InetAddress.getByName("127.0.0.1");
            this.server = HttpServer.create(new InetSocketAddress(localAddress, port), 0);
            //sign up -> POST
            //login -> GET
            //Profile -> GET & PUT
            server.createContext("/signup", new UserHandler());
            server.createContext("/login", new UserHandler());
            server.createContext("/search", new UserHandler());
            server.createContext("/profile", new ProfileHandler());
            server.createContext("/post" , new PostHandler());
            server.createContext("/follow" , new FollowHandler());
        }catch (IOException e){

        }

        server.start();
    }

    public static HttpServer getServer() {
        return server;
    }


}