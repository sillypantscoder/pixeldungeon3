package com.sillypantscoder.pixeldungeon3.entity;

import java.awt.Color;
import java.util.Optional;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.pixeldungeon3.particle.DeathParticle;
import com.sillypantscoder.window.Surface;

/**
 * An entity in the world.
 * This is the functionality half of the "Entity/Actor" pair.
 */
public abstract class Entity implements PathfindTarget {
	public Game game;
	public int x;
	public int y;
	public int time;
	public Optional<Action> action;
	public Actor actor;
	public int health;
	public int maxHealth;
	public Entity(Game game, int x, int y) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.time = game.getSpawnTime();
		this.action = Optional.empty();
		this.actor = new Actor(x, y, getSpritesheet());
		this.maxHealth = getMaxHealth();
		this.health = this.maxHealth;
	}
	public int getX() { return x; }
	public int getY() { return y; }
	public void setAction(Action action) {
		this.action = Optional.ofNullable(action);
	}
	/**
	 * Request that an action be set for this entity.
	 */
	public abstract void requestAction();
	public abstract Spritesheet getSpritesheet();
	public abstract int getMaxHealth();
	public abstract int getDamage();
	/**
	 * A function that can be used to create an entity.
	 */
	@FunctionalInterface
	public static interface EntityCreator<T extends Entity> {
		public T create(Game game, int x, int y);
	}
	/**
	 * Get the tile at this entity's location.
	 */
	public Tile getTile() {
		return game.level.get_at(this.x, this.y);
	}
	public void damage(int amount) {
		this.health -= amount;
		this.actor.damage("" + amount);
		if (this.health <= 0) this.die();
	}
	/**
	 * Kill the player.
	 * Removes the entity from the game and spawns the death animation.
	 */
	public void die() {
		this.remove();
		game.particles.add(new DeathParticle(this));
	}
	/**
	 * Return whether this entity is alive.
	 */
	public boolean alive() {
		return game.level.entities.contains(this);
	}
	/**
	 * Removes the entity from the game.
	 */
	public void remove() {
		game.level.entities.remove(this);
	}
	/**
	 * Draw this entity to the screen.
	 * @param s The surface to draw on.
	 */
	public void draw(Surface s) {
		// Draw actor
		this.actor.draw(s);
		// Draw health bar
		drawHealthBar(s);
	}
	public void drawHealthBar(Surface s) {
		if (this.health >= this.maxHealth) return;
		// omg so much casting
		int barWidth = (int)(Tile.TILE_SIZE * 1.3d);
		int barHeight = (int)(Tile.TILE_SIZE * 0.3d);
		int thisCenterX = (int)((this.actor.x + 0.5d) * Tile.TILE_SIZE);
		int thisTopY = (int)(this.actor.y * Tile.TILE_SIZE);
		int rectX = (int)(thisCenterX - (barWidth / 2d));
		int rectY = (int)(thisTopY - barHeight);
		s.drawRect(Color.RED, rectX, rectY, barWidth, barHeight);
		int greenWidth = (int)(barWidth * ((double)(this.health) / this.maxHealth));
		s.drawRect(Color.GREEN, rectX, rectY, greenWidth, barHeight);
	}
}
