package solutions.graf;

import java.awt.Color;
import java.awt.image.BufferedImage;

import solutions.CaseSolution;

/**
 * @author Nex0Zero
 *
 */
public class GrafSolution implements CaseSolution {

	@Override
	public BufferedImage findDoor(BufferedImage image) {
		int height = image.getWidth();
		int width = image.getHeight();
		BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Color color;
		for(int w = 0; w < width; w++)
			for(int h = 0; h < height; h++) {
				color = new Color( image.getRGB(w, h) );
			}
		
		return null;
	}

	@Override
	public BufferedImage findCorridor(BufferedImage image) {
		// TODO Auto-generated method stub
		return null;
	}

}
