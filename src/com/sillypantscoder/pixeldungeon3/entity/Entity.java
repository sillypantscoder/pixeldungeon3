package com.sillypantscoder.pixeldungeon3.entity;

import java.util.Optional;

import com.sillypantscoder.pixeldungeon3.Game;

public abstract class Entity {
	public Game game;
	public int x;
	public int y;
	public int time;
	public Optional<Action> action;
	public Entity(Game game, int x, int y) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.action = Optional.empty();
	}
	public abstract void requestAction();
}
