package com.sillypantscoder.pixeldungeon3.entity;

import java.util.Optional;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.level.Tile;

public abstract class Entity {
	public Game game;
	public int x;
	public int y;
	public int time;
	public Optional<Action> action;
	public Actor actor;
	public Entity(Game game, int x, int y) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.time = game.getSpawnTime();
		this.action = Optional.empty();
		this.actor = new Actor(x, y, getSpritesheet());
	}
	public void setAction(Action action) {
		this.action = Optional.ofNullable(action);
	}
	public abstract void requestAction();
	public abstract Spritesheet getSpritesheet();
	@FunctionalInterface
	public static interface EntityCreator<T extends Entity> {
		public T create(Game game, int x, int y);
	}
	public Tile getTile() {
		return game.level.get_at(this.x, this.y);
	}
}
