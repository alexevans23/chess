package server;

import model.GameData;
import result.*;
import service.GameService;
import service.UserService;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.UserData;
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
                response.status(HttpURLConnection.HTTP_UNAUTHORIZED);
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

        Spark.put("/game", (request, response) -> {
            String authToken = request.headers("Authorization");
            Gson gson = new Gson();
            GameData gameRequest = gson.fromJson(request.body(), GameData.class);
            String playerColor = gson.fromJson(request.body(), Map.class).get("playerColor").toString();

            if (gameRequest == null || gameRequest.gameID() <= 0 || playerColor == null || playerColor.trim().isEmpty()) {
                response.status(HttpURLConnection.HTTP_BAD_REQUEST);
                return gson.toJson(Map.of("message", "Error: bad request"));
            }

            JoinGameResult joinGameResult = gameService.joinGame(authToken, gameRequest.gameID(), playerColor, userService.getUsernameFromToken(authToken));

            response.type("application/json");
            if (joinGameResult.success()) {
                response.status(HttpURLConnection.HTTP_OK);
                return gson.toJson(joinGameResult);
            } else {
                response.status(joinGameResult.message().contains("slot already taken") ? HttpURLConnection.HTTP_FORBIDDEN : HttpURLConnection.HTTP_UNAUTHORIZED);
                return gson.toJson(Map.of("message", joinGameResult.message()));
            }
        });




        Spark.get("/game", (request, response) -> {
            String authToken = request.headers("Authorization");
            Gson gson = new Gson();
            if (authToken == null || authToken.trim().isEmpty()) {
                response.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                return gson.toJson(Map.of("message", "Error: Unauthorized - No auth token provided"));
            }
            ListGamesResult listGamesResult = gameService.listGames(authToken);
            response.type("application/json");
            if (listGamesResult.success()) {
                response.status(HttpURLConnection.HTTP_OK);
                return gson.toJson(listGamesResult);
            } else {
                response.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                return gson.toJson(Map.of("message", listGamesResult.message()));
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


