package server;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import model.GameInteractionRequest;
import result.*;
import service.GameService;
import service.UserService;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import spark.Spark;

import java.net.HttpURLConnection;
import java.util.Map;

public class Server {

    private final UserService userService;
    private final GameService gameService;

    public Server() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

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
            GameInteractionRequest interactionRequest = gson.fromJson(request.body(), GameInteractionRequest.class);

            if (interactionRequest == null || interactionRequest.gameID() <= 0) {
                response.status(HttpURLConnection.HTTP_BAD_REQUEST);
                return gson.toJson(Map.of("message", "Error: bad request - missing or invalid game ID"));
            }

            String playerColor = interactionRequest.playerColor();
            if ((!"WHITE".equalsIgnoreCase(playerColor) && !"BLACK".equalsIgnoreCase(playerColor))) {
                response.status(HttpURLConnection.HTTP_BAD_REQUEST);
                return gson.toJson(Map.of("message", "Error: Bad request - Invalid or missing player color"));
            }


            if ("WATCHER".equalsIgnoreCase(interactionRequest.role())) {
                JoinGameResult watchResult = gameService.watchGame(authToken, interactionRequest.gameID());
                response.type("application/json");
                if (watchResult.success()) {
                    response.status(HttpURLConnection.HTTP_OK);
                    return gson.toJson(Map.of("message", "Watching the game successfully"));
                } else {
                    response.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                    return gson.toJson(Map.of("message", watchResult.message()));
                }
            } else {
                String username = userService.getUsernameFromToken(authToken);
                if (username == null) {
                    response.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                    return gson.toJson(Map.of("message", "Error: Unauthorized - Invalid token"));
                }

                JoinGameResult joinGameResult = gameService.joinGame(authToken, interactionRequest.gameID(), playerColor, username);

                response.type("application/json");
                if (joinGameResult.success()) {
                    response.status(HttpURLConnection.HTTP_OK);
                    return gson.toJson(joinGameResult);
                } else {
                    int statusCode = joinGameResult.message().contains("Error: Slot already taken") ? HttpURLConnection.HTTP_FORBIDDEN : HttpURLConnection.HTTP_UNAUTHORIZED;
                    response.status(statusCode);
                    return gson.toJson(Map.of("message", joinGameResult.message()));
                }
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

        Spark.init();
        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
