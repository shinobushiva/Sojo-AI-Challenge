/**
 * 
 */
package jp.ac.sojou.cis.shiva.tron.dev;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import tron.utils.Map;

class TronBotRenderer extends JFrame {

	private static final long serialVersionUID = -9213947703993218478L;

	private JPanel panel;

	private int width;
	private int height;
	private boolean[][] walls;
	private tron.utils.Point my;
	private tron.utils.Point op;

	private JTextArea area;

	private StringBuilder buf;

	public void log(Object log) {
		if (buf == null) {
			buf = new StringBuilder();
		}
		buf.append(log).append("\n");

		area.setText(buf.toString());
		area.setCaretPosition(buf.length());
	}

	public TronBotRenderer() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		panel = new JPanel() {
			private static final long serialVersionUID = -8880304131250288411L;

			@Override
			protected void paintComponent(Graphics g) {
				double w = panel.getWidth();
				double h = panel.getHeight();

				double xGrid = w / width;
				double yGrid = h / height;

				int gridSize = (int) Math.min(xGrid, yGrid);

				for (int i = 0; i <= width; i++) {
					g.drawLine((int) (gridSize * i), 0, (int) (gridSize * i),
							(int) h);
				}
				for (int i = 0; i <= height; i++) {
					g.drawLine(0, (int) (gridSize * i), (int) w,
							(int) (gridSize * i));
				}

				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						if (walls[i][j]) {
							g.fillRect(i * gridSize, j * gridSize, gridSize,
									gridSize);
						}
					}
				}

				g.setColor(Color.BLUE);
				if (my != null) {
					g.fillRect(my.getX() * gridSize, my.getY() * gridSize,
							gridSize, gridSize);
				}

				g.setColor(Color.RED);
				if (op != null) {
					g.fillRect(op.getX() * gridSize, op.getY() * gridSize,
							gridSize, gridSize);
				}
			}
		};

		area = new JTextArea();
		area.setEditable(false);

		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel,
				new JScrollPane(area));
		jsp.setResizeWeight(0.8);
		add(jsp);

		setBounds(100, 100, 640, 480);
		setVisible(true);
	}

	public void callback(Map m) {
		width = m.getWidth();
		height = m.getHeight();
		walls = m.getWalls();
		my = m.getMyLocation();
		op = m.getOpponentLocation();

		repaint();

	}
}