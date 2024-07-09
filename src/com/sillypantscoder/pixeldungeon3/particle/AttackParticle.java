package com.sillypantscoder.pixeldungeon3.particle;

import java.awt.Color;
import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.pixeldungeon3.utils.Utils;
import com.sillypantscoder.window.Surface;

public class AttackParticle extends Particle {
	public double cx;
	public double cy;
	public double size;
	public double vx;
	public double vy;
	public AttackParticle(int x, int y, int fromDX, int fromDY) {
		super(x, y);
		cx = 0.5;
		cy = 0.5;
		size = 0.2;
		double[] direction = Utils.normalize(fromDX, fromDY);
		vx = (direction[0] * 0.1) + ((Math.random() - 0.5) * 0.15);
		vy = (direction[1] * 0.1) + ((Math.random() - 0.5) * 0.15);
	}
	public static ArrayList<AttackParticle> createCluster(Entity attacker, Entity attackTarget) {
		ArrayList<AttackParticle> list = new ArrayList<AttackParticle>();
		for (int i = 0; i < 10; i++) {
			list.add(new AttackParticle(attackTarget.x, attackTarget.y, attackTarget.x - attacker.x, attackTarget.y - attacker.y));
		}
		return list;
	}
	public void onTick() {
		size -= 0.01;
		cx += vx;
		cy += vy;
		vx *= 0.99;
		vy *= 0.99;
	}
	public boolean canBeRemoved() {
		return size <= 0;
	}
	public void draw(Surface s) {
		int pxSize = (int)(size * Tile.TILE_SIZE);
		int centerX = (int)((x + cx) * Tile.TILE_SIZE);
		int centerY = (int)((y + cy) * Tile.TILE_SIZE);
		s.drawRect(Color.RED.darker(), centerX - pxSize, centerY - pxSize, pxSize * 2, pxSize * 2);
	}
}
