package io.nibby.qipan.game;

import io.nibby.qipan.ui.board.Stone;

import java.util.ArrayList;
import java.util.List;

/**
 * Each node represents a game position
 */
public class MoveNode {

    private MoveNode parent;
    private List<MoveNode> children = new ArrayList<>();

    private int moveX, moveY;
    private Stone[] boardPosition;     // The stones on the board at this instance
    private int whitePrisoners; // Number of captures made by the white player
    private int blackPrisoners; // Number of captures made by the black player
    private int lastKoX;        // Where the last illegal ko co-ordinate is
    private int lastKoY;
    private String comment;

    private Stone[] lastCapture; // Opponent boardPosition captured by the last move

    // Root node initializer
    public MoveNode() {
        this.parent = null;
        this.moveX = -1;
        this.moveY = -1;
        this.boardPosition = null;
        this.whitePrisoners = 0;
        this.blackPrisoners = 0;
        this.lastKoX = -1;
        this.lastKoY = -1;
        this.comment = "";
        this.lastCapture = null;
    }

    public MoveNode(MoveNode parent) {
        this.parent = parent;
    }

    public void addChild(MoveNode child) {
        if (!children.contains(child))
            this.children.add(child);
    }

    public boolean hasChildren() {
        return getChildren().size() > 0;
    }

    public MoveNode getParent() {
        return parent;
    }

    public void setParent(MoveNode parent) {
        this.parent = parent;
    }

    public List<MoveNode> getChildren() {
        return children;
    }

    public void setChildren(List<MoveNode> children) {
        this.children = children;
    }

    public int getMoveX() {
        return moveX;
    }

    public void setMoveX(int moveX) {
        this.moveX = moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    public void setMoveY(int moveY) {
        this.moveY = moveY;
    }

    public Stone[] getBoardPosition() {
        return boardPosition;
    }

    public void setBoardPosition(Stone[] boardPosition) {
        this.boardPosition = boardPosition;
    }

    public int getWhitePrisoners() {
        return whitePrisoners;
    }

    public void setWhitePrisoners(int whitePrisoners) {
        this.whitePrisoners = whitePrisoners;
    }

    public int getBlackPrisoners() {
        return blackPrisoners;
    }

    public void setBlackPrisoners(int blackPrisoners) {
        this.blackPrisoners = blackPrisoners;
    }

    public int getLastKoX() {
        return lastKoX;
    }

    public void setLastKoX(int lastKoX) {
        this.lastKoX = lastKoX;
    }

    public int getLastKoY() {
        return lastKoY;
    }

    public void setLastKoY(int lastKoY) {
        this.lastKoY = lastKoY;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Stone[] getLastCapture() {
        return lastCapture;
    }

    public void setLastCapture(Stone[] lastCapture) {
        this.lastCapture = lastCapture;
    }

    public int getMoveNumber() {
        int length = 1;
        MoveNode parent = this.parent;
        while (parent != null) {
            parent = parent.getParent();
            length++;
        }
        return length;
    }
}
