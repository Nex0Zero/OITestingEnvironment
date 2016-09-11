package newSolver;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineListener;

import VPC.Line;
import VPC.Point2DDouble;
import VPC.VanishingPointsCalculator;
import lsd.GUI;
import lsd.LSDModule;
import lsd.LineLSD;
import others.ImageProcess;

public class NewRun {

	public static void main(String[] args) {
		System.out.println("NewRun : Start");
		
		BufferedImage image;
	
		image = exampleTwo();
		
		// Open Folder
//		openFolder( LSDModule.path );
		
		// Save image
//		ImageProcess.saveImage(LSDModule.path + "LSDtest.png", image);
		
	}	
//	private static BufferedImage exampleOne() {
//		BufferedImage image;
//			
//		// LSD for image
//		image = LSDModule.LSDImageTest();
//
//		return image;
//	}
	private static BufferedImage exampleTwo() {
		BufferedImage image;
	
		// 1. Load image from file
		image = ImageProcess.loadImage("src/main/resources/LSD-test/doors.png");
		
		// 2. LSD Module - find lines
		double zm_sigma_scale = 1.0; // 0.65
		double zm_quant = 0.58; // 0.58
		double zm_ang_th = 27;	// 27
		double zm_density_th = 0.3; // 0.3	
		HashSet<LineLSD> lineLSDs = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		HashSet<LineLSD> linesF = (HashSet<LineLSD>) lineLSDs.clone();
		ImageProcess.saveImage(LSDModule.path + "01- All lines.png", 
				LSDModule.linesToLSDImage(image, linesF) );
		
		linesF = LineInterpreter.allExceptVerAndHor(linesF, 9, 9);
		ImageProcess.saveImage(LSDModule.path + "02- No VerHor.png", 
				LSDModule.linesToLSDImage(image, linesF) );
		linesF = LineInterpreter.sieveOnlyLong(linesF, 40);
		ImageProcess.saveImage(LSDModule.path + "03- No VerHor and short.png", 
				LSDModule.linesToLSDImage(image, linesF) );
		
		// X. Vanishing Point
		int iter = 10;
		Point2DDouble bestPoint = new Point2DDouble();
		Vector lineVec = createVectorLines(linesF);
		Vector supportVec = new Vector<Line>();
		Vector ignoreVec = new Vector<Line>();
		
		VanishingPointsCalculator.getVanishingPoint(iter, lineVec, bestPoint, supportVec, ignoreVec);
		
		// X. Lines to point
		HashSet<LineLSD> linesToPoint = (HashSet<LineLSD>) lineLSDs.clone();
		linesToPoint = LineInterpreter.linesToPoint(linesToPoint, bestPoint, 40);
		
		linesToPoint = LineInterpreter.allExceptVerAndHor(linesToPoint, 9, 9);
		
		BufferedImage imageTemp = LSDModule.linesToLSDImage(image, linesToPoint);
		ImageProcess.saveImage(LSDModule.path + "04- Lines To Point.png", 
				imageTemp );
		//	* sort
		List<LineLSD> linesToPointList;
		linesToPointList = LineInterpreter.sortLeftToRight(linesToPoint);
		
		// X. Vertical lines
		HashSet<LineLSD> linesVert = (HashSet<LineLSD>) lineLSDs.clone();
		linesVert = LineInterpreter.sieveOnlyVertical(linesVert, 9);
		//	* only long enough
		linesVert = LineInterpreter.sieveOnlyLong(linesVert, 50);
		
		imageTemp = LSDModule.linesToLSDImage(image, linesVert);
		ImageProcess.saveImage(LSDModule.path + "05- Vertical lines.png", 
				imageTemp );
		
		//	* sort left to right
		List<LineLSD> linesList;
		linesList = LineInterpreter.sortLeftToRight(linesVert);
		
		imageTemp = DoorFinder.findDoorsUsingPoint(linesToPointList, linesList, bestPoint, image);
		ImageProcess.saveImage(LSDModule.path + "06- CORNERS.png", 
				imageTemp );

		//-------------------------
		
		
		// 4. Find doors
//		linesList = DoorFinder.findDoors(linesList);
		
		// lines to image
		image = LSDModule.linesToLSDImage(image, lineLSDs);
//		image = LSDModule.linesToLSDImage(image, linesList);
		
		imageTemp = drawPoint(image, bestPoint);
		ImageProcess.saveImage(LSDModule.path + "07- With Vanishing Point.png", 
				imageTemp );
		
		return image;
	}
	private static BufferedImage exampleThree() {
		BufferedImage image;
	
		// 1. Load image from file
		image = ImageProcess.loadImage("src/main/resources/LSD-test/doors.png");
		
		// 2. LSD Module - find lines
		double zm_sigma_scale = 0.6;
		double zm_quant = 2.0;
		double zm_ang_th = 22.5;
		double zm_density_th = 0.7;	
		HashSet<LineLSD> lineLSDs = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lineLSDs = LineInterpreter.sieveOnlyVertical(lineLSDs, 5);
		//	* only long enough
		lineLSDs = LineInterpreter.sieveOnlyLong(lineLSDs, 50);
		//	* sort left to right
		List<LineLSD> linesList;
		linesList = LineInterpreter.sortLeftToRight(lineLSDs);

		// 4. Find corridor
		image = CorridorFinder.findCorridor(linesList, image);
		
		
		// lines to image
//		image = LSDModule.linesToLSDImage(image, lines);
//		image = LSDModule.linesToLSDImage(image, linesList);
		
		return image;
	}
	
