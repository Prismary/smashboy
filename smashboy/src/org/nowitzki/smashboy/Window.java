package org.nowitzki.smashboy;

import javax.swing.*;

import org.nowitzki.smashboy.enums.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame {
	
	int width;
    int height;
    
    Board board;
	
	public Window(int width, int height) {
		this.width = width;
        this.height = height;
        
        board = new Board(width, height);
        
        setTitle("Smashboy"); 
        setSize(width, height); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Set background and add components
        setBackground( Color.BLACK );
        add(board);
        
        setVisible(true);
        
        // Key listener
        KeyListener keyListener = new ControlKeyListener();
		addKeyListener(keyListener);
		setFocusable(true);
	}	
	
	// Input handler
	public class ControlKeyListener implements KeyListener {
		boolean moveLeftPressed = false;
		boolean moveRightPressed = false;
		boolean acceleratePressed = false;
		boolean rotateLeftPressed = false;
		boolean rotateRightPressed = false;
		boolean hardDropPressed = false;
		
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (KeyEvent.getKeyText(e.getKeyCode())) {
			case "A":
				if (!moveLeftPressed) {
					moveLeftPressed = true;
					// Move left
					board.piece.move(Direction.Left);
					System.out.println("moved left");
				}
				break;
			case "D":
				if (!moveRightPressed) {
					moveRightPressed = true;
					// Move right
					board.piece.move(Direction.Right);
					System.out.println("moved right");
				}
				break;
			case "S":
				if (!acceleratePressed) {
					acceleratePressed = true;
					// Accelerate drop (NOT FINAL!!)
					board.piece.move(Direction.Down);
					System.out.println("accelerated");
				}
				break;
			case "J":
				if (!rotateLeftPressed) {
					rotateLeftPressed = true;
					// Rotate left
					board.piece.rotate(Direction.Left);
					System.out.println("rotated counter-clockwise");
				}
				break;
			case "K":
				if (!rotateRightPressed) {
					rotateRightPressed = true;
					// Rotate right
					board.piece.rotate(Direction.Right);
					System.out.println("rotated clockwise");
				}
				break;
			case "Space":
				if (!hardDropPressed) {
					hardDropPressed = true;
					// Hard drop
					board.piece.hardDrop();
					System.out.println("hard dropped");
				}
				break;
			case "L":
				Game.instance.increaseLevel();
				break;
			case "W":
				board.piece.move(Direction.Up);
				break;
			}
				
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (KeyEvent.getKeyText(e.getKeyCode())) {
			case "A":
				moveLeftPressed = false;
				break;
			case "D":
				moveRightPressed = false;
				break;
			case "S":
				acceleratePressed = false;
				break;
			case "J":
				rotateLeftPressed = false;
				break;
			case "K":
				rotateRightPressed = false;
				break;
			case "Space":
				hardDropPressed = false;
				break;
			}
		}
	}
}
