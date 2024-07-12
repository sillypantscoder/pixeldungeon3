package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;
import java.util.ArrayList;

import com.sillypantscoder.window.Surface;

/**
 * Combine a list of elements vertically.
 * Represents: this > * { display: block; }
 */
public class VCombine extends UIElement {
	public UIElement[] contents;
	public VCombine(UIElement[] contents) {
		this.contents = contents;
	}
	public Surface render(int maxWidth, int maxHeight) {
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		int cum_height = 0;
		for (int i = 0; i < contents.length; i++) {
			Surface surface = contents[i].render(maxWidth, maxHeight - cum_height);
			surfaces.add(surface);
			cum_height += surface.get_height();
		}
		return Surface.combineVertically(surfaces, new Color(0, 0, 0, 0));
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
