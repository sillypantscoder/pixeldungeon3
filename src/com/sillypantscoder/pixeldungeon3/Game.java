package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;
import java.util.ArrayList;
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
import com.sillypantscoder.pixeldungeon3.particle.Particle;
import com.sillypantscoder.window.Surface;

public class Game {
	public Level level;
	public ArrayList<Particle> particles;
	public Player player;
	public Entity turn;
	/**
	 * Whether the current action is functionally complete and other actions may be run.
	 */
	public boolean canContinue;
	public int timeLeft;
	public AtomicBoolean needsLightRefresh;
	public int[] recentSize;
	public Game() {
		level = new Level(this, SubdivisionLevelGeneration.generateLevel());
		particles = new ArrayList<Particle>();
		needsLightRefresh = new AtomicBoolean(true);
		// Spawn a player
		player = spawn(Player::new);
		turn = player;
		// Spawn some rats
		for (int i = 0; i < 50; i++) spawn(Rat::new);
	}
	public<T extends Entity> T spawn(Entity.EntityCreator<T> creator) {
		int[] spawn = level.getSpawnLocation();
		return spawn(creator, spawn[0], spawn[1]);
	}
	public<T extends Entity> T spawn(Entity.EntityCreator<T> creator, int x, int y) {
		AtomicReference<T> freshEntity = new AtomicReference<T>(creator.create(this, x, y));
		level.entities.add(freshEntity.get());
		return freshEntity.get();
	}
	public void tick() {
		/**
		 * If true, indicates that the current entity's action has completed immediately,
		 * 	and it is possible to go immediately to the next entity.
		 */
		boolean canFastSwitch = getAndInitiateActionFromCurrentEntity();
		// If we can go to the next entity...
		for (int i = 0; i < 100 && canFastSwitch && goToNextEntityIfPossible(); i++) {
			// ...switch and restart the tick.
			canFastSwitch = getAndInitiateActionFromCurrentEntity();
		}
		tickAllEntities();
		goToNextEntityIfPossible();
		// Then tick the particles:
		tickAllParticles();
	}
	public boolean getAndInitiateActionFromCurrentEntity() {
		AtomicBoolean canFastSwitch = new AtomicBoolean();
		// If we already have an action, we don't need to get a new one
		if (! turn.action.isPresent()) {
			// 1a. Request an action
			turn.requestAction();
			// 1b. Initiate the action
			turn.action.ifPresent((a) -> {
				a.initiate();
				needsLightRefresh.set(true);
				// 1c. Check whether the action can immediately be removed
				if (a.canBeRemoved()) {
					turn.action = Optional.empty();
					canFastSwitch.set(true);
				}
			});
		}
		return canFastSwitch.get();
	}
	public void tickAllEntities() {
		// For each entity...
		for (int i = 0; i < level.entities.size(); i++) {
			// 2a. Tick the action
			level.entities.get(i).action.ifPresent((a) -> {
				a.tick();
				needsLightRefresh.set(true);
			});
			// 2b. Check whether the action can be removed
			if (level.entities.get(i).action.map((a) -> a.canBeRemoved()).orElse(false)) {
				level.entities.get(i).action = Optional.empty();
			}
			// 2c. Tick the actor
			level.entities.get(i).actor.tick();
		}
	}
	public boolean goToNextEntityIfPossible() {
		if (canContinue) {
			// 3b. Make sure the new entity does not have an action at all
			Entity next = getNextEntity();
			if (! next.action.isPresent()) {
				// 3c. Switch!
				turn = next;
				canContinue = false;
				return true;
			}
		}
		return false;
	}
	public void tickAllParticles() {
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			boolean canBeRemoved = p.tick();
			if (canBeRemoved) {
				particles.remove(p);
				i -= 1;
			}
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
	public int[] getCameraPos(int width, int height) {
		double tileWidth = width / (double)(Tile.TILE_SIZE);
		double tileHeight = height / (double)(Tile.TILE_SIZE);
		double tileCamX = (player.actor.x + 0.5) - (tileWidth / 2d);
		double tileCamY = (player.actor.y + 0.5) - (tileHeight / 2d);
		return new int[] {
			(int)(tileCamX * Tile.TILE_SIZE),
			(int)(tileCamY * Tile.TILE_SIZE)
		};
	}
	public Surface render(int width, int height) {
		// Update light, if necessary
		if (needsLightRefresh.get()) {
			this.level.updateLight();
			needsLightRefresh.set(false);
		}
		// Update recent size and create surface
		recentSize = new int[] { width, height };
		Surface s = new Surface(width, height, Color.BLACK);
		// Render the world
		int[] cameraPos = getCameraPos(width, height);
		s.blit(renderWorld(), -cameraPos[0], -cameraPos[1]);
		// Return
		return s;
	}
	public Surface renderWorld() {
		Surface s = new Surface(level.getWidth() * Tile.TILE_SIZE, level.getHeight() * Tile.TILE_SIZE, Color.BLACK);
		// 1. Draw the level
		level.draw(s);
		// 2. Draw the items
		// 		TODO: add items
		// 3. Draw the entities
		for (int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if (e.getTile().lightStatus == LightStatus.Current) {
				level.entities.get(i).draw(s);
			}
		}
		// 4. Draw the particles
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			p.draw(s);
		}
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
			int[] camera = getCameraPos(recentSize[0], recentSize[1]);
			int pixelXWithCamera = x + camera[0];
			int pixelYWithCamera = y + camera[1];
			int worldX = pixelXWithCamera / Tile.TILE_SIZE;
			int worldY = pixelYWithCamera / Tile.TILE_SIZE;
			turnPlayer.click(worldX, worldY);
		}
	}
}
