package com.sillypantscoder.pixeldungeon3.level;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Random;

public class SubdivisionLevelGeneration {
	public static void main(String[] args) {
		Level board = generateLevel();
		// Print board as ASCII art
		for (int y = 0; y < board.board.length; y++) {
			for (int x = 0; x < board.board[y].length; x++) {
				if (board.board[x][y].type == TileType.Wall) System.out.print("#");
				else System.out.print(".");
			}
			System.out.println();
		}
	}
	public static Level generateLevel() {
		int worldSize = 60;
		worldSize /= 2;
		ArrayList<Rect> rectsToDivide = new ArrayList<Rect>();
		ArrayList<Rect> resultRects = new ArrayList<Rect>();
		rectsToDivide.add(new Rect(0, 0, worldSize, worldSize));
		// Divide
		while (rectsToDivide.size() > 0) {
			@SuppressWarnings("unchecked")
			ArrayList<Rect> loopRects = (ArrayList<Rect>)(rectsToDivide.clone());
			rectsToDivide = new ArrayList<Rect>();
			for (int i = 0; i < loopRects.size(); i++) {
				Rect r = loopRects.get(i);
				boolean discardRect = false;
				// If the rect has no area, remove it
				if (r.w == 0 || r.h == 0) {
					// Aaaaaa!
					rectsToDivide.remove(r);
					continue;
				}
				// If the rect is too small to be divided further, finish it
				if (r.w < 3 || r.h < 3) discardRect = true;
				// If the rect is generally small enough, finish it
				if (r.w < 7 && r.h < 7) discardRect = true;
				// Handle the rect
				if (discardRect) {
					rectsToDivide.remove(r);
					resultRects.add(r);
				} else {
					// Divide the rect
					Rect[] newRects = r.subdivide();
					for (int n = 0; n < newRects.length; n++) {
						rectsToDivide.add(newRects[n]);
					}
				}
			}
		}
		// Create the board
		Level board = new Level((worldSize * 2) + 1, (worldSize * 2) + 1);
		for (int i = 0; i < resultRects.size(); i++) {
			// Top
			for (int x = resultRects.get(i).left() * 2; x < resultRects.get(i).right() * 2; x++) board.board[x][resultRects.get(i).top() * 2].type = TileType.Wall;
			// Bottom
			for (int x = resultRects.get(i).left() * 2; x < resultRects.get(i).right() * 2; x++) board.board[x][resultRects.get(i).bottom() * 2].type = TileType.Wall;
			// Left
			for (int y = resultRects.get(i).top() * 2; y < resultRects.get(i).bottom() * 2; y++) board.board[resultRects.get(i).left() * 2][y].type = TileType.Wall;
			// Right
			for (int y = resultRects.get(i).top() * 2; y < resultRects.get(i).bottom() * 2; y++) board.board[resultRects.get(i).right() * 2][y].type = TileType.Wall;
			// Add doors
			int nDoors = 1 + (int)(Math.round(Math.random() * 2));
			for (int d = 0; d < nDoors; d++) {
				int doorSide = (int)(Math.round(Math.random() * 3));
				int doorX = 0;
				int doorY = 0;
				switch (doorSide) {
					case 0:
						// Top
						doorX = Random.randint((resultRects.get(i).left() * 2) + 1, (resultRects.get(i).right() * 2) - 1);
						doorY = resultRects.get(i).top() * 2;
						board.board[doorX][doorY].type = TileType.Ground;
						break;
					case 1:
						// Bottom
						doorX = Random.randint((resultRects.get(i).left() * 2) + 1, (resultRects.get(i).right() * 2) - 1);
						doorY = resultRects.get(i).bottom() * 2;
						board.board[doorX][doorY].type = TileType.Ground;
						break;
					case 2:
						// Left
						doorX = resultRects.get(i).left() * 2;
						doorY = Random.randint((resultRects.get(i).top() * 2) + 1, (resultRects.get(i).bottom() * 2) - 1);
						board.board[doorX][doorY].type = TileType.Ground;
						break;
					case 3:
						// Right
						doorX = resultRects.get(i).right() * 2;
						doorY = Random.randint((resultRects.get(i).top() * 2) + 1, (resultRects.get(i).bottom() * 2) - 1);
						board.board[doorX][doorY].type = TileType.Ground;
						break;
				}
			}
		}
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				board.board[x][y].updateTileImage();
			}
		}
		return board;
	}
}

class Rect {
	public int x, y, w, h;
	public Rect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public int left() { return this.x; }
	public int right() { return this.x + this.w; }
	public int top() { return this.y; }
	public int bottom() { return this.y + this.h; }
	public boolean collidePoint(int x, int y) {
		return  left() <= x && x <= right() &&
				top() <= y && y <= bottom();
	}
	public boolean collideRect(Rect other) {
		boolean xOverlap =
			this.intervalContains(this.left(), this.right(), other.left()) ||	// \ ____ Check whether the left or right of the other
			this.intervalContains(this.left(), this.right(), other.right()) ||	// /		rect is inside of this rect
			this.intervalContains(other.left(), other.right(), this.left()) ||	// \ ____ Check whether the left or right of this rect
			this.intervalContains(other.left(), other.right(), this.right());	// /		is inside the other rect
		boolean yOverlap =
			this.intervalContains(this.top(), this.bottom(), other.top()) ||
			this.intervalContains(this.top(), this.bottom(), other.bottom()) ||
			this.intervalContains(other.top(), other.bottom(), this.top()) ||
			this.intervalContains(other.top(), other.bottom(), this.bottom());
		return xOverlap && yOverlap;
	}
	protected boolean intervalContains(int start, int end, int point) {
		return start <= point && point <= end;
	}
	public Rect square() {
		// Center a square inside of this rect
		int size = Math.min(this.w, this.h);
		int x = this.x + ((this.w - size) / 2);
		int y = this.y + ((this.h - size) / 2);
		return new Rect(x, y, size, size);
	}
	public Rect translate(int bx, int by, int bw, int bh) {
		return new Rect(x + bx, y + by, w + bw, h + bh);
	}
	public Rect translate(int bx, int by) {
		return translate(bx, by, 0, 0);
	}
	public Rect[] subdivide() {
		boolean doSplitVertical = false;
		if (w > h) {
			doSplitVertical = true;
		} else if (w < h) {
			doSplitVertical = false;
		} else {
			doSplitVertical = Math.random() < 0.5;
		}
		if (doSplitVertical) {
			int split = Random.randint(2, w - 2);
			return new Rect[] {
				new Rect(x, y, split, h),
				new Rect(x + split, y, w - split, h)
			};
		} else {
			int split = Random.randint(2, h - 2);
			return new Rect[] {
				new Rect(x, y, w, split),
				new Rect(x, y + split, w, h - split)
			};
		}
	}
	public String describe() {
		return "Rect [" + x + ", " + y + ", " + w + ", " + h + "]";
	}
	@Override
	public boolean equals(Object j) {
		if (j instanceof Rect) {
			Rect jr = (Rect)(j);
			return x == jr.x && y == jr.y && w == jr.w && h == jr.h;
		}
		return false;
	}
}