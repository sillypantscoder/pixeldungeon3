package com.sillypantscoder.pixeldungeon3.entity.type;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;

public class Rat extends Entity {
	public Rat(Game game, int x, int y) {
		super(game, x, y);
	}
	public void requestAction() {
		// TODO: Rat pathfinding AI
	}
	public Spritesheet getSpritesheet() {
		return Spritesheet.read("rat");
	}
}
