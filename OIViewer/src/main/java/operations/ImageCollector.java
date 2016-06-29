package operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageCollector {
	
	public File[] images;
	public File[] corridorMask;
	public File[] solutionCorridorMask;
	private static String[] numbers = {"0","1","2","3","4","5","6","7","8","9"};
	
	public static String corridorMaskString = "Corridor";
	public static String corridorOurMaskString = "corridor0";
	public static String doors = "oor";
	
	public ImageCollector(File f) {
		images = f.listFiles();
	}
	
//	private void setCorridorMask(){
//		List<String> list = PathOperations.checkDirectoryPath(new File(PathOperations.pathToCorridorMask), corridor);
////		corridorMask = 
//	}
	
	
	public static void displayDirectoryContents(File dir, int choice, int number, String match, List<String> lists) {
		int counter = 0;
	    try {
	        File[] files = dir.listFiles();
	        for (File file : files) {
	        	if(choice == 0){
	        		if (file.isDirectory() && file.getCanonicalPath().contains(match)) {
//	        			System.out.println("Corridor Name==>:" + file.getCanonicalPath());
	        			displayDirectoryContents(file, choice, number, match, lists);
	        		}
	        		else if(file.getCanonicalPath().contains(match)){
//	        			System.out.println("Corridor Plik==>:" + file.getCanonicalPath());
//	        			System.out.println(file.getName());
	        			int num = getNumberFromName(file.getName());
	        			if(number == num){
	        				lists.add(file.getCanonicalPath());
//	        				System.out.println("Corridor Plik==>:" + file.getCanonicalPath());
	        				break;
	        			}
	        			counter++;
	        		}
	        	}else if(choice == 1){
	        		if (file.isDirectory() && file.getCanonicalPath().contains(doors)) {
	        			
//	        			System.out.println("Doors Name==>:" + file.getCanonicalPath());
	        			displayDirectoryContents(file, choice, number, match, lists);
	        		}else if(file.getCanonicalPath().contains(doors)){
	        			int num = getNumberFromName(file.getName());
	        			if(number == num){
	        				lists.add(file.getCanonicalPath());
//	        				System.out.println("Corridor Plik==>:" + file.getCanonicalPath());
	        				break;
	        			}
	        			counter++;
	        		}
	        	}
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}
	
	
	private static int getNumberFromName(String name){
		String str = "";
		for (int i = 0; i < name.length(); i++) {
			String c = name.substring(i, i+1);
			for (int j = 0; j < numbers.length; j++) {
				if(c.equals(numbers[j])){
					str += c;
				}
			}
		}
		int num= Integer.parseInt(str);
		return num;
	}
	
}
