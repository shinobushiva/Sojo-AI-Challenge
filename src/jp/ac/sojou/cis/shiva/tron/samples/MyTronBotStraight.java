package jp.ac.sojou.cis.shiva.tron.samples;

// MyTronBot.java
// Author: your name goes here

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tron.utils.Map;
import tron.utils.TronBot;

/**
 * できるかぎり直進するボットのサンプルです。
 * 
 * @author shiva
 * 
 */
public class MyTronBotStraight implements TronBot {

	/**
	 * 現在の進行方向を保持します
	 */
	private String currentDirection = null;

	/**
	 * なるべく直進するように移動方向を決定します
	 */
	public String makeMove(Map map) {

		// 自分の位置を取得
		int x = map.myX();
		int y = map.myY();

		// 動ける方向を格納する可変長配列
		List<String> validMoves = new ArrayList<String>();

		// 動ける方向をチェック
		if (!map.isWall(x, y - 1)) {
			validMoves.add("North");
		}
		if (!map.isWall(x + 1, y)) {
			validMoves.add("East");
		}
		if (!map.isWall(x, y + 1)) {
			validMoves.add("South");
		}
		if (!map.isWall(x - 1, y)) {
			validMoves.add("West");
		}

		if (validMoves.size() == 0) {
			return "North"; // 動ける方向がなければ、北に移動　(望みないけど、、、)
		} else {
			// 進める方向の中に、現在進んでいる方向があれば決定(直進)
			if (validMoves.contains(currentDirection)) {
				return currentDirection;
			}

			// 直進できない場合は進める方向からランダムに選択
			Random rand = new Random();
			int whichMove = rand.nextInt(validMoves.size());

			// 現在の進行方向をつぎのステップのために保存
			currentDirection = validMoves.get(whichMove);

			return currentDirection;
		}
	}

}
