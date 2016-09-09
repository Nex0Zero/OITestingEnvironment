package newSolver;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.sound.sampled.LineListener;

import lsd.GUI;
import lsd.LSDModule;
import lsd.Line;
import others.ImageProcess;

public class NewRun {

	public static void main(String[] args) {
		System.out.println("NewRun : Start");
		
		BufferedImage image;
	
		image = exampleThree();
		
		// Open Folder
//		openFolder( LSDModule.path );
		
		// Save image
		ImageProcess.saveImage(LSDModule.path + "LSDtest.png", image);
		
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
		double zm_sigma_scale = 0.6;
		double zm_quant = 2.0;
		double zm_ang_th = 22.5;
		double zm_density_th = 0.7;	
		HashSet<Line> lines = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lines = LineInterpreter.sieveOnlyVertical(lines, 9);
		//	* only long enough
		lines = LineInterpreter.sieveOnlyLong(lines, 50);
		//	* sort left to right
		List<Line> linesList;
		linesList = LineInterpreter.sortLeftToRight(lines);

		// 4. Find doors
//		linesList = DoorFinder.findDoors(linesList);
		
		// lines to image
//		image = LSDModule.linesToLSDImage(image, lines);
		image = LSDModule.linesToLSDImage(image, linesList);
		
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
		HashSet<Line> lines = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lines = LineInterpreter.sieveOnlyVertical(lines, 5);
		//	* only long enough
		lines = LineInterpreter.sieveOnlyLong(lines, 50);
		//	* sort left to right
		List<Line> linesList;
		linesList = LineInterpreter.sortLeftToRight(lines);

		// 4. Find doors
//		linesList = DoorFinder.findDoors(linesList);
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
	
	
	public static BufferedImage doItDoors(BufferedImage image) {

		// 2. LSD Module - find lines
		double zm_sigma_scale = 0.65;
		double zm_quant = 0.58;
		double zm_ang_th = 27;
		double zm_density_th = 0.5;	
		HashSet<Line> lines = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lines = LineInterpreter.sieveOnlyVertical(lines, 9);
		//	* only long enough
		lines = LineInterpreter.sieveOnlyLong(lines, 60);
		//	* sort left to right
		List<Line> linesList;
		linesList = LineInterpreter.sortLeftToRight(lines);

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
		HashSet<Line> lines = LSDModule.imageToLSDLines(image,
				zm_sigma_scale, zm_quant, zm_ang_th, zm_density_th);

		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lines = LineInterpreter.sieveOnlyVertical(lines, 9);
		//	* only long enough
		lines = LineInterpreter.sieveOnlyLong(lines, 50);
		//	* sort left to right
		List<Line> linesList;
		linesList = LineInterpreter.sortLeftToRight(lines);

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