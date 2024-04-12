package service;

import dataAccess.*;
import model.*;
import result.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {

    private UserService userService;
    private GameService gameService;

    @BeforeEach
    void setup() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
    }

    // UserService Tests
    @Test
    void registerUserSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        RegisterResult result = userService.register(newUser);
        assertTrue(result.success());
        assertNotNull(result.authToken());
    }

    @Test
    void registerUserFailure() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        userService.register(newUser);
        RegisterResult result = userService.register(newUser);
        assertFalse(result.success());
        assertEquals("Error: already taken", result.message());
    }

    @Test
    void loginUserSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        userService.register(newUser);
        LoginResult result = userService.login(new UserData("user", "password", null));
        assertTrue(result.success());
        assertNotNull(result.authToken());
    }

    @Test
    void loginUserFailure() throws DataAccessException {
        UserData newUser = new UserData("user", "wrongPassword", null);
        LoginResult result = userService.login(newUser);
        assertFalse(result.success());
    }

    @Test
    void logoutSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        RegisterResult registered = userService.register(newUser);
        LogoutResult result = userService.logout(registered.authToken());
        assertTrue(result.success());
    }

    @Test
    void logoutFailure() throws DataAccessException {
        LogoutResult result = userService.logout("invalidToken");
        assertFalse(result.success());
    }

    // GameService Tests
    @Test
    void createGameSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        RegisterResult registered = userService.register(newUser);
        GameData newGame = new GameData(0, null, null, "Chess");
        CreateGameResult result = gameService.createGame(registered.authToken(), newGame);
        assertTrue(result.success());
        assertNotEquals(-1, result.gameID());
    }

    @Test
    void createGameFailure() throws DataAccessException {
        GameData newGame = new GameData(0, null, null, "Chess");
        CreateGameResult result = gameService.createGame("invalidToken", newGame);
        assertFalse(result.success());
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        RegisterResult registered = userService.register(newUser);
        GameData newGame = new GameData(0, null, null, "Chess");
        CreateGameResult created = gameService.createGame(registered.authToken(), newGame);
        JoinGameResult result = gameService.joinGame(registered.authToken(), created.gameID(), "WHITE", "user");
        assertTrue(result.success());
    }

    @Test
    void joinGameFailure() throws DataAccessException {
        JoinGameResult result = gameService.joinGame("invalidToken", 1, "WHITE", "user");
        assertFalse(result.success());
    }

    @Test
    void watchGameSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        RegisterResult registered = userService.register(newUser);

        GameData newGame = new GameData(0, null, null, "Chess");
        CreateGameResult createGameResult = gameService.createGame(registered.authToken(), newGame);
        System.out.println("Created game: " + createGameResult);

        JoinGameResult result = gameService.watchGame(registered.authToken(), createGameResult.gameID());
        System.out.println("Watch game result: " + result);

        assertTrue(result.success());
    }

    @Test
    void watchGameFailure() throws DataAccessException {
        JoinGameResult result = gameService.watchGame("invalidToken", 999);
        assertFalse(result.success());
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        RegisterResult registered = userService.register(newUser);
        ListGamesResult result = gameService.listGames(registered.authToken());
        assertTrue(result.success());
        assertNotNull(result.games());
        assertTrue(result.games().isEmpty());
    }

    @Test
    void listGamesFailure() throws DataAccessException {
        ListGamesResult result = gameService.listGames("invalidToken");
        assertFalse(result.success());
    }

    @Test
    void clearAllGames() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        RegisterResult registered = userService.register(newUser);
        GameData newGame = new GameData(0, null, null, "Chess");
        gameService.createGame(registered.authToken(), newGame);
        gameService.clearAllGames();

        ListGamesResult result = gameService.listGames(registered.authToken());
        assertTrue(result.success());
        assertTrue(result.games().isEmpty());
    }

    @Test
    void clearAllUsersAndAuthTokens() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        userService.register(newUser);
        userService.clearAllUsersAndAuthTokens();

        LoginResult loginAttempt = userService.login(new UserData("user", "password", null));
        assertFalse(loginAttempt.success());
    }

}


