package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDAO {

    private final Map<Integer, GameData> games = new HashMap<>();
    public void insertGame(GameData game) throws DataAccessException {
        if (games.containsKey(game.gameID())) {
            throw new DataAccessException("Game already exists with ID: " + game.gameID());
        }
        games.put(game.gameID(), game);
    }
    public GameData getGame(int gameID) throws DataAccessException {
        GameData foundGame = games.get(gameID);
        if (foundGame == null) {
            throw new DataAccessException("Game not found with ID: " + gameID);
        }
        return foundGame;
    }
    public void updateGame(GameData game) throws DataAccessException {
        if (!games.containsKey(game.gameID())) {
            throw new DataAccessException("Cannot update non-existing game with ID: " + game.gameID());
        }
        games.put(game.gameID(), game);
    }
    public void deleteGame(int gameID) throws DataAccessException {
        if (!games.containsKey(gameID)) {
            throw new DataAccessException("Cannot delete non-existing game with ID: " + gameID);
        }
        games.remove(gameID);
    }
    public void clear() {
        games.clear();
    }
    public Collection<GameData> getAllGames() {
        return games.values();
    }
}

