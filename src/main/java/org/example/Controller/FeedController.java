package org.example.Controller;

import org.example.DataBaseHandler.DAO;
import org.example.DataBaseHandler.FeedDAO;

public class FeedController extends Controller{
    public String feed(String email){
        return FeedDAO.feed(DAO.personId(email)).toString();
    }
}
