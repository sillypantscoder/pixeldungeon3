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
}
