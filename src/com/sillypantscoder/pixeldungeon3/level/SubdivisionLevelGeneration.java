package com.sillypantscoder.pixeldungeon3.level;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.utils.Rect;

/**
 * Generate a level by starting with a large square and subdividing it.
 */
public class SubdivisionLevelGeneration {
	public static void main(String[] args) {
		TileType[][] board = generateLevel();
		// Print board as ASCII art
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[y].length; x++) {
				if (board[x][y] == TileType.Wall) System.out.print("#");
				else System.out.print(".");
			}
			System.out.println();
		}
	}
	public static TileType[][] generateLevel() {
		int worldSize = 40;
		worldSize /= 2;
		ArrayList<Rect> rectsToDivide = new ArrayList<Rect>();
		ArrayList<Rect> resultRects = new ArrayList<Rect>();
		rectsToDivide.add(new Rect(0, 0, worldSize, worldSize));
		// Divide
		while (rectsToDivide.size() > 0) {
			@SuppressWarnings("unchecked")
			ArrayList<Rect> loopRects = (ArrayList<Rect>)(rectsToDivide.clone());
			rectsToDivide = new ArrayList<Rect>();
			for (int i = 0; i < loopRects.size(); i++) {
				Rect r = loopRects.get(i);
				boolean discardRect = false;
				// If the rect has no area, remove it
				if (r.w == 0 || r.h == 0) {
					// Aaaaaa!
					rectsToDivide.remove(r);
					continue;
				}
				// If the rect is too small to be divided further, finish it
				if (r.w < 3 || r.h < 3) discardRect = true;
				// If the rect is generally small enough, finish it
				if (r.w < 7 && r.h < 7) discardRect = true;
				// Handle the rect
				if (discardRect) {
					rectsToDivide.remove(r);
					resultRects.add(r);
				} else {
					// Divide the rect
					Rect[] newRects = r.subdivide();
					for (int n = 0; n < newRects.length; n++) {
						rectsToDivide.add(newRects[n]);
					}
				}
			}
		}
		// Create the board
		TileType[][] board = Level.generateBoard((worldSize * 2) + 1, (worldSize * 2) + 1);
		for (int i = 0; i < resultRects.size(); i++) {
			// Top
			for (int x = resultRects.get(i).left() * 2; x < resultRects.get(i).right() * 2; x++) board[x][resultRects.get(i).top() * 2] = TileType.Wall;
			// Bottom
			for (int x = resultRects.get(i).left() * 2; x < resultRects.get(i).right() * 2; x++) board[x][resultRects.get(i).bottom() * 2] = TileType.Wall;
			// Left
			for (int y = resultRects.get(i).top() * 2; y < resultRects.get(i).bottom() * 2; y++) board[resultRects.get(i).left() * 2][y] = TileType.Wall;
			// Right
			for (int y = resultRects.get(i).top() * 2; y < resultRects.get(i).bottom() * 2; y++) board[resultRects.get(i).right() * 2][y] = TileType.Wall;
			// Add doors
			int nDoors = 1 + (int)(Math.round(Math.random() * 2));
			for (int d = 0; d < nDoors; d++) {
				int doorSide = (int)(Math.round(Math.random() * 3));
				int doorX = 0;
				int doorY = 0;
				switch (doorSide) {
					case 0:
						// Top
						doorX = Random.randint((resultRects.get(i).left() * 2) + 1, (resultRects.get(i).right() * 2) - 1);
						doorY = resultRects.get(i).top() * 2;
						board[doorX][doorY] = TileType.Door;
						break;
					case 1:
						// Bottom
						doorX = Random.randint((resultRects.get(i).left() * 2) + 1, (resultRects.get(i).right() * 2) - 1);
						doorY = resultRects.get(i).bottom() * 2;
						board[doorX][doorY] = TileType.Door;
						break;
					case 2:
						// Left
						doorX = resultRects.get(i).left() * 2;
						doorY = Random.randint((resultRects.get(i).top() * 2) + 1, (resultRects.get(i).bottom() * 2) - 1);
						board[doorX][doorY] = TileType.Door;
						break;
					case 3:
						// Right
						doorX = resultRects.get(i).right() * 2;
						doorY = Random.randint((resultRects.get(i).top() * 2) + 1, (resultRects.get(i).bottom() * 2) - 1);
						board[doorX][doorY] = TileType.Door;
						break;
				}
			}
		}
		return board;
	}
}