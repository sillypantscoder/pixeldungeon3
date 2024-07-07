package com.sillypantscoder.pixeldungeon3.entity;

import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.window.Surface;

public class Actor {
	public double x;
	public double y;
	public Surface texture;
	public Actor(double x, double y, Surface texture) {
		this.x = x;
		this.y = y;
		this.texture = texture;
	}
	public void draw(Surface s) {
		s.blit(texture, (int)(this.x * Tile.TILE_SIZE), (int)(this.y * Tile.TILE_SIZE));
	}
}
