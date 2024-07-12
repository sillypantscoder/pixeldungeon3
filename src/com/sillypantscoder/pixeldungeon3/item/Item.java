package com.sillypantscoder.pixeldungeon3.item;

import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.entity.type.Player;
import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

/**
 * An item.
 * This object should contain JUST the item. Items that have been dropped in the world should be represented by a DroppedItem.
 */
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
	/**
	 * Get the location of the sprite in the "items.png" file.
	 */
	public abstract int[] getSpriteLocation();
	/**
	 * Get the name of this item.
	 */
	public abstract String getName();
	// TODO: Item descriptions
	/**
	 * Get the list of buttons that appear when viewing this item in the inventory.
	 * @return
	 */
	public Button[] getButtons() {
		return new Button[] {
			new Button() {
				public String getName() { return "Drop"; }
				public void execute(Game game, Player player, Item item) {
					game.drop(item, player.x, player.y);
					player.inventory.remove(item);
				}
			}
		};
	}
	public static interface Button {
		public String getName();
		public void execute(Game game, Player player, Item item);
	}
}
