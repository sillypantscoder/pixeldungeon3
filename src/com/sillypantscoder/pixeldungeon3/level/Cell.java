package com.sillypantscoder.pixeldungeon3.level;

import java.io.IOException;

import com.sillypantscoder.pixeldungeon3.utils.TextureLoader;
import com.sillypantscoder.window.Surface;

public class Cell {
	public CellType type;
	public LightStatus lightStatus;
	public int x;
	public int y;
	protected Surface image;
	public Cell(CellType type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.lightStatus = LightStatus.Unknown;
		try {
			this.image = TextureLoader.loadAsset("tiles0.png");
		} catch (IOException e) {
			System.out.println("Cell failed to load texture!");
		}
	}
	public Surface draw() {
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
		}
		Surface newImg = image.crop(srcPos[0] * 16, srcPos[1] * 16, 16, 16);
		if (lightStatus == LightStatus.Unknown) {
			newImg = newImg.scaleValues(0.0f);
		}
		if (lightStatus == LightStatus.Memory) {
			newImg = newImg.scaleValues(0.5f);
		}
		return newImg;
	}
}
