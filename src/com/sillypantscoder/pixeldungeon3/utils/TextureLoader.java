package com.sillypantscoder.pixeldungeon3.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

import com.sillypantscoder.window.Surface;

import java.awt.image.WritableRaster;
import java.awt.geom.AffineTransform;

public class TextureLoader {
	public static Surface loadAsset(String filename) throws IOException {
		File f = AssetLoader.getResource(filename);
		BufferedImage image = ImageIO.read(f);
		return new Surface(image);
	}
	public static BufferedImage flipVertical(BufferedImage image) {
		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(1, -1));
		at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
		return createTransformed(image, at);
	}
	public static BufferedImage flipHorizontal(BufferedImage image) {
		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(-1, 1));
		at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		return createTransformed(image, at);
	}
	private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
		BufferedImage newImage = new BufferedImage(
			image.getWidth(), image.getHeight(),
			BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}
	public static BufferedImage copyImage(BufferedImage i) {
		ColorModel cm = i.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = i.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}