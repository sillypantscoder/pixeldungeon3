package com.sillypantscoder.pixeldungeon3.entity;

import java.io.IOException;
import java.util.HashMap;

import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.pixeldungeon3.utils.Utils;
import com.sillypantscoder.window.Surface;

/**
 * This object contains data about the sprites. Each animation has a name, a list of surfaces, and a designated "next" animation that should play once the current animation is complete.
 */
public class Spritesheet {
	public HashMap<String, AnimationEntry> entries;
	public static class AnimationEntry {
		public Surface[] surfaces;
		public String next;
		public AnimationEntry(Surface[] surfaces, String next) {
			this.surfaces = surfaces;
			this.next = next;
		}
	}
	public Spritesheet(HashMap<String, AnimationEntry> entries) {
		this.entries = entries;
	}
	public static Spritesheet read(String name) {
		String[] data = Utils.getResource("spritesheet/" + name + ".txt").split("\n");
		String[] header = data[0].split(" ");
		int pxSize = Integer.parseInt(header[1]);
		Surface source = null;
		try {
			source = TextureLoader.loadAsset(header[0]);
		} catch (IOException e) {
			System.out.println("Failed to load spritesheet: " + name);
			e.printStackTrace();
		}
		if (source == null) return null;
		HashMap<String, AnimationEntry> entries = new HashMap<String, AnimationEntry>();
		for (int i = 1; i < data.length; i++) {
			String[] parts = data[i].split(": ");
			String animName = parts[0];
			String[] coords = parts[1].split(", ");
			String next = parts[2];
			// Parse coords
			Surface[] surfaces = new Surface[coords.length];
			for (int j = 0; j < coords.length; j++) {
				String[] pos = coords[j].split(" ");
				int x = Integer.parseInt(pos[0]);
				int y = Integer.parseInt(pos[1]);
				Surface s = source.crop(x * pxSize, y * pxSize, pxSize, pxSize);
				surfaces[j] = s;
			}
			// Finish
			entries.put(animName, new AnimationEntry(surfaces, next));
		}
		return new Spritesheet(entries);
	}
}
