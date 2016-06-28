package others;

import java.awt.Color;

public class ArrayData {
	
	/**
	 * PRIVS
	 */
	private int table[][][];

	private int imageType;
	
	/**
	 * GETTERS and SETTERS
	 */
	public int[][][] getTable() {
		return table;
	}
	public int getN() {
		return table.length;
	}
	public int getH() {
		return table[0].length;
	}
	public int getW() {
		return table[0][0].length;
	}
	public int get(int n, int h, int w) {
		return table[n][h][w];
	}
	
	public int getImageType() {
		return imageType;
	}
	
	public void setColor(int h, int w, Color color) {
		table[0][h][w] = color.getRed();
		table[1][h][w] = color.getGreen();
		table[2][h][w] = color.getBlue();
	}
	
	
	/**
	 * CONSTRUCTORS
	 */
	public ArrayData(int[][][] table, int imageType) {
		this.table = table;
		this.imageType = imageType;		
	}
	public ArrayData(ArrayData data) {
		this.table = new int[data.getN()][data.getH()][data.getW()];
		
		for(int n = 0; n < data.getTable().length;n++)
			for(int h = 0; h < data.getTable()[n].length; h++) {
				int[] array = data.getTable()[n][h];
				System.arraycopy(array, 0, this.table[n][h], 0, array.length);
			}
		
		this.imageType = data.getImageType();
	}

	
}



