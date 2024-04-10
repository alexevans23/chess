package server;

import model.GameData;
import result.CreateGameResult;
import result.LogoutResult;
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
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
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

        Spark.delete("/session", (request, response) -> {
            String authToken = request.headers("Authorization");
            LogoutResult logoutResult = userService.logout(authToken);
            response.type("application/json");

            if (logoutResult.success()) {
                response.status(HttpURLConnection.HTTP_OK);
            } else {
                response.status(HttpURLConnection.HTTP_UNAUTHORIZED);
            }

            return new Gson().toJson(logoutResult);
        });

        Spark.post("/game", (request, response) -> {
            String authToken = request.headers("Authorization");
            Gson gson = new Gson();
            GameData gameData = gson.fromJson(request.body(), GameData.class);

            if (gameData == null || gameData.gameName() == null || gameData.gameName().trim().isEmpty()) {
                response.status(HttpURLConnection.HTTP_BAD_REQUEST);
                return gson.toJson(Map.of("message", "Error: bad request"));
            }

            CreateGameResult createGameResult = gameService.createGame(authToken, gameData);

            if (createGameResult.success()) {
                response.status(HttpURLConnection.HTTP_OK);
                response.type("application/json");
                return gson.toJson(createGameResult);
            } else {
                response.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                response.type("application/json");
                return gson.toJson(Map.of("message", createGameResult.message()));
            }
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


