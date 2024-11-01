package dataaccess;
import model.UserData;
import java.sql.*;
import com.google.gson.Gson;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLUserDAO implements UserDAO {
    private final Gson gson = new Gson();
    public MySQLUserDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM users";
        executeUpdate(statement);
    }

    @Override

    public void createUser(UserData user) throws DataAccessException {
        String statement = "INSERT INTO users (username, password, email, json) VALUES (?, ?, ?, ?)";
        String json = gson.toJson(user);

        executeUpdate(statement, user.username(), user.password(), user.email(), json);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
    private void testConnection() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to connect to database: " + e.getMessage());
        }
    }
    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                switch (param) {
                    case String p -> ps.setString(i + 1, p);
                    case Integer p -> ps.setInt(i + 1, p);
                    case null -> ps.setNull(i + 1, NULL);
                    default -> {
                    }
                }
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update database: " + e.getMessage());
        }
    }
    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS users (
            id INT NOT NULL AUTO_INCREMENT,
            username VARCHAR(50) NOT NULL UNIQUE,
            json TEXT DEFAULT NULL,
            PRIMARY KEY (id)
        )
        """
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: " + ex.getMessage());
        }
    }
}

