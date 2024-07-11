package com.sillypantscoder.pixeldungeon3.item;

import com.sillypantscoder.pixeldungeon3.entity.PathfindTarget;
import com.sillypantscoder.pixeldungeon3.level.Level;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.window.Surface;

public class DroppedItem implements PathfindTarget {
	public Level level;
	public Item item;
	public int x;
	public int y;
	public DroppedItem(Level level, Item item, int x, int y) {
		this.level = level;
		this.item = item;
		this.x = x;
		this.y = y;
	}
	public int getX() { return x; }
	public int getY() { return y; }
	public Tile getTile() {
		return level.get_at(this.x, this.y);
	}
	public void draw(Surface s) {
		s.blit(item.image, x * Tile.TILE_SIZE, y * Tile.TILE_SIZE);
	}
	public void remove() {
		level.items.remove(this);
	}
}
