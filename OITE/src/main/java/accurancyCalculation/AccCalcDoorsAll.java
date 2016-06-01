package accurancyCalculation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AccCalcDoorsAll {

	public static void main(String[] args) {
		/*
corridorMasks\SEQ\PART\
corridor-Graf\SEQ\PART\door0
GrafSolution
		 */
		accCalc( new File(args[0]), new File(args[1]), args[2] );
		
	}
	
	private static void accCalc(File original, File solution, String name) {
		double result = 0;
		
		File originalPicFiles[] = original.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {		
				return name.startsWith("Door_");
			}
		});
		File solutionPicFiles[] = solution.listFiles();
		
		BufferedImage originalImage = null;
		BufferedImage solutionImage = null;
		double localResult;
		for(int i = 0 ; i < solutionPicFiles.length; i++) {
			// Calc
			// Load images
			try {
				originalImage = mergeDoors(originalPicFiles, i);
				solutionImage = ImageIO.read( solutionPicFiles[i] );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Compare images
			localResult = compareImages(originalImage, solutionImage);
			// Add to result
			result += localResult;
		}
		// Average
		result = (double)(result / solutionPicFiles.length);
		
		System.out.println(name + ": " + AccCalcDoorsAll.class.getName() + ": result: " + result);
	}
	
	private static BufferedImage mergeDoors(File doorFiles[], int index) {
		Color white = new Color(255, 255, 255);
		
		// pics in file[0]
		File[] picFiles = doorFiles[0].listFiles();
		// load pic[index]
		BufferedImage doorPic = null;
		try {
			doorPic = ImageIO.read( picFiles[index] );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = doorPic.getWidth();
		int height = doorPic.getHeight();
		
		// create merged pic
		BufferedImage merged = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_RGB);
		
		// first merge
		for(int w = 0; w < width; w++)
			for(int h = 0; h < height; h++)
				if(doorPic.getRGB(w, h) == white.getRGB())
					merged.setRGB(w, h, white.getRGB());
		
		// rest merges
		for(int i = 1; i < doorFiles.length; i++) {
			// pics in file[0]
			picFiles = doorFiles[i].listFiles();
			// load pic[index]
			doorPic = null;
			try {
				doorPic = ImageIO.read( picFiles[index] );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(int w = 0; w < width; w++)
				for(int h = 0; h < height; h++)
					if(doorPic.getRGB(w, h) == white.getRGB())
						merged.setRGB(w, h, white.getRGB());
		}
		
		return merged;
	}

	private static double compareImages(BufferedImage originalImage, BufferedImage solutionImage) {
		double result;
		/*
		 * trafne / trafne + nietrafne
		 */
		
		if(originalImage.getWidth() != solutionImage.getWidth() || originalImage.getHeight() != solutionImage.getHeight()) {
			System.out.println("ERROR: dimensions are not equal!!!");
			return 0;
		}
		
		// white
		int whiteRGB = new Color(255,255,255).getRGB();
		
		int originalColor;
		int solutionColor;
		long matchingWhite = 0;
		long mismatch = 0;
		for(int w = 0; w < originalImage.getWidth(); w++)
			for(int h = 0; h < originalImage.getHeight(); h++) {
				originalColor = originalImage.getRGB(w, h);
				solutionColor = solutionImage.getRGB(w, h);
				
				// white match
				if(originalColor == solutionColor && originalColor == whiteRGB)
					matchingWhite += 1;
				// mismatch
				if(originalColor != solutionColor)
					mismatch += 1;
			}

		// acc formula
		result = (double)matchingWhite / (double)(matchingWhite+mismatch);
		
		return result;
	}
	
}











