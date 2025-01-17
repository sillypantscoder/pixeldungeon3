package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;
import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.item.Item;
import com.sillypantscoder.pixeldungeon3.item.type.Weapon;
import com.sillypantscoder.pixeldungeon3.ui.AbsCombine;
import com.sillypantscoder.pixeldungeon3.ui.AlignedElement;
import com.sillypantscoder.pixeldungeon3.ui.ChromeBorder;
import com.sillypantscoder.pixeldungeon3.ui.DataContainer;
import com.sillypantscoder.pixeldungeon3.ui.HzCombine;
import com.sillypantscoder.pixeldungeon3.ui.InventoryItemElement;
import com.sillypantscoder.pixeldungeon3.ui.Spacer;
import com.sillypantscoder.pixeldungeon3.ui.TextElement;
import com.sillypantscoder.pixeldungeon3.ui.ImageDisplay;
import com.sillypantscoder.pixeldungeon3.ui.UIElement;
import com.sillypantscoder.pixeldungeon3.ui.VCombine;
import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

/**
 * This class contains code related to the UI. The UI is drawn on top of the game and can intercept clicks.
 * This class makes heavy use of the classes in the "ui" folder.
 */
public class GameUI {
	public static final int UI_SCALE = 3;
	// Button images
	public Surface button_backpack;
	// Game
	public Game game;
	public UIState state;
	/**
	 * The current UI is cached for use in clicking.
	 * This variable should be populated with the "make____UI()" functions.
	 */
	public UIElement currentUI;
	public GameUI(Game game) {
		this.game = game;
		this.state = UIState.GAME;
		try {
			Surface toolbar = TextureLoader.loadAsset("toolbar.png");
			button_backpack = toolbar.crop(61, 8, 21, 23);
		} catch (IOException e) {
			System.out.println("UI failed to load texture");
			e.printStackTrace();
		}
		this.currentUI = makeGameUI();
	}
	public UIElement makeGameUI() {
		return new AlignedElement(new ImageDisplay(button_backpack), 1, 1);
	}
	public UIElement makeBackpackUI() {
		UIElement dialog = new VCombine(new UIElement[] {
			new TextElement("Inventory", 6, Color.WHITE),
			new InventoryItemElement(game.player.weaponSlot),
			InventoryItemElement.fromInventory(game.player)
		});
		return new AbsCombine(new UIElement[] {
			makeGameUI(),
			new AlignedElement(new ChromeBorder(0, 0, 7, dialog), 0, 0)
		}, true);
	}
	public UIElement makeBackpackItemUI(Item item) {
		// Get button list
		Item.Button[] _buttons = item.getButtons();
		if (item instanceof Weapon w) _buttons = w.getButtons(game.player.weaponSlot == w);
		final Item.Button[] buttons = _buttons;
		// Render buttons
		UIElement[] buttonElms = new UIElement[buttons.length];
		for (int i = 0; i < buttons.length; i++) {
			final int index = i;
			TextElement txt = new TextElement(buttons[index].getName(), 6, Color.WHITE);
			DataContainer<Runnable> container = new DataContainer<Runnable>(txt, () -> buttons[index].execute(game, game.player, item));
			buttonElms[index] = new ChromeBorder(58, 0, 2, container);
		}
		// Make dialog element
		UIElement dialog = new VCombine(new UIElement[] {
			new HzCombine(new UIElement[] {
				// icon and name
				new ImageDisplay(item.image),
				new Spacer(5, 1),
				new TextElement(item.getName(), 8, Color.WHITE)
			}),
			// description
			new Spacer(1, 3),
			new TextElement(item.getDescription(), 5, Color.WHITE),
			new Spacer(1, 3),
			// buttons
			new HzCombine(buttonElms)
		});
		// Combine with previous dialogs
		return new AbsCombine(new UIElement[] {
			makeBackpackUI(),
			new AlignedElement(new ChromeBorder(0, 0, 7, dialog), 0, 0)
		}, true);
	}
	public Surface render(int width, int height) {
		return this.currentUI.render(width, height);
	}
	public boolean click(int xLeft, int yTop) {
		UIElement clicked = this.currentUI.elementAtPoint(game.recentSize[0], game.recentSize[1], xLeft, yTop);
		// Find what button was pressed
		if (this.state == UIState.GAME) {
			// Only button available is the backpack button
			if (clicked == null) return false;
			this.state = UIState.BACKPACK;
			this.currentUI = makeBackpackUI();
			return true;
		} else if (this.state == UIState.BACKPACK) {
			// Was an item clicked?
			if (clicked == null) {
				this.state = UIState.GAME;
				this.currentUI = makeGameUI();
			} else if (clicked instanceof InventoryItemElement inv) {
				if (inv.item != null) {
					this.state = UIState.BACKPACK_ITEM;
					this.currentUI = makeBackpackItemUI(inv.item);
				}
			}
			return true;
		} else if (this.state == UIState.BACKPACK_ITEM) {
			// Was a button clicked?
			if (clicked == null) {
				this.state = UIState.BACKPACK;
				this.currentUI = makeBackpackUI();
			} else if (clicked instanceof DataContainer container && container.data instanceof Runnable button) {
				button.run();
				this.state = UIState.GAME;
				this.currentUI = makeGameUI();
			}
			return true;
		}
		return false;
	}
	public static enum UIState {
		GAME,
		BACKPACK,
		BACKPACK_ITEM
	}
}
