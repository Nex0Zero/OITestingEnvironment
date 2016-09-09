package newSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import lsd.Line;

public class LineInterpreter {
	
	public static HashSet<Line> sieveOnlyVertical(HashSet<Line> linesIn, int diffAngle) {
		HashSet<Line> linesOut = new HashSet<Line>();
		
		double angle;
		for(Line l: linesIn) {
			angle = l.angle();

			if( (90-diffAngle) <= angle && angle <= (90+diffAngle) )
				linesOut.add(l);
		}
		
		return linesOut;
	}
	
	public static HashSet<Line> sieveOnlyLong(HashSet<Line> linesIn, int min) {
		HashSet<Line> linesOut = new HashSet<Line>();
		
		double length;
		for(Line l: linesIn) {
			length = l.length();
			if(length > min)
				linesOut.add(l);
		}
		
		return linesOut;
	}
	
	public static List<Line> sortLeftToRight(HashSet<Line> linesIn) {
		List<Line> linesOut = new ArrayList<Line>();
		
		for(Line line : linesIn)
			linesOut.add(line);
		
		Collections.sort(linesOut, leftToRightComparator);
//		for(Line line : linesOut)
//			System.out.println(line.length());
			
		return linesOut;
	}
	public static Comparator<Line> leftToRightComparator = new Comparator<Line>() {

		public int compare(Line line1, Line line2) {
			
			Double lineY1 = line1.getCenterX();
			Double lineY2 = line2.getCenterX();
			
			return lineY1.compareTo(lineY2);
		}
		
	};
	
	
	
}
