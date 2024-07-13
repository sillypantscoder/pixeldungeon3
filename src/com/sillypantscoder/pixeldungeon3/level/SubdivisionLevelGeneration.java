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
	public static ArrayList<Rect> getDividedRects(int worldSize, int minRoomSize, int maxRoomSize) {
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
				boolean finishRect = false;
				// If the rect has no area, remove it
				if (r.w == 0 || r.h == 0) {
					// Aaaaaa!
					rectsToDivide.remove(r);
					continue;
				}
				// If the rect is too small to be divided further, finish it
				if (r.w < minRoomSize || r.h < minRoomSize) finishRect = true;
				// If the rect is generally small enough, finish it
				if (r.w < maxRoomSize && r.h < maxRoomSize) finishRect = true;
				// Handle the rect
				if (finishRect) {
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
		return resultRects;
	}
	public static void addDoor(TileType[][] board, Rect rect) {
		int nDoors = 1 + (int)(Math.round(Math.random() * 2));
		for (int d = 0; d < nDoors; d++) {
			int doorSide = (int)(Math.round(Math.random() * 3));
			int doorX = 0;
			int doorY = 0;
			switch (doorSide) {
				case 0:
					// Top
					doorX = Random.randint((rect.left() * 2) + 1, (rect.right() * 2) - 1);
					doorY = rect.top() * 2;
					board[doorX][doorY] = TileType.Door;
					break;
				case 1:
					// Bottom
					doorX = Random.randint((rect.left() * 2) + 1, (rect.right() * 2) - 1);
					doorY = rect.bottom() * 2;
					board[doorX][doorY] = TileType.Door;
					break;
				case 2:
					// Left
					doorX = rect.left() * 2;
					doorY = Random.randint((rect.top() * 2) + 1, (rect.bottom() * 2) - 1);
					board[doorX][doorY] = TileType.Door;
					break;
				case 3:
					// Right
					doorX = rect.right() * 2;
					doorY = Random.randint((rect.top() * 2) + 1, (rect.bottom() * 2) - 1);
					board[doorX][doorY] = TileType.Door;
					break;
			}
		}
	}
	public static TileType[][] generateLevel() {
		int worldSize = 60;
		worldSize /= 2;
		ArrayList<Rect> resultRects = getDividedRects(worldSize, 3, 7);
		// Draw the rooms
		TileType[][] board = Level.generateBoard((worldSize * 2) + 1, (worldSize * 2) + 1, TileType.Ground);
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
			addDoor(board, resultRects.get(i));
		}
		// Draw the border
		for (int i = 0; i < board.length; i++) {
			board[i][0] = TileType.Wall;
			board[i][board[i].length - 1] = TileType.Wall;
			board[0][i] = TileType.Wall;
			board[board.length - 1][i] = TileType.Wall;
		}
		// Finish
		return board;
	}
}