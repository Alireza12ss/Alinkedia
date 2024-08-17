package org.example.Http;

import com.sun.net.httpserver.HttpServer;
import org.example.Handler.*;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {

    public Server(int port) {
        try {
            createServer(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createServer(int port) throws IOException {
        InetAddress localAddress = InetAddress.getByName("127.0.0.1");
        HttpServer server = HttpServer.create(new InetSocketAddress(localAddress, port), 0);
        //sign up -> POST
        //login -> GET
        //Profile -> GET & PUT
        server.createContext("/post" , new PostHandler());
        server.createContext("/user" , new UserHandler());
        server.createContext("/signup", new UserHandler());
        server.createContext("/login", new UserHandler());
        server.createContext("/search", new UserHandler());
        server.createContext("/profile", new ProfileHandler());
        server.createContext("/follow" , new FollowHandler());
        server.createContext("/connect" , new ConnectionHandler());
        server.createContext("/feed" , new FeedHandler());
        server.createContext("/like", new LikeHandler());
        server.createContext("/hashtag" , new HashtagHandler());
        server.createContext("/callback" , new UserHandler());

        server.start();
    }


}
