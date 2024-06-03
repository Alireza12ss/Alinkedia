package org.example.Controller;

import org.example.DataBaseHandler.ConnectionDAO;

public class ConnectionController {
    public static String Allconnections(String email) {
        return ConnectionDAO.Allconnnections(email).toString();
    }

    public static String requestedConnections(String email) {
        return ConnectionDAO.requestedConnections(email).toString();
    }

    public static String sendRequest(String senderEmail , String receiverEmail , String description) {
        return ConnectionDAO.sendRequest(senderEmail , receiverEmail , description);
    }

    public static String acceptRequest(String receiverEmail , String senderEmail){
        return ConnectionDAO.acceptRequest(receiverEmail , senderEmail);
    }
}
