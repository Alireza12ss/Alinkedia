package org.example.Controller;


import com.sun.net.httpserver.HttpHandler;
import org.example.DataBaseHandler.DatabaseHandler;
import org.example.DataBaseHandler.GetData;
import org.example.DataBaseHandler.Insert;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.*;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

public class UserController extends Controller{
    public static String GetUser() throws IOException {
        String str = null;
        try {
            str = GetData.getData(DatabaseHandler.CreateConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    public static void CreateUser(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        requestBody.close();

        JSONObject jsonObject = new JSONObject(body.toString());


        String response = "yes";

        try {
            response = Insert.insertNameAndEmail(DatabaseHandler.CreateConnection() , (String) jsonObject.get("firstname")
                    , (String)jsonObject.get("lastname") , (String)jsonObject.get("additionalname")
                    , (String)jsonObject.get("email") , (String)jsonObject.get("password"));

        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("SQLIntegrityConstraintViolationException");
        }catch (SQLException e){
            System.out.println("SQLException");
        }
        exchange.sendResponseHeaders(200, response.length());

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }

    }

    private static Map<String, String> parseUrl(String urlBody) throws UnsupportedEncodingException {
        String[] requestBody = urlBody.split("\\?");
        String url = requestBody[1];
        final Map<String, String> urlpairs = new LinkedHashMap<String, String>();
        final String[] pairs = url.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!urlpairs.containsKey(key)) {
                urlpairs.put(key, null);
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            urlpairs.put(key , value);
        }
        return urlpairs;
    }


}
