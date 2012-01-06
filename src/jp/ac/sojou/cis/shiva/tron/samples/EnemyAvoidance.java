package jp.ac.sojou.cis.shiva.tron.samples;

// MyTronBot.java
// Author: your name goes here

import tron.utils.Map;
import tron.utils.TronBot;

/**
 * 敵から遠ざかるように移動方向を決定するボットのサンプルです。
 * 
 * @author shiva
 * 
 */
public class EnemyAvoidance implements TronBot {

	/**
	 * 敵から遠ざかるように移動方向を決定します
	 */
	public String makeMove(Map map) {

		// 自分の位置を取得
		int x = map.myX();
		int y = map.myY();

		// 敵の位置を取得
		int ox = map.opponentX();
		int oy = map.opponentY();

		double maxDist = Double.NEGATIVE_INFINITY;
		String move = "North";

		// 北方向をチェック
		if (!map.isWall(x, y - 1)) {
			double d = java.awt.Point.distance(x, y - 1, ox, oy);
			if (d > maxDist) {
				System.out.println("N:" + d);
				maxDist = d;
				move = "North";
			}
		}
		if (!map.isWall(x + 1, y)) {
			double d = java.awt.Point.distance(x + 1, y, ox, oy);
			if (d > maxDist) {
				System.out.println("E:" + d);
				maxDist = d;
				move = "East";
			}
		}
		if (!map.isWall(x, y + 1)) {
			double d = java.awt.Point.distance(x, y + 1, ox, oy);
			if (d > maxDist) {
				System.out.println("S:" + d);
				maxDist = d;
				move = "South";
			}
		}
		if (!map.isWall(x - 1, y)) {
			double d = java.awt.Point.distance(x - 1, y, ox, oy);
			if (d > maxDist) {
				System.out.println("W:" + d);
				maxDist = d;
				move = "West";
			}
		}
		return move;
	}

}
