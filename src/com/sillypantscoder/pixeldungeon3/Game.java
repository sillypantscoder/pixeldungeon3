package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;

import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.level.Level;
import com.sillypantscoder.pixeldungeon3.level.SubdivisionLevelGeneration;
import com.sillypantscoder.window.Surface;

public class Game {
	public Level level;
	public Entity player;
	public Entity turn;
	public Game() {
		level = SubdivisionLevelGeneration.generateLevel();
		int[] spawn = level.getSpawnLocation();
		Entity player = new Entity(this, spawn[0], spawn[1]) {
			public void requestAction() {
			}
		};
		level.entities.add(player);
		turn = player;
		this.player = player;
	}
	public void tick() {
		// 1. Handle clicks
		//		TODO: handle clicks
		// 2. Request an action
		turn.requestAction();
		// 3. Initiate the action
		turn.action.ifPresent((a) -> a.initiate());
		// 4. For each entity:
		for (int i = 0; i < level.entities.size(); i++) {
			// 4a. Tick the action
			level.entities.get(i).action.ifPresent((a) -> a.tick());
			// 4b. Tick the actor
			// 		TODO: add actors
		}
		// 5. Check if we can go to the next entity yet
		// 		TODO: go to next entity
	}
	public Surface render(int width, int height) {
		Surface s = new Surface(width, height, Color.BLACK);
		// 1. Draw the level
		// 		TODO: draw the level
		// 2. Draw the items
		// 		TODO: add items
		// 3. Draw the actors
		// 		TODO: add actors
		// 4. Draw the particles
		// 		TODO: add particles
		// Finish
		return s;
	}
}
