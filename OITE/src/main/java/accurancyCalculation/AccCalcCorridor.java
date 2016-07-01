package accurancyCalculation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class AccCalcCorridor {
	
	private static final Logger LOGGER= Logger.getLogger( AccCalcCorridor.class.getName() );
	
	public static void main(String[] args) {
		/*
		 corridor/SEQ/PART/
		 */
//		allSolutionAutoFinder(args[0]);
		
		/*
corridorMasks\SEQ\PART\Corridor_0
corridor-Graf\SEQ\PART\corridor
GrafSolution
		 */
		/*
corridorMasks\SEQ\PART\Corridor_0
corridor-Kluch\SEQ\PART\corridor
KluchSolution
		 */
		accCalc2( new File(args[0]), new File(args[1]), args[2] );
	}
	
	private static void allSolutionAutoFinder(String arg1) {
		LOGGER.info("Logger Name: " + LOGGER.getName());
		
		//String path = "corridor/SEQ/PART/";
		String path = arg1;
		String[] parts = path.split("/");
		final String setName = parts[0];
		
		String workingDir = System.getProperty("user.dir");		
		File solutionsFile = new File(workingDir);
		File[] fileElements = solutionsFile.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {		
				return name.startsWith(setName+"-");
			}
		});

		for(int i = 0; i <fileElements.length; i++) {			
			// Original Path
			String originalPath = setName + "Masks/";
			for(int j = 1; j < parts.length; j++)
				originalPath = originalPath + parts[j] + "/";
			File original = new File(originalPath);
			original = original.listFiles(new FilenameFilter() {		
				@Override
				public boolean accept(File dir, String name) {		
					return name.startsWith("Corridor");
				}
			}
			)[0];
			
			// Solution Path
			String solutionPath = fileElements[i].getName() + "/";
			for(int j = 1; j < parts.length; j++)
				solutionPath = solutionPath + parts[j] + "/";
			solutionPath += "corridor/";
			File solution = new File(solutionPath);
			
			// Acc Calc
			accCalc(original, solution, fileElements[i].getName());
		}
		
	}
	
	private static void accCalc(File original, File solution, String name) {
		double result = 0;
		
		File originalPicFiles[] = original.listFiles();
		File solutionPicFiles[] = solution.listFiles();
		
		if(originalPicFiles.length != solutionPicFiles.length) {
			System.out.println("ERROR: number of pictures is not equal!!!");
			return;
		}
		
		BufferedImage originalImage = null;
		BufferedImage solutionImage = null;
		double localResult;
		for(int i = 0 ; i < originalPicFiles.length; i++) {
			// Calc
			// Load images
			try {
				originalImage = ImageIO.read( originalPicFiles[i] );
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
		result = (double)(result / originalPicFiles.length);
		
		System.out.println(name + ": " + AccCalcCorridor.class.getName() + ": result: " + result);
	}
	private static void accCalc2(File original, File solution, String name) {
		String doors = "corridor0";
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
						return name.startsWith("Corridor_");
					}
				});
				// solution
				String solutionPath = solution.getAbsolutePath() + "/" + fileSEQ[s].getName() + "/" + filePART[p].getName()
						+ "/" + doors;
				File solutionPicFiles[] = new File(solutionPath).listFiles();

				BufferedImage originalImage = null;
				BufferedImage solutionImage = null;
				double localResult;
				File[] origs = originalPicFiles[0].listFiles();
				for (int i = 0; i < solutionPicFiles.length; i++) {
					// Calc
					// Load images
					try {
						originalImage = ImageIO.read(origs[i]);
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
				resultSEQ += result;
			}
			resultSEQ = resultSEQ / (double) filePART.length;
			resultFULL += resultSEQ;
		}
		resultFULL = resultFULL / fileSEQ.length;
		
		System.out.println(name + ": " + AccCalcCorridor.class.getName() + ": result: " + resultFULL);
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












