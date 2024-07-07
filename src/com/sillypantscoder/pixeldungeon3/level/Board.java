package com.sillypantscoder.pixeldungeon3.level;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Random;

public class Board {
	public Cell[][] board;
	public Board() {
		board = new Cell[10][10];
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				board[x][y] = new Cell(CellType.Ground, x, y);
			}
		}
	}
	public Board(int width, int height) {
		board = new Cell[width][height];
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				board[x][y] = new Cell(CellType.Ground, x, y);
			}
		}
	}
	public int[] getSpawnLocation() {
		ArrayList<int[]> allowableLocs = new ArrayList<int[]>();
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				if (board[x][y].type.walkable()) {
					allowableLocs.add(new int[] { board[x][y].x, board[x][y].y });
				}
			}
		}
		return Random.choice(allowableLocs);
	}
}
