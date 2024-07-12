package com.sillypantscoder.pixeldungeon3.item.type;

import com.sillypantscoder.pixeldungeon3.item.Item;

/**
 * This item is intended to be a ration of food,
 * but right now it doesn't do anything.
 */
public class FoodRation extends Item {
	public int[] getSpriteLocation() {
		return new int[] { 4, 0 };
	}
	public String getName() {
		return "Ration of food";
	}
}
