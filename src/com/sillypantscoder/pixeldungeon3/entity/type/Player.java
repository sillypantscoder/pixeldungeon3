package com.sillypantscoder.pixeldungeon3.entity.type;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;
import com.sillypantscoder.pixeldungeon3.level.LightStatus;
import com.sillypantscoder.pixeldungeon3.level.LinePoints;
import com.sillypantscoder.pixeldungeon3.level.Tile;

public class Player extends Entity {
	public Action selectedAction;
	public Player(Game game, int x, int y) {
		super(game, x, y);
		selectedAction = null;
	}
	public void requestAction() {
		this.setAction(selectedAction);
		this.selectedAction = null;
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
				this.selectedAction = Action.MoveAction.createFromDelta(this, -1, 0);
			} else {
				// Right
				this.selectedAction = Action.MoveAction.createFromDelta(this, 1, 0);
			}
		} else {
			// Tap on Y direction
			if (yDiff < 0) {
				// Up
				this.selectedAction = Action.MoveAction.createFromDelta(this, 0, -1);
			} else {
				// Down
				this.selectedAction = Action.MoveAction.createFromDelta(this, 0, 1);
			}
		}
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
