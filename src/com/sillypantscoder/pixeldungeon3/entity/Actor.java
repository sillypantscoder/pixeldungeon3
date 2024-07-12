package com.sillypantscoder.pixeldungeon3.entity;

import java.awt.Color;
import java.util.ArrayList;

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
	public ArrayList<String> damages;
	public ArrayList<Integer> damageTimes;
	public Actor(double x, double y, Spritesheet sheet) {
		this.x = x;
		this.y = y;
		this.animationName = "idle";
		this.animationPos = 0;
		this.sheet = sheet;
		this.direction = true;
		this.damages = new ArrayList<String>();
		this.damageTimes = new ArrayList<Integer>();
	}
	public void tick() {
		animationPos += 1;
		int realAnimationPos = animationPos / 6;
		if (realAnimationPos >= sheet.entries.get(animationName).surfaces.length) {
			animationPos = 0;
			animationName = sheet.entries.get(animationName).next;
		}
		// Damage text thingys:
		for (int i = 0; i < damages.size(); i++) {
			damageTimes.set(i, damageTimes.get(i) + 1);
			if (damageTimes.get(i) > 60) {
				damages.remove(i);
				damageTimes.remove(i);
				i -= 1;
			}
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
		// Damage text thingys:
		for (int i = 0; i < damages.size(); i++) {
			Surface data = Surface.renderText(11, damages.get(i), Color.RED);
			data.scaleValues(damageTimes.get(i) / 60f);
			int offset = (int)((damageTimes.get(i) / 60f) * Tile.TILE_SIZE);
			// Draw
			int drawX = (int)((this.x + 0.5) * Tile.TILE_SIZE);
			drawX -= data.get_width() / 2;
			int drawY = (int)((this.y - 1) * Tile.TILE_SIZE);
			drawY -= offset;
			s.blit(data, drawX, drawY);
		}
	}
	public void damage(String data) {
		damages.add(data);
		damageTimes.add(0);
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
