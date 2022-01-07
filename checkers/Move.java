package checkers;

import java.awt.*;

/**
 * This class creates move objects that store important information that the
 * undo function in Checkers
 * can use to manipulate the board to reverse the last playTurn call.
 * It contains methods to get all instance variables representing the initial
 * and final positions.
 */
public class Move {
    private Point initial;
    private Point target;

    /**
     * Constructor creates a move
     * 
     * @param initial former position of the piece moved
     * @param target  current position of the piece moved
     */

    public Move(Point initial, Point target) {
        this.initial = initial;
        this.target = target;
    }

    /**
     * @return initial position as a Point
     */
    public Point getInitial() {
        return new Point(initial);
    }

    /**
     * @return current position as a Point
     */
    public Point getTarget() {
        return new Point(target);
    }

    /**
     * @return true if the move is a jump
     *         false if the move is not a jump
     */
    public boolean isJump() {
        return (Math.abs(initial.x - target.x) == 2);
    }

    @Override
    public String toString() {
        return Integer.toString(initial.x) + "," + Integer.toString(initial.y) + "," +
                Integer.toString(target.x) + "," + Integer.toString(target.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Move m = (Move) o;
        return this.toString().equals(m.toString());
    }
}
