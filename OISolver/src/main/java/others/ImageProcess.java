package others;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageProcess {
	
	static public ArrayData loadImageData(String fileName) {
		
		ArrayData data = null;
		
		try {
			// Load Image
			BufferedImage inputImage = ImageIO.read(new File(fileName));
			int imageType = inputImage.getType();
			
			// Image info
			int height = inputImage.getHeight();
			int width = inputImage.getWidth();
			
			
			// fill table
			int[][][] table = new int[3][height][width];
			Color color;
			for(int h = 0; h < height; h++)
				for(int w = 0; w < width; w++) {
					color = new Color( inputImage.getRGB(w, h) );
					table[0][h][w] = color.getRed();
					table[1][h][w] = color.getGreen();
					table[2][h][w] = color.getBlue();
				}
			
			// create new ArrayData
			data = new ArrayData(table, imageType);
			
		} catch (IOException e) { e.printStackTrace(); }
		
		return data;
	}

	static public void saveImageData(String fileName, ArrayData data) {

		BufferedImage outputImage = getImageFromData(data);
		try {

			ImageIO.write(outputImage, "PNG", new File(fileName));

		} catch (IOException e) { e.printStackTrace(); }
		
	}
	
	static public BufferedImage loadImage(String fileName) {
		
		BufferedImage inputImage = null;
		
		try {
			// Load Image
			inputImage = ImageIO.read(new File(fileName));
			
		} catch (IOException e) { e.printStackTrace(); }
		
		return inputImage;
	}

	static public void saveImage(String fileName, BufferedImage outputImage) {

		try {
			
			ImageIO.write(outputImage, "PNG", new File(fileName));
			
		} catch (IOException e) { e.printStackTrace(); }		
	}

	static public BufferedImage getImageFromData(ArrayData data) {
		int size = data.getN();
		int height = data.getH();
		int width = data.getW();
		
		BufferedImage outputImage = new BufferedImage(width, height, data.getImageType());
		
		Color color;
		for(int h = 0; h < height; h++)
			for(int w = 0; w < width; w++) {
				color = new Color(
						data.get(0, h, w), 
						data.get(1, h, w), 
						data.get(2, h, w));
				outputImage.setRGB(w, h, color.getRGB());
			}
		
		return outputImage;
	}
	
	static public ArrayData loadImageDataFromBI(BufferedImage inputImage) {
		ArrayData data = null;

		// Load Image
		int imageType = inputImage.getType();

		// Image info
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();

		// fill table
		int[][][] table = new int[3][height][width];
		Color color;
		for (int h = 0; h < height; h++)
			for (int w = 0; w < width; w++) {
				color = new Color(inputImage.getRGB(w, h));
				table[0][h][w] = color.getRed();
				table[1][h][w] = color.getGreen();
				table[2][h][w] = color.getBlue();
			}

		// create new ArrayData
		data = new ArrayData(table, imageType);

		return data;
	}

}





















