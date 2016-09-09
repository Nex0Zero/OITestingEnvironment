package newSolver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import lsd.Line;

public class CorridorFinder {

	public static BufferedImage findCorridor(List<Line> linesIn, BufferedImage image) {
		int gSize = 5;
		double x,y, oldX,oldY;
		
		List<Point> points = new ArrayList<Point>();
		points.add( new Point(0, image.getHeight()) );
		
		int lowestId;
		for(int i = 0; i < linesIn.size()-gSize+1; i++) {
			lowestId = 0;
			
			for(int j = 1; j < gSize; j++) {
				if( linesIn.get(i+lowestId).getY2() <= linesIn.get(i+j).getY2() )
					lowestId = j;
			}
			
			points.add( new Point(linesIn.get(i+lowestId).getX2(), linesIn.get(i+lowestId).getY2()) );

		}
		points.add( new Point(image.getWidth(), image.getHeight()) );
		
//		for(int i = 0; i < points.size()-1; i++) {
//			linesIn.add( new Line(
//					points.get(i).X, points.get(i).Y, 
//					points.get(i+1).X, points.get(i+1).Y) );
//		}
		
		return draw(image, points);
	}
	
	private static BufferedImage draw(BufferedImage image, List<Point> points) {
		BufferedImage imageOut = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		Graphics2D g2dClear = imageOut.createGraphics();

		Color color2 = new Color(255,255,255);
		g2dClear.setColor(color2);
		
		for(int i = 0; i < points.size()-1;i++) {
			int[] xs = { 
					(int) points.get(i).X,
					(int) points.get(i).X,
					(int) points.get(i+1).X,
					(int) points.get(i+1).X
			};
			int[] ys = { 
					(int) points.get(i).Y,
					image.getHeight(),
					image.getHeight(),
					(int) points.get(i+1).Y
			};
			int n = 4;
			g2dClear.fillPolygon(xs, ys, n);
		}
		
		return imageOut;
	}
	
}


















