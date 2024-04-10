package server;

import service.GameService;
import service.UserService;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.UserData;
import result.LoginResult;
import result.RegisterResult;
import spark.Spark;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.Map;

public class Server {

    private UserService userService;
    private GameService gameService;

    public Server() {
        this.userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        this.gameService = new GameService(new MemoryGameDAO(), new MemoryAuthDAO());
    }

    public void setupRoutes() {
        Spark.post("/user", (request, response) -> {
            UserData userData = new Gson().fromJson(request.body(), UserData.class);
            RegisterResult registerResult = userService.register(userData);
            response.type("application/json");

            if (registerResult.success()) {
                response.status(HttpURLConnection.HTTP_OK);
            } else {
                if (registerResult.message().equals("Error: already taken")) {
                    response.status(HttpURLConnection.HTTP_FORBIDDEN);
                } else {
                    response.status(HttpURLConnection.HTTP_BAD_REQUEST);
                }
            }

            return new Gson().toJson(registerResult);
        });

        Spark.post("/session", (request, response) -> {
            UserData loginData = new Gson().fromJson(request.body(), UserData.class);
            LoginResult loginResult = userService.login(loginData);
            response.type("application/json");

            if (loginResult.success()) {
                response.status(HttpURLConnection.HTTP_OK);
            } else {
                response.status(HttpURLConnection.HTTP_UNAUTHORIZED);  // Unauthorized
            }

            return new Gson().toJson(loginResult);
        });



        Spark.delete("/db", (request, response) -> {
            try {
                userService.clearAllUsersAndAuthTokens();
                gameService.clearAllGames();
                response.status(HttpURLConnection.HTTP_OK);
                response.type("application/json");
                return new Gson().toJson(Map.of("message", "Database cleared successfully"));
            } catch (Exception e) {
                response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
                response.type("application/json");
                return new Gson().toJson(Map.of("error", "Error clearing database: " + e.getMessage()));
            }
        });
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        setupRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}


