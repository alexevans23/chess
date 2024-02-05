package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    //initialize the chessboard
    private ChessBoard board;
    private TeamColor teamTurn;
    private Set<ChessMove> potentialMoves = new HashSet<>();
    private Stack<MoveHistory> moveHistory = new Stack<>();

    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;
        board.resetBoard();
        this.moveHistory = new Stack<>();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }
    public void applyMove(ChessMove move) {
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());
        ChessPiece capturedPiece = board.getPiece(move.getEndPosition());

        // Apply the move on the board
        board.applyMove(move);

        // Track the move for potential undoing
        boolean wasPromotion = move.getPromotionPiece() != null;
        moveHistory.push(new MoveHistory(move, capturedPiece, wasPromotion));
    }
    public void undoLastMove() {
        if (moveHistory.isEmpty()) {
            System.out.println("No moves to undo.");
            return;
        }

        MoveHistory lastMoveHistory = moveHistory.pop();
        ChessMove lastMove = lastMoveHistory.getMove();
        ChessPiece movedPiece = board.getPiece(lastMove.getEndPosition()); // The piece that was moved to the end position

        // If the last move was a promotion, we need to revert the piece back to a pawn, otherwise, move back the original piece
        ChessPiece originalPiece = lastMoveHistory.wasPromotion() ?
                new ChessPiece(movedPiece.getTeamColor(), ChessPiece.PieceType.PAWN) : movedPiece;

        // Move the piece back to its start position
        board.addPiece(lastMove.getStartPosition(), originalPiece);

        // Restore the captured piece, if there was one
        if (lastMoveHistory.getCapturedPiece() != null) {
            board.addPiece(lastMove.getEndPosition(), lastMoveHistory.getCapturedPiece());
        } else {
            // Ensure the end position is cleared if no piece was captured
            board.addPiece(lastMove.getEndPosition(), null);
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        Collection<ChessMove> potentialMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);

        for (ChessMove move : potentialMoves) {
            applyMove(move);
            if (!isInCheck(teamTurn)) {
                validMoves.add(move);
            }
            undoLastMove();
        }

        return validMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
