package service;

import dataaccess.MySQLUserDAO;
import dataaccess.DataAccessException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            MySQLUserDAO userDAO = new MySQLUserDAO();
        } catch (DataAccessException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
