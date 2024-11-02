package dataaccess;

import model.AuthData;
import java.sql.*;
import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO {

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM auth";
        executeUpdate(statement);
    }

    @Override
    public boolean validateAuthToken(String authToken) throws DataAccessException {
        return false;
    }

    @Override
    public AuthData findAuthByToken(String authToken) throws DataAccessException {
        return null;
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
}

