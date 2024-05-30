package org.example;

import org.example.Http.Server;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8888);
    }
}