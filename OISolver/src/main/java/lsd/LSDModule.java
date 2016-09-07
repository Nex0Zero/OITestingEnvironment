package lsd;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import others.ImageProcess;

public class LSDModule {
	
	public static String path = "src/main/resources/LSD-test/";
	public static String file1 = "piet.jpg";
	public static String file2 = "steam.jpg";
	public static String file3 = "doors.jpg";
	
	public static BufferedImage LSDImageTest() {
		String filepath = path + file3;
		
		return filepathToLSDImage(filepath);
		
	}
	public static BufferedImage filepathToLSDImage(String filepath) {
		BufferedImage image = ImageProcess.loadImage(filepath);
		
		return imageToLSDImage(image);
		
	}
	public static BufferedImage imageToLSDImage(BufferedImage image) {		
		HashSet<Line> lines = imageToLSDLines(image);
		
		return linesToLSDImage(image, lines);

	}
	public static HashSet<Line> imageToLSDLines(BufferedImage image) {
		Graphics2D g2d = image.createGraphics();
		int x = image.getWidth();
		int y = image.getHeight();
		
		HashSet<Line> lines = new HashSet<Line>();


		double [] arr = image.getData().getPixels(0,0,x,y,new double[x*y*3]);

		double [] arr2 = new double[x*y];
	
		System.out.println("	pixels : " + arr.length);
		int c=0;
		for(int i = 0; i < arr.length-3; i+=3) {
			double B = arr[i];
			double G = arr[i+1];
			double R = arr[i+2];
			double level = R * 0.2126 + G * 0.7152 + B * 0.0722;
			arr2[c++] = level;
		}

		LSD lsd = new LSD();

		double [] out = lsd.lsd(arr2,x,y);

		for(int i = 0; i < lsd.n_out; i++) {
			for (int j = 0; j < 7; j++)
			
			lines.add(new Line(out[7 * i + 0], out[7 * i + 1],
					out[7 * i + 2], out[7 * i + 3]));

		}
		
		return lines;
	}
	public static BufferedImage linesToLSDImage(BufferedImage image, HashSet<Line> lines) {
		int x = image.getWidth();
		int y = image.getHeight();
		BufferedImage myPictureClear = new BufferedImage(x, y, image.getType());
		Graphics2D g2dClear = myPictureClear.createGraphics();
		for ( Line l : lines ) {
			g2dClear.drawLine((int)l.x1,(int)l.y1,(int)l.x2,(int)l.y2);
		}
		
		return myPictureClear;
		
	}
	public static BufferedImage linesToLSDImage(BufferedImage image, List<Line> lines) {
		int x = image.getWidth();
		int y = image.getHeight();
		BufferedImage myPictureClear = new BufferedImage(x, y, image.getType());
		Graphics2D g2dClear = myPictureClear.createGraphics();
		
		int r = 255, g = 255, b = 255;
		Color color = new Color(255,255,255);
		for ( Line l : lines ) {
			
			g2dClear.setColor(color);
			g2dClear.drawLine((int)l.x1,(int)l.y1,(int)l.x2,(int)l.y2);
			if(r > 70) {
				r -= 10;
				color = new Color(r, g, b);
			} else if(g > 70) {
				g -= 10;
				color = new Color(255, g, b);
			} else if(b > 70) {
				b -= 10;
				color = new Color(255, 255, b);
			}
				
		}
		
		return myPictureClear;
		
	}
	
}











