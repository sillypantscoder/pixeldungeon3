package com.sillypantscoder.pixeldungeon3.item;

import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

public abstract class Item {
	public Surface image;
	public Item() {
		try {
			Surface items = TextureLoader.loadAsset("items.png");
			int[] loc = getSpriteLocation();
			image = items.crop(loc[0] * 16, loc[1] * 16, 16, 16);
		} catch (IOException e) {
			System.out.println("Item failed to load texture!");
			e.printStackTrace();
		}
	}
	public abstract int[] getSpriteLocation();
	public abstract String getName();
}
