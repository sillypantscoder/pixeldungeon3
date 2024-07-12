package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;
import java.util.ArrayList;

import com.sillypantscoder.window.Surface;

/**
 * Combine a list of elements horizontally.
 * Represents: this > * { display: inline-block; }
 */
public class HzCombine extends UIElement {
	public UIElement[] contents;
	public Color background;
	public HzCombine(UIElement[] contents, Color background) {
		this.contents = contents;
		this.background = background;
	}
	public Surface render(int maxWidth, int maxHeight) {
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		for (int i = 0; i < contents.length; i++) {
			surfaces.add(contents[i].render(maxWidth, maxHeight));
		}
		return Surface.combineHorizontally(surfaces, background);
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		int cumWidth = 0;
		for (int i = 0; i < contents.length; i++) {
			Surface surface = contents[i].render(maxWidth, maxHeight);
			int thisWidth = surface.get_width();
			if (x < cumWidth + thisWidth && y < surface.get_height()) {
				return contents[i].elementAtPoint(maxWidth, maxHeight, x - cumWidth, y);
			}
			cumWidth += thisWidth;
		}
		return null;
	}
	public static void main(String[] args) {
		HzCombine z = new HzCombine(new UIElement[] {
			new UIElement() { public Surface render(int maxWidth, int maxHeight) { return new Surface(100, 100, Color.BLACK); } public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
				System.out.println(x + " " + y);
				return this;
			} public String toString() { return "number 1"; } },
			new UIElement() { public Surface render(int maxWidth, int maxHeight) { return new Surface(50, 50, Color.BLACK); } public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
				System.out.println(x + " " + y);
				return this;
			} public String toString() { return "number 2"; } }
		}, Color.WHITE);
		System.out.println(z.elementAtPoint(200, 200, 101, 51));
	}
}
