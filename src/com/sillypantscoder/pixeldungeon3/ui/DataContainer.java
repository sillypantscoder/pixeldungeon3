package com.sillypantscoder.pixeldungeon3.ui;

import com.sillypantscoder.window.Surface;

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
