package com.sillypantscoder.pixeldungeon3.level;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.window.Surface;

public class Level {
	public Tile[][] board;
	public ArrayList<Entity> entities;
	public Level(int width, int height) {
		board = new Tile[width][height];
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				board[x][y] = new Tile(TileType.Ground, x, y);
			}
		}
		entities = new ArrayList<Entity>();
	}
	public void draw(Surface s) {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				Surface tile = board[x][y].draw();
				s.blit(tile, x * Tile.TILE_SIZE, y * Tile.TILE_SIZE);
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
	public int getWidth() {
		return board.length;
	}
	public int getHeight() {
		return board.length > 0 ? board[0].length : 0;
	}
}
