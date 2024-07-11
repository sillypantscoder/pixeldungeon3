package com.sillypantscoder.pixeldungeon3.item;

import com.sillypantscoder.pixeldungeon3.entity.PathfindTarget;
import com.sillypantscoder.pixeldungeon3.level.Level;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.window.Surface;

public class DroppedItem implements PathfindTarget {
	public static final int BOUNCE_TICKS = 25;
	public static final double BOUNCE_HEIGHT = 0.6;
	public Level level;
	public Item item;
	public int x;
	public int y;
	public int ticks;
	public DroppedItem(Level level, Item item, int x, int y) {
		this.level = level;
		this.item = item;
		this.x = x;
		this.y = y;
		this.ticks = 0;
	}
	public int getX() { return x; }
	public int getY() { return y; }
	public double getYOffset() {
		return BOUNCE_HEIGHT * Math.sin((this.ticks * Math.PI) / -BOUNCE_TICKS);
	}
	public int getYOffsetPixels() { return (int)(getYOffset() * Tile.TILE_SIZE); }
	public Tile getTile() {
		return level.get_at(this.x, this.y);
	}
	public void tick() {
		if (ticks < BOUNCE_TICKS) {
			ticks += 1;
		}
	}
	public void draw(Surface s) {
		s.blit(item.image, x * Tile.TILE_SIZE, (y * Tile.TILE_SIZE) + getYOffsetPixels());
	}
	public void remove() {
		level.items.remove(this);
	}
}
