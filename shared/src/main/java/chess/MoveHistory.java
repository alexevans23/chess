package chess;

public class MoveHistory {
    private final ChessMove move;
    private final ChessPiece capturedPiece;
    private final boolean wasPromotion;

    public MoveHistory(ChessMove move, ChessPiece capturedPiece, boolean wasPromotion) {
        this.move = move;
        this.capturedPiece = capturedPiece;
        this.wasPromotion = wasPromotion;
    }

    // Getter methods
    public ChessMove getMove() {
        return move;
    }

    public ChessPiece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean wasPromotion() {
        return wasPromotion;
    }
}

