package org.nowitzki.smashboy;

import java.util.Dictionary;
import java.util.Hashtable;

import org.nowitzki.smashboy.enums.BlockType;
import org.nowitzki.smashboy.enums.Direction;
import org.nowitzki.smashboy.enums.PieceType;
import org.nowitzki.smashboy.utils.Vector2;

public class Piece {
	Dictionary<PieceType, Vector2[][]> rotationChanges;
	
	PieceType type;
	Vector2[] blocks;
	int rotationState;
	
	public Piece(PieceType type) {
		rotationChanges = new Hashtable<>();
		
		this.type = type;
		blocks = new Vector2[4];
		rotationState = 0;
		
		initBlocks();
		setRotationChanges();
	}
	
	public void move(Direction direction) {
		Vector2[] newBlocks = new Vector2[4];
		Vector2 offset = new Vector2(0 ,0);
		
		// Set offset vector for given direction
		switch (direction) {
		case Left:
			offset = new Vector2(-1, 0);
			break;
		case Right:
			offset = new Vector2(1, 0);
			break;
		case Down:
			offset = new Vector2(0, 1);
			break;
		case Up:
			offset = new Vector2(0, -1);
			break;
		}
		
		// Save new block positions
		for (int i = 0; i < 4; i++) {
			newBlocks[i] = new Vector2(blocks[i].X + offset.X, blocks[i].Y + offset.Y);
		}
		
		// Check collision and cancel move if necessary
		if (checkCollision(newBlocks)) {
			return;
		}
		
		// Apply new block positions
		for (int i = 0; i < 4; i++) {
			blocks[i] = newBlocks[i];
		}
		
		// Update
		Game.instance.window.board.update();
	}
	
	public void hardDrop() {
		Vector2[] newBlocks = new Vector2[4];
		
		// Copy current blocks
		for (int i = 0; i < 4; i++) {
			newBlocks[i] = new Vector2(blocks[i].X, blocks[i].Y);
		}
		
		while (true) {
			// Calculate new block positions
			for (int i = 0; i < 4; i++) {
				newBlocks[i] = new Vector2(newBlocks[i].X, newBlocks[i].Y + 1);
			}
			
			// Check collision and break out of loop if necessary
			if (checkCollision(newBlocks)) {
				break;
			}
		}
		
		// Apply last valid new block positions
		for (int i = 0; i < 4; i++) {
			blocks[i] = new Vector2(newBlocks[i].X, newBlocks[i].Y - 1);
		}
		
		// Apply to board
		Game.instance.window.board.applyToBoard();
		
		// Update
		Game.instance.window.board.update();
	}
	
	public void rotate(Direction direction) {
		Vector2[] newBlocks = new Vector2[4];
		
		// Calculate new block positions
		switch(direction) {
		case Right: // Clockwise
			for (int i = 0; i < 4; i++) {
				newBlocks[i] = new Vector2(blocks[i].X + rotationChanges.get(type)[rotationState][i].X, blocks[i].Y + rotationChanges.get(type)[rotationState][i].Y);
			}
			break;
		case Left: // Counter-Clockwise
			for (int i = 0; i < 4; i++) {
				newBlocks[i] = new Vector2(blocks[i].X + (rotationChanges.get(type)[(rotationState + 3) % 4][i].X * -1), blocks[i].Y + (rotationChanges.get(type)[(rotationState + 3) % 4][i].Y * -1));
			}
			break;
		}
		
		// Check collision and cancel move if necessary
		if (checkCollision(newBlocks)) {
			return;
		}
		
		// Update rotation state		
		switch (direction) {
		case Right:
			rotationState++;
			break;
		case Left:
			rotationState += 3;
			break;
		}
		rotationState %= 4; // Roll rotation state over if greater than 3
		
		// Apply new block positions
		for (int i = 0; i < 4; i++) {
			blocks[i] = newBlocks[i];
		}
		
		// Update
		Game.instance.window.board.update();
	}
	
	public boolean isOnGround() {
		Vector2[] newBlocks = new Vector2[4];
		
		// Calculate block positions if piece was moved down
		for (int i = 0; i < 4; i++) {
			newBlocks[i] = new Vector2(blocks[i].X, blocks[i].Y + 1);
		}
		
		// Return based on collision
		return checkCollision(newBlocks);
	}
	
	public boolean checkCollision(Vector2[] pieceBlocks) {
		// Check whether piece is in bounds
		for (int i = 0; i < 4; i++) {
			if ((pieceBlocks[i].X < 0 || pieceBlocks[i].X > Game.instance.window.board.boardDims.X - 1) ||
					(pieceBlocks[i].Y > Game.instance.window.board.boardDims.Y - 1)) { // Upper bound is exempt from collision check
				return true;
			}
		}
		
		// Check board blocks
		for (int i = 0; i < 4; i++) {
			if (pieceBlocks[i].Y < 0) { // Skip iteration if block is out of bounds
				continue;
			}
			if (Game.instance.window.board.board[pieceBlocks[i].X][pieceBlocks[i].Y] != null) {
				return true;
			}
		}
			
		return false;
	}
	
