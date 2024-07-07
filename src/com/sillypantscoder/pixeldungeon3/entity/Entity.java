package com.sillypantscoder.pixeldungeon3.entity;

import java.awt.Color;
import java.util.Optional;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.window.Surface;

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
		this.action = Optional.empty();
		this.actor = new Actor(x, y, new Surface(Tile.TILE_SIZE, Tile.TILE_SIZE, Color.ORANGE));
	}
	public abstract void requestAction();
}
