package com.sillypantscoder.pixeldungeon3.particle;

import com.sillypantscoder.pixeldungeon3.entity.Actor;
import com.sillypantscoder.pixeldungeon3.entity.LivingEntity;
import com.sillypantscoder.window.Surface;

/**
 * A particle that plays the death animation for a specific entity.
 * This object keeps a reference to the dead entity's actor.
 */
public class DeathParticle extends Particle {
	public Actor actor;
	public int maxTicks;
	public DeathParticle(LivingEntity target) {
		super(target.x, target.y);
		this.actor = target.actor;
		this.actor.animate("death");
		int surfaces = this.actor.sheet.entries.get("death").surfaces.length;
		maxTicks = (surfaces * 6) - 1;
	}
	public void onTick() {
		int animationTicksLeft = maxTicks - ticks;
		if (animationTicksLeft > 0) {
			fixSprite();
			actor.tick();
		}
	}
	public boolean canBeRemoved() {
		int animationTicksLeft = maxTicks - ticks;
		int fadeTicksLeft = animationTicksLeft + 80;
		return fadeTicksLeft <= 0;
	}
	public void draw(Surface s) {
		fixSprite();
		int animationTicksLeft = maxTicks - ticks;
		if (animationTicksLeft > 0) {
			actor.draw(s);
		} else {
			int fadeTicksLeft = animationTicksLeft + 80;
			actor.draw(s, fadeTicksLeft / 80f);
		}
	}
	public void fixSprite() {
		if (actor.animationName != "death") {
			actor.animationName = "death";
			actor.animationPos = maxTicks - 1;
		}
	}
}
