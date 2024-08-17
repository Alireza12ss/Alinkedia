package org.example.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.CommentController;
import org.example.Controller.LikeController;
import org.example.Controller.PostController;
import org.example.DataBaseHandler.DAO;
import org.example.DataBaseHandler.PostDAO;
import org.example.DataBaseHandler.UserDAO;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static org.example.DataBaseHandler.DAO.personEmail;
import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class PostHandler implements HttpHandler {
    private static int responseCodePostHandler ;
    private static String responsePostHandler;

    public static void setResponsePostHandler(String response) {
        PostHandler.responsePostHandler = response;
    }

    public static String getResponse() {
        return responsePostHandler;
    }

    public int getResponseCodePostHandler() {
        return responseCodePostHandler;
    }

    public static void setResponseCodePostHandler(int responseCode) {
        PostHandler.responseCodePostHandler = responseCode;
    }

    public static String findHashtags(String hashtag) {
        return Objects.requireNonNull(PostDAO.findHashtags(hashtag)).toString();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PostController postController = new PostController();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        System.out.println(path);
        String response = "";
        String[] pathSplit = path.split("/");
        Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
        assert decoded != null;
        String Email = decoded.get("email").toString();
        System.out.println(path);
        switch (method){
            case "GET" :
                /*
                /post -> return All posts of this user ,
                /post/postId -> return unique post ,
                /post/postId/like -> return All users likes this post ,
                /post/postId/comment -> return All comments
                 */
                if (pathSplit.length == 2) {
                    response = postController.getPosts(Email);
                }else if (pathSplit.length == 3){
                    response = postController.getPost(Integer.valueOf(pathSplit[2]));
                }else if (pathSplit.length == 4 && pathSplit[3].equals("like")){
                    response = LikeController.postLike(Integer.valueOf(pathSplit[2]));
                }else if (pathSplit.length == 4 && pathSplit[3].equals("comment")){
                    response = CommentController.getComments(Integer.valueOf(pathSplit[2]));
                }else if (pathSplit.length == 4 && pathSplit[3].equals("user")){
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        response = objectMapper.writeValueAsString(UserDAO.getUniqueUser(personEmail(Integer.valueOf(pathSplit[2]))));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "POST" :
                /*
                /post -> create post ,
                /post/postId/like -> like post ,
                /post/postId/comment -> create comment
                 */
                if (pathSplit.length == 2){
                    JSONObject jsonObject = createJsonObject(exchange);
                    response = postController.createPost(Email , jsonObject.getString("text") , jsonObject.getString("mediaPath"));
                }else if (pathSplit.length == 4 && pathSplit[3].equals("like")){
                    response = LikeController.like(Email , Integer.valueOf(pathSplit[2]));
                }else if (pathSplit.length == 4 && pathSplit[3].equals("comment")){
                    JSONObject jsonObject = createJsonObject(exchange);
                    response = CommentController.createComment(DAO.personId(Email) , Integer.valueOf(pathSplit[2])
                            , jsonObject.getString("text") , jsonObject.getString("mediaPath"));
                }
                break;

        }
        System.out.println("okk" + response +"\n" + getResponseCodePostHandler());
        exchange.sendResponseHeaders(getResponseCodePostHandler(), response.length());
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
    static JSONObject createJsonObject(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        requestBody.close();

        return new JSONObject(body.toString());
    }
}
