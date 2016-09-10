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
		double zm_sigma_scale = 0.65;
		double zm_quant = 0.58;
		double zm_ang_th = 27;
		double zm_density_th = 0.3;	
		HashSet<LineLSD> lineLSDs = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		HashSet<LineLSD> linesF = (HashSet<LineLSD>) lineLSDs.clone();
		ImageProcess.saveImage(LSDModule.path + "01- All lines.png", 
				LSDModule.linesToLSDImage(image, linesF) );
		
		linesF = LineInterpreter.allExceptVerAndHor(linesF, 9, 9);
		linesF = LineInterpreter.sieveOnlyLong(linesF, 40);
		ImageProcess.saveImage(LSDModule.path + "02- No VerHor and short.png", 
				LSDModule.linesToLSDImage(image, linesF) );
		
		// X. Vanishing Point
		int iter = 10;
		Point2DDouble bestPoint = new Point2DDouble();
		Vector lineVec = createVectorLines(linesF);
		Vector supportVec = new Vector<Line>();
		Vector ignoreVec = new Vector<Line>();
		
		VanishingPointsCalculator.getVanishingPoint(iter, lineVec, bestPoint, supportVec, ignoreVec);
		System.out.println("	x: " + bestPoint.x + " y: "+bestPoint.y);
		
		
		//-------------------------
		
		
		// 3. Line Interpreter - lines management
		// 	* only Vertical
//		lineLSDs = LineInterpreter.sieveOnlyVertical(lineLSDs, 9);
		//	* only long enough
//		lineLSDs = LineInterpreter.sieveOnlyLong(lineLSDs, 50);
		//	* sort left to right
//		List<LineLSD> linesList;
//		linesList = LineInterpreter.sortLeftToRight(lineLSDs);

		// 4. Find doors
//		linesList = DoorFinder.findDoors(linesList);
		
		// lines to image
		image = LSDModule.linesToLSDImage(image, lineLSDs);
//		image = LSDModule.linesToLSDImage(image, linesList);
		
		BufferedImage imageTemp = drawPoint(image, bestPoint);
		ImageProcess.saveImage(LSDModule.path + "03- With Vanishing Point.png", 
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
		System.out.println(x + " : "+y);
		
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
		image = LSDModule.linesToLSDImage(image, linesList);
		
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
	
}

/**
 * SZUKANIE DRZWI
 * odleg³oœci y miêdzy parami punktów lini (dó³/góra), a ich ró¿nice d³ugoœci.
 * albo
 * k¹ty punktów (osobno górne dolne w parach)
 * NOTKA: sprawdzac dolne.
 * 
 * PAROWNIE LINII
 * - posortowac od lewej do prawej
 * - obliczyc d³ugoœc lewej i dac jej max odl do nastepnej
 * - iterowac
 * - jak przeskoczy to wziasc kolejna linie, i znow obliczyc dlogosc i max odleglosc
 * - iterowac od aktualnaLinia+1
 **/