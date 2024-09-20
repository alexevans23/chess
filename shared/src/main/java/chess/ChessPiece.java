package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();

        switch (this.getPieceType()) {
            case KING -> kingMoves(board, myPosition, moves);
            case QUEEN -> queenMoves(board, myPosition, moves);
            case BISHOP -> bishopMoves(board, myPosition, moves);
            case KNIGHT -> knightMove(board, myPosition, moves);
            case ROOK -> rookMove(board, myPosition, moves);
            case PAWN -> pawnMove(board, myPosition, moves);
        }
        return moves;
    }
    private void kingMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int[][] directions = {{1,1},{1,0},{0,1},{-1,-1},{-1,0},{0,-1},{-1,1},{1,-1}};
        for (int[] dir : directions) {
            addMoveIfValid(board, myPosition, dir[0], dir[1], moves);
        }
    }
    private void queenMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int[][] directions = {{1,1},{1,0},{0,1},{-1,-1},{-1,0},{0,-1},{-1,1},{1,-1}};
        for (int[] direction : directions) {
            addMovesInDirection(board, myPosition, moves, direction[0], direction[1]);
        }
    }
    private void bishopMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int[][] directions = {{1,1},{-1,1},{-1,-1},{1,-1}};
        for (int[] direction : directions) {
            addMovesInDirection(board, myPosition, moves, direction[0], direction[1]);
        }
    }
    private void knightMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int[][] directions = {{1,2},{2,1},{-1,2},{-2,1},{-1,-2},{-2,-1},{1,-2},{2,-1}};
        for (int[] dir : directions) {
            addMoveIfValid(board, myPosition, dir[0], dir[1], moves);
        }
    }
    private void rookMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int[][] directions = {{1,0},{0,1},{-1,0},{0,-1}};
        for (int[] direction : directions) {
            addMovesInDirection(board, myPosition, moves, direction[0], direction[1]);
        }
    }
    private void pawnMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int direction = this.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int nextRow = myPosition.getRow() + direction;
        if (validMove(nextRow, myPosition.getColumn())) {
            ChessPosition newPosition = new ChessPosition(nextRow, myPosition.getColumn());
            if (board.getPiece(newPosition) == null) {
                addPawnMove(board, myPosition, newPosition, moves);
            }
        }
        if ((this.getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
                (this.getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
            int twoRowsAhead = myPosition.getRow() + 2 * direction;
            ChessPosition twoSquaresPosition = new ChessPosition(twoRowsAhead, myPosition.getColumn());
            if (board.getPiece(new ChessPosition(nextRow, myPosition.getColumn())) == null && board.getPiece(twoSquaresPosition) == null) {
                addPawnMove(board, myPosition, twoSquaresPosition, moves);
            }
        }
        int[] captureCols = {myPosition.getColumn() - 1, myPosition.getColumn() + 1};
        for (int col : captureCols) {
            if (validMove(nextRow, col)) {
                ChessPosition capturePosition = new ChessPosition(nextRow, col);
                ChessPiece pieceAtCapturePosition = board.getPiece(capturePosition);
                if (pieceAtCapturePosition != null && pieceAtCapturePosition.getTeamColor() != this.getTeamColor()) {
                    addPawnMove(board, myPosition, capturePosition, moves);
                }
            }
        }
    }
    private void addPawnMove(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition, Collection<ChessMove> moves) {
        boolean isPromotionRow = (this.getTeamColor() == ChessGame.TeamColor.WHITE && newPosition.getRow() == 8) ||
                (this.getTeamColor() == ChessGame.TeamColor.BLACK && newPosition.getRow() == 1);

        if (isPromotionRow) {
            for (ChessPiece.PieceType promotionType : new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP}) {
                moves.add(new ChessMove(myPosition, newPosition, promotionType));
            }
        } else {
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
    }

    private void addMovesInDirection(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int rowDirection, int colDirection) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        while (true) {
            row += rowDirection;
            col += colDirection;

            if (!validMove(row, col)) {
                break;
            }

            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

            if (pieceAtNewPosition == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else {
                if (pieceAtNewPosition.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                break;
            }
        }
    }
    private void addMoveIfValid(ChessBoard board, ChessPosition currentPosition, int dRow, int dCol, Collection<ChessMove> moves) {
        int newRow = currentPosition.getRow() + dRow;
        int newCol = currentPosition.getColumn() + dCol;
        if (validMove(newRow, newCol)) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
            if (pieceAtNewPosition == null || pieceAtNewPosition.getTeamColor() != this.getTeamColor()) {
                moves.add(new ChessMove(currentPosition, newPosition, null));
            }
        }
    }
    private boolean validMove(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
    public String getSymbol() {
        return switch (this.type) {
            case PAWN -> pieceColor == ChessGame.TeamColor.WHITE ? "P" : "p";
            case KNIGHT -> pieceColor == ChessGame.TeamColor.WHITE ? "N" : "n";
            case BISHOP -> pieceColor == ChessGame.TeamColor.WHITE ? "B" : "b";
            case ROOK -> pieceColor == ChessGame.TeamColor.WHITE ? "R" : "r";
            case QUEEN -> pieceColor == ChessGame.TeamColor.WHITE ? "Q" : "q";
            case KING -> pieceColor == ChessGame.TeamColor.WHITE ? "K" : "k";
        };
    }
}


