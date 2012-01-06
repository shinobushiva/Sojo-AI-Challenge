import java.util.Stack;

public class ScanlineSeedfillCount {

	// マップのサイズ
	private static int WIDTH = 100;
	private static int HEIGHT = 100;

	// 塗りつぶし始めの位置
	private static int TEST_X = WIDTH / 2;
	private static int TEST_Y = HEIGHT / 2;

	// trueにするとデバッグ出力する
	private static boolean DEBUG_PRINT = false;

	// ３ｘ３のテスト用マップ
	private static boolean[][] TEST_BED = new boolean[][] {
			{ false, false, false }, { false, false, true },
			{ true, true, false } };
	// テスト用マップを使うかどうか
	private static boolean USE_TEST_BED = false;

	public static void print(Stack<int[]> st) {
		if (!DEBUG_PRINT)
			return;

		System.out.println("Stack/----");
		int c = 0;
		for (int[] ia : st) {
			System.out.println("" + c + " : x,y=" + ia[0] + "," + ia[1]);
			c++;
		}
		System.out.println("----/Stack");
	}

	public static void print(boolean[][] ba, int width, int height) {
		if (!DEBUG_PRINT)
			return;

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (!ba[i][j]) {
					if (i == TEST_X && j == TEST_Y) {
						System.out.print("☆");
					} else {
						System.out.print("　");
					}
				} else {
					if (i == TEST_X && j == TEST_Y) {
						System.out.print("★");
					} else {
						System.out.print("◎");
					}
				}
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		boolean[][] org = null;
		if (USE_TEST_BED) {
			// テストマップを使う場合
			org = TEST_BED;
			WIDTH = 3;
			HEIGHT = 3;
			TEST_X = 1;
			TEST_Y = 1;
		} else {
			// テストマップを使わない場合はランダムに生成
			org = new boolean[WIDTH][HEIGHT];
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < HEIGHT; j++) {
					if (Math.random() < 0.3) {
						org[i][j] = true;
					}
				}
			}
			org[TEST_X][TEST_Y] = false;
		}

		// 計算用のコピーを格納する配列
		boolean[][] copy = new boolean[WIDTH][HEIGHT];

		// 再帰的にカウント
		{
			copy(org, copy);
			int count = count(copy, TEST_X, TEST_Y, WIDTH, HEIGHT) - 1;
			System.out.println("count=" + count);
			print(copy, WIDTH, HEIGHT);
		}

		// Scanline-seedfillでカウント
		{
			copy(org, copy);
			int count = scanlineSeedfill(copy, TEST_X, TEST_Y, WIDTH, HEIGHT);
			System.out.println("count=" + count);
			print(copy, WIDTH, HEIGHT);
		}
	}

	public static int scanlineSeedfill(boolean[][] walls, int x, int y,
			int width, int height) {
		// ここを実装
		return 0;
	}

	/**
	 * 壁かどうかを判断します
	 */
	public static boolean isWall(boolean[][] walls, int x, int y, int width,
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
	public static int count(boolean[][] wallsCopy, int x, int y, int width,
			int height) {

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
	public static void copy(boolean[][] from, boolean[][] to) {
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

}
