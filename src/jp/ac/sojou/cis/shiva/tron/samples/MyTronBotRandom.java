package jp.ac.sojou.cis.shiva.tron.samples;

// MyTronBot.java
// Author: your name goes here

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tron.utils.Map;
import tron.utils.TronBot;

/**
 * ランダムに移動方向を決めるボットです。
 * 
 * 壁以外の方向をランダムで選択して移動します。
 * 
 * @author shiva
 * 
 */
public class MyTronBotRandom implements TronBot {

	/**
	 * ランダムに値を生成するインスタンス
	 */
	private final Random rand = new Random();

	/**
	 * ランダムに移動方向を決定します。
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
			// ランダムな整数を取得
			int whichMove = rand.nextInt(validMoves.size());
			// 方向決定
			return validMoves.get(whichMove);
		}

	}

}
