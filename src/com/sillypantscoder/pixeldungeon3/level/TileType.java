package com.sillypantscoder.pixeldungeon3.level;

import java.util.Arrays;

/**
 * A type of tile in the level.
 */
public enum TileType {
	Chasm,
	Ground,
	Wall,
	Door;
	public boolean walkable() {
		return Arrays.stream(new TileType[] {
			TileType.Ground,
			TileType.Door
		}).anyMatch(this::equals);
	}
	public boolean canSeeThrough() {
		return Arrays.stream(new TileType[] {
			TileType.Ground,
			TileType.Chasm
		}).anyMatch(this::equals);
	}
}
