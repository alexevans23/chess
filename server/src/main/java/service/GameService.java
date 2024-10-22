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
        if (!authDAO.validateAuthToken(authToken)) {
            return new JoinGameResult(false, "Error: Unauthorized - Invalid auth token", -1);
        }

        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            return new JoinGameResult(false, "Error: Game not found", -1);
        }

        if (("WHITE".equalsIgnoreCase(playerColor) && game.whiteUsername() != null) ||
                ("BLACK".equalsIgnoreCase(playerColor) && game.blackUsername() != null)) {
            return new JoinGameResult(false, "Error: Slot already taken", -1);
        }

        if ("WHITE".equalsIgnoreCase(playerColor)) {
            game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName());
        } else if ("BLACK".equalsIgnoreCase(playerColor)) {
            game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName());
        }

        gameDAO.updateGame(game);
        return new JoinGameResult(true, "Joined game successfully", gameID);
    }

    public JoinGameResult watchGame(String authToken, int gameID) {
        try {
            if (!authDAO.validateAuthToken(authToken)) {
                return new JoinGameResult(false, "Error: Unauthorized - Invalid auth token", -1);
            }

            GameData game = gameDAO.getGame(gameID);
            if (game == null) {
                return new JoinGameResult(false, "Error: Game not found", -1);
            }

            return new JoinGameResult(true, "Watching game successfully", gameID);
        } catch (DataAccessException e) {
            return new JoinGameResult(false, "Failed to watch game: " + e.getMessage(), -1);
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

