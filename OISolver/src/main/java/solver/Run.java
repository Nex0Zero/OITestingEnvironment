package solver;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import imageDetection.CannyEdgeDetector;
import imageDetection.MeanShift;
import others.ArrayData;
import others.ImageProcess;

/**
 * @author Nex0Zero
 *
 */
public class Run {

	private static final Logger LOGGER = Logger.getLogger(Run.class.getName());
	
	public static void main(String[] args) {
		LOGGER.info("Run:START");
		
		// Assign args
		String inputImagePath = args[0];
		String outputImagePath = args[1];
		
		// load Image
		ArrayData data;
		data = ImageProcess.loadImageData( inputImagePath );
		BufferedImage inputImage = ImageProcess.loadImage( inputImagePath );

		BufferedImage outputImage = ImageProcess.getImageFromData(data);
		
		// Median Filter
		//outputImage = medianCalc(data);
		//ImageProcess.saveImage(outputImagePath + "1.png", outputImage);
		//LOGGER.info("Run:After_Median");
		
		// Canny Edge Detection
		outputImage = CannyCalc( outputImage);
		ImageProcess.saveImage(outputImagePath + "2.png", outputImage);
		LOGGER.info("Run:After_Canny");
		
		
		LOGGER.info("Run:END");
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
	
}


























