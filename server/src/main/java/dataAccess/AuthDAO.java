package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO {

    private final Map<String, AuthData> authTokens = new HashMap<>();
    public void createAuthToken(AuthData authData) throws DataAccessException {
        if (authTokens.containsKey(authData.authToken())) {
            throw new DataAccessException("AuthToken already exists: " + authData.authToken());
        }
        authTokens.put(authData.authToken(), authData);
    }
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        AuthData authData = authTokens.get(authToken);
        if (authData == null) {
            throw new DataAccessException("AuthToken not found: " + authToken);
        }
        return authData;
    }
    public void deleteAuthToken(String authToken) throws DataAccessException {
        if (!authTokens.containsKey(authToken)) {
            throw new DataAccessException("AuthToken not found: " + authToken);
        }
        authTokens.remove(authToken);
    }
    public void clearAuthTokens() {
        authTokens.clear();
    }
}

