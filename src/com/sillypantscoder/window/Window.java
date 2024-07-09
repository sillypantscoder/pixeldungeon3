package com.sillypantscoder.window;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public abstract class Window {
	public RepaintingPanel panel;
	public Window() {
		panel = new RepaintingPanel();
		panel.painter = this::painter;
		panel.mouseClicked = this::mouseClicked;
		panel.mouseMoved = this::mouseMoved;
	}
	public void open(String title, int width, int height) {
		panel.run(title, width, height);
	}
	public BufferedImage painter(int width, int height) {
		return this.frame(width, height).img;
	}
	public abstract Surface frame(int width, int height);
	public abstract void mouseClicked(MouseEvent e);
	public abstract void mouseMoved(MouseEvent e);
}
