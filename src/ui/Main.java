package ui;

import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        CheckersWindow window = new CheckersWindow();
        window.setDefaultCloseOperation(CheckersWindow.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}