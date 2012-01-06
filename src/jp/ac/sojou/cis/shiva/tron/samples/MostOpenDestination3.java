package jp.ac.sojou.cis.shiva.tron.samples;

// MyTronBot.java
// Author: your name goes here

import java.awt.geom.Point2D;

import tron.utils.Map;
import tron.utils.TronBot;

/**
 * 相手と比べて、移動可能なタイルが多くなる方向を選択するボットのサンプルです
 * 
 * 相手とぶつかる可能性のあるタイルは避けます
 * 
 * @author shiva
 * 
 */
public class MostOpenDestination3 implements TronBot {

	/**
	 * 敵との距離が指定した数値以下であるかを調べます
	 */
	private boolean isEnemyRange(int nx, int ny, int ox, int oy, int dist) {
		if (Point2D.distance(nx, ny, ox, oy) <= dist) {
			return true;
		} else {
			return false;
		}
	}

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

	/**
	 * 配列を複製します
	 * 
	 * @param from
	 * @param to
	 */
	private void copy(boolean[][] from, boolean[][] to) {
		// 配列を複製
		for (int i = 0; i < from.length; i++) {
			boolean[] wallRow = from[i];
			boolean[] wallRowCopy = new boolean[wallRow.length];
			for (int j = 0; j < wallRow.length; j++) {
				wallRowCopy[j] = wallRow[j];
			}
			to[i] = wallRowCopy;
		}
	}

	/**
	 * nx, nyに移動したと仮定して、進むことが出来るタイルの数を数えます
	 */
	private double calcPoint(Map map, int nx, int ny) {
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

		// 自分と敵の位置を壁に
		wallsCopy[x][y] = true;
		wallsCopy[ox][oy] = true;
		boolean[][] wallsCopyBackup = new boolean[walls.length][];
		copy(wallsCopy, wallsCopyBackup);

		// 進んだ場合の自分の移動可能タイル数を取得
		int myTiles = count(wallsCopy, nx, ny, map.getWidth(), map.getHeight());

		// 進んだ場合の相手の移動可能タイル数を取得
		copy(wallsCopyBackup, wallsCopy);
		// 自分の進んだ位置を壁に
		wallsCopy[nx][ny] = true;
		int opTiles = count(wallsCopy, ox, oy, map.getWidth(), map.getHeight());

		double point = myTiles - opTiles;
		// 敵とぶつかる可能性がある場合は0点
		if (isEnemyRange(nx, ny, ox, oy, 1)) {
			point /= 10;
		}

		return point;
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
			double d = calcPoint(map, x, y - 1);
			System.out.println("N:" + d);
			if (d > maxDist) {
				maxDist = d;
				move = "North";
			}
		}
		if (!map.isWall(x + 1, y)) {
			double d = calcPoint(map, x + 1, y);
			System.out.println("E:" + d);
			if (d > maxDist) {
				maxDist = d;
				move = "East";
			}
		}
		if (!map.isWall(x, y + 1)) {
			double d = calcPoint(map, x, y + 1);
			System.out.println("S:" + d);
			if (d > maxDist) {
				maxDist = d;
				move = "South";
			}
		}
		if (!map.isWall(x - 1, y)) {
			double d = calcPoint(map, x - 1, y);
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
