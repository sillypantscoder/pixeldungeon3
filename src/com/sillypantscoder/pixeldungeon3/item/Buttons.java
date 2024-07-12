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
		public static class DropItemAction extends Action {
			public Player targetPlayer;
			public Item item;
			public DropItemAction(Player target, Item item) {
				super(target);
				this.targetPlayer = target;
				this.item = item;
			}
			public void initiate() {
				targetPlayer.time += 5;
				targetPlayer.inventory.remove(item);
				targetPlayer.game.drop(item, targetPlayer.x, targetPlayer.y);
				targetPlayer.game.canContinue = true;
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
		public static class WeaponEquipAction extends Action {
			public Player targetPlayer;
			public Weapon item;
			public WeaponEquipAction(Player target, Weapon item) {
				super(target);
				this.targetPlayer = target;
				this.item = item;
			}
			public void initiate() {
				targetPlayer.time += 5;
				targetPlayer.inventory.remove(item);
				targetPlayer.weaponSlot = item;
				targetPlayer.game.canContinue = true;
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
		public static class WeaponUnEquipAction extends Action {
			public Player targetPlayer;
			public WeaponUnEquipAction(Player target) {
				super(target);
				this.targetPlayer = target;
			}
			public void initiate() {
				targetPlayer.time += 5;
				targetPlayer.inventory.add(targetPlayer.weaponSlot);
				targetPlayer.weaponSlot = null;
				targetPlayer.game.canContinue = true;
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
		public static class FoodEatAction extends Action {
			public Player targetPlayer;
			public Item item;
			public FoodEatAction(Player target, Item item) {
				super(target);
				this.targetPlayer = target;
				this.item = item;
			}
			public void initiate() {
				targetPlayer.time += 25;
				targetPlayer.inventory.remove(item);
				targetPlayer.health += 4;
				targetPlayer.game.canContinue = true;
			}
			public void onTick() {
			}
			public boolean canBeRemoved() {
				return true;
			}
		}
	}
}
