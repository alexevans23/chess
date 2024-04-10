package service;

import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
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
            boolean tokenValid = authDAO.validateAuthToken(authToken);
            if (!tokenValid) {
                return new CreateGameResult(false, "Error: Invalid auth token or game ID", -1);
            }
            int gameID = gameDAO.createGame(gameData);
            return new CreateGameResult(true, "Game created successfully", gameID);
        } catch (DataAccessException e) {
            return new CreateGameResult(false, e.getMessage(), -1);
        }
    }

    public JoinGameResult joinGame(String authToken, int gameID, String playerColor) {
        try {
            if (authDAO.getAuth(authToken) == null || gameDAO.getGame(gameID) == null) {
                return new JoinGameResult(false, "Error: Invalid auth token or game ID", -1);
            }
            return new JoinGameResult(true, "Joined game successfully", gameID);
        } catch (DataAccessException e) {
            return new JoinGameResult(false, e.getMessage(), -1);
        }
    }

    public ListGamesResult listGames(String authToken) {
        try {
            if (authDAO.getAuth(authToken) == null) {
                return new ListGamesResult(false, "Error: unauthorized", null);
            }
            List<GameData> games = gameDAO.listGames();
            return new ListGamesResult(true, "Games listed successfully", games);
        } catch (DataAccessException e) {
            return new ListGamesResult(false, e.getMessage(), null);
        }
    }

    public void clearAllGames() {
        try {
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to clear games: " + e.getMessage(), e);
        }
    }
}


