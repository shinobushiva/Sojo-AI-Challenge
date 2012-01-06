package jp.ac.sojou.cis.shiva.tron.samples;

// MyTronBot.java
// Author: your name goes here

import tron.utils.Map;
import tron.utils.TronBot;

/**
 * 移動可能なタイルが多い方向を選択するボットのサンプルです
 * 
 * @author shiva
 * 
 */
public class MostOpenDestination implements TronBot {

	/**
	 * 壁かどうかを判断します
	 */
	private boolean isWall(boolean[][] walls, int x, int y, int width,
			int height) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return true;
		} else {
			return walls[x][y];
		}
	}

	/**
	 * 再帰的に移動可能なタイル数を数えます
	 */
	private int count(boolean[][] wallsCopy, int x, int y, int width, int height) {

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

	private void copy(boolean[][] wallsFrom, boolean[][] wallsCopy) {

		for (int i = 0; i < wallsFrom.length; i++) {
			boolean[] wallRow = wallsFrom[i];
			boolean[] wallRowCopy = new boolean[wallRow.length];
			for (int j = 0; j < wallRow.length; j++) {
				wallRowCopy[j] = wallRow[j];
			}
			wallsCopy[i] = wallRowCopy;
		}

	}

	/**
	 * nx, nyに移動したと仮定して、進むことが出来るタイルの数を数えます
	 */
	private int openTiles(Map map, int nx, int ny) {
		// 自分の位置を取得
		int x = map.myX();
		int y = map.myY();

		// 敵の位置を取得
		int ox = map.opponentX();
		int oy = map.opponentY();

		// 配列を複製
		boolean[][] walls = map.getWalls();

		boolean[][] wallsCopy = new boolean[walls.length][];
		copy(walls, wallsCopy);

		boolean[][] wallsCopy2 = new boolean[walls.length][];
		copy(walls, wallsCopy2);

		// 自分と敵の位置を壁に
		wallsCopy[x][y] = true;
		wallsCopy[ox][oy] = true;

		// 進んだ場合の移動可能タイル数を取得
		return count(wallsCopy, nx, ny, map.getWidth(), map.getHeight());
	}

	/**
	 * 移動できるマスが多い方向に移動します
	 */
	public String makeMove(Map map) {

		// 自分の位置を取得
		int x = map.myX();
		int y = map.myY();

		double maxDist = Double.NEGATIVE_INFINITY;
		String move = "North";

		// 動ける方向をチェック
		if (!map.isWall(x, y - 1)) {
			double d = openTiles(map, x, y - 1);
			System.out.println("N:" + d);
			if (d > maxDist) {
				maxDist = d;
				move = "North";
			}
		}
		if (!map.isWall(x + 1, y)) {
			double d = openTiles(map, x + 1, y);
			System.out.println("E:" + d);
			if (d > maxDist) {
				maxDist = d;
				move = "East";
			}
		}
		if (!map.isWall(x, y + 1)) {
			double d = openTiles(map, x, y + 1);
			System.out.println("S:" + d);
			if (d > maxDist) {
				maxDist = d;
				move = "South";
			}
		}
		if (!map.isWall(x - 1, y)) {
			double d = openTiles(map, x - 1, y);
			System.out.println("W:" + d);
			if (d > maxDist) {
				maxDist = d;
				move = "West";
			}
		}
		System.out.println("Move:" + move);

		return move;
	}
}
