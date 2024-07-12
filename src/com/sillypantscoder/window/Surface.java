package com.sillypantscoder.window;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sillypantscoder.pixeldungeon3.utils.Utils;

import java.util.ArrayList;
import java.awt.RenderingHints;
import java.awt.FontMetrics;

/**
 * This object represents an image. It allows you to do a lot of things with the image.
 * This entire class was inspired by Pygame! :)
 */
public class Surface {
	public BufferedImage img;
	public Surface(int width, int height, Color color) {
		if (width == 0 || height == 0) {
			width = 1;
			height = 1;
			color = new Color(0, 0, 0, 0);
		}
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.fill(color);
	}
	public Surface(BufferedImage image) {
		img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(image, 0, 0, new DummyImageObserver());
		g2d.dispose();
	}
	public void fill(Color color) {
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(color);
		graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
		graphics.dispose();
	}
	public void blit(Surface other, int x, int y) {
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(other.img, x, y, new DummyImageObserver());
		g2d.dispose();
	}
	public Surface crop(int x, int y, int width, int height) {
		Surface n = new Surface(width, height, new Color(0, 0, 0, 0));
		n.blit(this, -x, -y);
		return n;
	}
	public int get_width() {
		return img.getWidth();
	}
	public int get_height() {
		return img.getHeight();
	}
	public Surface copy() {
		Surface r = new Surface(get_width(), get_height(), new Color(0, 0, 0, 0));
		r.blit(this, 0, 0);
		return r;
	}
	public void set_at(int x, int y, Color color) {
		if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
			img.setRGB(x, y, color.getRGB());
		}
	}
	public Color get_at(int x, int y) {
		if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
			return new Color(img.getRGB(x, y));
		}
		return Color.BLACK;
	}
	public void drawLine(Color color, int x1, int y1, int x2, int y2, int thickness) {
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(color);
		BasicStroke bs = new BasicStroke(thickness);
		g2d.setStroke(bs);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}
	public void drawRect(Color color, int x, int y, int width, int height) {
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(color);
		g2d.fillRect(x, y, width, height);
		g2d.dispose();
	}
	public void drawRect(Color color, int x, int y, int width, int height, int lineWidth) {
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(lineWidth));
		g2d.drawRect(x, y, width, height);
		g2d.dispose();
	}
	public void drawCircle(Color color, int cx, int cy, int r) {
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(color);
		g2d.fillOval(cx - r, cy - r, r*2, r*2);
		g2d.dispose();
	}
	public Surface scaleValues(float amount) {
		RescaleOp op = new RescaleOp(new float[] { amount, amount, amount, amount }, new float[] { 0, 0, 0, 0 }, null);
		BufferedImage newImg = op.filter(this.img, null);
		return new Surface(newImg);
	}
	public Surface scale_size(int amount) {
		int newWidth = this.img.getWidth() * amount;
		int newHeight = this.img.getHeight() * amount;
		BufferedImage newImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImg.createGraphics();
		g2d.drawImage(this.img, 0, 0, newWidth, newHeight, 0, 0, this.img.getWidth(), this.img.getHeight(), null);
		g2d.dispose();
		return new Surface(newImg);
	}
	public Surface tile(int tileWidth, int tileHeight) {
		// if tileWidth or tileHeight are -1, use the dimensions of the original image
		if (tileWidth == -1) { tileWidth = this.img.getWidth(); }
		if (tileHeight == -1) { tileHeight = this.img.getHeight(); }
		BufferedImage newImg = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImg.createGraphics();
		for (int x = 0; x < tileWidth; x += this.img.getWidth()) {
			for (int y = 0; y < tileHeight; y += this.img.getHeight()) {
				g2d.drawImage(this.img, x, y, null);
			}
		}
		g2d.dispose();
		return new Surface(newImg);
	}
	public Surface flipHorizontally() {
		BufferedImage newImg = new BufferedImage(this.img.getWidth(), this.img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImg.createGraphics();
		g2d.drawImage(this.img, this.img.getWidth(), 0, 0, this.img.getHeight(), 0, 0, this.img.getWidth(), this.img.getHeight(), null);
		g2d.dispose();
		return new Surface(newImg);
	}
	public void writeToFile(String filename) throws IOException {
		File outputfile = new File(filename);
		ImageIO.write(img, "png", outputfile);
	}
	public void save(String name) {
		int n = 1;
		while (new File(name + n + ".png").exists()) {
			n += 1;
		}
		try {
			this.writeToFile(name + n + ".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Surface renderText(int size, String text, Color color) {
		// Get font
		Font font = new Font("Monospaced", 0, size);
		// Measure the text
		Surface measure = new Surface(1, 1, Color.BLACK);
		Graphics2D big = (Graphics2D)(measure.img.getGraphics());
		big.setFont(font);
		FontMetrics fm = big.getFontMetrics();
		Surface ret = new Surface(fm.stringWidth(text), fm.getHeight(), new Color(0, 0, 0, 0));
		// Draw the text
		try {
			Graphics2D g2d = ret.img.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(color);
			g2d.setFont(font);
			g2d.drawString(text, 0, fm.getAscent());
			g2d.dispose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		// Finish
		return ret;
	}
	public static Surface renderMultilineText(int size, String text, Color color) {
		String[] t = text.split("\n");
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		for (int i = 0; i < t.length; i++) {
			if (t[i].length() == 0) {
				surfaces.add(new Surface(1, size, new Color(0, 0, 0, 0)));
				continue;
			}
			surfaces.add(renderText(size, t[i], color));
		}
		return combineVertically(surfaces, new Color(0, 0, 0, 0));
	}
	public static Surface renderWrappedText(int i, String text, Color color, int maxWidth) {
		// Get font
		Font font = new Font("Monospaced", 0, i);
		// Measure the text
		Surface measure = new Surface(1, 1, Color.BLACK);
		Graphics2D big = (Graphics2D)(measure.img.getGraphics());
		big.setFont(font);
		FontMetrics fm = big.getFontMetrics();
		// Draw the text
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		String[] words = text.split(" ");
		String line = "";
		for (int j = 0; j < words.length; j++) {
			String word = words[j];
			if (fm.stringWidth(line + " " + word) > maxWidth) {
				surfaces.add(renderText(i, line, color));
				line = word;
			} else {
				if (line.length() == 0) {
					line = word;
				} else {
					line += " " + word;
				}
			}
		}
		surfaces.add(renderText(i, line, color));
		return combineVertically(surfaces, new Color(0, 0, 0, 0));
	}
	public static Surface combineVertically(ArrayList<Surface> surfaces, Color background) {
		int width = 1;
		for (int i = 0; i < surfaces.size(); i++) { int w = surfaces.get(i).get_width(); if (w > width) { width = w; } }
		int height = 1;
		for (int i = 0; i < surfaces.size(); i++) { int h = surfaces.get(i).get_height(); height += h; }
		Surface total = new Surface(width, height, background);
		int cum_y = 0;
		for (int i = 0; i < surfaces.size(); i++) {
			total.blit(surfaces.get(i), 0, cum_y);
			cum_y += surfaces.get(i).get_height();
		}
		return total;
	}
	public static Surface combineVertically(Surface[] surfaces, Color background) { return combineVertically(Utils.arrayToArrayList(surfaces), background); }
	public static Surface combineHorizontally(ArrayList<Surface> surfaces, Color background) {
		int height = 1;
		for (int i = 0; i < surfaces.size(); i++) { int h = surfaces.get(i).get_height(); if (h > height) { height = h; } }
		int width = 1;
		for (int i = 0; i < surfaces.size(); i++) { int w = surfaces.get(i).get_width(); width += w; }
		Surface total = new Surface(width, height, background);
		int cum_x = 0;
		for (int i = 0; i < surfaces.size(); i++) {
			total.blit(surfaces.get(i), cum_x, 0);
			cum_x += surfaces.get(i).get_width();
		}
		return total;
	}
	public static Surface combineHorizontally(Surface[] surfaces, Color background) { return combineHorizontally(Utils.arrayToArrayList(surfaces), background); }
	public static class DummyImageObserver implements ImageObserver {
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
			return false;
		}
	}
}