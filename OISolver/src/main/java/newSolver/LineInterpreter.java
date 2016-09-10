package newSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

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
	public static HashSet<LineLSD> allExceptVerAndHor(HashSet<LineLSD> linesIn, int diffAngle) {
		HashSet<LineLSD> linesOut = new HashSet<LineLSD>();
		
		double angle;
		for(LineLSD l: linesIn) {
			angle = l.angle();

			if( (90+diffAngle) <= angle && angle <= (180-diffAngle) 
					|| (0+diffAngle) <= angle && angle <= (90-diffAngle) )
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
	
	
	
}
