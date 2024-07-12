package com.sillypantscoder.pixeldungeon3.entity;

import java.util.Optional;

import com.sillypantscoder.pixeldungeon3.Game;

/**
 * An entity in the world.
 * This is the functionality half of the "Entity/Actor" pair.
 */
public abstract class Entity {
	public Game game;
	public int time;
	public Optional<Action<?>> action;
	public Entity(Game game) {
		this.game = game;
		this.time = game.getSpawnTime();
		this.action = Optional.empty();
	}
	public void setAction(Action<?> action) {
		this.action = Optional.ofNullable(action);
	}
	/**
	 * Request that an action be set for this entity.
	 */
	public abstract void requestAction();
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
	public void draw() {}
}
