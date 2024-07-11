package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.item.Item;
import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

public class GameUI {
	// Images
	public static final int UI_SCALE = 3;
	public Surface button_backpack;
	// Game
	public Game game;
	public UIState state;
	public GameUI(Game game) {
		this.game = game;
		this.state = UIState.GAME;
		try {
			Surface toolbar = TextureLoader.loadAsset("toolbar.png");
			button_backpack = toolbar.crop(61, 8, 21, 23);
			button_backpack = button_backpack.scale_size(UI_SCALE);
		} catch (IOException e) {
			System.out.println("UI failed to load texture");
			e.printStackTrace();
		}
	}
	public Surface render(int width, int height) {
		Surface result = new Surface(width, height, new Color(0, 0, 0, 0));
		// Bottom right: Draw inventory buttons
		result.blit(button_backpack, result.get_width() - button_backpack.get_width(), result.get_height() - button_backpack.get_height());
		if (this.state == UIState.BACKPACK) {
			// Draw backpack
			Surface backpack = drawBackpack(width);
			result.blit(backpack, (result.get_width() / 2) - (backpack.get_width() / 2), (result.get_height() / 2) - (backpack.get_height() / 2));
		}
		// Finish
		return result;
	}
	public Surface drawBackpack(int maxWidth) {
		ArrayList<Item> inv = game.player.inventory;
		int padding = 4;
		int cellsize = (30 * UI_SCALE);
		int pcellsize = cellsize + (padding * 2);
		int cols = maxWidth / pcellsize;
		int rows = Math.ceilDiv(inv.size(), cols);
		Surface s = new Surface(cols * pcellsize, rows * pcellsize, Color.GRAY.darker());
		int i = 0;
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				// Draw square
				int drawX = (x * pcellsize) + padding;
				int drawY = (y * pcellsize) + padding;
				s.drawRect(Color.GRAY, drawX, drawY, cellsize, cellsize);
				// Draw item
				if (i < inv.size()) {
					Surface item = inv.get(i).image.scale_size(UI_SCALE);
					int itemCellX = (cellsize / 2) - (item.get_width() / 2);
					int itemCellY = (cellsize / 2) - (item.get_height() / 2);
					s.blit(item, drawX + itemCellX, drawY + itemCellY);
				}
				// Increment
				i += 1;
			}
		}
		return s;
	}
	public boolean click(int xLeft, int yTop) {
		int xRight = game.recentSize[0] - xLeft;
		int yBottom = game.recentSize[1] - yTop;
		if (state == UIState.GAME) {
			// Bottom right: Inventory buttons
			if (xRight <= button_backpack.get_width() && yBottom <= button_backpack.get_height()) {
				state = UIState.BACKPACK;
				return true;
			}
		} else if (state == UIState.BACKPACK) {
			state = UIState.GAME;
			return true;
		}
		// No buttons
		return false;
	}
	public static enum UIState {
		GAME,
		BACKPACK
	}
}
