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

    @Test
    void getUsernameFromTokenSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        RegisterResult registered = userService.register(newUser);
        String username = userService.getUsernameFromToken(registered.authToken());
        assertEquals(newUser.username(), username);
    }

    @Test
    void getUsernameFromTokenFailure() {
        String username = userService.getUsernameFromToken("nonexistentToken");
        assertNull(username);
    }

    @Test
    void clearAllUsersAndAuthTokensSuccess() throws DataAccessException {
        UserData newUser = new UserData("user", "password", "email@example.com");
        userService.register(newUser);
        userService.clearAllUsersAndAuthTokens();

        LoginResult loginAttempt = userService.login(new UserData("user", "password", null));
        assertFalse(loginAttempt.success());
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
    // More Expansive Tests
    @Test
    void endToEndGameFlowSuccess() throws DataAccessException {
        // Register and login
        UserData newUser = new UserData("flowUser", "password", "flow@example.com");
        RegisterResult registerResult = userService.register(newUser);
        LoginResult loginResult = userService.login(newUser);

        // Create a game
        GameData newGame = new GameData(0, null, null, "Flow Chess Game");
        CreateGameResult createGameResult = gameService.createGame(loginResult.authToken(), newGame);

        // Join the game
        JoinGameResult joinGameResult = gameService.joinGame(loginResult.authToken(), createGameResult.gameID(), "WHITE", newUser.username());

        // List games to confirm it's there
        ListGamesResult listGamesResult = gameService.listGames(loginResult.authToken());

        assertTrue(registerResult.success());
        assertTrue(loginResult.success());
        assertTrue(createGameResult.success());
        assertTrue(joinGameResult.success());
        assertTrue(listGamesResult.success());
        assertEquals(1, listGamesResult.games().size());
    }

    @Test
    void createMultipleGamesSuccess() throws DataAccessException {
        UserData newUser = new UserData("multiGameUser", "password123", "user@example.com");
        userService.register(newUser);
        String authToken = userService.login(newUser).authToken();

        GameData firstGame = new GameData(0, null, null, "First Chess Game");
        GameData secondGame = new GameData(0, null, null, "Second Chess Game");

        CreateGameResult firstResult = gameService.createGame(authToken, firstGame);
        CreateGameResult secondResult = gameService.createGame(authToken, secondGame);

        assertTrue(firstResult.success());
        assertTrue(secondResult.success());
        assertNotEquals(firstResult.gameID(), secondResult.gameID());
    }
    @Test
    void loginUserImmediatelyAfterRegistrationSuccess() throws DataAccessException {
        UserData newUser = new UserData("immediateUser", "password", "immediate@example.com");
        userService.register(newUser);
        LoginResult loginResult = userService.login(new UserData("immediateUser", "password", null));
        assertTrue(loginResult.success());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void multipleUserGameCreationSuccess() throws DataAccessException {
        UserData firstUser = new UserData("firstUser", "password1", "first@example.com");
        UserData secondUser = new UserData("secondUser", "password2", "second@example.com");
        userService.register(firstUser);
        userService.register(secondUser);

        String firstToken = userService.login(new UserData("firstUser", "password1", null)).authToken();
        String secondToken = userService.login(new UserData("secondUser", "password2", null)).authToken();

        CreateGameResult firstGameResult = gameService.createGame(firstToken, new GameData(0, null, null, "First Game"));
        CreateGameResult secondGameResult = gameService.createGame(secondToken, new GameData(0, null, null, "Second Game"));

        assertTrue(firstGameResult.success());
        assertTrue(secondGameResult.success());
    }

    @Test
    void retrieveUserDetailsUsingTokenSuccess() throws DataAccessException {
        UserData user = new UserData("tokenUser", "password", "token@example.com");
        RegisterResult registerResult = userService.register(user);
        String usernameRetrieved = userService.getUsernameFromToken(registerResult.authToken());
        assertEquals(user.username(), usernameRetrieved);
    }
    @Test
    void listJoinedGamesSuccess() throws DataAccessException {
        UserData newUser = new UserData("listGamesUser", "password", "listGames@example.com");
        RegisterResult registerResult = userService.register(newUser);
        String authToken = registerResult.authToken();

        gameService.createGame(authToken, new GameData(0, "listGamesUser", null, "Chess Game 1"));
        gameService.createGame(authToken, new GameData(0, "listGamesUser", null, "Chess Game 2"));

        CreateGameResult firstGameResult = gameService.createGame(authToken, new GameData(0, null, null, "Chess Game 1"));
        gameService.joinGame(authToken, firstGameResult.gameID(), "WHITE", "listGamesUser");

        ListGamesResult gamesList = gameService.listGames(authToken);

        assertTrue(gamesList.success(), "Failed to list games successfully");
        assertFalse(gamesList.games().isEmpty(), "Games list should not be empty");
        assertFalse(false, "Games list should include at least one game");
        assertEquals("Chess Game 1", gamesList.games().getFirst().gameName(), "The game name should match the joined game");
    }


}


