package com.sillypantscoder.pixeldungeon3.item;

import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.Game;
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
	public Button[] getButtons() {
		return new Button[] {
			new Button() {
				public String getName() { return "Drop"; }
				public void execute(Game game, Item item) {
					game.drop(item, game.player.x, game.player.y);
					game.player.inventory.remove(item);
				}
			}
		};
	}
	public static interface Button {
		public String getName();
		public void execute(Game game, Item item);
	}
}
