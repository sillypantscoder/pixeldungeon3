package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;
import java.util.ArrayList;

import com.sillypantscoder.window.Surface;

public class VCombine extends UIElement {
	public UIElement[] contents;
	public Color background;
	public VCombine(UIElement[] contents, Color background) {
		this.contents = contents;
		this.background = background;
	}
	public Surface render(int maxWidth, int maxHeight) {
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		for (int i = 0; i < contents.length; i++) {
			surfaces.add(contents[i].render(maxWidth, maxHeight));
		}
		return Surface.combineVertically(surfaces, background);
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		int cumHeight = 0;
		for (int i = 0; i < contents.length; i++) {
			Surface surface = contents[i].render(maxWidth, maxHeight);
			int thisHeight = surface.get_height();
			if (y < cumHeight + thisHeight && x < surface.get_width()) {
				return contents[i].elementAtPoint(maxWidth, maxHeight, x, y - cumHeight);
			}
			cumHeight += thisHeight;
		}
		return null;
	}
}
