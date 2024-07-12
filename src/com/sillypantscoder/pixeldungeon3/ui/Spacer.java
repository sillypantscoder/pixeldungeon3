package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;

import com.sillypantscoder.pixeldungeon3.GameUI;
import com.sillypantscoder.window.Surface;

/**
 * An element that functions as a blank spacer.
 */
public class Spacer extends UIElement {
	public int width;
	public int height;
	public Color color;
	public Spacer(int width, int height) {
		this.width = width;
		this.height = height;
		this.color = new Color(0, 0, 0, 0);
	}
	public Spacer(int width, int height, Color color) {
		this.width = width;
		this.height = height;
		this.color = color;
	}
	public Surface render(int maxWidth, int maxHeight) {
		return new Surface(width * GameUI.UI_SCALE, height * GameUI.UI_SCALE, color);
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		return this;
	}
}
