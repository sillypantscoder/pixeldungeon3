package com.sillypantscoder.pixeldungeon3.ui;

import com.sillypantscoder.pixeldungeon3.GameUI;
import com.sillypantscoder.window.Surface;

/**
 * Display an image, as an element.
 * Represents: <img>
 */
public class ImageDisplay extends UIElement {
	public Surface image;
	public ImageDisplay(Surface image) {
		this.image = image;
	}
	public Surface render(int maxWidth, int maxHeight) {
		return this.image.scale_size(GameUI.UI_SCALE);
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		return this;
	}
}
