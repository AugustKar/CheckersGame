package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Player;

@Getter
@Setter
@AllArgsConstructor
public class CheckersWindow extends JFrame {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 800;
    public static final String DEFAULT_TITLE = "Checkers";
    private CheckerBoard board;

    public CheckersWindow() {

        super(DEFAULT_TITLE);
        super.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        super.setLocationByPlatform(true);

        JPanel layout = new JPanel(new BorderLayout());
        this.board = new CheckerBoard(this);
        layout.add(board, BorderLayout.CENTER);
        this.add(layout);

    }

}