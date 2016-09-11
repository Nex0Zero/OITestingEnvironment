package accurancyCalculation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class AccCalcDoorsAll {
	
	private static final Logger LOGGER= Logger.getLogger( AccCalcDoorsAll.class.getName() );

	public static void main(String[] args) {
		/*
corridorMasks\SEQ\PART\
corridor-Graf\SEQ\PART\door0
GrafSolution
		 */
		accCalc3( new File(args[0]), new File(args[1]), args[2] );
		
	}
	
	private static void accCalc(File original, File solution, String name) {
		String doors = "door0";
		// original = corridorMasks
		// solution = corridor-Graf
		double resultFULL = 0;
		
		// SEQ
		File[] fileSEQ = original.listFiles();
		for(int s = 0; s < fileSEQ.length; s++) {
			double resultSEQ = 0;
			
			// PART
			File[] filePART = fileSEQ[s].listFiles();
			for(int p = 0; p < filePART.length; p++) {
				double result = 0;
				
				LOGGER.info("Calc location: " + filePART[p].getPath());
				
				// original
				File originalPicFiles[] = filePART[p].listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.startsWith("Door_");
					}
				});
				// solution
				String solutionPath = solution.getAbsolutePath() + "/" + fileSEQ[s].getName() + "/" + filePART[p].getName()
						+ "/" + doors;
				File solutionPicFiles[] = new File(solutionPath).listFiles();

				BufferedImage originalImage = null;
				BufferedImage solutionImage = null;
				double localResult;
				for (int i = 0; i < solutionPicFiles.length; i++) {
					// Calc
					// Load images
					try {
						originalImage = mergeDoors(originalPicFiles, i);
						solutionImage = ImageIO.read(solutionPicFiles[i]);
					} catch (IOException e) {
						// TODO Auto-generated catch block

					}

					// Compare images  HERE!!!
					localResult = compareImagesGray(originalImage, solutionImage);
					// Add to result
					result += localResult;
				}
				// Average
				result = (double) (result / solutionPicFiles.length);
				resultSEQ += result;
			}
			resultSEQ = resultSEQ / (double) filePART.length;
			resultFULL += resultSEQ;
		}
		resultFULL = resultFULL / fileSEQ.length;
		
		System.out.println(name + ": " + AccCalcDoorsAll.class.getName() + ": result: " + resultFULL);
	}
	private static void accCalc2(File original, File solution, String name) {
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
	private static void accCalc3(File original, File solution, String name) {
		String doors = "door0";
		// original = corridorMasks
		// solution = corridor-Graf
		double resultFULL = 0;
		
		// SEQ
		File[] fileSEQ = original.listFiles();
		for(int s = 0; s < fileSEQ.length; s++) {
			double resultSEQ = 0;
			
			// PART
			File[] filePART = fileSEQ[s].listFiles();
			for(int p = 0; p < filePART.length; p++) {
				double result = 0;
				
				LOGGER.info("Calc location: " + filePART[p].getPath());
				
				// original
				File originalPicFiles[] = filePART[p].listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {		
						return name.startsWith("Door_");
					}
				});
				// solution
				String solutionPath = solution.getAbsolutePath() + "/" + fileSEQ[s].getName() + "/" + filePART[p].getName()
						+ "/" + doors;
				File solutionPicFiles[] = new File(solutionPath).listFiles();

				BufferedImage originalImage = null;
				BufferedImage solutionImage = null;
				double localResult;
				for (int i = 0; i < solutionPicFiles.length; i++) {
					// Calc
					// Load images
					try {
						originalImage = mergeDoors(originalPicFiles, i);
						solutionImage = ImageIO.read(solutionPicFiles[i]);
					} catch (IOException e) {
						// TODO Auto-generated catch block

					}

					// Compare images  HERE!!!
					localResult = compareImages(originalImage, solutionImage);
					// Add to result
					result += localResult;
				}
				// Average
				result = (double) (result / solutionPicFiles.length);
//				System.out.println("PART: " + result + "\n");
				resultSEQ += result;
			}
			resultSEQ = resultSEQ / (double) filePART.length;
//			System.out.println("SEQ: " + resultSEQ + "\n_____________________________\n\n\n\n");
			resultFULL += resultSEQ;
		}
		resultFULL = resultFULL / fileSEQ.length;
		
		System.out.println(name + ": " + AccCalcDoorsAll.class.getName() + ": result: " + resultFULL);
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
		result = (double)matchingWhite / (double)(matchingWhite+mismatch+0.0001);
		
		if(result < 0 || result > 1)
			result = 0;
		
		return result;
	}
	
	private static double compareImagesGray(BufferedImage originalImage, BufferedImage solutionImage) {
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
				Color color = new Color( originalImage.getRGB(w, h) );
				Color color2= new Color( solutionImage.getRGB(w, h) );
				originalColor = color.getRed();
				solutionColor = color2.getRed();
				
				// white match
				if(solutionColor > 10 && originalColor > 10)
					matchingWhite += 1;
				// mismatch
				if(originalColor <= 10 && solutionColor > 10)
					mismatch += 1;
				if(originalColor > 10 && solutionColor <= 10)
					mismatch += 1;
			}

		// acc formula
		result = (double)matchingWhite / (double)(matchingWhite+mismatch+0.0001);
		
		if(result < 0 || result > 1)
			result = 0;
		
		return result;
	}
	
}











