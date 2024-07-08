package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.type.Player;
import com.sillypantscoder.pixeldungeon3.entity.type.Rat;
import com.sillypantscoder.pixeldungeon3.level.Level;
import com.sillypantscoder.pixeldungeon3.level.LightStatus;
import com.sillypantscoder.pixeldungeon3.level.SubdivisionLevelGeneration;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.window.Surface;

public class Game {
	public Level level;
	public Player player;
	public Entity turn;
	/**
	 * Whether the current action is functionally complete and other actions may be run.
	 */
	public boolean canContinue;
	public Game() {
		level = SubdivisionLevelGeneration.generateLevel();
		// Spawn a player
		player = spawn(Player::new);
		turn = player;
		// Spawn a rat
		spawn(Rat::new);
		// Update light
		level.updateLight();
	}
	public<T extends Entity> T spawn(Entity.EntityCreator<T> creator) {
		int[] spawn = level.getSpawnLocation();
		AtomicReference<T> freshEntity = new AtomicReference<T>(creator.create(this, spawn[0], spawn[1]));
		level.entities.add(freshEntity.get());
		return freshEntity.get();
	}
	public void tick() {
		AtomicBoolean needsLightRefresh = new AtomicBoolean();
		// 1. Handle clicks
		//		TODO: handle clicks
		// 2. Get an action if possible
		if (! turn.action.isPresent()) {
			// 2a. Request an action
			turn.requestAction();
			// 2b. Initiate the action
			turn.action.ifPresent((a) -> {
				a.initiate();
				turn.action.ifPresent((b) -> needsLightRefresh.set(true));
			});
		}
		// 3. For each entity:
		for (int i = 0; i < level.entities.size(); i++) {
			// 3a. Tick the action
			level.entities.get(i).action.ifPresent((a) -> {
				a.tick();
				needsLightRefresh.set(true);
			});
			// 3b. Check whether the action can be removed
			if (level.entities.get(i).action.map((a) -> a.canBeRemoved()).orElse(false)) {
				level.entities.get(i).action = Optional.empty();
			}
			// 3c. Tick the actor
			level.entities.get(i).actor.tick();
		}
		// 4. Check if we can go to the next entity yet
		// 4a. Make sure this action can continue
		if (canContinue) {
			// 4b. Make sure the new entity does not have an action at all
			Entity next = getNextEntity();
			if (! next.action.isPresent()) {
				// 4c. Switch!
				turn = next;
				canContinue = false;
			}
		}
		// 5. Update light, if necessary
		if (needsLightRefresh.get()) {
			this.level.updateLight();
		}
	}
	public Entity getNextEntity() {
		Entity nextEntity = null;
		int lowestTime = Integer.MAX_VALUE;
		for (Entity entity : level.entities) {
			if (entity.time < lowestTime) {
				lowestTime = entity.time;
				nextEntity = entity;
			}
		}
		return nextEntity;
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
			Entity e = level.entities.get(i);
			if (e.getTile().lightStatus == LightStatus.Current) {
				level.entities.get(i).actor.draw(s);
			}
		}
		// 4. Draw the particles
		// 		TODO: add particles
		// Finish
		return s;
	}
	public int getSpawnTime() {
		int time = 0;
		for (int i = 0; i < level.entities.size(); i++) {
			time = Math.max(time, level.entities.get(i).time);
		}
		return time + 5;
	}
	public void click(int x, int y) {
		if (this.turn instanceof Player turnPlayer) {
			int worldX = x / Tile.TILE_SIZE;
			int worldY = y / Tile.TILE_SIZE;
			turnPlayer.click(worldX, worldY);
		}
	}
}
