package com.sillypantscoder.pixeldungeon3.entity;

import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

/**
 * Indicates the state of an enemy.
 */
public enum EnemyState {
	SLEEPING,
	WAKING_UP,
	WANDERING,
	HUNTING;
	/**
	 * Draw a small annotation (depending on this value) onto the surface.
	 * For example, a "z" icon appears above sleeping entities.
	 * @param s The surface to draw onto.
	 */
	public void drawAnnotation(Surface s, int x, int y) {
		if (this == WANDERING) return;
		if (this == HUNTING) return;
		try {
			Surface icon = TextureLoader.loadAsset("icons.png");
			if (this == SLEEPING) icon = icon.crop(13, 45, 9, 8);
			else icon = icon.crop(22, 45, 8, 8);
			s.blit(icon, x, y);
		} catch (IOException e) {
			System.out.println("Enemy failed to load sleeping icon");
			e.printStackTrace();
		}
	}
}
