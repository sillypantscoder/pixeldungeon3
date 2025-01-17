package com.sillypantscoder.pixeldungeon3;

import java.awt.event.MouseEvent;

import com.sillypantscoder.window.Surface;
import com.sillypantscoder.window.Window;

/**
 * The main window. Maintains a reference to a game.
 */
public class MainWindow extends Window {
	public Game game;
	public MainWindow() {
		super();
		this.game = new Game();
	}
	public void open() {
		super.open("Pixel Dungeon", 700, 1000);
	}
	public Surface frame(int width, int height) {
		game.tick();
		Surface s = game.render(width, height);
		return s;
	}
	public void mouseClicked(MouseEvent e) {
		this.game.click(e.getX(), e.getY());
	}
	public void mouseMoved(MouseEvent e) {
		this.game.mousePos = new int[] { e.getX(), e.getY() };
	}
}