	private static void openFolder(String path) {
		try {
			Desktop.getDesktop().open(new File( path ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static Vector createVectorLines(HashSet<LineLSD> lines) {
		Vector lineVec = new Vector<Line>();
		
		for(LineLSD line : lines) {
			lineVec.add( new Line(
					new Point2DDouble(line.getX1(), line.getY1()), 
					new Point2DDouble(line.getX2(), line.getY2())
					));
		}
		
		return lineVec;
	}
	private static BufferedImage drawPoint(BufferedImage image, Point2DDouble point) {
		int x = image.getWidth();
		int y = image.getHeight();
//		System.out.println(x + " : "+y);
		
		Graphics2D g2dClear = image.createGraphics();
		Color color = new Color(255,0,0);
		g2dClear.setColor(color);
		
		g2dClear.fillOval((int)point.x, (int)point.y, 20, 20);
		
		return image;
	}
	
	public static BufferedImage doItDoors(BufferedImage image) {

		// 2. LSD Module - find lines
		double zm_sigma_scale = 0.65;
		double zm_quant = 0.58;
		double zm_ang_th = 27;
		double zm_density_th = 0.5;	
		HashSet<LineLSD> lineLSDs = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lineLSDs = LineInterpreter.sieveOnlyVertical(lineLSDs, 9);
		//	* only long enough
		lineLSDs = LineInterpreter.sieveOnlyLong(lineLSDs, 60);
		//	* sort left to right
		List<LineLSD> linesList;
		linesList = LineInterpreter.sortLeftToRight(lineLSDs);

		// 4. Find doors
		BufferedImage imageOut = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		imageOut = DoorFinder.findDoorsPic(linesList, imageOut);
		
		// lines to image
//		image = LSDModule.linesToLSDImage(image, lines);
//		image = LSDModule.linesToLSDImage(image, linesList);
		
		return imageOut;
	}
	public static BufferedImage doItCorridor(BufferedImage image) {

		// 2. LSD Module - find lines
		double zm_sigma_scale = 0.6;
		double zm_quant = 2.0;
		double zm_ang_th = 22.5;
		double zm_density_th = 0.7;	
		HashSet<LineLSD> lineLSDs = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lineLSDs = LineInterpreter.sieveOnlyVertical(lineLSDs, 9);
		//	* only long enough
		lineLSDs = LineInterpreter.sieveOnlyLong(lineLSDs, 50);
		//	* sort left to right
		List<LineLSD> linesList;
		linesList = LineInterpreter.sortLeftToRight(lineLSDs);

		// 4. Find corridor
		image = CorridorFinder.findCorridor(linesList, image);

		
				return image;
	}
	
	public static BufferedImage doItDoorsPersp(BufferedImage image) {

		// 2. LSD Module - find lines
		double zm_sigma_scale = 1.0; // 0.65
		double zm_quant = 0.58; // 0.58
		double zm_ang_th = 27;	// 27
		double zm_density_th = 0.3; // 0.3	
		HashSet<LineLSD> lineLSDs = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		HashSet<LineLSD> linesF = (HashSet<LineLSD>) lineLSDs.clone();
//		ImageProcess.saveImage(LSDModule.path + "01- All lines.png", 
//				LSDModule.linesToLSDImage(image, linesF) );
		
		linesF = LineInterpreter.allExceptVerAndHor(linesF, 9, 9);
//		ImageProcess.saveImage(LSDModule.path + "02- No VerHor.png", 
//				LSDModule.linesToLSDImage(image, linesF) );
		linesF = LineInterpreter.sieveOnlyLong(linesF, 40);
//		ImageProcess.saveImage(LSDModule.path + "03- No VerHor and short.png", 
//				LSDModule.linesToLSDImage(image, linesF) );
		
		// X. Vanishing Point
		int iter = 10;
		Point2DDouble bestPoint = new Point2DDouble();
		Vector lineVec = createVectorLines(linesF);
		Vector supportVec = new Vector<Line>();
		Vector ignoreVec = new Vector<Line>();
		
		VanishingPointsCalculator.getVanishingPoint(iter, lineVec, bestPoint, supportVec, ignoreVec);
		
		// X. Lines to point
		HashSet<LineLSD> linesToPoint = (HashSet<LineLSD>) lineLSDs.clone();
		linesToPoint = LineInterpreter.linesToPoint(linesToPoint, bestPoint, 40);
		
		linesToPoint = LineInterpreter.allExceptVerAndHor(linesToPoint, 9, 9);
		
//		BufferedImage imageTemp = LSDModule.linesToLSDImage(image, linesToPoint);
//		ImageProcess.saveImage(LSDModule.path + "04- Lines To Point.png", 
//				imageTemp );
		//	* sort
		List<LineLSD> linesToPointList;
		linesToPointList = LineInterpreter.sortLeftToRight(linesToPoint);
		
		// X. Vertical lines
		HashSet<LineLSD> linesVert = (HashSet<LineLSD>) lineLSDs.clone();
		linesVert = LineInterpreter.sieveOnlyVertical(linesVert, 9);
		//	* only long enough
		linesVert = LineInterpreter.sieveOnlyLong(linesVert, 50);
		
//		imageTemp = LSDModule.linesToLSDImage(image, linesVert);
//		ImageProcess.saveImage(LSDModule.path + "05- Vertical lines.png", 
//				imageTemp );
		
		//	* sort left to right
		List<LineLSD> linesList;
		linesList = LineInterpreter.sortLeftToRight(linesVert);
		
		BufferedImage imageOut = DoorFinder.findDoorsUsingPoint(linesToPointList, linesList, bestPoint, image);
//		ImageProcess.saveImage(LSDModule.path + "06- CORNERS.png", 
//				imageTemp );

		//-------------------------
		
		
		// 4. Find doors
//		linesList = DoorFinder.findDoors(linesList);
		
		// lines to image
//		image = LSDModule.linesToLSDImage(image, lineLSDs);
//		image = LSDModule.linesToLSDImage(image, linesList);
		
//		imageTemp = drawPoint(image, bestPoint);
//		ImageProcess.saveImage(LSDModule.path + "07- With Vanishing Point.png", 
//				imageTemp );
		
		return imageOut;
	}

	public static BufferedImage doItDoorsFull(BufferedImage image) {

		// 2. LSD Module - find lines
		double zm_sigma_scale = 1.0; // 0.65
		double zm_quant = 0.58; // 0.58
		double zm_ang_th = 27;	// 27
		double zm_density_th = 0.3; // 0.3	
		HashSet<LineLSD> lineLSDs = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		HashSet<LineLSD> linesF = (HashSet<LineLSD>) lineLSDs.clone();

		linesF = LineInterpreter.allExceptVerAndHor(linesF, 9, 9);
		linesF = LineInterpreter.sieveOnlyLong(linesF, 40);

		// X. Vanishing Point
		int iter = 10;
		Point2DDouble bestPoint = new Point2DDouble();
		Vector lineVec = createVectorLines(linesF);
		Vector supportVec = new Vector<Line>();
		Vector ignoreVec = new Vector<Line>();
		
		VanishingPointsCalculator.getVanishingPoint(iter, lineVec, bestPoint, supportVec, ignoreVec);
		
		// X. Lines to point
		HashSet<LineLSD> linesToPoint = (HashSet<LineLSD>) lineLSDs.clone();
		linesToPoint = LineInterpreter.linesToPoint(linesToPoint, bestPoint, 40);
		
		linesToPoint = LineInterpreter.allExceptVerAndHor(linesToPoint, 9, 9);
		
		//	* sort
		List<LineLSD> linesToPointList;
		linesToPointList = LineInterpreter.sortLeftToRight(linesToPoint);
		
		// X. Vertical lines
		HashSet<LineLSD> linesVert = (HashSet<LineLSD>) lineLSDs.clone();
		linesVert = LineInterpreter.sieveOnlyVertical(linesVert, 9);
		//	* only long enough
		linesVert = LineInterpreter.sieveOnlyLong(linesVert, 50);
		
		//	* sort left to right
		List<LineLSD> linesList;
		linesList = LineInterpreter.sortLeftToRight(linesVert);
		
		BufferedImage imagePersp = DoorFinder.findDoorsUsingPoint(linesToPointList, linesList, bestPoint, image);

		//-------------------------


		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lineLSDs = LineInterpreter.sieveOnlyVertical(lineLSDs, 9);
		//	* only long enough
		lineLSDs = LineInterpreter.sieveOnlyLong(lineLSDs, 60);
		//	* sort left to right
		List<LineLSD> linesList2;
		linesList2 = LineInterpreter.sortLeftToRight(lineLSDs);

		// 4. Find doors
		BufferedImage imageLSD = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		imageLSD = DoorFinder.findDoorsPic(linesList, imageLSD);
		
		imagePersp = mergeDoors(imagePersp, imageLSD);		
		return imagePersp;
	}

	private static BufferedImage mergeDoors(BufferedImage image1, BufferedImage image2) {
		
		int width = image1.getWidth();
		int height = image1.getHeight();
		Color white = new Color(255,255,255);
		
		for (int w = 0; w < width; w++)
			for (int h = 0; h < height; h++)
				if (image2.getRGB(w, h) == white.getRGB())
					image1.setRGB(w, h, white.getRGB());

		return image1;
	}
	
	
}

/**
 * Punkt glebii:
 * 	Punkty do srodka:
 * 		wywalic bliskie pionu
 * 		posortowac linie od lewej po lewym punkcie
 * 		oddzielic dolne od gornych
 * 		
 */














