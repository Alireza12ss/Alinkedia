package Router;

import Controller.UserController;
import Http.Server;

import java.sql.SQLException;

public class Router {

    public static void route(Server server) {

        server.get("/users" , UserController::GetUser);
        server.get("/home" , UserController::CreateUser);

    }
}
