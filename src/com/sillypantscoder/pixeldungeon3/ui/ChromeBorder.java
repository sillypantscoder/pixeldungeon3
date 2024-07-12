package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;
import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.GameUI;
import com.sillypantscoder.pixeldungeon3.utils.Rect;
import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

public class ChromeBorder extends UIElement {
	public Surface border;
	public int size;
	public UIElement contents;
	public ChromeBorder(int x, int y, int size, UIElement contents) {
		try {
			Surface texture = TextureLoader.loadAsset("chrome.png");
			border = texture.crop(x, y, x + (size * 3), y + (size * 3));
			border = border.scale_size(GameUI.UI_SCALE);
		} catch (IOException e) {
			System.out.println("Chrome border failed to load texture:");
			e.printStackTrace();
		}
		this.size = size * GameUI.UI_SCALE;
		this.contents = contents;
	}
	public Surface render(int maxWidth, int maxHeight) {
		Surface c = contents.render(maxWidth - (size * 2), maxHeight - (size * 2));
		Surface result = new Surface(c.get_width() + (size * 2), c.get_height() + (size * 2), new Color(0, 0, 0, 0));
		for (int x : new int[] { 0, 1, 2 }) {
			for (int y : new int[] { 0, 1, 2 }) {
				// Get the border tile
				Surface tile = border.crop(x * size, y * size, size, size);
				// Repeat the image
				int tileWidth = x == 1 ? c.get_width() : -1;
				int tileHeight = y == 1 ? c.get_height() : -1;
				Surface tiled = tile.tile(tileWidth, tileHeight);
				// Draw the image
				int drawX = x == 2 ? (result.get_width() - tiled.get_width()) : size * x;
				int drawY = y == 2 ? (result.get_height() - tiled.get_height()) : size * y;
				result.blit(tiled, drawX, drawY);
			}
		}
		result.blit(c, size, size);
		return result;
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		Surface c = contents.render(maxWidth - (size * 2), maxHeight - (size * 2));
		if ((new Rect(size, size, c.get_width(), c.get_height())).collidePoint(x, y)) {
			return this.contents.elementAtPoint(maxWidth - (size * 2), maxHeight - (size * 2), x - size, y - size);
		}
		return this;
	}
	public static void main(String[] args) {
		ChromeBorder b = new ChromeBorder(0, 0, 7, new TextElement("hi there!", 30, Color.RED));
		try {
			b.render(100, 100).writeToFile("stuff.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
