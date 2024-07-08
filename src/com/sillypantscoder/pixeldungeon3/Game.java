package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;

import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.type.Player;
import com.sillypantscoder.pixeldungeon3.level.Level;
import com.sillypantscoder.pixeldungeon3.level.SubdivisionLevelGeneration;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.window.Surface;

public class Game {
	public Level level;
	public Player player;
	public Entity turn;
	public Game() {
		level = SubdivisionLevelGeneration.generateLevel();
		int[] spawn = level.getSpawnLocation();
		player = new Player(this, spawn[0], spawn[1]);
		level.entities.add(player);
		turn = player;
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
			level.entities.get(i).actor.tick();
		}
		// 5. Check if we can go to the next entity yet
		// 		TODO: go to next entity
	}
	public Surface render(int width, int height) {
		Surface s = new Surface(width, height, Color.BLACK);
		s.blit(renderWorld(), 0, 0);
		// TODO: camera movement
		return s;
	}
	public Surface renderWorld() {
		Surface s = new Surface(level.getWidth() * Tile.TILE_SIZE, level.getHeight() * Tile.TILE_SIZE, Color.BLACK);
		// 1. Draw the level
		level.draw(s);
		// 2. Draw the items
		// 		TODO: add items
		// 3. Draw the actors
		for (int i = 0; i < level.entities.size(); i++) {
			level.entities.get(i).actor.draw(s);
		}
		// 4. Draw the particles
		// 		TODO: add particles
		// Finish
		return s;
	}
}
