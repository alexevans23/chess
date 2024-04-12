package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean validateAuthToken(String authToken) throws DataAccessException;
    AuthData findAuthByToken(String authToken) throws DataAccessException;
}



