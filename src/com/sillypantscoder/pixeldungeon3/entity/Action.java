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
	 * Return whether this action is functionally complete and other actions may be run.
	 */
	public abstract boolean canContinue();
	/**
	 * Return whether this action is totally complete and may be removed.
	 * @return
	 */
	public abstract boolean canBeRemoved();
	public static class MoveAction extends Action {
		public int newX;
		public int newY;
		public MoveAction(Entity target, int deltaX, int deltaY) {
			super(target);
			this.newX = deltaX + target.x;
			this.newY = deltaY + target.y;
		}
		public void initiate() {
			// TODO: check if we can actually move here
			target.x = newX;
			target.y = newY;
			// TODO: animate moving
			target.actor.x = newX;
			target.actor.y = newY;
		}
		public void onTick() {
		}
		public boolean canContinue() {
			return true;
		}
		public boolean canBeRemoved() {
			return true;
		}
	}
}
