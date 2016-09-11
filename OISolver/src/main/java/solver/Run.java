package solver;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import downloaded.CannyEdgeDetector;
import newSolver.NewRun;
import others.ArrayData;
import others.ImageProcess;
import ownMethods.MeanShift;
import ownMethods.SOMContener;
import ownMethods.SOMContener2;
import ownMethods.SOMMethods;

/**
 * @author Nex0Zero
 *
 */
public class Run {

	private static final Logger LOGGER = Logger.getLogger(Run.class.getName());
	
	static double LEARNING_RATE = 0.0001;
	static int ITERATIONS = 20;
	
	public static void main(String[] args) {
		LOGGER.info("Deploy:START");
		long start=System.currentTimeMillis();
		
		SOMContener contener = new SOMContener(3, 2);
		SOMContener2 contener2 = new SOMContener2(2, 4);
		
		// LEARNING
//		learningM(args, contener);
//		learningP(args, contener2);

		// DEPLOY
//		deploy(args, contener);
		deployP(args, contener2);
		
		// TIME runned
		long stop=System.currentTimeMillis();
		timeRunned(start, stop);
		
		LOGGER.info("Deploy:END");
	}
	/**
	 * Drzwi
	 */
	private static void learningM(String[] args, SOMContener contener) {
		String folderRootPathPictures = args[0];
		String folderRootPathMasks = args[1];
		
		File pictures = new File( folderRootPathPictures );
		File masks = new File( folderRootPathMasks );
		
		// SEQ
		File[] fileSEQ = pictures.listFiles();
		for(int s = 0; s < fileSEQ.length; s++) {
			
			// PART
			File[] filePART = fileSEQ[s].listFiles();
			for(int p = 0; p < filePART.length; p++) {
				
				LOGGER.info("Learn location: " + filePART[p].getPath());
			
				// zdjecia
				File picturesFiles[] = filePART[p].listFiles();
			
				// maski
				String masksPath = masks.getAbsolutePath() + "/" + fileSEQ[s].getName() + "/" + filePART[p].getName()
						+ "/";
				
				File maskFile = new File(masksPath);
				File masksFiles[] = maskFile.listFiles(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return name.startsWith("Door_");
					}
				});
				
				if (masksFiles.length >= 1) {
					BufferedImage picture = null;
					BufferedImage mask = null;
					for (int i = 0; i < picturesFiles.length; i++) {
						// Load images
						try {
							picture = ImageIO.read(picturesFiles[i]);
							mask = mergeDoors(masksFiles, i);
						} catch (IOException e) {
							/* TODO */ }

						// NAUKA
						LOGGER.info("Doors s: " + fileSEQ[s].getName() + " p:" + filePART[p].getName() + " i:" + i);
						contener.runBasicTrain(LEARNING_RATE, ITERATIONS, picture, mask);
					}
				}
			}
		}
				
					
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
	
	private static void deploy(String[] args, SOMContener contener) {
		String path = args[2];
		
		File pathFile = new File( path );
		
		// SEQ
		File[] fileSEQ = pathFile.listFiles();
		for(int s = 0; s < fileSEQ.length; s++) {
			
			// PART
			File[] filePART = fileSEQ[s].listFiles();
			for(int p = 0; p < filePART.length; p++) {
				String savePath = path + "-Graf/" + fileSEQ[s].getName() + "/" + filePART[p].getName() + "/";
				LOGGER.info("Location for generated masks: " + savePath);
			
				// Picture
				File[] filePicture = filePART[p].listFiles();
			
				for(int i = 0; i < filePicture.length; i++) {
					BufferedImage image = null;
			
					BufferedImage[] doorMasks = new BufferedImage[1];
			
					try {
						// read image
						image = ImageIO.read( filePicture[i] );
						
						// find doors (masks)
						LOGGER.info("s: "+s +" p:"+p +" i:"+i);
						doorMasks[0] = contener.runBasic(image);
						
						// save doors
						for(int j = 0; j < doorMasks.length; j++) {
							String doorName = "door-pic" + i;
							File outputFile = new File(savePath + "door" + j + "/" + doorName + ".png");
							outputFile.mkdirs();
							ImageIO.write(doorMasks[j], "png", outputFile);
						}
						
					} catch (IOException e) { e.printStackTrace(); }
				}
			}
		}	
			
	}
	
	/**
	 * Podloga
	 */
	private static void learningP(String[] args, SOMContener2 contener) {
		String folderRootPathPictures = args[0];
		String folderRootPathMasks = args[1];
		
		File pictures = new File( folderRootPathPictures );
		File masks = new File( folderRootPathMasks );
		
		// SEQ
		File[] fileSEQ = pictures.listFiles();
		for (int s = 0; s < fileSEQ.length; s++) {

			// PART
			File[] filePART = fileSEQ[s].listFiles();
			for (int p = 0; p < filePART.length; p++) {

				LOGGER.info("Learn location: " + filePART[p].getPath());

				// zdjecia
				File picturesFiles[] = filePART[p].listFiles();

				// maski
				String masksPath = masks.getAbsolutePath() + "/" + fileSEQ[s].getName() + "/" + filePART[p].getName()
						+ "/";

				File maskFile = new File(masksPath);
				File masksFiles[] = maskFile.listFiles(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return name.startsWith("Corridor_");
					}
				});

				BufferedImage picture = null;
				BufferedImage mask = null;
				
				File masksImg[] = masksFiles[0].listFiles();
				for (int i = 0; i < picturesFiles.length; i++) {
					// Load images
					try {
						picture = ImageIO.read(picturesFiles[i]);
						mask = ImageIO.read(masksImg[i]);
					} catch (IOException e) {
						/* TODO */ }

					// NAUKA
					LOGGER.info("Corridor s: " + fileSEQ[s].getName() + " p:" + filePART[p].getName() + " i:" + i);
					contener.runBasicTrain(LEARNING_RATE, ITERATIONS, picture, mask);
				}

			}
		}

	}
	
	private static void deployP(String[] args, SOMContener2 contener) {
		
		String path = args[0];
		
		File pathFile = new File( path );
		File pathFileSol = new File( path );

		// SEQ
		File[] fileSEQ = pathFile.listFiles();
		for(int s = 0; s < fileSEQ.length; s++) {
			
			// PART
			File[] filePART = fileSEQ[s].listFiles();
			for(int p = 0; p < filePART.length; p++) {
				String savePath = pathFileSol.getParent() +"/GKSolution/" + fileSEQ[s].getName() + "/" + filePART[p].getName() + "/";
				LOGGER.info("Location for generated masks: " + savePath);
			
				// Picture
				File[] filePicture = filePART[p].listFiles();
			
				for(int i = 0; i < filePicture.length; i++) {
					BufferedImage image = null;
			
					BufferedImage[] doorMasks = new BufferedImage[1];
					BufferedImage corridorMask;
					
					try {
						// read image
						image = ImageIO.read( filePicture[i] );
						
						// find doors (masks)
						LOGGER.info("s: "+s +" p:"+p +" i:"+i);
						//doorMasks[0] = contener.runBasic(image);
						doorMasks[0] = NewRun.doItDoorsFull(image);
						corridorMask = NewRun.doItCorridor(image);
						
						
						// save doors
						for(int j = 0; j < doorMasks.length; j++) {
							String doorName = "corridor-pic" + i;
							File outputFile = new File(savePath + "door" + j + "/" + doorName + ".png");
							outputFile.mkdirs();
							ImageIO.write(doorMasks[j], "png", outputFile);
						}
						// save corridor
						String corridorName = "corridor-pic" + i;	
						File outputFile = new File(savePath + "corridor/" + corridorName + ".png");
						outputFile.mkdirs();
						ImageIO.write(corridorMask, "png", outputFile);
						
						
					} catch (IOException e) { e.printStackTrace(); }
				}
			}
		}	
			
	}
	
	/**
	 * ARGS[]
	 * src\main\resources\Image.png
	 * src\main\resources\ProcessedImage
	 */
	private static void deployTest(String[] args) {
			
		
		// Assign args
		String inputImagePath = args[0];
		String outputImagePath = args[1];
		
		// load Image
		ArrayData data;
		data = ImageProcess.loadImageData( inputImagePath );
		BufferedImage inputImage = ImageProcess.loadImage( inputImagePath );

		BufferedImage outputImage = ImageProcess.getImageFromData(data);
		
// MEDIAN FILTER
		//outputImage = medianCalc(data);
		//ImageProcess.saveImage(outputImagePath + "1.png", outputImage);
		//LOGGER.info("Run:After_Median");
		
// CANNY EDGE DETECTION
//		outputImage = CannyCalc( outputImage);
//		ImageProcess.saveImage(outputImagePath + "2.png", outputImage);
//		LOGGER.info("Run:After_Canny");

// SOM
//		somCalc(outputImage);
		
// Images paths
		String[] paths = getImagesArray("C:/Users/Nex0Zero/Desktop/OI/GrafKluch/corridor");

	}
	
	private static BufferedImage medianCalc(ArrayData data) {
		BufferedImage outputImage;
		
		int size= 11, tolerance= 15, iterations= 3;
		ArrayData processedData = MeanShift.runMedianFilter(data, size, tolerance, iterations);
		// 11, 15, 6 - 19s
		// 11, 15, 6 - 13s
		outputImage = ImageProcess.getImageFromData(processedData);
		
		return outputImage;
	}

	private static BufferedImage CannyCalc(BufferedImage inputImage) {
		BufferedImage outputImage;
		
		CannyEdgeDetector detector = new CannyEdgeDetector();
		detector.setLowThreshold(1.0f);
		detector.setHighThreshold(2.0f);
		
		detector.setSourceImage(inputImage);
		detector.process();
		outputImage = detector.getEdgesImage();
		
		return outputImage;
	}
	
	private static void somCalc(BufferedImage inputImage) {
		
		SOMMethods.test1();
		
		
	}

	
	private static String[] getImagesArray(String folderRootPath) {

		List<String> picturesList = new ArrayList<String>();
		
		// ROOT
		File rootFile = new File(folderRootPath);
		
		File[] seqFiles = rootFile.listFiles();
		// for each SEQ
		for(File seqFile : seqFiles) {
			File[] partFiles = seqFile.listFiles();
			// for each PART
			for(File partFile : partFiles) {
				File[] picFiles = partFile.listFiles();
				// for each PICTURE
				for(File picFile : picFiles)
					picturesList.add( picFile.getAbsolutePath() );
			}
		}
		
		// List to Array
		String[] picturesArray = new String[picturesList.size()];
		picturesList.toArray(picturesArray);
		
		return picturesArray;
	}
	
	private static void timeRunned(long start, long stop) {
		long time = stop-start;
		String text = "";
		text += "Run worked -";

		text +=  " h:"+ ( time/(3600*1000) );

		text +=  " m:"+ ( time/(60*1000) );

		text +=  " s:"+ ( time/1000 %60 );
		
		text +=  " ms:"+ ( time %1000 );
		
		LOGGER.info( text );
	}
	
}


























