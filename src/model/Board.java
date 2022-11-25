package model;

import lombok.Getter;
import lombok.Setter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Board {
    public static final int INVALID = -1;
    public static final int EMPTY = 0;
    public static final int BLACK_CHECKER_ID = 1;
    public static final int BLACK_KING_ID = 2;
    public static final int WHITE_CHECKER_ID = 3;
    public static final int WHITE_KING_ID = 4;
    private int[] state;


    public Board() {
        reset();
    }

    public Board copy() {
        Board copy = new Board();
        copy.state = state.clone();
        return copy;
    }

    public void reset() {
        this.state = new int[3];
        for (int i = 0; i < 12; i ++) {
            set(i, BLACK_CHECKER_ID);
            set(31 - i, WHITE_CHECKER_ID);
        }
    }

    public List<Point> findAllById(int id) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 32; i ++) {
            if (get(i) == id) {
                points.add(toPoint(i));
            }
        }

        return points;
    }

    public void set(int index, int id) {

        if (!isValidIndex(index)) {
            return;
        }

        if (id < 0) {
            id = EMPTY;
        }

        for (int i = 0; i < state.length; i ++) {
            boolean set = ((1 << (state.length - i - 1)) & id) != 0;
            this.state[i] = setBit(state[i], index, set);
        }
    }
    public int get(int x, int y) {
        return get(toIndex(x, y));
    }

    public int get(int index) {
        if (!isValidIndex(index)) {
            return INVALID;
        }
        return getBit(state[0], index) * 4 + getBit(state[1], index) * 2
                + getBit(state[2], index);
    }

    public static Point toPoint(int index) {
        int y = index / 4;
        int x = 2 * (index % 4) + (y + 1) % 2;
        return !isValidIndex(index)? new Point(-1, -1) : new Point(x, y);
    }
    public static int toIndex(int x, int y) {

        if (!isValidPoint(new Point(x, y))) {
            return -1;
        }

        return y * 4 + x / 2;
    }

    public static int toIndex(Point point) {
        return (point == null)? -1 : toIndex(point.x, point.y);
    }

    public static int setBit(int target, int bit, boolean set) {

        // Nothing to do
        if (bit < 0 || bit > 31) {
            return target;
        }

        // Set the bit
        if (set) {
            target |= (1 << bit);
        }

        // Clear the bit
        else {
            target &= (~(1 << bit));
        }

        return target;
    }

    public static int getBit(int target, int bit) {

        if (bit < 0 || bit > 31) {
            return 0;
        }

        return (target & (1 << bit)) != 0? 1 : 0;
    }

    public static Point middle(Point p1, Point p2) {

        if (p1 == null || p2 == null) {
            return new Point(-1, -1);
        }

        return middle(p1.x, p1.y, p2.x, p2.y);
    }

    public static Point middle(int index1, int index2) {
        return middle(toPoint(index1), toPoint(index2));
    }

    public static Point middle(int x1, int y1, int x2, int y2) {

        int dx = x2 - x1, dy = y2 - y1;
        if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 ||
                x1 > 7 || y1 > 7 || x2 > 7 || y2 > 7) {
            return new Point(-1, -1);
        } else if (x1 % 2 == y1 % 2 || x2 % 2 == y2 % 2) {
            return new Point(-1, -1);
        } else if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) != 2) {
            return new Point(-1, -1);
        }

        return new Point(x1 + dx / 2, y1 + dy / 2);
    }

    public static boolean isValidIndex(int testIndex) {
        return testIndex >= 0 && testIndex < 32;
    }

    public static boolean isValidPoint(Point testPoint) {

        if (testPoint == null) {
            return false;
        }

        // Check that it is on the board
        final int x = testPoint.x, y = testPoint.y;
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            return false;
        }

        // Check that it is on a black tile
        return x % 2 != y % 2;
    }

    @Override
    public String toString() {
        String obj = getClass().getName() + "[";
        for (int i = 0; i < 31; i ++) {
            obj += get(i) + ", ";
        }
        obj += get(31);

        return obj + "]";
    }
}