package com.sillypantscoder.pixeldungeon3;

import java.awt.Color;
import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.item.Item;
import com.sillypantscoder.pixeldungeon3.ui.AbsCombine;
import com.sillypantscoder.pixeldungeon3.ui.AlignedElement;
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

public class GameUI {
	// Images
	public static final int UI_SCALE = 3;
	public Surface button_backpack;
	// Game
	public Game game;
	public UIState state;
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
		return new AbsCombine(new UIElement[] {
			makeGameUI(),
			new AlignedElement(new HzCombine(InventoryItemElement.fromInventory(game.player.inventory), Color.GRAY.darker()), 0, 0)
		}, true);
	}
	public UIElement makeBackpackItemUI(Item item) {
		// Buttons
		Item.Button[] btns = item.getButtons();
		UIElement[] buttons = new UIElement[btns.length];
		for (int i = 0; i < btns.length; i++) {
			final int index = i;
			TextElement txt = new TextElement(btns[index].getName(), 6, Color.WHITE);
			buttons[index] = new DataContainer<Runnable>(txt, () -> btns[index].execute(game, item));
		}
		// Make dialog element
		UIElement dialog = new VCombine(new UIElement[] {
			new HzCombine(new UIElement[] {
				// icon and name
				new ImageDisplay(item.image),
				new Spacer(5, 1),
				new TextElement(item.getName(), 8, Color.WHITE)
			}, Color.GRAY.darker()),
			// buttons
			new HzCombine(buttons, Color.GRAY.darker())
		}, Color.GRAY.darker());
		// Combine with previous dialogs
		return new AbsCombine(new UIElement[] {
			makeBackpackUI(),
			new AlignedElement(dialog, 0, 0)
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
		} else if (this.state == UIState.BACKPACK) {
			// Was an item clicked?
			if (clicked == null) {
				this.state = UIState.GAME;
				this.currentUI = makeGameUI();
			} else if (clicked instanceof InventoryItemElement inv) {
				this.state = UIState.BACKPACK_ITEM;
				this.currentUI = makeBackpackItemUI(inv.item);
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
		}
		return false;
	}
	public static enum UIState {
		GAME,
		BACKPACK,
		BACKPACK_ITEM
	}
}
