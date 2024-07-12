package com.sillypantscoder.pixeldungeon3.item.type;

import com.sillypantscoder.pixeldungeon3.item.Buttons;
import com.sillypantscoder.pixeldungeon3.item.Item;

public class FoodRation extends Item {
	public int[] getSpriteLocation() {
		return new int[] { 4, 0 };
	}
	public String getName() {
		return "Ration of food";
	}
	public String getDescription() {
		return "Nothing fancy here: dried meat, some biscuits - things like that.";
	}
	public Item.Button[] getButtons() {
		return new Button[] {
			new Buttons.DropItemButton(),
			new Buttons.FoodEatButton()
		};
	}
}
