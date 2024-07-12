package com.sillypantscoder.pixeldungeon3.entity;

import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.window.Surface;

/**
 * This class is the rendering half of the entity structure.
 * The x and y positions can be animated, and are not required to stay the same as the entity's x and y positions.
 * `animationName`, `animationPos`, and `sheet` are used for keeping track of the spritesheet animations.
 */
public class Actor {
	public double x;
	public double y;
	public String animationName;
	public int animationPos;
	public Spritesheet sheet;
	/**
	 * False = left; True = right
	 */
	public boolean direction;
	public Actor(double x, double y, Spritesheet sheet) {
		this.x = x;
		this.y = y;
		this.animationName = "idle";
		this.animationPos = 0;
		this.sheet = sheet;
		this.direction = true;
	}
	public void tick() {
		animationPos += 1;
		int realAnimationPos = animationPos / 6;
		if (realAnimationPos >= sheet.entries.get(animationName).surfaces.length) {
			animationPos = 0;
			animationName = sheet.entries.get(animationName).next;
		}
	}
	/**
	 * Get the current frame of the animation.
	 */
	public Surface getImage() {
		int realAnimationPos = animationPos / 6;
		Surface s = sheet.entries.get(animationName).surfaces[realAnimationPos];
		if (!direction) s = s.flipHorizontally();
		return s;
	}
	public void draw(Surface s) {
		s.blit(getImage(), (int)(this.x * Tile.TILE_SIZE), (int)(this.y * Tile.TILE_SIZE));
	}
	public void draw(Surface s, float opacity) {
		Surface thisImage = getImage();
		thisImage = thisImage.scaleValues(opacity);
		s.blit(thisImage, (int)(this.x * Tile.TILE_SIZE), (int)(this.y * Tile.TILE_SIZE));
	}
	/**
	 * Start the selected animation.
	 * @param type The name of the animation to start.
	 */
	public void animate(String type) {
		if (animationName == type) return;
		animationName = type;
		animationPos = 0;
	}
}
