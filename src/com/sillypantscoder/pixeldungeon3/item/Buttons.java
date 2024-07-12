package com.sillypantscoder.pixeldungeon3.item;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.type.Player;
import com.sillypantscoder.pixeldungeon3.item.Item.Button;
import com.sillypantscoder.pixeldungeon3.item.type.Weapon;

public class Buttons {
	public static class DropItemButton implements Button {
		public String getName() { return "Drop"; }
		public void execute(Game game, Player player, Item item) {
			player.pendingAction = new DropItemAction(player, item);
		}
		public static class DropItemAction extends Action<Player> {
			public Item item;
			public DropItemAction(Player target, Item item) {
				super(target);
				this.item = item;
			}
			public void initiate() {
				target.time += 5;
				target.inventory.remove(item);
				target.game.drop(item, target.x, target.y);
				target.game.canContinue = true;
			}
			public void onTick() {
			}
			public boolean canBeRemoved() {
				return true;
			}
		}
	}
	public static class WeaponEquipButton implements Button {
		public String getName() { return "Equip"; }
		public void execute(Game game, Player player, Item item) {
			if (item instanceof Weapon w) {
				player.pendingAction = new WeaponEquipAction(player, w);
			}
		}
		public static class WeaponEquipAction extends Action<Player> {
			public Weapon item;
			public WeaponEquipAction(Player target, Weapon item) {
				super(target);
				this.item = item;
			}
			public void initiate() {
				if (target.weaponSlot == null) {
					target.time += 5;
					target.inventory.remove(item);
					target.weaponSlot = item;
					target.game.canContinue = true;
				}
			}
			public void onTick() {
			}
			public boolean canBeRemoved() {
				return true;
			}
		}
	}
	public static class WeaponUnEquipButton implements Button {
		public String getName() { return "Unequip"; }
		public void execute(Game game, Player player, Item item) {
			player.pendingAction = new WeaponUnEquipAction(player);
		}
		public static class WeaponUnEquipAction extends Action<Player> {
			public WeaponUnEquipAction(Player target) {
				super(target);
			}
			public void initiate() {
				target.time += 5;
				target.inventory.add(target.weaponSlot);
				target.weaponSlot = null;
				target.game.canContinue = true;
			}
			public void onTick() {
			}
			public boolean canBeRemoved() {
				return true;
			}
		}
	}
	public static class FoodEatButton implements Button {
		public String getName() { return "Eat"; }
		public void execute(Game game, Player player, Item item) {
			player.pendingAction = new FoodEatAction(player, item);
		}
		public static class FoodEatAction extends Action<Player> {
			public Item item;
			public FoodEatAction(Player target, Item item) {
				super(target);
				this.item = item;
			}
			public void initiate() {
				target.time += 25;
				target.inventory.remove(item);
				target.health += 4;
				target.game.canContinue = true;
			}
			public void onTick() {
			}
			public boolean canBeRemoved() {
				return true;
			}
		}
	}
}
