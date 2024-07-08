package com.sillypantscoder.pixeldungeon3.utils;

import java.util.ArrayList;

public class Pathfinding {
	public static void main(String[] args) {
		int[][] path = findPath(new int[][] {
			new int[] { 1, 1, 1, 1, 1 },
			new int[] { 1, 0, 0, 0, 1 },
			new int[] { 1, 0, 0, 0, 1 },
			new int[] { 1, 1, 0, 1, 1 }
		}, new int[] { 0, 0 }, new int[] { 3, 3 });
		for (int i = 0; i < path.length; i++) {
			System.out.print(path[i][0]);
			System.out.print(",");
			System.out.print(path[i][1]);
			System.out.print(" -> ");
		}
		System.out.println("end");
	}
	public static int[][] findPath(int[][] board, int[] startPos, int[] endPos) {
		ArrayList<Walker> w = new ArrayList<Walker>();
		w.add(new Walker(startPos[0], startPos[1], 1, new int[0][2]));
		// Find the path
		boolean finished = false;
		ArrayList<Walker> finishers = new ArrayList<Walker>();
		while (!finished) {
			ArrayList<Walker> newPos = new ArrayList<Walker>();
			for (; w.size() > 0; w.remove(0)) {
				Walker[] toAdd = w.get(0).walk(board);
				for (int i = 0; i < toAdd.length; i++) {
					newPos.add(toAdd[i]);
					board[toAdd[i].x][toAdd[i].y] = 0;
				}
			}
			for (int i = 0; i < newPos.size(); i++) {
				w.add(newPos.get(i));
				if (newPos.get(i).x == endPos[0] && newPos.get(i).y == endPos[1]) finishers.add(newPos.get(i));
			}
			// Finish
			if (w.size() == 0) finished = true;
		}
		// Find which route is shortest
		Walker winner = null;
		int shortestLength = Integer.MAX_VALUE;
		for (int i = 0; i < finishers.size(); i++) {
			if (finishers.get(i).path.length < shortestLength) {
				winner = finishers.get(i);
				shortestLength = finishers.get(i).path.length;
			}
		}
		if (winner == null) return new int[0][2];
		// Finish
		int[][] out = new int[winner.path.length][2];
		for (int i = 0; i < winner.path.length; i++) {
			out[i][0] = winner.path[i][0];
			out[i][1] = winner.path[i][1];
		}
		return out;
	}
}

class Walker {
	public int x;
	public int y;
	public int score;
	public int[][] path;
	public Walker(int x, int y, int score, int[][] path) {
		this.x = x;
		this.y = y;
		this.score = score;
		this.path = new int[path.length + 1][2];
		for (int i = 0; i < path.length; i++) {
			this.path[i][0] = path[i][0];
			this.path[i][1] = path[i][1];
		}
		this.path[path.length][0] = x;
		this.path[path.length][1] = y;
	}
	public Walker[] walk(int[][] board) {
		ArrayList<Walker> items = new ArrayList<Walker>();
		for (int cx : new int[] { 0, -1, 1 }) {
			for (int cy : new int[] { 0, -1, 1 }) {
				if (!(cx == 0 && cy == 0)) {
					walkAtPoint(board, new int[] { cx + x, cy + y }, items);
				}
			}
		}
		// Finish
		Walker[] ret = new Walker[items.size()];
		for (int i = 0; i < items.size(); i++) {
			ret[i] = items.get(i);
			// System.out.print(" - New item: ");
			// System.out.print(items.get(i).x);
			// System.out.print(", ");
			// System.out.println(items.get(i).y);
		}
		return ret;
	}
	public void walkAtPoint(int[][] board, int[] pos, ArrayList<Walker> items) {
		if (pos[0] >= 0 && pos[1] >= 0 && pos[0] < board.length && pos[1] < board[pos[0]].length) {
			if (board[pos[0]][pos[1]] > 0) {
				items.add(new Walker(pos[0], pos[1], score + board[pos[0]][pos[1]], path));
			}
		}
	}
}