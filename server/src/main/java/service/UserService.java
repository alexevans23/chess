package service;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import result.LoginResult;
import result.RegisterResult;
import result.LogoutResult;
public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public RegisterResult register(UserData newUser) {
        try {
            userDAO.createUser(newUser);
            String authToken = generateAuthToken(newUser.username());
            return new RegisterResult(newUser.username(), authToken, true, "User registered successfully");
        } catch (DataAccessException e) {
            return new RegisterResult(newUser.username(), null, false, "Registration failed: " + e.getMessage());
        }
    }
    public LoginResult login(UserData loginData) {
        try {
            UserData user = userDAO.getUser(loginData.username());
            if (checkPassword(loginData.password(), user.password())) {
                String authToken = generateAuthToken(user.username());
                return new LoginResult(true, "Login successful", authToken);
            }
            return new LoginResult(false, "Login failed: Invalid credentials", null);
        } catch (DataAccessException e) {
            return new LoginResult(false, "Login failed: " + e.getMessage(), null);
        }
    }
    public LogoutResult logout(String authToken) {
        try {
            authDAO.deleteAuth(authToken);
            return new LogoutResult(true, "Logout successful");
        } catch (DataAccessException e) {
            return new LogoutResult(false, "Logout failed: " + e.getMessage());
        }
    }
    private String generateAuthToken(String username) {
        return java.util.UUID.randomUUID().toString();
    }
    private boolean userExists(String username) {
        try {
            return userDAO.getUser(username) != null;
        } catch (DataAccessException e) {
            return false;
        }
    }
    private boolean checkPassword(String inputPassword, String storedPassword) {
        return inputPassword.equals(storedPassword);
    }
    public void clearAllUsersAndAuthTokens() {
        try {
            userDAO.clear();
            authDAO.clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear users and auths: " + e.getMessage(), e);
        }
    }
}


