/**
 * 
 */
package jp.ac.sojou.cis.shiva.tron.bin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

/**
 * このクラスはトロンの対戦結果を見るためのビューワーです。
 * 
 * @author shiva
 * 
 */
public class TronMatchViewer extends JFrame {

	private static final long serialVersionUID = -9213947703993218478L;

	private JPanel panel;

	private List<char[][]> mapList;

	private int width;
	private int height;

	private JButton next;
	private JButton prev;
	private JButton first;
	private JButton last;

	private JButton openFile;
	private JLabel label;

	private int frame = 0;

	public TronMatchViewer() {
		mapList = new ArrayList<char[][]>();

		panel = new JPanel() {
			private static final long serialVersionUID = -8880304131250288411L;

			@Override
			protected void paintComponent(Graphics g) {
				if (mapList.size() == 0) {
					return;
				}
				double w = panel.getWidth();
				double h = panel.getHeight();

				double xGrid = w / width;
				double yGrid = h / height;

				char[][] map = mapList.get(frame);

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
						if (map[i][j] == '#') {
							g.setColor(Color.BLACK);
							g.fillRect(i * gridSize, j * gridSize, gridSize,
									gridSize);
						} else if (map[i][j] == '1') {
							g.setColor(Color.BLUE);
							g.fillRect(i * gridSize, j * gridSize, gridSize,
									gridSize);
						} else if (map[i][j] == '2') {
							g.setColor(Color.RED);
							g.fillRect(i * gridSize, j * gridSize, gridSize,
									gridSize);
						}
					}
				}
			}
		};
		panel.setBorder(new EtchedBorder());

		first = new JButton("First");
		first.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				frame = 0;
				repaint();
			}
		});
		prev = new JButton("Prev");
		prev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (frame > 0) {
					frame--;
					repaint();
				}
			}
		});
		next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (frame < mapList.size() - 1) {
					frame++;
					repaint();
				}
			}
		});
		last = new JButton("Last ");
		last.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				frame = mapList.size() - 1;
				repaint();
			}
		});

		JPanel bp = new JPanel();
		bp.add(first);
		bp.add(prev);
		bp.add(next);
		bp.add(last);
		bp.setBorder(new EtchedBorder());

		openFile = new JButton("Open...");
		openFile.addActionListener(new ActionListener() {
			JFileChooser jfc = new JFileChooser("logs");

			@Override
			public void actionPerformed(ActionEvent ae) {
				int op = jfc.showOpenDialog(TronMatchViewer.this);
				if (op == JFileChooser.APPROVE_OPTION) {
					try {

						int w = 0;
						int h = 0;
						ArrayList<char[][]> mapList = new ArrayList<char[][]>();

						File f = jfc.getSelectedFile();
						BufferedReader r = new BufferedReader(new FileReader(f));
						String str = r.readLine();
						while (str != null) {
							String[] wh = str.split(" ");
							w = Integer.parseInt(wh[0]);
							h = Integer.parseInt(wh[1]);
							char[][] map = new char[w][h];
							for (int j = 0; j < h; j++) {
								String line = r.readLine();
								for (int i = 0; i < w; i++) {
									map[i][j] = line.charAt(i);
								}
							}
							mapList.add(map);
							str = r.readLine();
						}

						TronMatchViewer.this.width = w;
						TronMatchViewer.this.height = h;
						TronMatchViewer.this.mapList = mapList;

						label.setText(f.getName());
						repaint();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		label = new JLabel("");
		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, label,
				openFile);
		jsp.setResizeWeight(1.0);
		jsp.setDividerSize(0);

		add(jsp, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(bp, BorderLayout.SOUTH);

	}

	public static void main(String[] args) {
		TronMatchViewer p = new TronMatchViewer();
		p.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		p.setBounds(100, 100, 480, 480);
		p.setVisible(true);
	}
}