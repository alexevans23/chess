package chess;

public record MoveHistory(ChessMove move, ChessPiece capturedPiece, boolean wasPromotion) { }
