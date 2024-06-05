package org.example.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Controller.CommentController;
import org.example.Controller.LikeController;
import org.example.Controller.PostController;
import org.example.DataBaseHandler.DAO;
import org.example.DataBaseHandler.PostDAO;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;
import java.util.Objects;

import static org.example.JWTgenerator.JwtGenerator.decodeToken;

public class PostHandler implements HttpHandler {

    public static String findHashtags(String hashtag) {
        return Objects.requireNonNull(PostDAO.findHashtags(hashtag)).toString();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PostController postController = new PostController();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathSplit = path.split("/");
        Map<String, Object> decoded = decodeToken(exchange.getRequestHeaders().getFirst("Authorization"));
        assert decoded != null;
        String Email = decoded.get("email").toString();
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
        exchange.sendResponseHeaders(200, response.length());
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
