package ownMethods;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import others.ArrayData;
import others.ImageProcess;

/**
 * @author Nex0Zero
 *
 */
public class MeanShift {


	public static ArrayData runMedianFilter(ArrayData data, int size, int tolerance, int iterations) {
		// median filter
		ArrayData processedData = data;
		

		for(int i = 0; i < iterations; i++)
			processedData = filterMedian(processedData, size, tolerance);
		

		return processedData;
	}
	
	/**
	 * Median - POiD
	 */
	public static ArrayData filterMedian(ArrayData data, int size, int tolerance) {
		ArrayData processedData = new ArrayData(data);
		int center = size/2;
		
		Color color;
		for(int i = 0; i < processedData.getH() - size+1; i++)
			for(int j = 0; j < processedData.getW() - size+1; j++) {
				color = filterMedianCalc(data, j, i, size, tolerance);
				processedData.setColor(i+center, j+center, color);
			}
		
		return processedData;
	}
	private static Color filterMedianCalc(ArrayData data, int dW, int dH, int size, int tolerance) {
		int values[][] = new int[3][size*size];
		
		/*
		 * Get pixels
		 */
		Color color;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				color = new Color(
						data.get(0, dH+i, dW+j), 
						data.get(1, dH+i, dW+j), 
						data.get(2, dH+i, dW+j));
				values[0][i*size +j] = color.getRed();
				values[1][i*size +j] = color.getGreen();
				values[2][i*size +j] = color.getBlue();
			}
		}	
		
		/*
		 * Find median findMedian()
		 */
		int indR = findMedian(size, values[0], tolerance);
		int indG = findMedian(size, values[1], tolerance);
		int indB = findMedian(size, values[2], tolerance);
		
		/*
		 * Change pixel
		 */
		Color result = new Color(
//				data.get(0, dW+(indR%size), dH+(indR/size)),
//				data.get(1, dW+(indG%size), dH+(indG/size)),
//				data.get(2, dW+(indB%size), dH+(indB/size))
//				
				data.get(0, dH+(indR/size), dW+(indR%size)),
				data.get(1, dH+(indG/size), dW+(indG%size)),
				data.get(2, dH+(indB/size), dW+(indB%size))
				);
		
		
		return result;
		//return new Color(n0ImageP.getRGB(x+(medianIndex%size), y+(medianIndex/size) ));
	}
	private static int findMedian(int size, int[] values, int tolerance) {
		int tempValues[] = new int[size*size];
		System.arraycopy(values, 0, tempValues, 0, values.length);
		
		int origin = values[values.length/2];
		
		// sort and find
		Arrays.sort(tempValues);
		
		
//		int n = tempValues.length/2 ;
//		int tempVal = tempValues[n];

		
		List<Integer> vals = new ArrayList<Integer>();
		int diff;
		for(int i = 0; i < values.length; i++) {
			diff = Math.abs( origin - tempValues[i] );
			if( diff <= tolerance )
				vals.add(tempValues[i]);
		}
		int tempVal = vals.get( vals.size()/2 );
		
		
		// find med
		int medianIndex = -1;
		outerloop:
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if(values[i*size + j] == tempVal) {
					medianIndex = i*size + j;
					break outerloop;
				}
		
		return medianIndex;
	}

}




















