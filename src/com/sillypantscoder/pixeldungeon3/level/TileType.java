package com.sillypantscoder.pixeldungeon3.level;

import java.util.Arrays;

public enum TileType {
	Chasm,
	Ground,
	Wall;
	public boolean walkable() {
		return Arrays.stream(new TileType[] {
			TileType.Ground
		}).anyMatch(this::equals);
	}
	public boolean canSeeThrough() {
		return Arrays.stream(new TileType[] {
			TileType.Ground,
			TileType.Chasm
		}).anyMatch(this::equals);
	}
}
