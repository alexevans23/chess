package result;

public record LoginResult(String username, String authToken, boolean success, String message) {}

