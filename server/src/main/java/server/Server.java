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

public class Server {

    private UserService userService;
    private GameService gameService;

    public Server() {
        this.userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        this.gameService = new GameService(new MemoryGameDAO(), new MemoryAuthDAO());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.post("/user", (request, response) -> {
            UserData userData = new Gson().fromJson(request.body(), UserData.class);
            RegisterResult registerResult = userService.register(userData);
            response.type("application/json");
            if (registerResult.success()) {
                response.status(200);
            } else {
                response.status(400);
            }

            return new Gson().toJson(registerResult);
        });

        Spark.delete("/db", (request, response) -> {
            try {
                userService.clearAllUsersAndAuthTokens();
                gameService.clearAllGames();
                response.status(200);  // OK
                return "Database cleared successfully";
            } catch (Exception e) {
                response.status(500);  // Internal Server Error
                return "Error clearing database: " + e.getMessage();
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

