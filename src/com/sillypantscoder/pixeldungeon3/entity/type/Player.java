package com.sillypantscoder.pixeldungeon3.entity.type;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;
import com.sillypantscoder.pixeldungeon3.level.LightStatus;
import com.sillypantscoder.pixeldungeon3.level.LinePoints;
import com.sillypantscoder.pixeldungeon3.level.Tile;

public class Player extends Entity {
	public int[] targetLocation;
	public Entity targetEntity;
	public Player(Game game, int x, int y) {
		super(game, x, y);
		targetLocation = null;
		targetEntity = null;
	}
	public void requestAction() {
		this.actor.animate("idle");
		if (this.targetLocation != null) {
			// Pathfind to selected location
			this.requestPathfind(this.targetLocation[0], this.targetLocation[1]);
		} else if (this.targetEntity != null) {
			// Pathfind to selected entity
			this.requestPathfind(this.targetEntity.x, this.targetEntity.y);
		}
	}
	public void requestPathfind(int targetX, int targetY) {
		int[][] path = game.level.findPath(this.x, this.y, targetX, targetY, true);
		if (path.length == 0) {
			this.targetLocation = null;
			return;
		}
		if (path.length == 1) {
			this.targetLocation = null;
			return;
		}
		if (path.length == 2) {
			this.targetLocation = null;
		}
		int[] nextTarget = path[1];
		this.setAction(new Action.MoveAction(this, nextTarget[0], nextTarget[1]));
	}
	public Spritesheet getSpritesheet() {
		return Spritesheet.read("player");
	}
	public void click(int worldX, int worldY) {
		if (targetLocation != null || targetEntity != null) {
			// Cancel
			targetLocation = null;
			targetEntity = null;
			return;
		}
		// Move to entity
		for (int i = 0; i < game.level.entities.size(); i++) {
			Entity e = game.level.entities.get(i);
			if (e.x == worldX && e.y == worldY) {
				targetEntity = e;
				return;
			}
		}
		// Move to location
		targetLocation = new int[] { worldX, worldY };
	}
	// Lighting
	public int getViewDistance() {
		return 7;
	}
	public void addLight() {
		int vd = getViewDistance();
		for (int cx = -vd; cx <= vd; cx++) {
			for (int cy = -vd; cy <= vd; cy++) {
				checkForLight(cx + this.x, cy + this.y);
			}
		}
	}
	protected void checkForLight(int cx, int cy) {
		int[][] points = LinePoints.get_line(new int[] { this.x, this.y }, new int[] { cx, cy });
		for (int i = 0; i < points.length; i++) {
			if (game.level.outOfBounds(points[i])) continue;
			Tile tile = game.level.board[points[i][0]][points[i][1]];
			if (points[i][0] == cx && points[i][1] == cy) {
				tile.lightStatus = LightStatus.Current;
				return;
			}
			if (! tile.type.canSeeThrough()) {
				break;
			}
			tile.lightStatus = LightStatus.Current;
		}
	}
}
