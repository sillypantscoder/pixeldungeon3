package com.sillypantscoder.pixeldungeon3.particle;

import com.sillypantscoder.window.Surface;

public abstract class Particle {
	public int x;
	public int y;
	public int ticks;
	public Particle(int x, int y) {
		this.x = x;
		this.y = y;
		this.ticks = 0;
	}
	public boolean tick() {
		this.onTick();
		this.ticks += 1;
		return canBeRemoved();
	}
	public abstract void onTick();
	public abstract boolean canBeRemoved();
	public abstract void draw(Surface s);
}
