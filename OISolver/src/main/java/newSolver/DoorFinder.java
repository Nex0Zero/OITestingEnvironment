package newSolver;

import java.util.List;

import lsd.Line;

public class DoorFinder {
	
	private static double distanceToLengthMAX = 0.5;
	private static double minLengMultipler = 0.25;
	private static double diffY = 40;
	
	public static List<Line> findDoors(List<Line> linesIn) {
		int size = linesIn.size();
		
		// for each line
		Line line1, line2;
		for(int i = 0; i < 3; i++) {
			line1 = linesIn.get(i);
			
			// for next lines to compare
			for(int j = i+1; j < size; j++) {
				line2 = linesIn.get(j);
				
				// break if to far
				if( !distanceBetweenCondition(line1, line2) )
					break;
				
				if( checkLinesConditions(line1, line2) ) {
					System.out.println("drzwi "+i+":"+j);
					addLinesToDoor(linesIn, line1, line2);
				}
			}
		}
		
		return linesIn;
	}
	
	private static boolean checkLinesConditions(Line line1, Line line2) {
		
		// Find longer line
		Line lineLonger, lineShorter;
		if( line2.length() >= line1.length() ) {
			lineLonger = line2;
			lineShorter = line1;
		} else {
			lineLonger = line1;
			lineShorter = line2;
		}
		
		// Check distance between ??POWTÓRKA??
		double distBetweenRatio = ( lineLonger.getCenterX() - lineShorter.getCenterX() ) / lineLonger.length();
		if(distBetweenRatio < 0)
			distBetweenRatio = -1* distBetweenRatio;
		if(distBetweenRatio > distanceToLengthMAX)
			return false;
		
		// Check difference X ratio
		double diffRatio = ( lineLonger.length() - lineShorter.length() ) / lineLonger.length();
		if(diffRatio > minLengMultipler)
			return false;		
		
		// Check difference y ratio
//		double diffYVal = lineLonger.getCenterY() - lineShorter.getCenterY();
//		if(diffYVal < 0)
//			diffYVal = -1* diffYVal;
//		if(diffYVal > diffY)
//			return false;
		
		// TODO: mniejsza jest w przedziale tej wiekszej
		double diffPoint = lineLonger.getY1() - lineShorter.getY1();
		if(diffPoint > 0)
			return false;
		diffPoint = lineLonger.getY2() - lineShorter.getY2();
		if(diffPoint < 0)
			return false;
		
		distToLeng(distBetweenRatio, diffRatio);
		
		return true;
	}
	private static boolean distanceBetweenCondition(Line line1, Line line2) {
		// Find longer line
		Line lineLonger, lineShorter;
		if( line2.length() >= line1.length() ) {
			lineLonger = line2;
			lineShorter = line1;
		} else {
			lineLonger = line1;
			lineShorter = line2;
		}
		
		// Check distance between
		double distBetweenRatio = ( lineLonger.getCenterX() - lineShorter.getCenterX() ) / lineLonger.length();
		if(distBetweenRatio < 0)
			distBetweenRatio = -1* distBetweenRatio;
		if(distBetweenRatio > distanceToLengthMAX)
			return false;
		
		return true;
		
	}
	
	private static void addLinesToDoor(List<Line> lines, Line line1, Line line2) {
		lines.add(new Line(
				line1.getX1(), line1.getY1(),
				line2.getX1(), line2.getY1()
				) );
		lines.add(new Line(
				line1.getX2(), line1.getY2(),
				line2.getX2(), line2.getY2()
				) );
	}

	private static boolean distToLeng(double dist, double leng){
		System.out.println("	MIN: ~" + 0.0 + " / " + minLengMultipler);
		System.out.println("	MAX: " + distanceToLengthMAX + " / ~" + 0.00);
		System.out.println("	NOW: " + dist + " / " + leng);
		
		// 0.122 / 0.177
		// 0.097 / 0.167
		// 0.104 / 0.212
		// 0.213 / 0.178
		
		return false;
	}
	
}












