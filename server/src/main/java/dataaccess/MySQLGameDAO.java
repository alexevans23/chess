package dataaccess;

import model.GameData;
import java.sql.*;
import java.util.List;

public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        String statement = "INSERT INTO games (whiteUsername, blackUsername, gameName) VALUES (?, ?, ?)";
        return executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT * FROM games WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setInt(1, gameID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    return new GameData(gameID, whiteUsername, blackUsername, gameName);
                } else {
                    throw new DataAccessException("Game not found: ID " + gameID);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game: " + e.getMessage());
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
    }
    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM games";
        executeUpdate(statement, false);
    }
    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                switch (params[i]) {
                    case String s -> ps.setString(i + 1, s);
                    case Integer integer -> ps.setInt(i + 1, integer);
                    case null -> ps.setNull(i + 1, Types.NULL);
                    default -> {
                    }
                }
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error executing update: " + e.getMessage());
        }
    }
    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS games (
            gameID INT NOT NULL AUTO_INCREMENT,
            whiteUsername VARCHAR(50) NOT NULL,
            blackUsername VARCHAR(50) NOT NULL,
            gameName VARCHAR(100),
            PRIMARY KEY (gameID)
        )
        """
    };
    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            System.out.println("Connected to database for configuration.");
            for (String statement : createStatements) {
                System.out.println("Executing statement: " + statement);
                try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                    System.out.println("Table created or already exists.");
                } catch (SQLException ex) {
                    System.err.println("Error executing statement: " + statement);
                    throw new DataAccessException("Unable to configure database: " + ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: " + ex.getMessage());
        }
    }
}

