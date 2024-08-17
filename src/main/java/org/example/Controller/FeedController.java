package org.example.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DataBaseHandler.DAO;
import org.example.DataBaseHandler.FeedDAO;

public class FeedController extends Controller{
    public String feed(String email) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(FeedDAO.feed(DAO.personId(email))).toString();
    }
}
