package model;

public class GameInteractionRequest {
    private int gameID;
    private String playerColor;
    private String role;

    public GameInteractionRequest(int gameID, String playerColor, String role) {
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.role = role;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

