package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;

import com.sillypantscoder.pixeldungeon3.GameUI;
import com.sillypantscoder.window.Surface;

/**
 * An object that displays some text.
 */
public class TextElement extends UIElement {
	public String text;
	public int size;
	public Color color;
	public TextElement(String text, int size, Color color) {
		this.text = text;
		this.size = size;
		this.color = color;
	}
	public Surface render(int maxWidth, int maxHeight) {
		return Surface.renderText(size * GameUI.UI_SCALE, text, color);
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		return this;
	}
}
