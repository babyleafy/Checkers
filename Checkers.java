package org.cis120.checkers;

/**
 * 2 Player Checkers Game
 * University of Pennsylvania
 * Created by Erik Wei
 */

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class is a model for Checkers.
 *
 * This game adheres to a Model-View-Controller design framework.
 *
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 *
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Checkers {

    private Piece[][] board;
    private int numTurns;
    private boolean player1;
    private boolean gameOver;
    private LinkedList<Move> storedMoves;
    public int selectedRow;
    public int selectedCol;
    public static final String SAVE_FILE = "C:\\Users\\erikw\\Downloads\\hw09_local\\hw09_local_temp\\files\\checkers.text";

    /**
     * Constructor sets up game state.
     */
    public Checkers() {
        reset();
    }

    /**
     * Constructor taking in a board for testing purposes
     */
    public Checkers(Piece[][] b) {
        reset();
        board = b;
    }

    /**
     * selectionMove allows players to play a turn. The first call to this method
     * selects the piece according to the x and y position of the mouseclick.
     * If the click is on a valid piece, it is selected and its position is
     * stored in selectedRow and selectedCol.
     * The second call makes the move if the move is valid. If the second call uses
     * a position of another valid piece, the selected piece changes and
     * the new coordinates are stored.
     *
     * For pieces having multiple skips, the player can select how many skips are
     * performed. If the player clicks on the same piece after a number of
     * skip(s), then the turn will end. Otherwise, the player may choose to
     * continue clicking valid skip squares.
     *
     * The move is also stored in the storedMoves linked list.
     *
     * @param row of move / selection
     * @param col of move / selection
     */
    public void selectionMove(int row, int col) {
        Move lastMove;
        if (!storedMoves.isEmpty()) {
            lastMove = storedMoves.getLast();
        } else {
            lastMove = null;
        }

        if (selectedRow < 0) {
            if (!getPlayerJumps(player1).isEmpty()) {
                for (Move m : getPlayerJumps(player1)) {
                    if (m.getInitial().y == row && m.getInitial().x == col) {
                        selectedRow = row;
                        selectedCol = col;
                    }

                }
                return;
            } else if (board[row][col] != null
                    && !getPossibleMoves(board[row][col]).isEmpty()
                    && board[row][col].getIsWhite() == player1) {
                selectedRow = row;
                selectedCol = col;
                return;
            }
        }

        if (selectedRow >= 0 && selectedCol >= 0 &&
                getPossibleMoves(board[selectedRow][selectedCol]).contains(
                        new Move(
                                new Point(selectedCol, selectedRow),
                                new Point(col, row)
                        )
                )) {
            Move moveToAdd = new Move(new Point(selectedCol, selectedRow), new Point(col, row));
            makeCheckerMove(moveToAdd);
            storedMoves.add(moveToAdd);
            return;
        } else if (lastMove != null && lastMove.isJump() // Choosing to stop skipping when multiple
                                                         // skips are possible
                && board[lastMove.getTarget().y][lastMove.getTarget().x].getIsWhite() == player1
                && lastMove.getTarget().y == row && lastMove.getTarget().x == col) {
            player1 = !player1;
            selectedRow = -1;
            selectedCol = -1;
        } else {
            if (!getPlayerJumps(player1).isEmpty()) {
                for (Move m : getPlayerJumps(player1)) {
                    if (m.getInitial().y == row && m.getInitial().x == col) {
                        selectedRow = row;
                        selectedCol = col;
                    }
                }
                return;
            } else if (board[row][col] != null
                    && !getPossibleMoves(board[row][col]).isEmpty()
                    && board[row][col].getIsWhite() == player1) {
                selectedRow = row;
                selectedCol = col;
                return;
            }
        }

    }

    /**
     * Reverses the previous move.
     */
    public void undo() {
        try {
            Move moveToUndo = storedMoves.removeLast();
            Point init = moveToUndo.getInitial();
            Point fin = moveToUndo.getTarget();
            boolean rColor = board[fin.y][fin.x].getIsWhite();
            boolean rKing = board[fin.y][fin.x].getIsKing();
            if (!moveToUndo.isJump()) {
                Piece tempPiece = new Piece(rColor, rKing, init.x, init.y);
                if (tempPiece.getIsKing()) {
                    tempPiece.setKing();
                }
                board[init.y][init.x] = tempPiece;
                board[fin.y][fin.x] = null;
                player1 = !player1;
                numTurns--;
            } else if (moveToUndo.isJump()) {
                Piece tempPiece = new Piece(rColor, rKing, init.x, init.y);
                if (tempPiece.getIsKing()) {
                    tempPiece.setKing();
                }
                board[init.y][init.x] = tempPiece;
                board[fin.y][fin.x] = null;
                board[(fin.y + init.y) / 2][(fin.x + init.x) / 2] = new Piece(
                        !tempPiece.getIsWhite(), false,
                        (fin.x + fin.x) / 2, (fin.y + init.y) / 2
                );
                if (!storedMoves.getLast().isJump()) {
                    player1 = !player1;
                    numTurns--;
                } else if (board[storedMoves.getLast().getTarget().y][storedMoves.getLast()
                        .getTarget().x].getIsWhite() == tempPiece.getIsWhite()) {
                    undo();
                } else {
                    player1 = !player1;
                    numTurns--;
                }
            }
            selectedRow = -1;
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Moves the checker piece while also checking for double skips. Updates the
     * player turn and selection index as well as the number of turns. If the piece
     * moved
     * has another possible skip, it is automatically selected.
     * 
     * @param move the move to be made
     */
    public void makeCheckerMove(Move move) {
        makeMove(move);
        if (move.isJump()) {
            Piece piece = board[move.getTarget().y][move.getTarget().x];
            if (!getPossibleJumps(piece).isEmpty()) {
                selectedRow = (int) move.getTarget().getY();
                selectedCol = (int) move.getTarget().getX();
                return;
            }
        }
        player1 = !player1;
        selectedRow = -1;
        numTurns++;
    }

    /**
     * Updates the game board given a move
     * 
     * @param move
     */
    public void makeMove(Move move) {
        makeMove(move.getInitial().y, move.getInitial().x, move.getTarget().y, move.getTarget().x);
    }

    /**
     * Updates the game board given coordinates
     * 
     * @param y1 the initial column of the piece to be moved
     * @param x1 the initial row
     * @param y2 the column the piece is moved to
     * @param x2 the final row
     */
    public void makeMove(int y1, int x1, int y2, int x2) {
        boolean rColor = board[y1][x1].getIsWhite();
        boolean rKing = board[y1][x1].getIsKing();
        Piece replacement = new Piece(rColor, rKing, x2, y2);
        board[y2][x2] = replacement;
        board[y1][x1] = null;
        if (Math.abs(x2 - x1) == 2 || Math.abs(y2 - y1) == 2) {
            int jumpedX = (x2 + x1) / 2;
            int jumpedY = (y2 + y1) / 2;
            board[jumpedY][jumpedX] = null;
        }
        if (y2 == 0 && !replacement.getIsWhite()) {
            board[y2][x2].setKing();
        }
        if (y2 == 7 && replacement.getIsWhite()) {
            board[y2][x2].setKing();
        }
    }

    /**
     * Obtains the selection of possible jumps of all of a player's pieces
     * 
     * @param playerColor
     * @return LinkedList of all possible jumps of a player
     */
    public LinkedList<Move> getPlayerJumps(boolean playerColor) {
        LinkedList<Move> moves = new LinkedList<>();
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                if (board[y][x] != null && board[y][x].getIsWhite() == playerColor) {
                    moves.addAll(getPossibleJumps(board[y][x]));
                }
            }
        }
        return moves;
    }

    /**
     * Obtains the selection of possible jumps of a piece
     * 
     * @param p piece in question
     * @return LinkedList of possible jumps
     */
    public LinkedList<Move> getPossibleJumps(Piece p) {
        LinkedList<Move> moves = new LinkedList<>();
        if (p != null) {
            int x = p.getX();
            int y = p.getY();
            boolean player = p.getIsWhite();
            if (canSkip(player, y, x, y + 1, x + 1, y + 2, x + 2)) {
                Point init = new Point(x, y);
                Point fin = new Point(x + 2, y + 2);
                moves.add(new Move(init, fin));
            }
            if (canSkip(player, y, x, y - 1, x + 1, y - 2, x + 2)) {
                Point init = new Point(x, y);
                Point fin = new Point(x + 2, y - 2);
                moves.add(new Move(init, fin));
            }
            if (canSkip(player, y, x, y + 1, x - 1, y + 2, x - 2)) {
                Point init = new Point(x, y);
                Point fin = new Point(x - 2, y + 2);
                moves.add(new Move(init, fin));
            }
            if (canSkip(player, y, x, y - 1, x - 1, y - 2, x - 2)) {
                Point init = new Point(x, y);
                Point fin = new Point(x - 2, y - 2);
                moves.add(new Move(init, fin));
            }

        }
        return moves;
    }

    /**
     * Obtains all possible moves including jumps of a piece
     * 
     * @param p piece in question
     * @return LinkedList of possible moves
     */
    public LinkedList<Move> getPossibleMoves(Piece p) {
        LinkedList<Move> moves = new LinkedList<>();
        if (p != null) {
            int x = p.getX();
            int y = p.getY();
            boolean player = p.getIsWhite();
            if (canSkip(player, y, x, y + 1, x + 1, y + 2, x + 2)) {
                Point init = new Point(x, y);
                Point fin = new Point(x + 2, y + 2);
                moves.add(new Move(init, fin));
            }
            if (canSkip(player, y, x, y - 1, x + 1, y - 2, x + 2)) {
                Point init = new Point(x, y);
                Point fin = new Point(x + 2, y - 2);
                moves.add(new Move(init, fin));
            }
            if (canSkip(player, y, x, y + 1, x - 1, y + 2, x - 2)) {
                Point init = new Point(x, y);
                Point fin = new Point(x - 2, y + 2);
                moves.add(new Move(init, fin));
            }
            if (canSkip(player, y, x, y - 1, x - 1, y - 2, x - 2)) {
                Point init = new Point(x, y);
                Point fin = new Point(x - 2, y - 2);
                moves.add(new Move(init, fin));
            }
            if (moves.size() == 0) {
                if (canMove(player, x, y, x + 1, y + 1)) {
                    Point init = new Point(x, y);
                    Point fin = new Point(x + 1, y + 1);
                    moves.add(new Move(init, fin));
                }
                if (canMove(player, x, y, x + 1, y - 1)) {
                    Point init = new Point(x, y);
                    Point fin = new Point(x + 1, y - 1);
                    moves.add(new Move(init, fin));
                }
                if (canMove(player, x, y, x - 1, y + 1)) {
                    Point init = new Point(x, y);
                    Point fin = new Point(x - 1, y + 1);
                    moves.add(new Move(init, fin));
                }
                if (canMove(player, x, y, x - 1, y - 1)) {
                    Point init = new Point(x, y);
                    Point fin = new Point(x - 1, y - 1);
                    moves.add(new Move(init, fin));
                }
            }
        }
        return moves;
    }

    /**
     * Helper function determining if a piece can skip from (x1, y1) to (x3, y3)
     * 
     * @param player
     * @param y1
     * @param x1
     * @param y2
     * @param x2
     * @param y3
     * @param x3
     * @return true if the jump is valid
     *         false if the jump is not valid
     */
    public boolean canSkip(boolean player, int y1, int x1, int y2, int x2, int y3, int x3) {
        if (y3 < 0 || y3 > 7 || x3 < 0 || x3 > 7) {
            return false;
        }
        if (board[y3][x3] != null || board[y1][x1] == null || board[y2][x2] == null) {
            return false;
        }
        if (player) {
            if (!board[y1][x1].getIsWhite()) {
                return false;
            } else if (!board[y1][x1].getIsKing() && y3 < y1) {
                return false;
            } else {
                return !(board[y2][x2].getIsWhite());
            }
        } else {
            if (board[y1][x1].getIsWhite()) {
                return false;
            } else if (!board[y1][x1].getIsKing() && y1 < y3) {
                return false;
            }
            return board[y2][x2].getIsWhite();
        }
    }

    /**
     * Helper function determining if a piece can move from (x1, y1) to (x2, y2)
     * 
     * @param player
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return true if the move is valid
     *         false is the move is not valid
     */
    public boolean canMove(boolean player, int x1, int y1, int x2, int y2) {
        if (x2 < 0 || x2 > 7 || y2 < 0 || y2 > 7) {
            return false;
        }
        if (board[y2][x2] != null || board[y1][x1] == null) {
            return false;
        }
        if (player) {
            if (!board[y1][x1].getIsWhite()) {
                return false;
            } else {
                if (board[y1][x1].getIsKing()) {
                    return true;
                } else {
                    return (y2 > y1);
                }
            }

        } else {
            if (board[y1][x1].getIsWhite()) {
                return false;
            } else {
                if (board[y1][x1].getIsKing()) {
                    return true;
                } else {
                    return (y1 > y2);
                }
            }
        }
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * A player has won if the opponent has no pieces or no moves left.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        int countWhite = 0;
        int countWhiteMoves = 0;
        int countBlack = 0;
        int countBlackMoves = 0;
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                if (board[y][x] != null) {
                    if (board[y][x].getIsWhite()) {
                        countWhite++;
                        if (!getPossibleMoves(board[y][x]).isEmpty()) {
                            countWhiteMoves++;
                        }
                    } else {
                        countBlack++;
                        if (!getPossibleMoves(board[y][x]).isEmpty()) {
                            countBlackMoves++;
                        }
                    }

                }
            }
        }
        if (countBlackMoves == 0 && countWhiteMoves == 0) {
            gameOver = true;
            return 3;
        } else if (countWhite == 0 || countWhiteMoves == 0) {
            gameOver = true;
            return 2;
        } else if (countBlack == 0 || countBlackMoves == 0) {
            gameOver = true;
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (board[i][j] == null) {
                    System.out.print("-");
                } else if (board[i][j].getIsWhite()) {
                    if (board[i][j].getIsKing()) {
                        System.out.println("W");
                    } else {
                        System.out.print("w");
                    }
                } else if (!board[i][j].getIsWhite()) {
                    if (board[i][j].getIsKing()) {
                        System.out.println("B");
                    } else {
                        System.out.print("b");
                    }
                }
                if (j < 8) {
                    System.out.print(" | ");
                }
            }
            if (i < 8) {
                System.out.println("\n---------------------");
            }
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        boolean isWhite;
        board = new Piece[8][8];
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                if (y < 3) {
                    isWhite = true;
                } else {
                    isWhite = false;
                }

                if (y < 3 || y > 4) {
                    if (y % 2 == 0) {
                        if (x % 2 == 1) {
                            board[y][x] = new Piece(isWhite, false, x, y);
                        } else {
                            board[y][x] = null;
                        }
                    } else if (y % 2 == 1) {
                        if (x % 2 == 0) {
                            board[y][x] = new Piece(isWhite, false, x, y);
                        } else {
                            board[y][x] = null;
                        }
                    }
                } else {
                    board[y][x] = null;
                }

            }
        }
        numTurns = 0;
        player1 = true;
        gameOver = false;
        storedMoves = new LinkedList<>();
        selectedCol = -1;
        selectedRow = -1;
    }

    /**
     * Saves the stored moves to a file location
     */
    public void writeFile() {
        File file = Paths.get(SAVE_FILE).toFile();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Move m : storedMoves) {
                bw.write(m.toString());
                bw.newLine();
            }
            System.out.println("Moves logged.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the saved game state by locating the stored file location and
     * simulating its moves while saving them in storedMoves.
     */
    public void readFile() {
        try {
            FileLineIterator fi = new FileLineIterator(SAVE_FILE);
            reset();
            while (fi.hasNext()) {
                String[] line = fi.next().split(",");
                Point p1 = new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                Point p2 = new Point(Integer.parseInt(line[2]), Integer.parseInt(line[3]));
                System.out.println(player1);
                makeCheckerMove(new Move(p1, p2));
                storedMoves.add(new Move(p1, p2));
            }
            System.out.println("Moves opened");
            System.out.println(player1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     *
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return a Piece denoting the contents of the corresponding cell on the
     *         game board.
     */
    public Piece getCell(int c, int r) {
        return board[r][c];
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Checkers t = new Checkers();
        t.printGameState();
        Point p = new Point(1, 2);
        System.out.println(p.toString());

        System.out.println("Winner is: " + t.checkWinner());
    }
}
