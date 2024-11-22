package service;

import dataaccess.MySQLGameDAO;
import dataaccess.MySQLUserDAO;
import dataaccess.MySQLAuthDAO;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
    private MySQLUserDAO userDAO;
    private MySQLAuthDAO authDAO;
    private MySQLGameDAO gameDAO;
    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new MySQLUserDAO();
        userDAO.configureDatabase();
        userDAO.clear();

        authDAO = new MySQLAuthDAO();
        authDAO.configureDatabase();
        authDAO.clear();

        gameDAO = new MySQLGameDAO();
        gameDAO.configureDatabase();
        gameDAO.clear();
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
    @Test
    public void testCreateUserSuccess() {
        UserData newUser = new UserData("test_user", "hashed_password_example", "test@example.com");

        assertDoesNotThrow(() -> {
            userDAO.createUser(newUser);
            System.out.println("User created successfully!");
        });
    }
    @Test
    public void testCreateUserDuplicate() {
        UserData newUser = new UserData("duplicate_user", "hashed_password_example", "duplicate@example.com");
        assertDoesNotThrow(() -> userDAO.createUser(newUser));
        assertThrows(DataAccessException.class, () -> userDAO.createUser(newUser));
    }
    @Test
    public void testClear() {
        UserData user1 = new UserData("user1", "password1", "user1@example.com");
        UserData user2 = new UserData("user2", "password2", "user2@example.com");
        assertDoesNotThrow(() -> {
            userDAO.createUser(user1);
            userDAO.createUser(user2);
            userDAO.clear();
            assertThrows(DataAccessException.class, () -> userDAO.getUser("user1"));
            assertThrows(DataAccessException.class, () -> userDAO.getUser("user2"));
        });
    }
    @Test
    public void testGetUserSuccess() {
        UserData newUser = new UserData("test_user", "hashed_password_example", "test@example.com");

        assertDoesNotThrow(() -> {
            userDAO.createUser(newUser);
            UserData retrievedUser = userDAO.getUser("test_user");
            assertNotNull(retrievedUser);
            assertEquals("test_user", retrievedUser.username());
            assertEquals("hashed_password_example", retrievedUser.password());
            assertEquals("test@example.com", retrievedUser.email());
            System.out.println("User retrieved successfully");
        });
    }
    @Test
    public void testGetUserNotFound() {
        assertThrows(DataAccessException.class, () -> userDAO.getUser("non_existent_user"));
    }
    @Test
    public void testCreateAuthSuccess() {
        AuthData newAuth = new AuthData("authToken123", "test_user");

        assertDoesNotThrow(() -> {
            authDAO.createAuth(newAuth);
            System.out.println("Auth token created successfully!");
        });
    }
    @Test
    public void testCreateAuthDuplicate() {
        AuthData newAuth = new AuthData("duplicateToken", "test_user");

        assertDoesNotThrow(() -> authDAO.createAuth(newAuth));
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(newAuth));
    }
    @Test
    public void testGetAuthSuccess() {
        AuthData newAuth = new AuthData("authToken123", "test_user");

        assertDoesNotThrow(() -> {
            authDAO.createAuth(newAuth);
            AuthData retrievedAuth = authDAO.getAuth("authToken123");
            assertNotNull(retrievedAuth);
            assertEquals("authToken123", retrievedAuth.authToken());
            assertEquals("test_user", retrievedAuth.username());
            System.out.println("Auth token retrieved successfully!");
        });
    }
    @Test
    public void testGetAuthNotFound() {
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("non_existent_token"));
    }
    @Test
    public void testValidateAuthTokenSuccess() throws DataAccessException {
        AuthData newAuth = new AuthData("validToken", "test_user");

        assertDoesNotThrow(() -> authDAO.createAuth(newAuth));
        assertTrue(authDAO.validateAuthToken("validToken"));
    }
    @Test
    public void testValidateAuthTokenFailure() throws DataAccessException {
        assertFalse(authDAO.validateAuthToken("invalidToken"));
    }
    @Test
    public void testDeleteAuthSuccess() {
        AuthData newAuth = new AuthData("tokenToDelete", "test_user");

        assertDoesNotThrow(() -> {
            authDAO.createAuth(newAuth);
            authDAO.deleteAuth("tokenToDelete");
            assertThrows(DataAccessException.class, () -> authDAO.getAuth("tokenToDelete"));
        });
    }
}




