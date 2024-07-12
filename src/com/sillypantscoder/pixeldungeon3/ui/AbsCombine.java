package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;

import com.sillypantscoder.window.Surface;

public class AbsCombine extends UIElement {
	public UIElement[] contents;
	public boolean lastOnly;
	public AbsCombine(UIElement[] contents, boolean lastOnly) {
		this.contents = contents;
		this.lastOnly = lastOnly;
	}
	public Surface render(int maxWidth, int maxHeight) {
		Surface s = new Surface(maxWidth, maxHeight, new Color(0, 0, 0, 0));
		for (int i = 0; i < contents.length; i++) {
			Surface c = contents[i].render(maxWidth, maxHeight);
			int drawX = (maxWidth / 2) - (c.get_width() / 2);
			int drawY = (maxHeight / 2) - (c.get_height() / 2);
			s.blit(c, drawX, drawY);
		}
		return s;
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		for (int i = contents.length - 1; i >= 0; i--) {
			Surface c = contents[i].render(maxWidth, maxHeight);
			int drawX = (maxWidth / 2) - (c.get_width() / 2);
			int drawY = (maxHeight / 2) - (c.get_height() / 2);
			if (new Rect(drawX, drawY, c.get_width(), c.get_height()).collidePoint(x, y)) {
				return contents[i].elementAtPoint(maxWidth, maxHeight, x - drawX, y - drawY);
			}
			if (lastOnly) return null;
		}
		return null;
	}
}
