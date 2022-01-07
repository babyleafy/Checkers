package checkers;

/**
 * Piece class represents a checkers piece
 * It has methods for obtaining all its instance variables as well as
 * changing the king status of the piece
 */

public class Piece {
    private final boolean isWhite;
    private boolean isKing;
    private int yPosition;
    private int xPosition;

    /**
     * Constructor creating an instance of a checkers piece
     * 
     * @param isWhite   if the piece is white
     * @param isKing    if the piece is a king
     * @param xPosition the x position of the piece
     * @param yPosition the y position of the piece
     */
    public Piece(boolean isWhite, boolean isKing, int xPosition, int yPosition) {
        this.isWhite = isWhite;
        this.isKing = isKing;
        this.yPosition = yPosition;
        this.xPosition = xPosition;
    }

    /**
     * @return true if the piece is white
     *         flase if the piece is black
     */
    public boolean getIsWhite() {
        return isWhite;
    }

    /**
     * @return true if the piece is a king
     *         flase if the piece is not a king
     */
    public boolean getIsKing() {
        return isKing;
    }

    /**
     * @return the x position of the piece
     */
    public int getX() {
        return xPosition;
    }

    /**
     * @return the y position of the piece
     */
    public int getY() {
        return yPosition;
    }

    /**
     * Switches king status
     */
    public void setKing() {
        isKing = !isKing;
    }

}
