package jp.ac.sojou.cis.shiva.tron.dev;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import tron.utils.Map;
import tron.utils.Point;


class TronBotFileWriter {
	private int width;
	private int height;
	private boolean[][] walls;
	private Point my;
	private Point op;

	private BufferedWriter w = null;

	public TronBotFileWriter() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

		File dir = new File("logs");
		dir.mkdirs();
		File f = new File(dir, "Match_at_" + sdf.format(d) + ".txt");

		try {
			w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					f)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void callback(Map m) {

		width = m.getWidth();
		height = m.getHeight();
		walls = m.getWalls();
		my = m.getMyLocation();
		op = m.getOpponentLocation();
		try {
			w.write("" + width);
			w.write(" ");
			w.write("" + height);
			w.newLine();
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {
					if (my.getX() == i && my.getY() == j) {
						w.write('1');
					} else if (op.getX() == i && op.getY() == j) {
						w.write('2');
					} else if (walls[i][j]) {
						w.write('#');
					} else {
						w.write(' ');
					}
				}
				w.newLine();
			}
			w.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
