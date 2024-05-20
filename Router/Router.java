package Router;

import Controller.UserController;
import Http.Server;

import java.sql.SQLException;

public class Router {

    public static void route(Server server) {

        server.get("/users" , UserController::GetUser);
        server.get("/add" , UserController::CreateUser);

    }
}
