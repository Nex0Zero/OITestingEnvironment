package solutions.kluch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import solutions.graf.GrafSolution;


public class KluchSolution {

	private static final Logger LOGGER= Logger.getLogger( GrafSolution.class.getName() );

	/**
	 * @param args[0] - path to images (.../name/SEQ/PART/)
	 */
	public static void main(String[] args) {
		LOGGER.info("Logger Name: " + LOGGER.getName());
		
		//String path = "corridor/SEQ/PART/";
		String path = args[0];
		String[] parts = path.split("/");
		String setName = parts[0];
		
		File pathFile = new File(path);
		File[] fileElements = pathFile.listFiles();
		
		String savePath = setName + "-Kluch/" + parts[1] + "/" + parts[2] + "/";
		LOGGER.info("Location for generated masks: " + savePath);
		
		for(int i = 0; i < fileElements.length; i++) {
			BufferedImage image = null;
			
			BufferedImage[] doorMasks = new BufferedImage[1];
			BufferedImage corridorMask;
			
			try {
				// read image
				image = ImageIO.read( fileElements[i] );
				
				// find doors (masks)
				doorMasks[0] = findDoor(image);
				// find corridor (mask)
				corridorMask = findCorridor(image);
				
				// save doors
				for(int j = 0; j < doorMasks.length; j++) {
					String doorName = "door-pic" + i;
					File outputFile = new File(savePath + "door" + j + "/" + doorName + ".png");
					outputFile.mkdirs();
					ImageIO.write(doorMasks[j], "png", outputFile);
				}
				// save corridor
				String corridorName = "corridor-pic" + i;	
				File outputFile = new File(savePath + "corridor/" + corridorName + ".png");
				outputFile.mkdirs();
				ImageIO.write(corridorMask, "png", outputFile);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		LOGGER.info("Method End");
	}
	
	private static BufferedImage mask;
	private static int width;
	private static int height;
	
	public static BufferedImage findCorridor(BufferedImage image) {
		createBlackMask(image);
		Color matchColor = new Color(245,255,204);
		int tolerance = 100;
		findMatches(image, mask, matchColor, tolerance);
//		optimalization(mask);
		return mask;
	}
	

	public static BufferedImage findDoor(BufferedImage image) {
		createBlackMask(image);
		Color matchColor = new Color(150,75,0);
		int tolerance = 100;
		findMatches(image, mask, matchColor, tolerance);
//		optimalization(mask);
		return mask;
	}
	
	private static void createBlackMask(BufferedImage image){
		getImageSize(image);
		mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	private static void getImageSize(BufferedImage image){
		width = image.getWidth();
		height = image.getHeight();
	}

	private static void findMatches(BufferedImage image, BufferedImage mask, Color color, int tolerance){
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
