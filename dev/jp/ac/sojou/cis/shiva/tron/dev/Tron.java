package jp.ac.sojou.cis.shiva.tron.dev;

import tron.utils.Map;

// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Tron.java

public class Tron {

	public static TronBotRenderer renderer = new TronBotRenderer();
	public static TronBotFileWriter writer = new TronBotFileWriter();

	public static void log(Object log) {
		renderer.log(log);
	}

	public static void main(String args[]) {
		if (args.length < 3) {
			System.err
					.println("FATAL ERROR: not enough command-line arguments.\nUSAGE: java -jar Tron.jar map-filename command-to-start-player-one command-to-start-player-two [delay-between-turns] [max-move-time]");
			System.exit(1);
		}

		String mapFilePath = args[0];
		String cls1 = args[1];
		String cls2 = args[2];
		int delayBetweenTurns = 1;
		if (args.length >= 4)
			delayBetweenTurns = Integer.parseInt(args[3]);
		int maxMoveTime = 3;
		if (args.length >= 5)
			maxMoveTime = Integer.parseInt(args[4]);

		TronMap map = new TronMap(mapFilePath);
		// map.print();

		Comm comPlayer1 = null;
		Comm comPlayer2 = null;
		try {
			comPlayer1 = new Comm(cls1, 0);
		} catch (Exception exception) {
			System.err.println((new StringBuilder())
					.append("Problem while starting program 1: ")
					.append(exception).toString());
			System.exit(1);
		}
		try {
			comPlayer2 = new Comm(cls2, 1);
		} catch (Exception exception1) {
			System.err.println((new StringBuilder())
					.append("Problem while starting program 2: ")
					.append(exception1).toString());
			System.exit(1);
		}

		do {

			Map m = TronUtils.createMyMap(map, 0);
			renderer.callback(m);
			writer.callback(m);

			try {
				Thread.sleep(delayBetweenTurns);
			} catch (Exception exception2) {
			}

			map.getWalls()[map.playerOneX()][map.playerOneY()] = true;
			map.getWalls()[map.playerTwoX()][map.playerTwoY()] = true;

			int k = comPlayer1.getMove(map, maxMoveTime);
			int l = comPlayer2.getMove(map, maxMoveTime);
			System.out.println("Move : " + (k) + "," + (l));

			k = map.movePlayerOne(k);
			l = map.movePlayerTwo(l);

			if (map.playerOneX() == map.playerTwoX()
					&& map.playerOneY() == map.playerTwoY()) {
				System.out.println("Players collided. Draw!");
				break;
			}
			if (k < 0 && l < 0) {
				System.out.println("Both players crashed. Draw!");
				break;
			}
			if (k < 0) {
				System.out.println("Player Two Wins!");
				break;
			}
			if (l < 0) {
				System.out.println("Player One Wins!");
				break;
			}

			// map.print();

		} while (true);
	}
}
