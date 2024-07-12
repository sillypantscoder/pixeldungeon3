package com.sillypantscoder.pixeldungeon3.entity.type;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.LivingEntity;
import com.sillypantscoder.pixeldungeon3.entity.PathfindTarget;
import com.sillypantscoder.pixeldungeon3.entity.Spritesheet;
import com.sillypantscoder.pixeldungeon3.item.DroppedItem;
import com.sillypantscoder.pixeldungeon3.item.Item;
import com.sillypantscoder.pixeldungeon3.item.type.FoodRation;
import com.sillypantscoder.pixeldungeon3.item.type.Knuckleduster;
import com.sillypantscoder.pixeldungeon3.item.type.Weapon;
import com.sillypantscoder.pixeldungeon3.level.LightStatus;
import com.sillypantscoder.pixeldungeon3.level.LinePoints;
import com.sillypantscoder.pixeldungeon3.level.Tile;

/**
 * Represents the player.
 * Most game elements (such as enemy pathfinding) work with multiple players, however the Game class contains a separate field for the main player.
 */
public class Player extends LivingEntity {
	public Action<Player> pendingAction;
	public PathfindTarget target;
	public ArrayList<Item> inventory;
	public Weapon weaponSlot;
	public Player(Game game, int x, int y) {
		super(game, x, y);
		target = null;
		inventory = new ArrayList<Item>();
		inventory.add(new FoodRation());
		this.weaponSlot = new Knuckleduster();
	}
	public void requestAction() {
		this.actor.animate("idle");
		if (this.pendingAction != null) {
			this.setAction(pendingAction);
			this.pendingAction = null;
			return;
		}
		if (this.target != null) {
			if (this.target instanceof LivingEntity targetEntity) {
				// Attack entity if close enough
				if (Math.abs(this.x - targetEntity.x) <= 1 && Math.abs(this.y - targetEntity.y) <= 1) {
					this.setAction(new Action.AttackAction(this, targetEntity));
					this.target = null;
					return;
				}
			}
			if (this.target instanceof DroppedItem item) {
				// Get item if close enough
				if (this.x == item.x && this.y == item.y) {
					this.setAction(new Action.PickupAction(this, item));
					this.target = null;
					return;
				}
			}
			// Pathfind to selected location
			this.requestPathfind(this.target.getX(), this.target.getY());
		}
	}
	public void requestPathfind(int targetX, int targetY) {
		if (this.x == targetX && this.y == targetY) {
			target = null;
			return;
		}
		int[][] path = game.level.findPath(this.x, this.y, targetX, targetY, true);
		if (path.length == 0) {
			this.target = null;
			return;
		}
		int[] nextTarget = path[1];
		this.setAction(new Action.MoveAction(this, nextTarget[0], nextTarget[1]));
	}
	public boolean hasPlainTarget() {
		if (target == null) return false;
		if (target instanceof Entity) return false;
		if (target instanceof DroppedItem) return false;
		return true;
	}
	public Spritesheet getSpritesheet() {
		return Spritesheet.read("player");
	}
	public int getMaxHealth() {
		return 20;
	}
	public int getDamage() {
		return this.weaponSlot == null ? 1 : this.weaponSlot.getDamage();
	}
	public void click(int worldX, int worldY) {
		if (target != null) {
			// Cancel
			target = null;
			return;
		}
		// Move to entity
		for (int i = 0; i < game.level.entities.size(); i++) {
			Entity e = game.level.entities.get(i);
			if (e instanceof LivingEntity l) {
				if (l.x == worldX && l.y == worldY && l != this) {
					target = l;
					return;
				}
			}
		}
		// Move to item
		for (int i = 0; i < game.level.items.size(); i++) {
			DroppedItem m = game.level.items.get(i);
			if (m.x == worldX && m.y == worldY) {
				target = m;
				return;
			}
		}
		// Move to location
		target = new PathfindTarget() {
			public int getX() { return worldX; }
			public int getY() { return worldY; }
		};
	}
	// Lighting
	public int getViewDistance() {
		return 7;
	}
	public void addLight() {
		game.level.board[x][y].lightStatus = LightStatus.Current;
		int vd = getViewDistance();
		for (int cx = -vd; cx <= vd; cx++) {
			for (int cy = -vd; cy <= vd; cy++) {
				checkForLight(cx + this.x, cy + this.y);
			}
		}
	}
	protected void checkForLight(int cx, int cy) {
		int[][] points = LinePoints.get_line(new int[] { this.x, this.y }, new int[] { cx, cy });
		for (int i = 1; i < points.length; i++) {
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
	public boolean canSee(int cx, int cy) {
		int[][] points = LinePoints.get_line(new int[] { this.x, this.y }, new int[] { cx, cy });
		for (int i = 0; i < points.length; i++) {
			if (game.level.outOfBounds(points[i])) continue;
			Tile tile = game.level.board[points[i][0]][points[i][1]];
			if (points[i][0] == cx && points[i][1] == cy) {
				return true;
			}
			if (! tile.type.canSeeThrough()) {
				return false;
			}
		}
		return false;
	}
}