	public void initBlocks() {
		// Blocks are counted by row, from the left
		switch (type) {
		case T:
			blocks[0] = new Vector2(4, 0);
			blocks[1] = new Vector2(5, 0);
			blocks[2] = new Vector2(6, 0);
			blocks[3] = new Vector2(5, 1);
			break;
		case J:
			blocks[0] = new Vector2(4, 0);
			blocks[1] = new Vector2(5, 0);
			blocks[2] = new Vector2(6, 0);
			blocks[3] = new Vector2(6, 1);
			break;
		case Z:
			blocks[0] = new Vector2(4, 0);
			blocks[1] = new Vector2(5, 0);
			blocks[2] = new Vector2(5, 1);
			blocks[3] = new Vector2(6, 1);
			break;
		case O:
			blocks[0] = new Vector2(4, 0);
			blocks[1] = new Vector2(5, 0);
			blocks[2] = new Vector2(4, 1);
			blocks[3] = new Vector2(5, 1);
			break;
		case S:
			blocks[0] = new Vector2(5, 0);
			blocks[1] = new Vector2(6, 0);
			blocks[2] = new Vector2(4, 1);
			blocks[3] = new Vector2(5, 1);
			break;
		case L:
			blocks[0] = new Vector2(4, 0);
			blocks[1] = new Vector2(5, 0);
			blocks[2] = new Vector2(6, 0);
			blocks[3] = new Vector2(4, 1);
			break;
		case I:
			blocks[0] = new Vector2(3, 0);
			blocks[1] = new Vector2(4, 0);
			blocks[2] = new Vector2(5, 0);
			blocks[3] = new Vector2(6, 0);
			break;
		}
	}
	
	public void setRotationChanges() { // Rotation change table for clockwise rotation
		Vector2[][] T = {
				{new Vector2(1, -1), new Vector2(-1, 0), new Vector2(-1, 0), new Vector2(0, 0)},
				{new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0), new Vector2(1, -1)},
				{new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 0), new Vector2(-1, 1)},
				{new Vector2(-1, 1), new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0)}
		};
		rotationChanges.put(PieceType.T, T);
		
		Vector2[][] J = {
				{new Vector2(1, -1), new Vector2(0, 0), new Vector2(-2, 1), new Vector2(-1, 0)},
				{new Vector2(-1, 0), new Vector2(-1, 0), new Vector2(1, -1), new Vector2(1, -1)},
				{new Vector2(1, 0), new Vector2(2, -1), new Vector2(0, 0), new Vector2(-1, 1)},
				{new Vector2(-1, 1), new Vector2(-1, 1), new Vector2(1, 0), new Vector2(1, 0)}
		};
		rotationChanges.put(PieceType.J, J);
		
		Vector2[][] Z = {
				{new Vector2(2, -1), new Vector2(0, 0), new Vector2(1, -1), new Vector2(-1, 0)},
				{new Vector2(-2, 1), new Vector2(0, 0), new Vector2(-1, 1), new Vector2(1, 0)},
				{new Vector2(2, -1), new Vector2(0, 0), new Vector2(1, -1), new Vector2(-1, 0)},
				{new Vector2(-2, 1), new Vector2(0, 0), new Vector2(-1, 1), new Vector2(1, 0)}
		};
		rotationChanges.put(PieceType.Z, Z);
		
		Vector2[][] O = {
				{new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0)},
				{new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0)},
				{new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0)},
				{new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0), new Vector2(0, 0)}
		};
		rotationChanges.put(PieceType.O, O);
		
		Vector2[][] S = {
				{new Vector2(0, -1), new Vector2(-1, 0), new Vector2(2, -1), new Vector2(1, 0)},
				{new Vector2(0, 1), new Vector2(1, 0), new Vector2(-2, 1), new Vector2(-1, 0)},
				{new Vector2(0, -1), new Vector2(-1, 0), new Vector2(2, -1), new Vector2(1, 0)},
				{new Vector2(0, 1), new Vector2(1, 0), new Vector2(-2, 1), new Vector2(-1, 0)}
		};
		rotationChanges.put(PieceType.S, S);
		
		Vector2[][] L = {
				{new Vector2(0, -1), new Vector2(0, -1), new Vector2(-1, 0), new Vector2(1, 0)},
				{new Vector2(2, 0), new Vector2(-1, 1), new Vector2(0, 0), new Vector2(1, -1)},
				{new Vector2(-1, 0), new Vector2(1, 0), new Vector2(0, 1), new Vector2(0, 1)},
				{new Vector2(-1, 1), new Vector2(0, 0), new Vector2(1, -1), new Vector2(-2, 0)}
		};
		rotationChanges.put(PieceType.L, L);
		
		Vector2[][] I = {
				{new Vector2(2, -2), new Vector2(1, -1), new Vector2(0, 0), new Vector2(-1, 1)},
				{new Vector2(-2, 2), new Vector2(-1, 1), new Vector2(0, 0), new Vector2(1, -1)},
				{new Vector2(2, -2), new Vector2(1, -1), new Vector2(0, 0), new Vector2(-1, 1)},
				{new Vector2(-2, 2), new Vector2(-1, 1), new Vector2(0, 0), new Vector2(1, -1)}
		};
		rotationChanges.put(PieceType.I, I);
	}
	
	public static BlockType getBlockType(PieceType pieceType) {
		switch (pieceType) {
		case Z:
		case L:
			return BlockType.Light;
		case J:
		case S:
			return BlockType.Dark;
		case T:
		case O:
		case I:
			return BlockType.Dual;
		default:
			return BlockType.Dual;
		}
	}
}
