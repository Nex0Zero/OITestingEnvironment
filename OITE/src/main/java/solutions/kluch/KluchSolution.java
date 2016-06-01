package solutions.kluch;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class KluchSolution {

	private BufferedImage mask;
	private int width;
	private int height;
	
	public BufferedImage findCorridor(BufferedImage image) {
		createBlackMask(image);
		Color matchColor = new Color(245,255,204);
		int tolerance = 10;
		findMatches(image, mask, matchColor, tolerance);
//		optimalization(mask);
		return mask;
	}
	

	public BufferedImage findDoor(BufferedImage image) {
		createBlackMask(image);
		Color matchColor = new Color(150,75,0);
		int tolerance = 30;
		findMatches(image, mask, matchColor, tolerance);
//		optimalization(mask);
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
					
					mask.setRGB(i, j, white.getRGB());				
				}
			}
		}
	}
	/**
	 * Przechodz¹c po pikselach tworzy tablice 3x3
	 * sprawdza czy jest przewaga bia³ych punktów
	 * jeœli tak to wype³nia reszte maski bia³ymi punktami
	 * @param image
	 */
	private void optimalization(BufferedImage image){
		for(int i = 0; i < width-2 ; i++){
			int[][] tab  = new int[3][3];
			for(int j = 0; j < height-2; j++){

				for(int p = 0; p < 3; p++){
					for(int k=0;k<3;k++){
						Color imageColor = new Color(image.getRGB(i+p, j+k));
						tab[p][k] = imageColor.getRGB();
					}
				}
				int counter = 0;
				for(int p = 0; p < 3; p++){
					for(int k=0;k<3;k++){
						if(tab[p][k] == 255)
							counter++;
					}
				}
				
				if(counter >= 6){
					for(int p = 0; p < 3; p++){
						for(int k=0;k<3;k++){
							image.setRGB(i+k, j+p, 255);
						}
					}
				}
				
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
