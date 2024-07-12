package com.sillypantscoder.pixeldungeon3.level;

import java.util.Arrays;

/**
 * Indicates whether a certain tile is lit up or not.
 */
public enum LightStatus {
	Unknown,
	Memory,
	Current;
	public boolean canSeeEntities() {
		return Arrays.stream(new LightStatus[] {
			LightStatus.Current
		}).anyMatch(this::equals);
	}
}
