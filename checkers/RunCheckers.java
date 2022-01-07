package checkers;

/**
 * 2 Player Checkers Game
 * University of Pennsylvania
 * Created by Erik Wei
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * This game adheres to a Model-View-Controller design framework.
 * 
 */
public class RunCheckers implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Checkers");
        frame.setLocation(400, 30);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final org.cis120.checkers.GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        JOptionPane.showMessageDialog(
                null, "GAME RULES: \n"
                        + "Checkers is played by two players.\n"
                        + "White begins the game and players alternate turns.\n"
                        + "Pieces only move diagonally. Pieces only move forward unless it " +
                        "is a king piece." + "\n"
                        + "A piece making a non-jump move can only move one square diagonally" +
                        " forward."
                        + "\n"
                        + "A piece making a jump move leaps over the opponent piece and lands" +
                        " diagonally forward from it."
                        + "\n"
                        + "When a piece is captured, it is removed from the board." + "\n"
                        + "If a player can jump, he/she must jump. \n" +
                        "If a piece can jump multiple times, the player can choose how many " +
                        "times the piece jumps."
                        + "\n"
                        + "Pieces can switch horizontal direction during a multiple jump turn."
                        + "\n"
                        + "If the piece reaches the opposite end, it becomes a king." + "\n"
                        + "Kings can move diagonally backward and forward\n" +
                        "The game ends when the opponent loses all pieces or cannot make a move."
        );

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);

        // save button
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.save();
            }
        });
        control_panel.add(save);

        // open button
        final JButton open = new JButton("Open");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.open();
            }
        });
        control_panel.add(open);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
