package com.sillypantscoder.pixeldungeon3.entity.type;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.EnemyState;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;
import com.sillypantscoder.pixeldungeon3.level.LightStatus;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.window.Surface;

public class Rat extends Entity {
	public EnemyState state;
	public Player target;
	public Rat(Game game, int x, int y) {
		super(game, x, y);
		state = EnemyState.SLEEPING;
	}
	public void requestAction() {
		if (state == EnemyState.SLEEPING) {
			// Sleeping: If we are visible, then wake up.
			if (this.getTile().lightStatus == LightStatus.Current) {
				this.state = EnemyState.WAKING_UP;
				this.time += 5;
			}
			this.setAction(new Action.SleepAction(this));
		} else if (state == EnemyState.WAKING_UP) {
			// Waking up: Choose a target.
			findNewTarget();
			if (target == null) state = EnemyState.WANDERING;
			else state = EnemyState.HUNTING;
			// (Do not move.)
			this.setAction(new Action.SleepAction(this));
		} else if (state == EnemyState.WANDERING) {
			// Wandering: Wander around and look for a target.
			this.setAction(new Action.SleepAction(this));
			// TODO: Wandering
			findNewTarget();
			if (target != null) state = EnemyState.HUNTING;
		} else if (state == EnemyState.HUNTING) {
			// Hunting: Pathfind to, or attack, the target.
			requestHuntingAction();
		}
	}
	public void requestHuntingAction() {
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
	public void findNewTarget() {
		// Choose a target
		ArrayList<Player> allPlayers = game.level.getPlayers();
		if (allPlayers.size() > 0) {
			target = Random.choice(allPlayers);
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
	public void draw(Surface s) {
		super.draw(s);
		// Draw sleeping icon
		int iconX = (int)((this.x + 0.8) * Tile.TILE_SIZE);
		int iconY = (int)((this.y - 0.1) * Tile.TILE_SIZE);
		this.state.drawAnnotation(s, iconX, iconY);
	}
}
