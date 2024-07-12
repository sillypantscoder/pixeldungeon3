package com.sillypantscoder.pixeldungeon3.item.type;

public class Sword extends Weapon {
	public int getTier() { return 3; }
	public int getBaseMaxDamage() { return 16; }
	public int[] getSpriteLocation() {
		return new int[] { 4, 2 };
	}
	public String getName() {
		return "Sword";
	}
	public String getWeaponDescription() {
		return "The razor-sharp length of steel blade shines reassuringly.";
	}
}
