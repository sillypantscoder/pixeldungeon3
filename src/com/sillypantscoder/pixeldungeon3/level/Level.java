package com.sillypantscoder.pixeldungeon3.level;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.LivingEntity;
import com.sillypantscoder.pixeldungeon3.entity.type.Player;
import com.sillypantscoder.pixeldungeon3.item.DroppedItem;
import com.sillypantscoder.pixeldungeon3.utils.Pathfinding;
import com.sillypantscoder.window.Surface;

/**
 * A level.
 * This object contains all the "functionality"-related parts of the level,
 * including the board, list of entities, and dropped items.
 */
public class Level {
	public Tile[][] board;
	public ArrayList<Entity> entities;
	public ArrayList<DroppedItem> items;
	public Level(Game game, TileType[][] layout) {
		board = new Tile[layout.length][layout[0].length];
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				board[x][y] = new Tile(game, layout[x][y], x, y);
				board[x][y].updateTileImage();
			}
		}
		entities = new ArrayList<Entity>();
		items = new ArrayList<DroppedItem>();
	}
	/**
	 * Generate a blank board of the specified size.
	 */
	public static TileType[][] generateBoard(int width, int height) {
		TileType[][] layout = new TileType[width][height];
		for (int x = 0; x < layout.length; x++) {
			for (int y = 0; y < layout[x].length; y++) {
				layout[x][y] = TileType.Ground;
			}
		}
		return layout;
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
	public int[][] findPath(int startX, int startY, int endX, int endY, boolean playerOnly) {
		int[][] nboard = new int[getWidth()][getHeight()];
		for (int x = 0; x < nboard.length; x++) {
			for (int y = 0; y < nboard[x].length; y++) {
				TileType type = this.board[x][y].type;
				LightStatus light = this.board[x][y].lightStatus;
				int weight = 0;
				if (type == TileType.Chasm) weight = 0;
				if (type == TileType.Ground) weight = 1;
				if (type == TileType.Wall) weight = 0;
				if (type == TileType.Door) weight = 1;
				if (playerOnly && light == LightStatus.Unknown) weight = 0;
				nboard[x][y] = weight;
			}
		}
		return Pathfinding.findPath(nboard, new int[] { startX, startY }, new int[] { endX, endY });
	}
	public ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (int i = 0; i < this.entities.size(); i++) {
			if (this.entities.get(i) instanceof Player p) {
				players.add(p);
			}
		}
		return players;
	}
	public void updateLight() {
		for (int x = 0; x < this.board.length; x++) {
			for (int y = 0; y < this.board[x].length; y++) {
				Tile tile = this.board[x][y];
				if (tile.lightStatus == LightStatus.Current) {
					tile.lightStatus = LightStatus.Memory;
				}
			}
		}
		ArrayList<Player> players = getPlayers();
		for (int i = 0; i < players.size(); i++) {
			players.get(i).addLight();
		}
	}
	public boolean outOfBounds(int[] location) {
		return location[0] < 0 || location[1] < 0 || location[0] >= getWidth() || location[1] >= getHeight();
	}
	public boolean outOfBounds(int x, int y) {
		return x < 0 || y < 0 || x >= getWidth() || y >= getHeight();
	}
	public Tile get_at(int x, int y) {
		if (outOfBounds(x, y)) return null;
		return board[x][y];
	}
	public LivingEntity getEntity(int x, int y) {
		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
			if (e instanceof LivingEntity l) {
				if (l.x == x && l.y == y) {
					return l;
				}
			}
		}
		return null;
	}
	public DroppedItem getItem(int x, int y) {
		for (int i = 0; i < this.items.size(); i++) {
			DroppedItem m = this.items.get(i);
			if (m.x == x && m.y == y) {
				return m;
			}
		}
		return null;
	}
}
