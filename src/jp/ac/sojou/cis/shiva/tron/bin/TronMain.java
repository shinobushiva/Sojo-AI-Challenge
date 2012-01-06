package jp.ac.sojou.cis.shiva.tron.bin;

import java.io.IOException;

import jp.ac.sojou.cis.shiva.tron.dev.Tron;

/**
 * このクラスはトロンを実行するためのクラスです。
 * 
 * @author shiva
 * 
 */
public class TronMain {

	public static void main(String[] args) throws IOException {

		// マップを指定します
		// String map = "maps/big-room.txt";
		// String map = "maps/huge-room.txt";
		// String map = "maps/apocalyptic.txt";
		// String map = "maps/test-board.txt";
		// String map = "maps/joust.txt";
		// String map = "maps/keyhole.txt";
		String map = "maps/center.txt";
		// String map = "maps/empty-room.txt";
		// String map = "maps/playground.txt";
		// String map = "maps/ring.txt";
		// String map = "maps/toronto.txt";
		// String map = "maps/trix.txt";

		// ボットのクラスをFQCNで指定します
		// String player2 = "jp.ac.sojou.cis.shiva.tron.samples.NearMinMax";
		// String player1 = "jp.ac.sojou.cis.shiva.tron.samples.MinMaxAB";
		String player1 = "jp.ac.sojou.cis.shiva.tron.samples.MyTronBotHuggerCopy";
		// String player1 = "jp.ac.sojou.cis.shiva.tron.samples.MinMax";
		String player2 = "jp.ac.sojou.cis.shiva.tron.samples.MyTronBotHugger";
		// String player2 = "jp.ac.sojou.cis.shiva.tron.samples.MinMax";
		// String player1 = "jp.ac.sojou.cis.shiva.tron.samples.NearMinMax";

		// 実行時のステップ待ち時間を設定します
		String delay = "100";

		// トロンを開始します
		Tron.main(new String[] { map, player1, player2, delay });

	}
}
