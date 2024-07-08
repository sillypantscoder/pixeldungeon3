package com.sillypantscoder.pixeldungeon3.entity;

import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.pixeldungeon3.utils.Utils;

public abstract class Action {
	public int ticks;
	public Entity target;
	public Action(Entity target) {
		ticks = 0;
		this.target = target;
	}
	public void tick() {
		this.onTick();
		this.ticks += 1;
	}
	public abstract void initiate();
	public abstract void onTick();
	/**
	 * Return whether this action is totally complete and may be removed.
	 * @return
	 */
	public abstract boolean canBeRemoved();
	public static class SleepAction extends Action {
		public SleepAction(Entity target) {
			super(target);
		}
		public void initiate() {
			target.time += 1;
			target.game.canContinue = true;
			target.actor.animate("idle");
		}
		public void onTick() {
		}
		public boolean canBeRemoved() {
			return true;
		}
	}
	public static class MoveAction extends Action {
		public double oldX;
		public double oldY;
		public int newX;
		public int newY;
		public int maxTime;
		public MoveAction(Entity target, int newX, int newY) {
			super(target);
			this.newX = newX;
			this.newY = newY;
		}
		public static MoveAction createFromDelta(Entity target, int deltaX, int deltaY) {
			return new MoveAction(target, deltaX + target.x, deltaY + target.y);
		}
		public boolean validTarget() {
			Tile targetTile = target.game.level.get_at(newX, newY);
			return targetTile.type.walkable();
		}
		public void initiate() {
			if (! validTarget()) return;
			this.oldX = target.actor.x;
			this.oldY = target.actor.y;
			target.x = newX;
			target.y = newY;
			target.time += 10;
			// Animation:
			maxTime = 7;
			target.game.canContinue = true;
			target.actor.animate("move");
			if (this.newX != this.oldX) target.actor.direction = this.newX > this.oldX;
		}
		public void onTick() {
			double frac = ticks / (double)(maxTime);
			double x = Utils.map(frac, 0, 1, this.oldX, this.newX);
			double y = Utils.map(frac, 0, 1, this.oldY, this.newY);
			target.actor.x = x;
			target.actor.y = y;
			// Finish
			if (ticks >= maxTime) {
				target.actor.x = newX;
				target.actor.y = newY;
			}
		}
		public boolean canBeRemoved() {
			if (! validTarget()) return true;
			return ticks > maxTime;
		}
	}
	public static class AttackAction extends Action {
		public Entity attackTarget;
		public int maxTime;
		public AttackAction(Entity attacker, Entity attackTarget) {
			super(attacker);
			this.attackTarget = attackTarget;
		}
		public void initiate() {
			target.time += 5;
			System.out.println(attackTarget.health);
			attackTarget.health -= this.target.getDamage();
			System.out.println(attackTarget.health);
			if (attackTarget.health <= 0) attackTarget.die();
			// Animation:
			target.actor.animate("action");
			maxTime = target.actor.sheet.entries.get("action").surfaces.length;
			if (this.attackTarget.x != this.target.x) target.actor.direction = this.attackTarget.x > this.target.x;
		}
		public void onTick() {
			// Finish
			if (ticks >= maxTime) {
				target.game.canContinue = true;
			}
		}
		public boolean canBeRemoved() {
			return ticks > maxTime;
		}
	}
}
