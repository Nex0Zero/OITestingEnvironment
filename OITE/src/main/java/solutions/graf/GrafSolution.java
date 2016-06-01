package solutions.graf;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


/**
 * @author Nex0Zero
 *
 */
public class GrafSolution{
	
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
		
		String savePath = setName + "-Graf/" + parts[1] + "/" + parts[2] + "/";
		LOGGER.info("Location for generated masks: " + savePath);
		
		for(int i = 0; i < fileElements.length; i++) {
			BufferedImage image = null;
			
			BufferedImage[] doorMasks = new BufferedImage[1];
			BufferedImage corridorMask;
			
			try {
				// read image
				image = ImageIO.read( fileElements[i] );
				
				// find doors (masks)
				doorMasks[0] = findDoors(image);
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
	
	public static BufferedImage findDoors(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// Logic
		Color botTolColor = new Color(125, 96, 76);
		Color topTolColor = new Color(158, 129, 109);
		colorTolerance(image, mask, botTolColor, topTolColor);
		// Logic end
		
		return mask;
	}
	
	private static void colorTolerance(BufferedImage original, BufferedImage mask, Color botTolColor, Color topTolColor) {
		int width = original.getWidth();
		int height = original.getHeight();
		
		Color white = new Color(255, 255, 255);
		
		Color color;
		for(int w = 0; w < width; w++)
			for(int h = 0; h < height; h++) {
				color = new Color( original.getRGB(w, h) );
				if( 
						botTolColor.getRed() <= color.getRed() && color.getRed() <= topTolColor.getRed() &&
						botTolColor.getGreen() <= color.getGreen() && color.getGreen() <= topTolColor.getGreen() &&
						botTolColor.getBlue() <= color.getBlue() && color.getBlue() <= topTolColor.getBlue()
						) {
					mask.setRGB(w, h, white.getRGB());
				}
			}
		
	}

	public static BufferedImage findCorridor(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// Logic
		Color botTolColor = new Color(162, 150, 130);
		Color topTolColor = new Color(201, 188, 169);
		colorTolerance(image, mask, botTolColor, topTolColor);
		// Logic end;
		
		return mask;
	}

}









