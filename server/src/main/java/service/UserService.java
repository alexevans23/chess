package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
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
        if (newUser.username() == null || newUser.username().trim().isEmpty() ||
                newUser.password() == null || newUser.password().trim().isEmpty() ||
                newUser.email() == null || newUser.email().trim().isEmpty()) {
            return new RegisterResult(null, null, false, "Error: bad request - missing or empty fields");
        }

        try {
            UserData existingUser = userDAO.getUser(newUser.username());
            if (existingUser != null) {
                return new RegisterResult(null, null, false, "Error: already taken");
            }
        } catch (DataAccessException ignored) {
        }

        try {
            userDAO.createUser(newUser);
            String authToken = generateAuthToken(newUser.username());
            AuthData authData = new AuthData(authToken, newUser.username());
            authDAO.createAuth(authData);
            return new RegisterResult(newUser.username(), authToken, true, "User registered successfully");
        } catch (DataAccessException e) {
            return new RegisterResult(null, null, false, "Registration failed: " + e.getMessage());
        }
    }

    public LoginResult login(UserData loginData) {
        try {
            UserData user = userDAO.getUser(loginData.username());
            if (user != null && checkPassword(loginData.password(), user.password())) {
                String authToken = generateAuthToken(user.username());
                AuthData authData = new AuthData(authToken, user.username());
                authDAO.createAuth(authData);
                return new LoginResult(user.username(), authToken, true, "Login successful");
            }
            return new LoginResult(null, null, false, "Error: Login failed: Invalid credentials");
        } catch (DataAccessException e) {
            return new LoginResult(null, null, false, "Error: Login failed: Invalid credentials");
        }
    }

    public LogoutResult logout(String authToken) {
        try {
            boolean tokenValid = authDAO.validateAuthToken(authToken);
            if (!tokenValid) {
                return new LogoutResult(false, "Error: Invalid or expired auth token");
            }
            authDAO.deleteAuth(authToken);
            return new LogoutResult(true, "Logout successful");
        } catch (DataAccessException e) {
            return new LogoutResult(false, "Error during logout: " + e.getMessage());
        }
    }

    private String generateAuthToken(String username) {
        return null;
    }

    private boolean checkPassword(String inputPassword, String storedPassword) {
        return false;
    }

    public void clearAllUsersAndAuthTokens() {
    }

    public String getUsernameFromToken(String authToken) {
        return null;
    }
}

