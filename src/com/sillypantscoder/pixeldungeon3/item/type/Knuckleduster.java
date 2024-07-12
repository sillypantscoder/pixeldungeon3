package com.sillypantscoder.pixeldungeon3.item.type;

public class Knuckleduster extends Weapon {
	public int getTier() { return 1; }
	public int getBaseMaxDamage() { return 5; }
	public int[] getSpriteLocation() {
		return new int[] { 0, 2 };
	}
	public String getName() {
		return "Knuckleduster";
	}
	public String getWeaponDescription() {
		return "A piece of iron shaped to fit around the knuckles.";
	}
}
