package com.sillypantscoder.pixeldungeon3.ui;

import com.sillypantscoder.window.Surface;

/**
 * The base class for an element.
 */
public abstract class UIElement {
	public abstract Surface render(int maxWidth, int maxHeight);
	public abstract UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y);
}
