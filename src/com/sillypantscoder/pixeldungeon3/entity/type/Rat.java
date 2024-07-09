package com.sillypantscoder.pixeldungeon3.entity.type;

import java.io.IOException;
import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;
import com.sillypantscoder.pixeldungeon3.level.LightStatus;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

public class Rat extends Entity {
	public boolean sleeping;
	public Player target;
	public Rat(Game game, int x, int y) {
		super(game, x, y);
		sleeping = true;
	}
	public void requestAction() {
		if (sleeping) {
			// Sleeping.
			// If we are visible, then wake up and choose a target.
			if (this.getTile().lightStatus == LightStatus.Current) {
				this.sleeping = false;
				this.time += 15;
			}
			this.setAction(new Action.SleepAction(this));
		} else {
			// Not sleeping: look for a new target if there is one.
			if (target == null) findNewTarget();
			if (target != null) {
				// Hunting.
				requestGoToTarget();
			} else {
				// Wandering.
				this.setAction(new Action.SleepAction(this));
				// TODO: Wandering
			}
		}
	}
	public void requestGoToTarget() {
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
		if (sleeping) {
			int iconX = (int)((this.x + 0.8) * Tile.TILE_SIZE);
			int iconY = (int)((this.y - 0.1) * Tile.TILE_SIZE);
			try {
				Surface icon = TextureLoader.loadAsset("icons.png");
				icon = icon.crop(13, 45, 9, 8);
				// ! icon: icon = icon.crop(22, 45, 8, 8);
				s.blit(icon, iconX, iconY);
			} catch (IOException e) {
				System.out.println("Enemy failed to load sleeping icon");
				e.printStackTrace();
			}
		}
	}
}
