package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    private final Map<String, UserData> users = new HashMap<>();
    public void insertUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("User already exists with username: " + user.username());
        }
        users.put(user.username(), user);
    }
    public UserData getUser(String username) throws DataAccessException {
        UserData foundUser = users.get(username);
        if (foundUser == null) {
            throw new DataAccessException("No user found with username: " + username);
        }
        return foundUser;
    }
    public void updateUser(UserData user) throws DataAccessException {
        if (!users.containsKey(user.username())) {
            throw new DataAccessException("User does not exist: " + user.username());
        }
        users.put(user.username(), user);
    }
    public void deleteUser(String username) throws DataAccessException {
        if (!users.containsKey(username)) {
            throw new DataAccessException("User does not exist: " + username);
        }
        users.remove(username);
    }
    public void clear() {
        users.clear();
    }
    public Collection<UserData> getAllUsers() {
        return users.values();
    }
}

