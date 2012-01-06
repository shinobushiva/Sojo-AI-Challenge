package jp.ac.sojou.cis.shiva.tron.samples;

// MyTronBot.java
// Author: your name goes here

import java.util.ArrayList;
import java.util.Collections;

import tron.utils.Map;
import tron.utils.TronBot;

/**
 * 相手と比べて、移動可能なタイルが多くなる方向を選択するボットのサンプルです
 * 
 * @author shiva
 * 
 */
public class MinMax implements TronBot {

	// 探索する深さを指定
	final int DEPTH = 9;

	// 移動方向を保持する配列定数
	final int[][] DIRECTIONS = new int[][] { { -1, 0, 'w' }, { 1, 0, 'e' },
			{ 0, -1, 'n' }, { 0, 1, 's' } };

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
	private int openTiles(boolean[][] walls, int width, int height, int mx,
			int my, int ox, int oy) {

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
		copy(wallsCopyBackup, wallsCopy);
		int opTiles = count(wallsCopy, ox, oy, width, height);

		return myTiles - opTiles;
	}

	/**
	 * ミニマックス法。最善手を探索する。打つ場所を探すだけで実際には打たない。
	 * 
	 * @param walls
	 *            壁データの２次元配列
	 * @param width
	 *            ステージの幅
	 * @param height
	 *            ステージの高さ
	 * @param mx
	 *            自分のX座標
	 * @param my
	 *            自分のY座標
	 * @param ox
	 *            相手のX座標
	 * @param oy
	 *            相手のY座標
	 * @param isOpponent
	 *            false-自分のターン、true-相手のターン
	 * @param level
	 *            探索中の深さ
	 * @param seachLevel
	 *            探索する最大深さ
	 */
	private int minMax(boolean[][] walls, int width, int height, int mx,
			int my, int ox, int oy, boolean isOpponent, int level,
			int seachLevel) {
		// ノードの評価値
		int value;
		// 子ノードから伝播してきた評価値
		int childValue;
		// ミニマックス法で求めた最大の評価値を持つ場所
		int best = 0;

		// ゲーム木の末端では盤面評価
		// その他のノードはMIN or MAXで伝播する
		if (level == 0) {
			// 盤面を評価して評価値を決める
			return  openTiles(walls, width, height, mx, my, ox, oy);
		}

		if (isOpponent) {
			// 相手の手番では最大の評価値を見つけたいので最初に最小値をセットしておく
			value = Integer.MIN_VALUE;
		} else {
			// 自分の手番では最小の評価値を見つけたいので最初に最大値をセットしておく
			value = Integer.MAX_VALUE;
		}

		for (int[] dir : DIRECTIONS) {
			// 進める方向はすべて試す（試すだけで実際には進まない）
			if (!isWall(walls, mx + dir[0], my + dir[1], width, height)) {
				// 試しに進んでみる
				walls[mx + dir[0]][my + dir[1]] = true;
				// 子ノードの評価値を計算（再帰）
				// 今度は相手の番なのでflagが逆転する
				childValue = minMax(walls, width, height, ox, oy, mx + dir[0],
						my + dir[1], !isOpponent, level - 1, seachLevel);
				// 子ノードとこのノードの評価値を比較する
				if (isOpponent) {
					// 相手のノードなら子ノードの中で最大の評価値を選ぶ
					if (childValue > value) {
						value = childValue;
						best = dir[2];
					}
				} else {
					// プレイヤーのノードなら子ノードの中で最小の評価値を選ぶ
					if (childValue < value) {
						value = childValue;
						best = dir[2];
					}
				}
				// 進む前に戻す
				walls[mx + dir[0]][my + dir[1]] = false;
			}
		}

		if (level == seachLevel) {
			// ルートノードなら最大評価の方向を返す
			return best;
		} else {
			// 子ノードならノードの評価値を返す
			return value;
		}
	}

	/**
	 * ミニマックス法に基づいて移動します
	 */
	public String makeMove(Map map) {

		// 自分の位置を取得
		int mx = map.myX();
		int my = map.myY();
		int ox = map.opponentX();
		int oy = map.opponentY();

		int c = minMax(map.getWalls(), map.getWidth(), map.getHeight(), mx, my,
				ox, oy, false, DEPTH, DEPTH);
		if (c == 0) {
			return makeRandomMove(map);
		}
		// 戻り値がint型なのでcharにキャスト
		String move = ("" + (char) c).toUpperCase();
		// System.out.println("Move:" + move);

		return move;
	}

	private String makeRandomMove(Map m) {

		int mx = m.myX();
		int my = m.myY();

		ArrayList<String> al = new ArrayList<String>();

		if (!m.isWall(mx, my - 1)) {
			al.add("North");
		}

		if (!m.isWall(mx, my + 1)) {
			al.add("South");
		}

		if (!m.isWall(mx - 1, my)) {
			al.add("West");
		}

		if (!m.isWall(mx + 1, my)) {
			al.add("East");
		}

		if (al.size() > 0) {
			Collections.shuffle(al);
			return al.get(0);
		}

		return "South";
	}
}
