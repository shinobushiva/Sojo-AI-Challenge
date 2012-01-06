package jp.ac.sojou.cis.shiva.tron.samples;

// MyTronBot.java
// Author: your name goes here

import java.util.ArrayList;
import java.util.List;

import tron.utils.Map;
import tron.utils.TronBot;

/**
 * 壁にそって移動するボットのサンプルです。
 * 
 * @author shiva
 * 
 */
public class MyTronBotHugger implements TronBot {

	/**
	 * ステップにおける移動方向を壁にそって移動するように決定します。
	 */
	public String makeMove(Map m) {

		// 自分の位置を取得
		int x = m.myX();
		int y = m.myY();

		// 動ける方向を格納する可変長配列
		List<String> validMoves = new ArrayList<String>();

		// 動ける方向をチェック
		if (!m.isWall(x, y - 1)) {
			validMoves.add("North");
		}
		if (!m.isWall(x + 1, y)) {
			validMoves.add("East");
		}
		if (!m.isWall(x, y + 1)) {
			validMoves.add("South");
		}
		if (!m.isWall(x - 1, y)) {
			validMoves.add("West");
		}

		if (validMoves.size() == 0) {
			return "North"; // 動ける方向がなければ、北に移動　(望みないけど、、、)
		} else {
			// 移動可能な方向について処理
			for (String dir : validMoves) {

				// 方向を表す文字列の１文字目を取り出す
				char c = dir.charAt(0);
				boolean flag = false;
				switch (c) {
				case 'N':
					flag = isHaggingWall(x, y - 1, 'N', m);
					break;
				case 'E':
					flag = isHaggingWall(x + 1, y, 'E', m);
					break;
				case 'S':
					flag = isHaggingWall(x, y + 1, 'S', m);
					break;
				case 'W':
					flag = isHaggingWall(x - 1, y, 'W', m);
					break;
				}

				// 壁に沿っていれば方向決定
				if (flag) {
					return dir;
				} else {
					return validMoves.get(0);
				}
			}

			// ここが実行されることはないが念のため北を返す
			return "North";
		}
	}

	/**
	 * 指定した方向が壁に沿っているかをチェックするメソッド
	 * 
	 * @param x
	 *            X座標
	 * @param y
	 *            Y座標
	 * @param c
	 *            移動方向を表す文字
	 * @param m
	 *            ステージ
	 * @return 壁に沿っていればtrue
	 */
	private boolean isHaggingWall(int x, int y, char c, Map m) {

		if (m.isWall(x, y - 1) && c != 'S') {
			return true;
		}
		if (m.isWall(x + 1, y) && c != 'W') {
			return true;
		}
		if (m.isWall(x, y + 1) && c != 'N') {
			return true;
		}
		if (m.isWall(x - 1, y) && c != 'E') {
			return true;
		}

		return false;
	}

}
