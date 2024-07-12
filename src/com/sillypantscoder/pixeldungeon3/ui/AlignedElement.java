package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;

import com.sillypantscoder.window.Surface;

public class AlignedElement extends UIElement {
	public UIElement contents;
	public int[] align;
	public AlignedElement(UIElement contents, int xAlign, int yAlign) {
		this.contents = contents;
		this.align = new int[] { xAlign, yAlign };
	}
	public int[] getPos(int maxWidth, int maxHeight) {
		Surface contentSurface = this.contents.render(maxWidth, maxHeight);
		// x
		int posX = 0;
		if (align[0] == 0) posX = (maxWidth / 2) - (contentSurface.get_width() / 2);
		if (align[0] == 1) posX = maxWidth - contentSurface.get_width();
		// y
		int posY = 0;
		if (align[1] == 0) posY = (maxHeight / 2) - (contentSurface.get_height() / 2);
		if (align[1] == 1) posY = maxHeight - contentSurface.get_height();
		// return
		return new int[] { posX, posY };
	}
	public Surface render(int maxWidth, int maxHeight) {
		// Find location
		Surface contentSurface = this.contents.render(maxWidth, maxHeight);
		int[] pos = getPos(maxWidth, maxHeight);
		// Render element
		Surface r = new Surface(maxWidth, maxHeight, new Color(0, 0, 0, 0));
		r.blit(contentSurface, pos[0], pos[1]);
		return r;
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		// Find location
		Surface contentSurface = this.contents.render(maxWidth, maxHeight);
		int[] pos = getPos(maxWidth, maxHeight);
		// Check for collision
		Rect cRect = new Rect(pos[0], pos[1], contentSurface.get_width(), contentSurface.get_height());
		if (cRect.collidePoint(x, y)) {
			return contents.elementAtPoint(maxWidth, maxHeight, x - pos[0], y - pos[1]);
		} else {
			return null;
		}
	}
}
