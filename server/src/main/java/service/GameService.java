package service;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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

    public JoinGameResult joinGame(String authToken, int gameID, String playerColor, String username) throws DataAccessException {
        return null;
    }

    public JoinGameResult watchGame(String authToken, int gameID) {
        return null;
    }

    public ListGamesResult listGames(String authToken) {
        return null;
    }

    public void clearAllGames() {
    }
}

