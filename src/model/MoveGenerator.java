package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    private static List<Point> getEndPoints(Board board, int startIndex, int move){
        System.out.println(board);
        List<Point> endPoints = new ArrayList<>();
        if (board == null || !Board.isValidIndex(startIndex)) {
            return endPoints;
        }

        int id = board.get(startIndex);
        Point p = Board.toPoint(startIndex);
        addPoints(endPoints, p, id, move);

        return endPoints;
    }
    public static void addPoints(List<Point> points, Point point, int id, int move) {

        boolean isKing = (id == Board.BLACK_KING_ID || id == Board.WHITE_KING_ID);
        if (isKing || id == Board.BLACK_CHECKER_ID) {
            points.add(new Point(point.x + move, point.y + move));
            points.add(new Point(point.x - move, point.y + move));
        }

        if (isKing || id == Board.WHITE_CHECKER_ID) {
            points.add(new Point(point.x + move, point.y - move));
            points.add(new Point(point.x - move, point.y - move));
        }
    }
    public static List<Point> getNormalMoves(Board board, int startIndex) {

        List<Point> points = getEndPoints(board, startIndex, 1);

        for (int i = 0; i < points.size(); i ++) {
            Point endPoint = points.get(i);
            if (board.get(endPoint.x, endPoint.y) != Board.EMPTY) {
                points.remove(i --);
            }
        }

        return points;
    }

    public static List<Point> getSkipMoves(Board board, int startIndex) {

        List<Point> points = getEndPoints(board, startIndex, 2);

        for (int i = 0; i < points.size(); i ++) {
            Point endPoint = points.get(i);
            if (!isValidSkipMove(board, startIndex, Board.toIndex(endPoint))) {
                points.remove(i--);
            }
        }

        return points;
    }


    public static boolean isValidSkipMove(Board board,
                                          int startIndex, int endIndex) {
        int id = board.get(startIndex);
        int middleID = board.get(Board.toIndex(Board.middle(startIndex, endIndex)));
        if ((board.get(endIndex) != Board.EMPTY)||(id == Board.INVALID || id == Board.EMPTY) || (middleID == Board.EMPTY || middleID == Board.INVALID)) {
            return false;
        } else return (middleID == Board.BLACK_CHECKER_ID || middleID == Board.BLACK_KING_ID) == (id == Board.WHITE_CHECKER_ID || id == Board.WHITE_KING_ID);
    }

}