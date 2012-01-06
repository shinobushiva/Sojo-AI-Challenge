import java.util.Stack;

public class CountTest {

	private static int WIDTH = 100;
	private static int HEIGHT = 100;
	private static final int TEST_X = WIDTH / 2;
	private static final int TEST_Y = HEIGHT / 2;

	private static int TIMES_TRY = 1000;
	private static int TIMES_PRE = 1000;

	private static boolean DEBUG_PRINT = false;

	private static boolean[][] TEST_BED = new boolean[][] {
			{ false, false, false }, { false, false, true },
			{ true, true, false } };

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
		boolean[][] org = new boolean[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (Math.random() < 0.3) {
					org[i][j] = true;
				}
			}
		}
		// org = TEST_BED;
		org[TEST_X][TEST_Y] = false;

		test2(org);
		// test1(org);
	}

	private static long time() {
		return System.currentTimeMillis();
	}

	private static void test1(boolean[][] org) {

		boolean[][] ba2 = new boolean[WIDTH][HEIGHT];
		{
			copy(org, ba2);
			long t = time();
			int count = count(ba2, TEST_X, TEST_Y, WIDTH, HEIGHT) - 1;
			// System.out.println("count="+count);
			System.out.println("Time : " + (time() - t) + " ns");
			print(ba2, WIDTH, HEIGHT);
		}

		{
			{
				copy(org, ba2);
				long t = time();
				int count = scanlineSeedfill(ba2, TEST_X, TEST_Y, WIDTH, HEIGHT) - 1;
				// System.out.println("count="+count);
				System.out.println("Time : " + (time() - t) + " ns");
				print(ba2, WIDTH, HEIGHT);
			}
		}
	}

	private static void test2(boolean[][] org) {
		boolean[][] ba2 = new boolean[WIDTH][HEIGHT];
		{
			long timeSum = 0;
			for (int i = 0; i < TIMES_TRY + TIMES_PRE; i++) {
				copy(org, ba2);
				long t = time();
				int count = scanlineSeedfill(ba2, TEST_X, TEST_Y, WIDTH, HEIGHT);
				if (i < TIMES_PRE)
					continue;
				timeSum += (time() - t);
				// System.out.println("count="+count);
			}
			System.out.println("Time : " + timeSum + " ns");
			print(ba2, WIDTH, HEIGHT);
		}

		{
			long timeSum = 0;
			for (int i = 0; i < TIMES_TRY + TIMES_PRE; i++) {
				copy(org, ba2);
				long t = time();
				int count = count(ba2, TEST_X, TEST_Y, WIDTH, HEIGHT) - 1;
				if (i < TIMES_PRE)
					continue;
				timeSum += (time() - t);
				// System.out.println("count="+count);
			}
			System.out.println("Time : " + timeSum + " ns");
			print(ba2, WIDTH, HEIGHT);
		}

	}

	public static int scanlineSeedfill(boolean[][] walls, int x, int y,
			int width, int height) {

		int count = 0;

		int cX = -1;
		int cY = -1;

		Stack<Integer> stX = new Stack<Integer>();
		Stack<Integer> stY = new Stack<Integer>();
		stX.push(x);
		stY.push(y);

		// スタックが空になるまで続ける
		while (!stX.empty()) {
			// スタックから１つ取り出す
			int xx = stX.pop();
			int yy = stY.pop();

			if (isWall(walls, xx, yy, width, height)) {
				// System.out.println("error!");
				continue;
			}
			walls[xx][yy] = true;
			count++;

			// 取り出した点の右側をr、左側をlに格納
			int r = xx;
			int l = xx;
			// 右端を調べる
			while (++r < width) {
				// 壁に当たるまで右側を調べる
				if (isWall(walls, r, yy, width, height)) {
					break;
				} else {
					// 壁でなければ壁にしてcount+1(塗りつぶし)
					walls[r][yy] = true;
					count++;
				}
			}
			r--;
			// 左端を調べる
			while (--l < width) {
				// 壁に当たるまで左側を調べる
				if (isWall(walls, l, yy, width, height)) {
					break;
				} else {
					// 壁でなければ壁にしてcount+1(塗りつぶし)
					walls[l][yy] = true;
					count++;
				}
			}
			l++;
			// System.out.println("l - r = " + l + " - " + r);

			if (yy + 1 < height) {
				cX = -1;
				cY = -1;
				for (int i = l; i <= r; i++) {
					if (!isWall(walls, i, yy + 1, width, height)) {
						cX = i;
						cY = yy + 1;
					} else {
						if (cX >= 0) {
							if (isWall(walls, cX, cY, width, height)) {
								System.out.println("check4");
							}
							stX.push(cX);
							stY.push(cY);
							cX = -1;
							cY = -1;
						}
					}
				}
				if (cX >= 0) {
					if (isWall(walls, cX, cY, width, height)) {
						System.out.println("check3");
					}
					stX.push(cX);
					stY.push(cY);
					cX = -1;
					cY = -1;
				}
			}
			// print(st);

			if (yy - 1 >= 0) {
				cX = -1;
				cY = -1;
				for (int i = l; i <= r; i++) {
					if (!isWall(walls, i, yy - 1, width, height)) {
						cX = i;
						cY = yy - 1;
					} else {
						if (cX >= 0) {
							if (isWall(walls, cX, cY, width, height)) {
								System.out.println("check2");
							}
							stX.push(cX);
							stY.push(cY);
							cX = -1;
							cY = -1;
						}
					}
				}
				if (cX >= 0) {
					if (isWall(walls, cX, cY, width, height)) {
						System.out.println("check1");
					}
					stX.push(cX);
					stY.push(cY);
					cX = -1;
					cY = -1;
				}
			}
		}

		return count;
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
