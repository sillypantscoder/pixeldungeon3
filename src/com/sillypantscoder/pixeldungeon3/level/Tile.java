package com.sillypantscoder.pixeldungeon3.level;

import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

public class Tile {
	public static final int TILE_SIZE = 16;
	public TileType type;
	public LightStatus lightStatus;
	public int x;
	public int y;
	protected static Surface image;
	protected Surface cacheTileImage;
	protected LightStatus cacheLightStatus;
	public Tile(TileType type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.lightStatus = LightStatus.Current;
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
	public Surface draw() {
		if (this.cacheLightStatus != this.lightStatus) updateTileImage();
		return cacheTileImage;
	}
}