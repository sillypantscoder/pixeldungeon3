package com.sillypantscoder.pixeldungeon3.ui;

import java.awt.Color;
import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.GameUI;
import com.sillypantscoder.pixeldungeon3.item.Item;
import com.sillypantscoder.window.Surface;

public class InventoryItemElement extends UIElement {
	public Item item;
	public InventoryItemElement(Item item) {
		this.item = item;
	}
	public Surface render(int maxWidth, int maxHeight) {
		Surface r = new Surface(20 * GameUI.UI_SCALE, 20 * GameUI.UI_SCALE, Color.GRAY.darker());
		int padding = 4;
		r.drawRect(Color.GRAY, padding, padding, r.get_width() - (padding * 2), r.get_height() - (padding * 2));
		// Draw item
		Surface i = this.item.image.scale_size(GameUI.UI_SCALE);
		int drawX = (r.get_width() / 2) - (i.get_width() / 2);
		int drawY = (r.get_height() / 2) - (i.get_height() / 2);
		r.blit(i, drawX, drawY);
		// Finish
		return r;
	}
	public UIElement elementAtPoint(int maxWidth, int maxHeight, int x, int y) {
		return this;
	}
	public static InventoryItemElement[] fromInventory(ArrayList<Item> inventory) {
		InventoryItemElement[] elms = new InventoryItemElement[inventory.size()];
		for (int i = 0; i < inventory.size(); i++) {
			elms[i] = new InventoryItemElement(inventory.get(i));
		}
		return elms;
	}
}