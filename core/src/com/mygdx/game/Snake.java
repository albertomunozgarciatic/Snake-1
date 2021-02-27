package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.LinkedList;

public class Snake {

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////
////// STATE
//////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final static int INIT_RELATIVE_COL=10;
    private final static int INIT_RELATIVE_ROW=10;
    private final static String IMAGE = "snake.png";

    private final float GAME_DISPLAY_INITIAL_X;
    private final float GAME_DISPLAY_INITIAL_Y;
    private final float GAME_DISPLAY_FINAL_X;
    private final float GAME_DISPLAY_FINAL_Y;

    private LinkedList<Piece> pieceList;
    private Directions currentMovement;
    private int width;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////
////// BEHAVIOR//CONDUCT
//////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Builder of snake
     *
     * @param initRelativeCol  initial relative X position
     * @param initRelativeRow  initial relative Y position
     * @param initDirection    initial snake direction
     * @param newWidth            width for every Piece
     */
    public Snake(int newGameDisplayInitialX, int newGameDisplayInitialY, int newGameDisplayFinalX, int newGameDisplayFinalY, Directions initDirection, int newWidth) {
        this.pieceList = new LinkedList<>();
        Piece piece = new Piece(newWidth*Snake.INIT_RELATIVE_COL+newGameDisplayInitialX,
                                newWidth*Snake.INIT_RELATIVE_ROW+newGameDisplayInitialY,
                                 newWidth, Snake.IMAGE);
        this.width = newWidth;
        this.pieceList.add(piece);
        this.currentMovement = initDirection;
        this.GAME_DISPLAY_FINAL_X = newGameDisplayFinalX;
        this.GAME_DISPLAY_FINAL_Y = newGameDisplayFinalY;
        this.GAME_DISPLAY_INITIAL_X = newGameDisplayInitialX;
        this.GAME_DISPLAY_INITIAL_Y = newGameDisplayInitialY;

    }

    /**
     * Method to move the Snake
     */
    public void move() {
        this.grow();
        this.pieceList.removeLast();
    }

    /**
     * Method to make grow the Snake
     */
    public void grow() {
        Piece clonedFirstPiece = this.pieceList.getFirst().clone();
        this.moveSpecificPiece(clonedFirstPiece);
        this.pieceList.addFirst(clonedFirstPiece);
    }

    /**
     * Method to move a specific piece
     * @param pieceToMove piece that it's going to move
     */
    private void moveSpecificPiece(Piece pieceToMove) {
        switch (currentMovement) {
            case UP:
                pieceToMove.incrementRow();
                break;
            case DOWN:
                pieceToMove.decrementRow();
                break;
            case LEFT:
                pieceToMove.decrementCol();
                break;
            case RIGHT:
                pieceToMove.incrementCol();
                break;
            default:
                throw new IllegalArgumentException("Something was wrong");
        }
    }

    /**
     * Method to change the direction of the Snake
     * @param movement one of the following directions (UP,DOWN,LEFT,RIGHT)
     */
    public void changeMovement(Directions movement) {
        if (this.isMovementValid(movement))
            this.currentMovement = movement;
    }

    /**
     * Method to check if the movement is opposite or not
     * @param movement
     * @return true if the movement is not opposite
     */
    private boolean isMovementValid(Directions movement) {
        return movement != null && this.currentMovement.value + movement.value != 0;
    }

    /**
     * Method to render the piece list from snake
     * @param spriteBatch
     */
    public void render(SpriteBatch spriteBatch) {
        for (Piece piece : this.pieceList) {
            piece.render(spriteBatch);
        }
    }

    /**
     * Method to dispose the piece list from snake
     */
    public void dispose() {
        for (Piece piece : this.pieceList) {
            piece.dispose();
        }
    }

    /**
     * Method to check if a piece can move
     *
     * @return true if the piece can move
     */
    public boolean isDead() {
        Piece head = this.pieceList.getFirst();
        return this.isTouchingHimSelf(head) || this.isOutOfRange(head);
    }

    private boolean isTouchingHimSelf(Piece head){
        for (int i=4;i<pieceList.size();i++) {
            if (head.isColliding(pieceList.get(i)))
                return true;
        }
        return false;
    }

    private boolean isOutOfRange(Piece head){
        return !(this.GAME_DISPLAY_INITIAL_X <= head.getAbsoluteCol() &&
                head.getAbsoluteCol() < this.GAME_DISPLAY_FINAL_X + this.GAME_DISPLAY_INITIAL_X &&
                this.GAME_DISPLAY_INITIAL_Y <= head.getAbsoluteRow() &&
                head.getAbsoluteRow() < this.GAME_DISPLAY_FINAL_Y + this.GAME_DISPLAY_INITIAL_Y);
    }

    public int getCellWidth(){
        return this.width;
    }
}
