package model;

import java.awt.Point;
import java.util.List;


public class Game {

    private Board board;
    private boolean isPlayer1Turn;
    private int skipIndex;

    public Game() {
        restart();
    }

    public Game copyCurrentBoard() {
        Game game = new Game();
        game.board = board.copy();
        game.isPlayer1Turn = isPlayer1Turn;
        game.skipIndex = skipIndex;
        return game;
    }
    public void restart() {
        this.board = new Board();
        this.isPlayer1Turn = true;
        this.skipIndex = -1;
    }
    public void move(Point start, Point end) {
        if (start == null || end == null) {
            return;
        }
        move(Board.toIndex(start), Board.toIndex(end));
    }

    public void move(int startIndex, int endIndex) {

        if (!MoveValidator.isValidMove(this, startIndex, endIndex)) {
            return;
        }

        Point middle = Board.middle(startIndex, endIndex);
        int midIndex = Board.toIndex(middle);
        this.board.set(endIndex, board.get(startIndex));
        this.board.set(midIndex, Board.EMPTY);
        this.board.set(startIndex, Board.EMPTY);

        Point end = Board.toPoint(endIndex);
        int id = board.get(endIndex);
        boolean switchPlayer = false;
        if (end.y == 0 && id == Board.WHITE_CHECKER_ID) {
            this.board.set(endIndex, Board.WHITE_KING_ID);
            switchPlayer = true;
        } else if (end.y == 7 && id == Board.BLACK_CHECKER_ID) {
            this.board.set(endIndex, Board.BLACK_KING_ID);
            switchPlayer = true;
        }

        boolean midValid = Board.isValidIndex(midIndex);
        if (midValid) {
            this.skipIndex = endIndex;
        }
        if (!midValid || MoveGenerator.getSkipMoves(
                board.copy(), endIndex).isEmpty()) {
            switchPlayer = true;
        }
        if (switchPlayer) {
            this.isPlayer1Turn = !isPlayer1Turn;
            this.skipIndex = -1;
        }

    }

    public Board getBoard() {
        return board.copy();
    }

    public boolean isGameOver() {
        List<Point> black = board.findAllById(Board.BLACK_CHECKER_ID);
        black.addAll(board.findAllById(Board.BLACK_KING_ID));
        if (black.isEmpty()) {
            return true;
        }
        List<Point> white = board.findAllById(Board.WHITE_CHECKER_ID);
        white.addAll(board.findAllById(Board.WHITE_KING_ID));
        if (white.isEmpty()) {
            return true;
        }

        List<Point> test = isPlayer1Turn ? black : white;
        for (Point p : test) {
            int i = Board.toIndex(p);
            if (!MoveGenerator.getNormalMoves(board, i).isEmpty() ||
                    !MoveGenerator.getSkipMoves(board, i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public int getSkipIndex() {
        return skipIndex;
    }

    public String getGameState() {

        // Add the game board
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < 32; i ++) {
            state.append("").append(board.get(i));
        }

        // Add the other info
        state.append(isPlayer1Turn ? "1" : "0");
        state.append(skipIndex);
        return state.toString();
    }
    public void setGameState(String state) {

        restart();

        if (state == null || state.isEmpty()) {
            return;
        }

        int n = state.length();
        for (int i = 0; i < 32 && i < n; i ++) {
            try {
                int id = Integer.parseInt("" + state.charAt(i));
                this.board.set(i, id);
            } catch (NumberFormatException ignored) {}
        }

        if (n > 32) {
            this.isPlayer1Turn = (state.charAt(32) == '1');
        }
        if (n > 33) {
            try {
                this.skipIndex = Integer.parseInt(state.substring(33));
            } catch (NumberFormatException e) {
                this.skipIndex = -1;
            }
        }
    }
}