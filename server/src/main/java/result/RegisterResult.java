package result;

public record RegisterResult(String username, String authToken, boolean success, String message) {}

