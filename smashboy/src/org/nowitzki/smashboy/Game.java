package org.nowitzki.smashboy;

public class Game {
	
	public static Game instance = new Game();
	
	public Window window;
	
	int level;
	
	private Game() {		
		// Set default values
		level = 0;
	}
	
	public void initialize() {
		// Initialize window
		window = new Window(336, 679);
		
		
	}
	
	public void increaseLevel() {
		level++;
		window.board.update();
		System.out.println(level);
	}
}
