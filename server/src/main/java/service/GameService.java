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
        return null;
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

