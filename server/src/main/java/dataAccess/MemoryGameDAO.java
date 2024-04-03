package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void createGame(GameData game) throws DataAccessException {
        if (games.containsKey(game.gameID())) {
            throw new DataAccessException("Game already exists with ID: " + game.gameID());
        }
        games.put(game.gameID(), game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game not found with ID: " + gameID);
        }
        return game;
    }

    @Override
    public List<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (!games.containsKey(game.gameID())) {
            throw new DataAccessException("Cannot update non-existing game with ID: " + game.gameID());
        }
        games.put(game.gameID(), game);
    }
}

