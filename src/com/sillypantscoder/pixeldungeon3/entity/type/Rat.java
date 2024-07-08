package com.sillypantscoder.pixeldungeon3.entity.type;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;

public class Rat extends Entity {
	public Player target;
	public Rat(Game game, int x, int y) {
		super(game, x, y);
	}
	public void requestAction() {
		if (target == null) {
			target = game.getRandomPlayer();
		}
		int[][] path = game.level.findPath(this.x, this.y, target.x, target.y);
		if (path.length == 0) {
			this.setAction(new Action.SleepAction(this));
			return;
		}
		int[] nextTarget = path[1];
		this.setAction(new Action.MoveAction(this, nextTarget[0], nextTarget[1]));
	}
	public Spritesheet getSpritesheet() {
		return Spritesheet.read("rat");
	}
}
