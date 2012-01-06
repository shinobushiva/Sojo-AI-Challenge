import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Map2Image {

	public static void main(String[] args) throws IOException {

		File dir = new File("maps");
		File destDir = new File("map2img");
		destDir.mkdirs();

		System.out.println(dir.getAbsolutePath());

		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.getName().endsWith(".txt")) {
				BufferedReader br = new BufferedReader(new FileReader(file));

				String line = null;
				line = br.readLine();
				String[] split = line.split(" ");
				int w = Integer.parseInt(split[0]);
				int h = Integer.parseInt(split[1]);

				BufferedImage bi = new BufferedImage(w, h,
						BufferedImage.TYPE_4BYTE_ABGR);

				BufferedImage bi2 = new BufferedImage(w * 10, h * 10,
						BufferedImage.TYPE_4BYTE_ABGR);
				// bi2.getGraphics().drawImage(bi, 0, 0,

				int r = 0;
				while ((line = br.readLine()) != null) {
					for (int i = 0; i < w; i++) {
						char c = line.charAt(i);
						switch (c) {
						case '1':
							bi.setRGB(i, r, Color.BLUE.getRGB());
							break;
						case '2':
							bi.setRGB(i, r, Color.RED.getRGB());
							break;
						case ' ':
							bi.setRGB(i, r, Color.WHITE.getRGB());
							break;
						case '#':
							bi.setRGB(i, r, Color.DARK_GRAY.getRGB());
							break;
						}
					}
					r++;
				}

				ImageIO.write(bi, "png", new File(destDir, file.getName()
						.replace(".txt", ".png")));

			}
		}

	}
}
