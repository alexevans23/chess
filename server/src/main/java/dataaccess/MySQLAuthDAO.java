package dataaccess;

import model.AuthData;
import java.sql.*;
import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO {
    public MySQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        String statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String statement = "SELECT * FROM auth WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    return new AuthData(authToken, username);
                } else {
                    throw new DataAccessException("Auth token not found: " + authToken);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving auth: " + e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = "DELETE FROM auth WHERE authToken = ?";
        executeUpdate(statement, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM auth";
        executeUpdate(statement);
    }

    @Override
    public boolean validateAuthToken(String authToken) throws DataAccessException {
        String statement = "SELECT 1 FROM auth WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error validating auth token: " + e.getMessage());
        }
    }
    @Override
    public AuthData findAuthByToken(String authToken) throws DataAccessException {
        return getAuth(authToken);
    }
    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                } else if (param == null) {
                    ps.setNull(i + 1, NULL);
                }
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update database: " + e.getMessage());
        }
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(50) PRIMARY KEY,
                username VARCHAR(50) NOT NULL
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

