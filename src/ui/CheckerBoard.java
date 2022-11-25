package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Board;
import model.Game;
import model.Player;

public class CheckerBoard extends JButton {

    private static final int PADDING = 0;

    private Game game;

    private CheckersWindow window;

    private Player player1;

    private Player player2;

    private Point selected;
    private Color lightTile;

    private Color darkTile;
    private boolean isGameOver;


    public CheckerBoard(CheckersWindow window) {
        this(window, new Game(), null, null);
    }

    public CheckerBoard(CheckersWindow window, Game game,
                        Player player1, Player player2) {

        super.setBorderPainted(false);
        super.setFocusPainted(false);
        super.setContentAreaFilled(false);
        super.setBackground(Color.LIGHT_GRAY);
        this.addActionListener(new ClickListener());

        this.game = (game == null)? new Game() : game;
        this.lightTile = (lightTile == null)? new Color(0xDAA06D) : lightTile;
        this.darkTile = (darkTile == null)? new Color(0x80461B) : darkTile;
        this.window = window;
        setPlayer1(player1);
        setPlayer2(player2);
    }

    public void update() {
        runPlayer();
        this.isGameOver = game.isGameOver();
        repaint();
    }

    private void runPlayer() {
        Player player = getCurrentPlayer();
        if (player == null)
            return;
        getCurrentPlayer().updateGame(game);
        update();
    }

    public synchronized boolean setGameState(boolean testValue,
                                             String newState, String expected) {

        if (testValue && !game.getGameState().equals(expected)) {
            return false;
        }

        this.game.setGameState(newState);
        repaint();

        return true;
    }

    public Game getGame() {
        return game;
    }

    public void setPlayer1(Player player1) {
        this.player1 = (player1 == null)? new Player() : player1;
        if (game.isPlayer1Turn()) {
            this.selected = null;
        }
    }

    public void setPlayer2(Player player2) {
        this.player2 = (player2 == null)? new Player() : player2;
        if (!game.isPlayer1Turn()) {
            this.selected = null;
        }
    }

    public Player getCurrentPlayer() {
        return game.isPlayer1Turn()? player1 : player2;
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Game game = this.game.copyCurrentBoard();

        // Perform calculations
        final int BOX_PADDING = 4;
        final int W = getWidth(), H = getHeight();
        final int DIM = Math.min(W, H), BOX_SIZE = (DIM - 2 * PADDING) / 8;
        final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
        final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
        final int CHECKER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);

        // Draw checker board
        g.setColor(Color.BLACK);
        g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
        g.setColor(lightTile);
        g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
        g.setColor(darkTile);
        for (int y = 0; y < 8; y ++) {
            for (int x = (y + 1) % 2; x < 8; x += 2) {
                g.fillRect(OFFSET_X + x * BOX_SIZE, OFFSET_Y + y * BOX_SIZE,
                        BOX_SIZE, BOX_SIZE);
            }
        }

        // Draw the checkers
        Board b = game.getBoard();
        for (int y = 0; y < 8; y ++) {
            int cy = OFFSET_Y + y * BOX_SIZE + BOX_PADDING;
            for (int x = (y + 1) % 2; x < 8; x += 2) {
                int id = b.get(x, y);

                // Empty, just skip
                if (id == Board.EMPTY) {
                    continue;
                }

                int cx = OFFSET_X + x * BOX_SIZE + BOX_PADDING;

                // Black checker
                if (id == Board.BLACK_CHECKER_ID) {
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.BLACK);
                    g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                }

                // Black king
                else if (id == Board.BLACK_KING_ID) {
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.BLACK);
                    g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
                }

                // White checker
                else if (id == Board.WHITE_CHECKER_ID) {
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.WHITE);
                    g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                }

                // White king
                else if (id == Board.WHITE_KING_ID) {
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.WHITE);
                    g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                }

                // Any king (add some extra highlights)
                if (id == Board.BLACK_KING_ID || id == Board.WHITE_KING_ID) {
                    g.setColor(Color.GREEN);
                    g.drawOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.drawOval(cx + 1, cy, CHECKER_SIZE - 4, CHECKER_SIZE - 4);
                }
            }
        }

        // Draw the player turn sign
        String msg = game.isPlayer1Turn()? "Player 1's turn" : "Player 2's turn";
        int width = g.getFontMetrics().stringWidth(msg);
        Color back = game.isPlayer1Turn()? Color.BLACK : Color.WHITE;
        Color front = game.isPlayer1Turn()? Color.WHITE : Color.BLACK;
        g.setColor(back);
        g.fillRect(W / 2 - width / 2 - 5, OFFSET_Y + 8 * BOX_SIZE + 2,
                width + 10, 15);
        g.setColor(front);
        g.drawString(msg, W / 2 - width / 2, OFFSET_Y + 8 * BOX_SIZE + 2 + 11);

        // Draw a game over sign
        if (isGameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            msg = "Game Over!";
            width = g.getFontMetrics().stringWidth(msg);
            g.setColor(new Color(240, 240, 255));
            g.fillRoundRect(W / 2 - width / 2 - 5,
                    OFFSET_Y + BOX_SIZE * 4 - 16,
                    width + 10, 30, 10, 10);
            g.setColor(Color.RED);
            g.drawString(msg, W / 2 - width / 2, OFFSET_Y + BOX_SIZE * 4 + 7);
        }
    }


    private void handleClick(int x, int y) {

        if (isGameOver) {
            return;
        }

        Game copy = game.copyCurrentBoard();

        final int W = getWidth(), H = getHeight();
        final int DIM = Math.min(W, H), BOX_SIZE = (DIM - 2 * PADDING) / 8;
        final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
        final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
        x = (x - OFFSET_X) / BOX_SIZE;
        y = (y - OFFSET_Y) / BOX_SIZE;
        Point sel = new Point(x, y);

        if (Board.isValidPoint(sel) && Board.isValidPoint(selected)) {
            boolean change = copy.isPlayer1Turn();
            String expected = copy.getGameState();
            boolean move = copy.move(selected, sel);
            boolean updated = (move?
                    setGameState(true, copy.getGameState(), expected) : false);

            change = (copy.isPlayer1Turn() != change);
            this.selected = change? null : sel;
        } else {
            this.selected = sel;
        }
        update();
    }

    private class ClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Point m = CheckerBoard.this.getMousePosition();
            if (m != null) {
                handleClick(m.x, m.y);
            }
        }
    }
}