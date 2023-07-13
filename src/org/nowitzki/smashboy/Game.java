package org.nowitzki.smashboy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.nowitzki.smashboy.enums.Direction;

public class Game {
	
	public static Game instance = new Game();
	
	public Window window;
	public Timer gameTimer;
	public boolean gameTimerActive;
	
	double[] delayTable;
	double refreshRate;
	double tickDelay;
	double acceleratedTickDelay;
	int level;
	
	private Game() {		
		// Set default values if necessary
		gameTimerActive = false;
	}
	
	public void initialize() { // Called on startup
		// Set delay table
		setDelayTable();
		
		// Initialize window
		window = new Window(336, 679);
		refreshRate = 60;
		
		// Launch game
		startGame(0);
	}
	
	public void startGame(int startLevel) {
		// Set level
		level = startLevel;
		
		// Set tick delays
		tickDelay = delayTable[level];
		acceleratedTickDelay = 2;
		
		// Spawn piece
		window.board.spawnPiece();
		
		// Set up game timer
		gameTimer = new Timer((int)(tickDelay * 1000 / refreshRate), new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// TICK ROUTINE
				
				// Check for stop condition
				if (!gameTimerActive) {
					gameTimer.stop();
				}
				
				// Check whether piece sits on ground, move piece otherwise
				if (window.board.piece.isOnGround()) {
					window.board.applyToBoard();
					window.board.spawnPiece();
					disableAcceleration();
				} else {
					window.board.piece.move(Direction.Down);
				}
			}
		});
		
		// Launch game timer
		gameTimerActive = true;
		gameTimer.start();
	}
	
	public void increaseLevel() {
		level++;
		updateDelay();
		window.board.update();
		System.out.println("Level: " + level);
	}
	
	public void enableAcceleration() {
		gameTimer.setInitialDelay((int)(acceleratedTickDelay * 1000 / refreshRate));
		gameTimer.setDelay((int)(acceleratedTickDelay * 1000 / refreshRate));
		gameTimer.restart();
	}
	
	public void disableAcceleration() {
		gameTimer.setInitialDelay((int)(tickDelay * 1000 / refreshRate));
		gameTimer.setDelay((int)(tickDelay * 1000 / refreshRate));
		gameTimer.restart();
	}
	
	public void updateDelay() {
		if (level < 30) {
			tickDelay = delayTable[level];
		} else {
			tickDelay = delayTable[29];
		}
		disableAcceleration();
	}
	
	public void setDelayTable() {
		delayTable = new double[] {
				48,
				43,
				38,
				33,
				28,
				23,
				18,
				13,
				8,
				6,
				5,
				5,
				5,
				4,
				4,
				4,
				3,
				3,
				3,
				2,
				2,
				2,
				2,
				2,
				2,
				2,
				2,
				2,
				2,
				1
		};
	}
}
