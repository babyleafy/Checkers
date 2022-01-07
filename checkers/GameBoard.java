package checkers;

/**
 * 2 Player Checkers Game
 * University of Pennsylvania
 * Created by Erik Wei
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class instantiates a Checkers object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Checkers c; // model for the game
    private JLabel status; // current status text
    private boolean selection;
    private int px;
    private int py;
    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        c = new Checkers(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        selection = true;
        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p1 = e.getPoint();
                px = p1.x / 100;
                py = p1.y / 100;
                // updates the model given the coordinates of the mouseclick
                c.selectionMove(py, px);
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board

            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        c.reset();
        status.setText("Player 1's Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Undos the last move
     */
    public void undo() {
        c.undo();
        String player;
        if (c.getCurrentPlayer()) {
            player = "Player 1";
        } else {
            player = "Player 2";
        }

        status.setText(player + "'s Turn");
        repaint();
        requestFocusInWindow();
    }

    /**
     * Saves the game moves
     */
    public void save() {
        c.writeFile();
    }

    /**
     * Opens a saved game file
     */
    public void open() {
        c.readFile();
        String player;
        if (c.getCurrentPlayer()) {
            player = "Player 1";
        } else {
            player = "Player 2";
        }
        status.setText(player + "'s Turn");
        repaint();
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (c.getCurrentPlayer()) {
            status.setText("Player 1's Turn");
        } else {
            status.setText("Player 2's Turn");
        }

        int winner = c.checkWinner();
        if (winner == 1) {
            status.setText("Player 1 wins!!!");
        } else if (winner == 2) {
            status.setText("Player 2 wins!!!");
        } else if (winner == 3) {
            status.setText("It's a tie.");
        }
    }

    /**
     * Draws the game board.
     * <p>
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        g.drawLine(100, 0, 100, 800);
        g.drawLine(200, 0, 200, 800);
        g.drawLine(300, 0, 300, 800);
        g.drawLine(400, 0, 400, 800);
        g.drawLine(500, 0, 500, 800);
        g.drawLine(600, 0, 600, 800);
        g.drawLine(700, 0, 700, 800);
        g.drawLine(0, 100, 800, 100);
        g.drawLine(0, 200, 800, 200);
        g.drawLine(0, 300, 800, 300);
        g.drawLine(0, 400, 800, 400);
        g.drawLine(0, 500, 800, 500);
        g.drawLine(0, 600, 800, 600);
        g.drawLine(0, 700, 800, 700);

        // Draws pieces
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                int[] xCrown = { 40 + 100 * x, 40 + 100 * x, 50 + 100 * x, 60 + 100 * x,
                    60 + 100 * x };
                int[] yCrown = { 55 + 100 * y, 45 + 100 * y, 50 + 100 * y, 45 + 100 * y,
                    55 + 100 * y };
                Piece state = c.getCell(x, y);
                if (state != null) {
                    if (state.getIsWhite()) {
                        g.setColor(Color.black);
                        g.drawOval(30 + 100 * x, 30 + 100 * y, 40, 40);
                        if (state.getIsKing()) {
                            g.setColor(Color.orange);
                            g.drawPolygon(xCrown, yCrown, 5);
                        }
                    } else if (!state.getIsWhite()) {
                        g.setColor(Color.black);
                        g.fillOval(30 + 100 * x, 30 + 100 * y, 40, 40);
                        if (state.getIsKing()) {
                            g.setColor(Color.orange);
                            g.drawPolygon(xCrown, yCrown, 5);
                        }
                    }
                }
            }
        }
        // Highlights selected piece
        if (c.selectedRow >= 0) {
            g.setColor(Color.CYAN);
            g.drawOval(29 + 100 * c.selectedCol, 29 + 100 * c.selectedRow, 42, 42);
            for (Move m : c.getPossibleMoves(c.getCell(c.selectedCol, c.selectedRow))) {
                g.setColor(Color.GREEN);
                g.fillRect(m.getTarget().x * 100 + 1, m.getTarget().y * 100 + 1, 99, 99);
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
