package extractor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * @author Nex0Zero
 *
 */
public class Extractor {
	
	public static void main(String[] args) {
		readCorridorTxtFile();
	}
	
	/**
	 * Method for Corridor set images
	 */
	public static void readCorridorTxtFile() {
		String path = "src/main/resources/";
		path = "C:/Workspace/#CORRIDOR/";
		String fileName = "corridor";
		
		processTxtFile(path, fileName);
	}
	
	/**
	 * @param path
	 */
	private static void processTxtFile(String path, String fileName) {
		System.out.println("processTxtFile:start");
		
		String line;
		try(Scanner sc = new Scanner(new File(path+fileName+".txt"), "UTF-8")) {
			
			int i = 0;
			while (sc.hasNextLine()) {
				System.out.println("processTxtFile:line_" + i);
				line = sc.nextLine();				
				
				lineProcessing(line, path, fileName);
				i++;
			}	
			
			if(sc.ioException() != null) {
				throw sc.ioException();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("processTxtFile:end");
	}	
	private static void lineProcessing(String line, String path, String fileName) {

		// Divide line by tabs ("\t")
		String[] tabs = line.split("\t");

		// Assign parts of line
		String strangeNumber = tabs[0];
		String dateTime = tabs[1];
		String strangeNumber2 = tabs[2];
		String setDirectory = tabs[3];
		int elementType = Integer.parseInt(tabs[4]);
		String data = tabs[5];

		// Create folder for "set"
		String setPath = path + fileName + "Masks" + "/" + setDirectory + "/";
		File setFile = new File(setPath);
		setFile.mkdirs();

		// Create folder for "element"
		File[] elements = setFile.listFiles();
		String imagePath = setPath + typeName(elementType) +"_" + (elements.length) + "/";
		File elementFile = new File(imagePath);
		elementFile.mkdir();
		
		// Divide all data by @
		String[] sets = data.split("@");

		// Check size of picture
		File imagesFolderDirectory = new File(path + fileName + "/" + setDirectory + "/");
		File[] imagesDirectory = imagesFolderDirectory.listFiles();
		BufferedImage image;
		int width = 0;
		int height = 0;
		try {
			image = ImageIO.read(imagesDirectory[0]);

			width = image.getWidth();
			height = image.getHeight();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// For each set
		for (String set : sets) {
			// Divide set by $
			String[] figures = set.split("\\$");
			
			// Create image - mask
			BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = mask.createGraphics();
			graphic.setColor(Color.WHITE);
			
			// for each figure
			for(String figure : figures) {
				String[] points = figure.split(";");
				int[] xs = new int[points.length];
				int[] ys = new int[points.length];
				
				// for each point
				for(int i = 0; i < points.length; i++) {
					String[] axis = points[i].split(":");
					if(axis.length == 2) {
						xs[i] = Math.round( Float.parseFloat(axis[0]) );
						ys[i] = Math.round( Float.parseFloat(axis[1]) );
					}
				}
				
				// create polygon
				if(points.length > 2)
					graphic.fillPolygon(xs, ys, points.length);
				
			}
			File imagesFile = new File(imagePath);
			File[] pics = imagesFile.listFiles();
			File outputfile = new File(imagePath+"imageMask"+pics.length+".png");
			try {
				ImageIO.write(mask, "png", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	
	private static String typeName(int val) {
		if(val == 703)
			return "Corridor";
		if(val == 704)
			return "Door";
		else
			return "Wrong_Type";
	}
	
}
