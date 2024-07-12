package com.sillypantscoder.pixeldungeon3.item.type;

import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.item.Buttons;
import com.sillypantscoder.pixeldungeon3.item.Item;

public abstract class Weapon extends Item {
	public abstract int getTier();
	public abstract int getBaseMaxDamage();
	public int getDamage() {
		return Random.randint(getTier(), getBaseMaxDamage());
	}
	public int averageDamage() {
		return (getTier() + getBaseMaxDamage()) / 2;
	}
	public abstract String getWeaponDescription();
	public String getDescription() {
		return getWeaponDescription() + "\nThis " + getName().toLowerCase() + " is a tier-" + getTier() + " melee weapon. Its average damage is " + averageDamage() + " points per hit.";
	}
	public Item.Button[] getButtons() { return this.getButtons(false); }
	public Button[] getButtons(boolean equipped) {
		if (equipped) {
			return new Button[] {
				new Buttons.WeaponUnEquipButton()
			};
		}
		return new Button[] {
			new Buttons.DropItemButton(),
			new Buttons.WeaponEquipButton()
		};
	}
}
