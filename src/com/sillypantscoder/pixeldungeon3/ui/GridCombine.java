package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;

import com.sillypantscoder.pixeldungeon3.utils.Rect;
import com.sillypantscoder.window.Surface;

public class GridCombine extends UIElement {
	public UIElement[] contents;
	public GridCombine(UIElement[] contents) {
		this.contents = contents;
	}
	public int[] getGridItemSize(int maxWidth, int maxHeight) {
		Surface s = contents[0].render(maxWidth, maxHeight);
		return new int[] {
			s.get_width(),
			s.get_height()
		};
	}
	public int[] getGridSize(int maxWidth, int maxHeight) {
		int cols = maxWidth / getGridItemSize(maxWidth, maxHeight)[0];
		cols = Math.min(cols, contents.length);
		return new int[] {
			cols,
			Math.ceilDiv(contents.length, cols)
		};
	}
	public Rect[] getLocations(int maxWidth, int maxHeight) {
		// System.out.println("\n");
		Rect[] locations = new Rect[contents.length];
		int[] size = getGridSize(maxWidth, maxHeight);
		int[] itemSize = getGridItemSize(maxWidth, maxHeight);
		for (int i = 0; i < contents.length; i++) {
			int x = (i % size[0]) * itemSize[0];
			int y = Math.floorDiv(i, size[0]) * itemSize[1];
			// System.out.println(i + " " + size[0] + " " + y);
			locations[i] = new Rect(x, y, itemSize[0], itemSize[1]);
		}
		return locations;
	}
	public Surface render(int maxWidth, int maxHeight) {
		int[] gridSize = getGridSize(maxWidth, maxHeight);
		int[] itemSize = getGridItemSize(maxWidth, maxHeight);
		Surface result = new Surface(gridSize[0] * itemSize[0], gridSize[1] * itemSize[1], new Color(0, 0, 0, 0));
		Rect[] locations = getLocations(maxWidth, maxHeight);
		for (int i = 0; i < locations.length; i++) {
			Rect cellRect = locations[i];
			UIElement cell = contents[i];
			Surface content = cell.render(cellRect.w, cellRect.h);
			result.blit(content, cellRect.x, cellRect.y);
		}
		return result;
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		Rect[] locations = getLocations(maxWidth, maxHeight);
		for (int i = 0; i < locations.length; i++) {
			if (locations[i].collidePoint(x, y)) {
				return contents[i].elementAtPoint(locations[i].w, locations[i].h, x - locations[i].x, y - locations[i].y);
			}
		}
		return this;
	}
}
