package com.sillypantscoder.pixeldungeon3.entity;

import com.sillypantscoder.pixeldungeon3.entity.type.Player;
import com.sillypantscoder.pixeldungeon3.item.DroppedItem;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.pixeldungeon3.particle.AttackParticle;
import com.sillypantscoder.pixeldungeon3.utils.Utils;

/**
 * Represents an action that an entity can do during its turn.
 */
public abstract class Action<T extends Entity> {
	public T target;
	/**
	 * The number of ticks this action has been active.
	 */
	public int ticks;
	public Action(T target) {
		this.target = target;
		ticks = 0;
	}
	public void tick() {
		this.onTick();
		this.ticks += 1;
	}
	/**
	 * This function will be called once, after the action has been added.
	 */
	public abstract void initiate();
	/**
	 * This function will be called once per tick.
	 * Once the action has finished, set `target.game.canContinue` to true to let the game know that it can proceed with the next entity's turn.
	 */
	public abstract void onTick();
	/**
	 * Return whether this action is totally complete and may be removed.
	 */
	public abstract boolean canBeRemoved();
	// === PUBLIC ACTIONS ===
	/**
	 * The entity sleeps for 10 time.
	 */
	public static class SleepAction extends Action<Entity> {
		public SleepAction(Entity target) {
			super(target);
		}
		public void initiate() {
			target.time += 10;
			target.game.canContinue = true;
			if (target instanceof LivingEntity l) l.actor.animate("idle");
		}
		public void onTick() {
		}
		public boolean canBeRemoved() {
			return true;
		}
	}
	/**
	 * The entity moves to a new location (usually an adjacent space).
	 */
	public static class MoveAction extends Action<LivingEntity> {
		public double oldX;
		public double oldY;
		public int newX;
		public int newY;
		public int maxTime;
		public MoveAction(LivingEntity target, int newX, int newY) {
			super(target);
			this.newX = newX;
			this.newY = newY;
		}
		/**
		 * Create a MoveAction from a delta amount, rather than an absolute target location.
		 */
		public static MoveAction createFromDelta(LivingEntity target, int deltaX, int deltaY) {
			return new MoveAction(target, deltaX + target.x, deltaY + target.y);
		}
		/**
		 * Check whether it is possible to move to the new location.
		 */
		public boolean validTarget() {
			if (target.game.level.outOfBounds(newX, newY)) return false;
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
	/**
	 * The entity attacks another entity.
	 */
	public static class AttackAction extends Action<LivingEntity> {
		/**
		 * The entity that is being attacked.
		 * (The normal `target` parameter indicates the attacker.)
		 */
		public LivingEntity attackTarget;
		public int maxTime;
		public AttackAction(LivingEntity attacker, LivingEntity attackTarget) {
			super(attacker);
			this.attackTarget = attackTarget;
		}
		public void initiate() {
			target.time += 5;
			attackTarget.damage(this.target.getDamage());
			// Animation:
			target.actor.animate("action");
			maxTime = 10;
			if (this.attackTarget.x != this.target.x) target.actor.direction = this.attackTarget.x > this.target.x;
			// Particles:
			this.target.game.particles.addAll(AttackParticle.createCluster(target, attackTarget));
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
	/**
	 * The player picks up an item.
	 */
	public static class PickupAction extends Action<Player> {
		public DroppedItem item;
		public int maxTime;
		public PickupAction(Player target, DroppedItem item) {
			super(target);
			this.item = item;
		}
		public void initiate() {
			target.time += 10;
			target.inventory.add(item.item);
			item.remove();
			target.game.canContinue = true;
			// Animation:
			target.actor.animate("action");
			maxTime = 10;
		}
		public void onTick() {
		}
		public boolean canBeRemoved() {
			return ticks > maxTime;
		}
	}
}
