package operations;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageOperations {
	
	private static int black = 16777216;
	private static int white = 1;
	
	
	public static void compare2Mask(BufferedImage mask, BufferedImage ourMask, BufferedImage image){
		for (int i = image.getMinX(); i < image.getWidth(); i++) {
			for (int j = image.getMinY(); j < image.getHeight(); j++) {
				int maskColor = Math.abs(mask.getRGB(i, j));
				int ourMaskColor = Math.abs(ourMask.getRGB(i, j));
//				System.out.println("Mask: "+maskColor +" Our: "+ourMaskColor);
				if(maskColor == black && ourMaskColor == black){//czarny czarny
//					System.out.println(0);
					//czarny - czyli nic
				}else if(maskColor == white && ourMaskColor == white){
//					System.out.println(1);
					//zielony
					image.setRGB(i, j, 8845141);
				}else if(maskColor == white && ourMaskColor == black){
//					System.out.println(2);
					//zó³te
					image.setRGB(i, j, 16250709);
				}else if(maskColor == black && ourMaskColor == white){
//					System.out.println(3);
					//czerwone
					image.setRGB(i, j, 15605278);
				}
			}
		}
	}
	
	public static BufferedImage mergeDoors(List<String> list){
		Color white = new Color(255, 255, 255);
		List<BufferedImage> doorsFile = new ArrayList<BufferedImage>();
		for (int i = 0; i < list.size(); i++) {
			BufferedImage image;
			try {
				image = ImageIO.read(new File(list.get(i)));
				doorsFile.add(image);
				System.out.println("Wczytano "+list.get(i));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedImage merged = new BufferedImage(doorsFile.get(0).getWidth(),doorsFile.get(0).getHeight(), 
				BufferedImage.TYPE_INT_RGB);
		
		
		for(int w = 0; w < doorsFile.get(0).getWidth(); w++)
			for(int h = 0; h < doorsFile.get(0).getHeight(); h++)
				if(doorsFile.get(0).getRGB(w, h) == white.getRGB())
					merged.setRGB(w, h, white.getRGB());
		
		
		for(int i = 1; i < doorsFile.size(); i++) {
			BufferedImage img = doorsFile.get(i);
			for(int w = 0; w < img.getWidth(); w++)
				for(int h = 0; h < img.getHeight(); h++)
					if(img.getRGB(w, h) == white.getRGB())
						merged.setRGB(w, h, white.getRGB());
		}

		return merged;
	}
	
}
