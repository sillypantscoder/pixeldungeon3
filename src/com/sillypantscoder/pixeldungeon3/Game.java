package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.type.Player;
import com.sillypantscoder.pixeldungeon3.entity.type.Rat;
import com.sillypantscoder.pixeldungeon3.item.DroppedItem;
import com.sillypantscoder.pixeldungeon3.item.Item;
import com.sillypantscoder.pixeldungeon3.item.type.Sword;
import com.sillypantscoder.pixeldungeon3.level.Level;
import com.sillypantscoder.pixeldungeon3.level.LightStatus;
import com.sillypantscoder.pixeldungeon3.level.SubdivisionLevelGeneration;
import com.sillypantscoder.pixeldungeon3.level.Tile;
import com.sillypantscoder.pixeldungeon3.particle.Particle;
import com.sillypantscoder.window.Surface;

/**
 * An object that holds all the data needed for the game, such as:
 *  - The level
 *  - The particles
 *  - The main player
 *  - Whose turn it is
 *  - A reference to the UI
 */
public class Game {
	public static final boolean DEBUG_MODE = false;
	public Level level;
	public ArrayList<Particle> particles;
	public Player player;
	public Entity turn;
	/**
	 * Whether the current action is functionally complete and other actions may be run.
	 */
	public boolean canContinue;
	public AtomicBoolean needsLightRefresh;
	public int[] recentSize;
	public int[] mousePos;
	public GameUI ui;
	public Game() {
		level = new Level(this, SubdivisionLevelGeneration.generateLevel());
		particles = new ArrayList<Particle>();
		needsLightRefresh = new AtomicBoolean(true);
		mousePos = new int[] { 100, 100 };
		ui = new GameUI(this);
		// Spawn a player
		player = spawn(Player::new);
		turn = player;
		// Spawn some rats
		for (int i = 0; i < 50; i++) spawn(Rat::new);
		// Drop a sword somewhere!
		drop(new Sword());
	}
	/**
	 * Spawn the specified entity.
	 * Usage: `spawn(EntityName::new)`
	 */
	public<T extends Entity> T spawn(Entity.EntityCreator<T> creator) {
		int[] spawn = level.getSpawnLocation();
		return spawn(creator, spawn[0], spawn[1]);
	}
	public<T extends Entity> T spawn(Entity.EntityCreator<T> creator, int x, int y) {
		AtomicReference<T> freshEntity = new AtomicReference<T>(creator.create(this, x, y));
		level.entities.add(freshEntity.get());
		return freshEntity.get();
	}
	/**
	 * Run one tick.
	 * Due to fast ticking, this can go through up to 100 turn changes.
	 */
	public void tick() {
		// Get new action from entity
		if (! canContinue) {
			/**
			 * If true, indicates that the current entity's action has completed immediately,
			 * 	and it is possible to go immediately to the next entity.
			 */
			boolean canFastSwitch = getAndInitiateActionFromCurrentEntity();
			// If we can go to the next entity...
			for (int i = 0; i < 100 && canFastSwitch; i++) {
				boolean succeeded = goToNextEntityIfPossible();
				if (! succeeded) break;
				// ...switch and restart the tick.
				canFastSwitch = getAndInitiateActionFromCurrentEntity();
			}
		}
		// Tick the world
		tickAllEntities();
		goToNextEntityIfPossible();
		tickParticlesAndItems();
	}
	/**
	 * Request (and initiate) an action from the current entity.
	 * @return true if the action completed immediately.
	 */
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
	public void tickParticlesAndItems() {
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			boolean canBeRemoved = p.tick();
			if (canBeRemoved) {
				particles.remove(p);
				i -= 1;
			}
		}
		for (int i = 0; i < level.items.size(); i++) {
			DroppedItem m = level.items.get(i);
			m.tick();
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
	/**
	 * Create a DroppedItem in the level.
	 */
	public void drop(Item item, int x, int y) {
		this.level.items.add(new DroppedItem(level, item, x, y));
	}
	public void drop(Item item) {
		int[] spawn = level.getSpawnLocation();
		drop(item, spawn[0], spawn[1]);
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
			player.addLight();
			needsLightRefresh.set(false);
		}
		// Update recent size and create surface
		recentSize = new int[] { width, height };
		Surface s = new Surface(width, height, Color.BLACK);
		// Render the world
		int[] cameraPos = getCameraPos(width, height);
		s.blit(renderWorld(), -cameraPos[0], -cameraPos[1]);
		// Draw the debug data
		if (DEBUG_MODE) {
			int mouseX = (int)((double)(mousePos[0] + cameraPos[0]) / Tile.TILE_SIZE);
			int mouseY = (int)((double)(mousePos[1] + cameraPos[1]) / Tile.TILE_SIZE);
			String text = "X: " + mouseX + " Y: " + mouseY;
			if (! level.outOfBounds(mouseX, mouseY)) {
				Tile t = level.get_at(mouseX, mouseY);
				s.drawRect(Color.RED, (mouseX * Tile.TILE_SIZE) - cameraPos[0], (mouseY * Tile.TILE_SIZE) - cameraPos[1], Tile.TILE_SIZE, Tile.TILE_SIZE, 1);
				text += "\n\nTile\nType: " + t.type.name() +
					"\nLight: " + t.lightStatus.name();
			}
			Entity e = level.getEntity(mouseX, mouseY);
			if (e != null) {
				text += "\n\nEntity\nHealth: " + e.health + "/" + e.maxHealth;
				if (e instanceof Rat rat) {
					text += "\nStatus: " + rat.state.name();
				}
				text += "\nTime: " + e.time;
				if (e instanceof Player player) {
					text += "\n\nInventory:";
					for (Item m : player.inventory) {
						text += "\n- " + m.getName();
					}
				}
			}
			DroppedItem m = level.getItem(mouseX, mouseY);
			if (m != null) {
				text += "\n\nItem\nName: " + m.item.getName();
			}
			s.blit(Surface.renderMultilineText(15, text, Color.RED), 0, 0);
		}
		// Add UI
		s.blit(ui.render(width, height), 0, 0);
		// Return
		return s;
	}
	public Surface renderWorld() {
		Surface s = new Surface(level.getWidth() * Tile.TILE_SIZE, level.getHeight() * Tile.TILE_SIZE, Color.BLACK);
		// 1. Draw the level
		level.draw(s);
		// 2. Draw the items
		for (int i = 0; i < level.items.size(); i++) {
			DroppedItem m = level.items.get(i);
			if (m.getTile().lightStatus == LightStatus.Current) {
				m.draw(s);
			}
		}
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
		return time + 1;
	}
	public void click(int x, int y) {
		if (ui.click(x, y)) return;
		int[] camera = getCameraPos(recentSize[0], recentSize[1]);
		int pixelXWithCamera = x + camera[0];
		int pixelYWithCamera = y + camera[1];
		int worldX = pixelXWithCamera / Tile.TILE_SIZE;
		int worldY = pixelYWithCamera / Tile.TILE_SIZE;
		player.click(worldX, worldY);
	}
}
