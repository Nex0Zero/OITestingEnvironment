package newSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import VPC.Point2DDouble;
import lsd.LineLSD;

public class LineInterpreter {
	
	public static HashSet<LineLSD> sieveOnlyVertical(HashSet<LineLSD> linesIn, int diffAngle) {
		HashSet<LineLSD> linesOut = new HashSet<LineLSD>();
		
		double angle;
		for(LineLSD l: linesIn) {
			angle = l.angle();

			if( (90-diffAngle) <= angle && angle <= (90+diffAngle) )
				linesOut.add(l);
		}
		
		return linesOut;
	}
	public static HashSet<LineLSD> allExceptVerAndHor(HashSet<LineLSD> linesIn, int diffAngle1, int diffAngle2) {
		HashSet<LineLSD> linesOut = new HashSet<LineLSD>();
		
		double angle;
		for(LineLSD l: linesIn) {
			angle = l.angle();

			if( (90+diffAngle1) <= angle && angle <= (180-diffAngle2) 
					|| (0+diffAngle2) <= angle && angle <= (90-diffAngle1) )
				linesOut.add(l);
		}
		
		return linesOut;
	}
	
	public static HashSet<LineLSD> sieveOnlyLong(HashSet<LineLSD> linesIn, int min) {
		HashSet<LineLSD> linesOut = new HashSet<LineLSD>();
		
		double length;
		for(LineLSD l: linesIn) {
			length = l.length();
			if(length > min)
				linesOut.add(l);
		}
		
		return linesOut;
	}
	
	public static List<LineLSD> sortLeftToRight(HashSet<LineLSD> linesIn) {
		List<LineLSD> linesOut = new ArrayList<LineLSD>();
		
		for(LineLSD lineLSD : linesIn)
			linesOut.add(lineLSD);
		
		Collections.sort(linesOut, leftToRightComparator);
//		for(Line line : linesOut)
//			System.out.println(line.length());
			
		return linesOut;
	}
	public static Comparator<LineLSD> leftToRightComparator = new Comparator<LineLSD>() {

		public int compare(LineLSD line1, LineLSD line2) {
			
			Double lineY1 = line1.getCenterX();
			Double lineY2 = line2.getCenterX();
			
			return lineY1.compareTo(lineY2);
		}
		
	};
	
	public static HashSet<LineLSD> linesToPoint(HashSet<LineLSD> linesIn, Point2DDouble bestPoint, double maxDistance) {
		HashSet<LineLSD> linesOut = new HashSet<LineLSD>();
		
		double distance;
		for(LineLSD line: linesIn) {
			distance = Distance.distBetweenPointAndLine(
					(float)bestPoint.x, (float)bestPoint.y, 
					(float)line.getX1(), (float)line.getY1(), 
					(float)line.getX2(), (float)line.getY2());
			
			if(distance < maxDistance)
				linesOut.add(line);
		}
		
//		System.out.println("linestopoint:" + linesOut.size());
		return linesOut;
	}
	
}
