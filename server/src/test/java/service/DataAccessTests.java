package service;

import dataaccess.MySQLUserDAO;
import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
    private MySQLUserDAO userDAO;
    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new MySQLUserDAO();
        userDAO.configureDatabase();
        userDAO.clear();
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        userDAO.clear();
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
}



