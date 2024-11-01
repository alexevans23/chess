package dataaccess;

import model.UserData;
import java.sql.*;

public class MySQLUserDAO implements UserDAO {
    public MySQLUserDAO() throws DataAccessException {
        testConnection();
    }
    @Override
    public void clear() throws DataAccessException {
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
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
}

