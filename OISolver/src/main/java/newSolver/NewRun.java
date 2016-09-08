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
	
		image = exampleTwo();
		
		// Open Folder
//		openFolder( LSDModule.path );
		
		// Save image
		ImageProcess.saveImage(LSDModule.path + "LSDtest.png", image);
	}	
	private static BufferedImage exampleOne() {
		BufferedImage image;
			
		// LSD for image
		image = LSDModule.LSDImageTest();

		return image;
	}
	private static BufferedImage exampleTwo() {
		BufferedImage image;
	
		// 1. Load image from file
		image = ImageProcess.loadImage("src/main/resources/LSD-test/doors.png");
		
		// 2. LSD Module - find lines
		HashSet<Line> lines = LSDModule.imageToLSDLines(image);
		
		// 3. Line Interpreter - lines management
		// 	* only Vertical
		lines = LineInterpreter.sieveOnlyVertical(lines, 6);
		//	* only long enough
		lines = LineInterpreter.sieveOnlyLong(lines);
		//	* sort left to right
		List<Line> linesList;
		linesList = LineInterpreter.sortLeftToRight(lines);
		
		// 4. Find doors
		//linesList = DoorFinder.findDoors(linesList);
		
		// lines to image
//		image = LSDModule.linesToLSDImage(image, lines);
		image = LSDModule.linesToLSDImage(image, linesList);
		
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