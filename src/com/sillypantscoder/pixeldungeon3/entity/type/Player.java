package com.sillypantscoder.pixeldungeon3.entity.type;

import java.util.Optional;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;

public class Player extends Entity {
	public Player(Game game, int x, int y) {
		super(game, x, y);
	}
	public void requestAction() {
		if (Math.random() > 0.01) return;
		this.action = Optional.of(Random.choice(new Action[] {
			new Action.MoveAction(this, 0, 1),
			new Action.MoveAction(this, 0, -1),
			new Action.MoveAction(this, 1, 0),
			new Action.MoveAction(this, -1, 0)
		}));
	}
	public Spritesheet getSpritesheet() {
		return Spritesheet.read("player");
	}
}
