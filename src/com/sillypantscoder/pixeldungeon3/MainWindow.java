package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;
import java.awt.event.MouseEvent;

import com.sillypantscoder.window.Surface;
import com.sillypantscoder.window.Window;

public class MainWindow extends Window {
	public MainWindow() {
		super("Pixel Dungeon", 700, 1000);
	}
	public Surface frame(int width, int height) {
		Surface s = new Surface(width, height, Color.BLACK);
		return s;
	}
	public void mouseClicked(MouseEvent e) {
	}
}
