package result;

import model.GameData;

import java.util.List;

public record ListGamesResult(boolean success, String message, List<GameData> games) {}

