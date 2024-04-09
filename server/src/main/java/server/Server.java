package server;

import dataAccess.MemoryGameDAO;
import service.GameService;
import service.UserService;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryAuthDAO;
import model.UserData;
import result.RegisterResult;
import spark.Spark;
import com.google.gson.Gson;

import java.util.Map;

public class Server {

    private UserService userService;
    private GameService gameService;

    public Server() {
        this.userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        this.gameService = new GameService(new MemoryGameDAO(), new MemoryAuthDAO());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.post("/user", (request, response) -> {
            UserData userData = new Gson().fromJson(request.body(), UserData.class);

            RegisterResult registerResult = userService.register(userData);
            response.type("application/json");

            if (registerResult.success()) {
                response.status(200);  // OK
            } else {
                if (registerResult.message().equals("Error: already taken")) {
                    response.status(403);  // Forbidden
                } else {
                    response.status(400);  // Bad Request
                }
            }

            return new Gson().toJson(registerResult);
        });



        Spark.delete("/db", (request, response) -> {
            try {
                userService.clearAllUsersAndAuthTokens();
                gameService.clearAllGames();
                response.status(200);  // OK
                response.type("application/json");
                return new Gson().toJson(Map.of("message", "Database cleared successfully"));
            } catch (Exception e) {
                response.status(500);  // Internal Server Error
                response.type("application/json");
                return new Gson().toJson(Map.of("error", "Error clearing database: " + e.getMessage()));
            }
        });

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

