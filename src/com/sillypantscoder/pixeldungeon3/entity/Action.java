package com.sillypantscoder.pixeldungeon3.entity;

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
			// TODO: un-animate moving
		}
		public void onTick() {
		}
		public boolean canBeRemoved() {
			return true;
		}
	}
	public static class MoveAction extends Action {
		public int newX;
		public int newY;
		public MoveAction(Entity target, int newX, int newY) {
			super(target);
			this.newX = newX;
			this.newY = newY;
		}
		public static MoveAction createFromDelta(Entity target, int deltaX, int deltaY) {
			return new MoveAction(target, deltaX + target.x, deltaY + target.y);
		}
		public void initiate() {
			// TODO: check if we can actually move here
			target.x = newX;
			target.y = newY;
			target.time += 1;
			// TODO: animate moving
			target.game.canContinue = true;
			target.actor.x = newX;
			target.actor.y = newY;
		}
		public void onTick() {
		}
		public boolean canBeRemoved() {
			return true;
		}
	}
}
