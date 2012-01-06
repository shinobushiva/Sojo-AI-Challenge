package jp.ac.sojou.cis.shiva.tron.samples;

import java.util.ArrayList;
import java.util.Collections;

import tron.utils.Map;
import tron.utils.TronBot;

public class MinMaxABTomoeda implements TronBot {

	final int DEPTH = 3;// 探索する深さを指定

	final int[][] DIRECTIONS = new int[][] {// 移動方向を保持する配列定数
	{ -1, 0, 'w' }, { 1, 0, 'e' }, { 0, -1, 'n' }, { 0, 1, 's' } };

	private boolean isWall(boolean[][] walls, int x, int y, int width,
			int height) {// 壁を判断する関数

		if (x < 0 || y < 0 || x >= width || y >= height) {
			return true;
		} else {
			return walls[x][y];
		}
	}

	private int count(boolean[][] wallsCopy, int x, int y, int width, int height) {// 移動可能タイルを数える関数

		int sum = 1;

		if (!isWall(wallsCopy, x - 1, y, width, height)) {
			wallsCopy[x - 1][y] = true;
			sum += count(wallsCopy, x - 1, y, width, height);
		}
		if (!isWall(wallsCopy, x, y - 1, width, height)) {
			wallsCopy[x][y - 1] = true;
			sum += count(wallsCopy, x, y - 1, width, height);
		}
		if (!isWall(wallsCopy, x + 1, y, width, height)) {
			wallsCopy[x + 1][y] = true;
			sum += count(wallsCopy, x + 1, y, width, height);
		}
		if (!isWall(wallsCopy, x, y + 1, width, height)) {
			wallsCopy[x][y + 1] = true;
			sum += count(wallsCopy, x, y + 1, width, height);
		}

		return sum;
	}

	/**
	 * 
	 * @param wallsFrom
	 *            コピー元になる2次元配列です。
	 * @param wallsCopy
	 *            コピー先になる2次元配列です。
	 */

	private void copy(boolean[][] from, boolean[][] to) {// 配列をコピーする関数

		for (int i = 0; i < from.length; i++) {
			boolean[] wallRow = from[i];
			boolean[] wallRowCopy = new boolean[wallRow.length];

			for (int j = 0; j < wallRow.length; j++) {
				wallRowCopy[j] = wallRow[j];
			}
			to[i] = wallRowCopy;
		}
	}

	private int openTiles(boolean[][] walls, int width, int height, int mx,
			int my, int ox, int oy) {// 敵と自分が移動したと仮定してタイルの数を数える(評価関数)

		// 配列を複製
		boolean[][] wallsCopy = new boolean[walls.length][];
		copy(walls, wallsCopy);

		// 自分と敵の位置を壁に
		wallsCopy[mx][my] = true;
		wallsCopy[ox][oy] = true;
		boolean[][] wallsCopyBackup = new boolean[walls.length][];

		// 進んだ場合の自分の移動可能タイル数を取得
		copy(wallsCopy, wallsCopyBackup);
		int myTiles = count(wallsCopy, mx, my, width, height);

		// 進んだ場合の相手の移動可能タイル数を取得
		copy(wallsCopy, wallsCopyBackup);
		int opTiles = count(wallsCopy, ox, oy, width, height);

		return myTiles - opTiles;
	}

	public String makeRandomMove(Map m) {// ランダムに移動方向を決める関数

		int mx = m.myX();
		int my = m.myY();

		ArrayList<String> al = new ArrayList<String>();

		if (!m.isWall(mx, my - 1)) {
			al.add("North");
		}
		if (!m.isWall(mx + 1, my)) {
			al.add("East");
		}
		if (!m.isWall(mx, my + 1)) {
			al.add("South");
		}
		if (!m.isWall(mx - 1, my)) {
			al.add("West");
		}

		if (al.size() > 0) {
			Collections.shuffle(al);
			return al.get(0);
		}

		return "South";
	}

	private int miniMaxαβ(boolean[][] walls, int width, int height, int mx,
			int my, int ox, int oy, boolean isOpponent, int level,
			int seachLevel, int α, int β) {

		int value;
		int childValue;
		int best = 0;

		if (level == 0) {
			return (isOpponent ? -1 : 1)
			*openTiles(walls, width, height, mx, my, ox, oy);
		}

		if (isOpponent) {
			value = Integer.MAX_VALUE;
		} else {
			value = Integer.MIN_VALUE;
		}

		for (int[] dir : DIRECTIONS) {
			if (!isWall(walls, mx + dir[0], my + dir[1], width, height)) {
				walls[mx + dir[0]][my + dir[1]] = true;
				childValue = miniMaxαβ(walls, width, height, ox, oy, mx
						+ dir[0], my + dir[1], !isOpponent, level - 1,
						seachLevel, α, β);
				walls[mx + dir[0]][my + dir[1]] = false;
				if (isOpponent) {

					if (value > childValue) {

						if (β > childValue) {
							β = childValue;
						}
						value = childValue;
						best = dir[2];
					}

					if (α >= β) {
						return α;
					}

				} else {

					if (value < childValue) {

						if (α < childValue) {
							α = childValue;
						}

						value = childValue;
						best = dir[2];
					}

					if (α >= β) {
						return β;
					}

				}
			}
		}

		if (level == seachLevel) {
			return best;
		} else {
			return value;
		}
	}

	public String makeMove(Map map) {

		// 自分の位置を取得

		int mx = map.myX();
		int my = map.myY();

		// 敵の位置を取得

		int ox = map.opponentX();
		int oy = map.opponentY();

		int α = Integer.MIN_VALUE;
		int β = Integer.MAX_VALUE;

		int c = miniMaxαβ(map.getWalls(), map.getWidth(), map.getHeight(), mx,
				my, ox, oy, false, DEPTH, DEPTH, α, β);
		if (c == 0) {
			return makeRandomMove(map);
		}

		String move = ("" + (char) c).toUpperCase();
		System.out.println("Move:" + move);

		return move;
	}
}