package com.sillypantscoder.pixeldungeon3.level;

import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

public class Tile {
	public static final int TILE_SIZE = 16;
	public Game game;
	public TileType type;
	public LightStatus lightStatus;
	public int x;
	public int y;
	protected static Surface image;
	protected Surface cacheTileImage;
	protected LightStatus cacheLightStatus;
	public Tile(Game game, TileType type, int x, int y) {
		this.game = game;
		this.type = type;
		this.x = x;
		this.y = y;
		this.lightStatus = LightStatus.Unknown;
		if (image == null) {
			try {
				image = TextureLoader.loadAsset("tiles0.png");
			} catch (IOException e) {
				System.out.println("Tile failed to load texture!");
			}
		}
		this.updateTileImage();
	}
	public void updateTileImage() {
		int[] srcPos = new int[] { 0, 0 };
		switch (this.type) {
			case Chasm:
				srcPos = new int[] { 0, 0 };
				break;
			case Ground:
				srcPos = new int[] { 1, 0 };
				break;
			case Wall:
				srcPos = new int[] { 4, 0 };
				break;
			case Door:
				srcPos = new int[] { 5, 0 };
				if (occupied()) srcPos = new int[] { 6, 0 };
				break;
			default:
				srcPos = new int[] { 15, 3 };
		}
		Surface newImg = image.crop(srcPos[0] * 16, srcPos[1] * 16, 16, 16);
		if (lightStatus == LightStatus.Unknown) {
			newImg = newImg.scaleValues(0.0f);
		}
		if (lightStatus == LightStatus.Memory) {
			newImg = newImg.scaleValues(0.5f);
		}
		this.cacheTileImage = newImg;
		this.cacheLightStatus = this.lightStatus;
	}
	public boolean occupied() {
		if (this.game.level == null) return false;
		return this.game.level.getEntity(x, y) != null;
	}
	public Surface draw() {
		if (this.type == TileType.Door && this.lightStatus != LightStatus.Unknown) updateTileImage();
		if (this.cacheLightStatus != this.lightStatus) updateTileImage();
		return cacheTileImage;
	}
}
