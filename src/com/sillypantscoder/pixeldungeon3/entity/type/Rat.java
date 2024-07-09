package com.sillypantscoder.pixeldungeon3.entity.type;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;
import com.sillypantscoder.pixeldungeon3.level.LightStatus;

public class Rat extends Entity {
	public Player target;
	public Rat(Game game, int x, int y) {
		super(game, x, y);
	}
	public void requestAction() {
		if (target == null) {
			// If someone can see us...
			if (this.getTile().lightStatus == LightStatus.Current) {
				// Choose a target
				ArrayList<Player> allPlayers = game.level.getPlayers();
				if (allPlayers.size() > 0) {
					target = Random.choice(allPlayers);
				}
			}
		}
		if (target == null) {
			this.setAction(new Action.SleepAction(this));
			return;
		}
		int[][] path = game.level.findPath(this.x, this.y, target.x, target.y, false);
		if (path.length == 0) {
			this.setAction(new Action.SleepAction(this));
			this.target = null;
			return;
		}
		int[] nextTarget = path[1];
		if (nextTarget[0] == target.x && nextTarget[1] == target.y) {
			// Attack!
			this.setAction(new Action.AttackAction(this, target));
		} else {
			this.setAction(new Action.MoveAction(this, nextTarget[0], nextTarget[1]));
		}
	}
	public Spritesheet getSpritesheet() {
		return Spritesheet.read("rat");
	}
	public int getMaxHealth() {
		return 4;
	}
	public int getDamage() {
		return 2;
	}
}
