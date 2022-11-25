package model;

import java.awt.Point;
import java.util.List;

public class MoveValidator {
    public static boolean isValidMove(Game game,
                                      int startIndex, int endIndex) {
        return game != null && isValidMove(game.getBoard(),
                game.isPlayer1Turn(), startIndex, endIndex, game.getSkipIndex());
    }

    public static boolean isValidMove(Board board, boolean isPlayer1Turn,
                                      int startIndex, int endIndex, int skipIndex) {

        if ((board == null || !Board.isValidIndex(startIndex) ||
                !Board.isValidIndex(endIndex)) || (startIndex == endIndex) || (Board.isValidIndex(skipIndex) && skipIndex != startIndex)) {
            return false;
        }

        if (!validateIDsWhileSkipping(board, isPlayer1Turn, startIndex, endIndex)) {
            return false;
        } else return validateDistanceWhileSkipping(board, isPlayer1Turn, startIndex, endIndex);

    }

    private static boolean validateIDsWhileSkipping(Board board, boolean isP1Turn,
                                                    int startIndex, int endIndex) {

        if (board.get(endIndex) != Board.EMPTY) {
            return false;
        }

        int id = board.get(startIndex);
        if ((isP1Turn && id != Board.BLACK_CHECKER_ID && id != Board.BLACK_KING_ID)
                || (!isP1Turn && id != Board.WHITE_CHECKER_ID
                && id != Board.WHITE_KING_ID)) {
            return false;
        }

        Point middle = Board.middle(startIndex, endIndex);
        int midID = board.get(Board.toIndex(middle));
        return midID == Board.INVALID || ((isP1Turn ||
                midID == Board.BLACK_CHECKER_ID || midID == Board.BLACK_KING_ID) &&
                (!isP1Turn || midID == Board.WHITE_CHECKER_ID ||
                        midID == Board.WHITE_KING_ID));

    }

    private static boolean validateDistanceWhileSkipping(Board board, boolean isPlayer1Turn,
                                                         int startIndex, int endIndex) {

        Point startPoint = Board.toPoint(startIndex);
        Point endPoint = Board.toPoint(endIndex);
        int diagonalX = endPoint.x - startPoint.x;
        int diagonalY = endPoint.y - startPoint.y;
        if (Math.abs(diagonalX) != Math.abs(diagonalY) || Math.abs(diagonalX) > 2 || diagonalX == 0) {
            return false;
        }

        int id = board.get(startIndex);
        if ((id == Board.WHITE_CHECKER_ID && diagonalY > 0) ||
                (id == Board.BLACK_CHECKER_ID && diagonalY < 0)) {
            return false;
        }

        Point middle = Board.middle(startIndex, endIndex);
        int midID = board.get(Board.toIndex(middle));
        if (midID < 0) {

            List<Point> checkers;
            if (isPlayer1Turn) {
                checkers = board.findAllById(Board.BLACK_CHECKER_ID);
                checkers.addAll(board.findAllById(Board.BLACK_KING_ID));
            } else {
                checkers = board.findAllById(Board.WHITE_CHECKER_ID);
                checkers.addAll(board.findAllById(Board.WHITE_KING_ID));
            }

            for (Point p : checkers) {
                int index = Board.toIndex(p);
                if (!MoveGenerator.getSkipMoves(board, index).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

}