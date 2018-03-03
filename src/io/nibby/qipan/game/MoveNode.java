package io.nibby.qipan.game;

import io.nibby.qipan.board.Stone;

import java.util.ArrayList;
import java.util.List;

/**
 * Each node represents a game position
 */
public class MoveNode {

    public MoveNode parent;
    public List<MoveNode> children = new ArrayList<>();

    public int moveNumber;
    public int moveX, moveY;
    public Stone[] stones;     // The stones on the board at this move
    public int whitePrisoners; // Number of captures made by the white player
    public int blackPrisoners; // Number of captures made by the black player
    public int lastKoX;        // Where the last illegal ko co-ordinate is
    public int lastKoY;
    public String comment;

    public Stone[] lastCapture; // Opponent stones captured by the last move

    // Root node initializer
    public MoveNode() {
        this.moveNumber = 0;
        this.parent = null;
        this.moveX = -1;
        this.moveY = -1;
        this.stones = null;
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

}
