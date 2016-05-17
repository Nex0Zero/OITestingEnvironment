package solutions.kluch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import solutions.CaseSolution;

public class KluchSolution implements CaseSolution {

	private BufferedImage mask;
	private int width;
	private int height;
	private Color matchColor = new Color(150,75,0);
	private int tolerance = 30;
	
	
	@Override
	public BufferedImage findBench(BufferedImage image) {
		createBlackMask(image);
		findMatches(image, mask, matchColor, tolerance);
		return mask;
	}
	
	private void createBlackMask(BufferedImage image){
		getImageSize(image);
		mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	private void getImageSize(BufferedImage image){
		width = image.getWidth();
		height = image.getWidth();
	}

	private void findMatches(BufferedImage image, BufferedImage mask, Color color, int tolerance){
		Color white = new Color(255, 255, 255);
		
		for(int i = 0 ; i < width; i++){
			for(int j = 0; j < height; j++){
				Color imageColor = new Color(image.getRGB(i, j));
				
				if(Math.abs(color.getBlue() - imageColor.getBlue()) < tolerance &
				   Math.abs(color.getGreen() - imageColor.getGreen()) < tolerance &
				   Math.abs(color.getRed() - imageColor.getRed()) < tolerance){
					
					mask.setRGB(i, j, white.getRGB());				}
			}
		}
	}

	public BufferedImage getMask() {
		return mask;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setMask(BufferedImage mask) {
		this.mask = mask;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
