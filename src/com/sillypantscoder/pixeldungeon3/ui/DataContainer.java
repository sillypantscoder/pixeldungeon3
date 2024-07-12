package com.sillypantscoder.pixeldungeon3.ui;

import com.sillypantscoder.window.Surface;

/**
 * This element is always recorded as having been clicked, regardless of its contents.
 * This element can also contain some data, which can be read after the click.
 */
public class DataContainer<T> extends UIElement {
	public T data;
	public UIElement contents;
	public DataContainer(UIElement contents, T data) {
		this.data = data;
		this.contents = contents;
	}
	public Surface render(int maxWidth, int maxHeight) {
		return this.contents.render(maxWidth, maxHeight);
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		return this;
	}
}
