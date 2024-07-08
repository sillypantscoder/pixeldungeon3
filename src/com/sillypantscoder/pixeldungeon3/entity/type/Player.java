package com.sillypantscoder.pixeldungeon3.entity.type;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;

public class Player extends Entity {
	public Player(Game game, int x, int y) {
		super(game, x, y);
	}
	public void requestAction() {
	}
	public Spritesheet getSpritesheet() {
		return Spritesheet.read("player");
	}
	public void click(int worldX, int worldY) {
		int xDiff = worldX - this.x;
		int yDiff = worldY - this.y;
		if (Math.abs(xDiff) > Math.abs(yDiff)) {
			// Tap on X direction
			if (xDiff < 0) {
				// Left
				this.setAction(new Action.MoveAction(this, -1, 0));
			} else {
				// Right
				this.setAction(new Action.MoveAction(this, 1, 0));
			}
		} else {
			// Tap on Y direction
			if (yDiff < 0) {
				// Up
				this.setAction(new Action.MoveAction(this, 0, -1));
			} else {
				// Down
				this.setAction(new Action.MoveAction(this, 0, 1));
			}
		}
	}
}
