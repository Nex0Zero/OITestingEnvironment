package newSolver;

import java.util.ArrayList;
import java.util.List;

import lsd.Line;

public class DoorFinder {
	
	private static double distanceToLengthMAX = 0.5;
	private static double minLengMultipler = 0.25;
	private static double diffY = 40;
	
	public static List<Line> findDoors(List<Line> linesIn) {
		int size = linesIn.size();
		
		List<Line> linesOut = new ArrayList<Line>();
		
		// for each line
		Line line1, line2;
		for(int i = 0; i < size; i++) {
			line1 = linesIn.get(i);
			linesOut.add(line1);
			
			// for next lines to compare
			for(int j = i+1; j < size; j++) {
				line2 = linesIn.get(j);
				linesOut.add(line2);
				
				System.out.println("	" + i +" "+j);
				System.out.println("	" + line1.length() +" "+line2.length());
				// break if to far
				if( !distanceBetweenCondition(line1, line2) )					
					break;
				
				if( checkLinesConditions(line1, line2) ) {
					System.out.println("drzwi "+i+":"+j);
					addLinesToDoor(linesIn, line1, line2);
					addLinesToDoor(linesOut, line1, line2);
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
		
		// ::1:: Czy nie sa za daleko
		double distBetweenRatio = ( lineLonger.getCenterX() - lineShorter.getCenterX() ) / lineLonger.length();
		if(distBetweenRatio < 0)
			distBetweenRatio = -1* distBetweenRatio;
		if(distBetweenRatio > distanceToLengthMAX)
			return false;

		// ::2:: Czy nie roznia sie od siebie dlogoscia
		double diffRatio = ( lineLonger.length() - lineShorter.length() ) / lineLonger.length();
		if(diffRatio > minLengMultipler)
			return false;
		
		// ::3:: mniejsza jest w przedziale 'Y' tej wiekszej
		double errorH = 2;
		
		double diffPoint = lineLonger.getY1() - lineShorter.getY1();
		System.out.println("O1: "+diffPoint);
		if(diffPoint > errorH) // if(Math.abs(diffPoint) > error1)
			return false;
		diffPoint = lineLonger.getY2() - lineShorter.getY2();
		System.out.println("O2: "+diffPoint);
		if(diffPoint < -errorH) // if(Math.abs(diffPoint) > error1)
			return false;
		System.out.println("js");
		// ::4:: Dlugosc bokow do ich odleglosci
		if( !distToLeng(distBetweenRatio, diffRatio) )
			return false;
		
		
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
		double error = 0.20;
		
		double distRatio = (distanceToLengthMAX-(distanceToLengthMAX - dist)) / distanceToLengthMAX;
		double distRatioIn = 1-distRatio;
		double expectedLeng = minLengMultipler * distRatioIn;
		
		System.out.println("	MIN: ~" + 0.0 + " / " + minLengMultipler);
		System.out.println("	MAX: " + distanceToLengthMAX + " / ~" + 0.00);
		System.out.println("	NOW: " + dist + " / " + leng);
		System.out.println("\n	ORO	" + (leng-expectedLeng) +"\n--------------------------");
		
		double exLengDiff = minLengMultipler * error;
		if( expectedLeng-exLengDiff < leng && leng < expectedLeng+exLengDiff)
			return true;
		
		// 0.122 / 0.177
		// 0.097 / 0.167
		// 0.104 / 0.212
		// 0.213 / 0.178
		
		
		System.out.println("FALSE");
		return false;
	}
	
}












