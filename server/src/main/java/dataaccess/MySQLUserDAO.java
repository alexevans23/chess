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
        String statement = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                } else {
                    throw new DataAccessException("User not found: " + username);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user: " + e.getMessage());
        }
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
            password VARCHAR(255) NOT NULL,
            email VARCHAR(100),
            json TEXT DEFAULT NULL,
            PRIMARY KEY (id)
        )
        """
    };
    public void configureDatabase() throws DataAccessException {
        // Ensure the database itself exists
        DatabaseManager.createDatabase();

        // Now create tables if they do not exist
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

