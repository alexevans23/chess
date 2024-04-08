package service;

import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import model.GameData;
import result.CreateGameResult;
import result.ListGamesResult;
import result.JoinGameResult;

import java.util.List;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateGameResult createGame(String authToken, GameData gameData) {
        try {
            if (authDAO.getAuth(authToken) == null) {
                return new CreateGameResult(false, "Invalid auth token", -1);
            }
            gameDAO.createGame(gameData);
            return new CreateGameResult(true, "Game created successfully", gameData.gameID());
        } catch (Exception e) {
            return new CreateGameResult(false, "Game creation failed: " + e.getMessage(), -1);
        }
    }

    public JoinGameResult joinGame(String authToken, int gameID, String playerColor) {
        try {
            if (authDAO.getAuth(authToken) == null || gameDAO.getGame(gameID) == null) {
                return new JoinGameResult(false, "Invalid auth token or game ID", -1);
            }
            // Additional logic for joining the game
            // This is simplified; you would normally update the game's state here
            return new JoinGameResult(true, "Joined game successfully", gameID);
        } catch (Exception e) {
            return new JoinGameResult(false, "Failed to join game: " + e.getMessage(), -1);
        }
    }

    public ListGamesResult listGames(String authToken) {
        try {
            if (authDAO.getAuth(authToken) == null) {
                return new ListGamesResult(false, "Invalid auth token", null);
            }
            List<GameData> games = gameDAO.listGames();
            return new ListGamesResult(true, "Games listed successfully", games);
        } catch (Exception e) {
            return new ListGamesResult(false, "Failed to list games: " + e.getMessage(), null);
        }
    }

    public void clearAllGames() {
        try {
            gameDAO.clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear games: " + e.getMessage(), e);
        }
    }
}

