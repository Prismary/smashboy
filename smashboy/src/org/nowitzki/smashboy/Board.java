package org.nowitzki.smashboy;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.*;

import org.nowitzki.smashboy.enums.BlockType;
import org.nowitzki.smashboy.enums.PieceType;
import org.nowitzki.smashboy.utils.Vector2;

public class Board extends JComponent {
	
	Vector2 boardDims;
	Vector2 boardOffset;
	int blockSize = 32;
	
	Color blockWhite;
	Color[][] colorTable;
	
	BlockType[][] board;
	Piece piece;
	
	Random random;
	
	public Board(int width, int height) {
		random = new Random();
		setBackground( Color.BLACK );
		setSize(width, height); 
		
		boardDims = new Vector2(10, 20);
		boardOffset = new Vector2(0, 0);
		
		board = new BlockType[boardDims.X][boardDims.Y];
		
		setColorTable();
		
		
		// Test
		spawnPiece();
	}
	
	public void spawnPiece() {
		// Get piece types as array
		PieceType[] typeArray = PieceType.values();
		
		// Get random int in array bounds and set piece accordingly
		piece = new Piece(typeArray[random.nextInt(typeArray.length)]);
	}
	
	public void applyToBoard() {
		for (int i = 0; i < 4; i++) {
			board[piece.blocks[i].X][piece.blocks[i].Y] = Piece.getBlockType(piece.type);
		}
		
		checkRowCompletion();
		spawnPiece();
		update();
	}
	
	public void checkRowCompletion() {
		int clearedRows = 0;
		int blockCount;
		
		// Iterate over all rows
		for (int row = 0; row < boardDims.Y; row++) {
			blockCount = 0;
			for (int block = 0; block < boardDims.X; block++) {
				// Check whether block is present
				if (board[block][row] != null) {
					blockCount++;
				}
			}
			// Check whether row is complete
			if (blockCount == boardDims.X) {
				dropRows(row);
				clearedRows++;
			}
		}
	}
	
	public void dropRows(int clearedRow) { // Pass row index that has been cleared
		for (int row = clearedRow - 1; row >= 0; row--) {
			// Copy all blocks one row down
			for (int block = 0; block < boardDims.X; block++) {
				board[block][row + 1] = board[block][row];
			}
		}
		
		// Clear uppermost row entirely
		for (int block = 0; block < boardDims.X; block++) {
			board[block][0] = null;
		}
	}
	
	public void setColorTable() {
		blockWhite = new Color(252, 252, 252);
		
		colorTable = new Color[10][2];
		
		// Level 0
		colorTable[0][0] = new Color(60, 188, 252); // light
		colorTable[0][1] = new Color(0, 88, 248); // dark
		
		// Level 1
		colorTable[1][0] = new Color(184, 248, 24);
		colorTable[1][1] = new Color(0, 168, 0);
		
		// Level 2
		colorTable[2][0] = new Color(248, 120, 248);
		colorTable[2][1] = new Color(216, 0, 204);
		
		// Level 3
		colorTable[3][0] = new Color(88, 216, 84);
		colorTable[3][1] = new Color(0, 88, 248);
		
		// Level 4
		colorTable[4][0] = new Color(88, 248, 152);
		colorTable[4][1] = new Color(228, 0, 88);
		
		// Level 5
		colorTable[5][0] = new Color(104, 136, 252);
		colorTable[5][1] = new Color(88, 248, 152);
		
		// Level 6
		colorTable[6][0] = new Color(124, 124, 124);
		colorTable[6][1] = new Color(248, 56, 0);
		
		// Level 7
		colorTable[7][0] = new Color(168, 0, 32);
		colorTable[7][1] = new Color(104, 68, 252);
		
		// Level 8
		colorTable[8][0] = new Color(248, 56, 0);
		colorTable[8][1] = new Color(0, 88, 248);
		
		// Level 9
		colorTable[9][0] = new Color(252, 160, 68);
		colorTable[9][1] = new Color(248, 56, 0);
	}
	
	public void update() {
		Game.instance.window.getContentPane().repaint();
	}
	
	@Override
    public void paint(Graphics g)
    { 
		// Draw background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(boardOffset.X, boardOffset.Y, blockSize * boardDims.X, blockSize * boardDims.Y);
		
		// Draw all board blocks
		for (int x = 0; x < boardDims.X; x++) {
			for (int y = 0; y < boardDims.Y; y++) {
				if (board[x][y] == null) { // Skip interation if entry is null
					continue;
				}
				switch (board[x][y]) {
				case Light:
					// Draw light type block
					g.setColor(colorTable[Game.instance.level % 10][0]);
			        g.fillRect(boardOffset.X + blockSize * x, boardOffset.Y + blockSize * y, blockSize, blockSize);
					break;
				case Dark:
					// Draw dark type block
					g.setColor(colorTable[Game.instance.level % 10][1]);
			        g.fillRect(boardOffset.X + blockSize * x, boardOffset.Y + blockSize * y, blockSize, blockSize);
					break;
				case Dual:
					// Draw dual type block
					g.setColor(colorTable[Game.instance.level % 10][1]);
			        g.fillRect(boardOffset.X + blockSize * x, boardOffset.Y + blockSize * y, blockSize, blockSize);
			        g.setColor(blockWhite);
			        g.fillRect(boardOffset.X + blockSize * x + blockSize / 8, boardOffset.Y + blockSize * y + blockSize / 8, (int)(blockSize * 0.75), (int)(blockSize * 0.75));
			        break;
				}
			}
		}
		
		// Determine block type for given piece type
		BlockType pieceBlockType = Piece.getBlockType(piece.type);
		
		// Draw piece blocks
		for (int i = 0; i < 4; i++) {
			if (piece.blocks[i].Y < 0) { // Skip interation if block is out of bounds
				continue;
			}
			switch (pieceBlockType) {
			case Light:
				// Draw light type block
				g.setColor(colorTable[Game.instance.level % 10][0]);
		        g.fillRect(boardOffset.X + blockSize * piece.blocks[i].X, boardOffset.Y + blockSize * piece.blocks[i].Y, blockSize, blockSize);
				break;
			case Dark:
				// Draw dark type block
				g.setColor(colorTable[Game.instance.level % 10][1]);
		        g.fillRect(boardOffset.X + blockSize * piece.blocks[i].X, boardOffset.Y + blockSize * piece.blocks[i].Y, blockSize, blockSize);
				break;
			case Dual:
				// Draw dual type block
				g.setColor(colorTable[Game.instance.level % 10][1]);
		        g.fillRect(boardOffset.X + blockSize * piece.blocks[i].X, boardOffset.Y + blockSize * piece.blocks[i].Y, blockSize, blockSize);
		        g.setColor(blockWhite);
		        g.fillRect(boardOffset.X + blockSize * piece.blocks[i].X + blockSize / 8, boardOffset.Y + blockSize * piece.blocks[i].Y + blockSize / 8, (int)(blockSize * 0.75), (int)(blockSize * 0.75));
				break;
			}
		}
    }
}
