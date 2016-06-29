package solver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.encog.Encog;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodSingle;

import downloaded.CannyEdgeDetector;
import others.ArrayData;
import others.ImageProcess;
import ownMethods.MeanShift;
import ownMethods.SOMMethods;

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
		
// MEDIAN FILTER
		//outputImage = medianCalc(data);
		//ImageProcess.saveImage(outputImagePath + "1.png", outputImage);
		//LOGGER.info("Run:After_Median");
		
// CANNY EDGE DETECTION
		outputImage = CannyCalc( outputImage);
		ImageProcess.saveImage(outputImagePath + "2.png", outputImage);
		LOGGER.info("Run:After_Canny");

// SOM
//		somCalc(outputImage);
		
		
		
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
	
	private static void somCalc(BufferedImage inputImage) {
		
		SOMMethods.test1();
		
		
	}

	
	private static String[] getImagesArray(String folderRootPath) {
		/**
		 * 1. Podajemy œciezke folderu ze zdjeciami
		 * 		* szkielet - nazwa/SEQxxxx/PARTxxxx/
		 * 2. Iteruje po folderach w poszukiwaniu zdjec
		 * 3. zapisac sciezki zdjec do listy
		 * 4. zwrocic array
		 */
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
		
		return (String[]) picturesList.toArray();
	}
	
	
}


























