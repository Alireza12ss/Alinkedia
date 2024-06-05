package org.example.Controller;

import org.example.DataBaseHandler.ConnectionDAO;

import java.util.Objects;

public class ConnectionController {
    public static String AllConnections(String email) {
        return Objects.requireNonNull(ConnectionDAO.AllConnnections(email)).toString();
    }

    public static String requestedConnections(String email) {
        return Objects.requireNonNull(ConnectionDAO.requestedConnections(email)).toString();
    }

    public static String sendRequest(String senderEmail , String receiverEmail , String description) {
        return ConnectionDAO.sendRequest(senderEmail , receiverEmail , description);
    }

    public static String acceptRequest(String receiverEmail , String senderEmail){
        return ConnectionDAO.acceptRequest(receiverEmail , senderEmail);
    }
}
