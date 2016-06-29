package operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PathOperations {
	
	//TODO: trzeba bêdzie pewno poprawi scie¿ki by ³apa³o dla wszystkich a nie tylko podfolderów startu

	public static String absolutePathToPart;	//sciezka do naszych maskek
	public static String pathToCorridor;		//sciezka do zdjêc oryginalnych
	public static String pathToCorridorMask;	//sciezka do oryginalnych masek
	
	public static String absoluteProjectPath;	//scie¿ka startowa wyszukiwania
	
	public static String pathToPart;			//scie¿ka od projektu do masek
	public static String pathWithoutSolName;	//scie¿ka bez nazwy rozwi¹zania
	
	public static final String corridorMaskName = "\\corridorMasks";
	public static final String corridorName = "\\corridor";
	public static File file;
	
	
	public static void cutPathToRelativePath(String end){
		absolutePathToPart = end;
//		absoluteProjectPath= start;
//		file = new File(absoluteProjectPath);
//		String o = end.replace(start,"");
//		pathToPart = o;
		cutPathWithoutSolName();
		file = new File(absoluteProjectPath);
		setAbsolutePath(PathOperations.file);
//		System.out.println("------------------------------");
//		System.out.println(absolutePathToPart);
//		System.out.println(pathToCorridor);
//		System.out.println(pathToCorridorMask);
//		System.out.println(absoluteProjectPath);
//		System.out.println(pathToPart);
//		System.out.println(pathWithoutSolName);
	}
	
	private static void cutPathWithoutSolName(){
//		StringTokenizer token = new StringTokenizer(pathToPart, "\\");
//		String[] tab = new String[token.countTokens()];
//		int p = 0;
//		while (token.hasMoreTokens()) {
//			tab[p] = token.nextToken();
//			p++;
//		}
		
//		pathWithoutSolName = pathToPart.replace(tab[0], "").substring(1);
//		System.out.println(pathWithoutSolName);
		
		absoluteProjectPath = setAbsoluteProjectPath();
		pathToPart = absolutePathToPart.replace(absoluteProjectPath, "");
		
		String[] tab = cutPathByElement(pathToPart.substring(1), "\\");
		String path = "\\";
		StringBuilder sB = new StringBuilder(path);
		for (int i = 1; i < tab.length; i++) {
			sB.append("\\"+tab[i]);
		}        
		path = sB.toString();
        path = path.substring(0, path.length());
		
		pathWithoutSolName = path.substring(1);
		
	}
	
	
	public static String[] cutPathByElement(String path, String s){
		StringTokenizer token = new StringTokenizer(path, s);
		String[] tab = new String[token.countTokens()];
		int p = 0;
		while (token.hasMoreTokens()) {
			tab[p] = token.nextToken();
			p++;
		}
		
		return tab;
	}
	
	
	private static String setAbsoluteProjectPath(){
		
		String[] tab = cutPathByElement(absolutePathToPart, "\\");
		String path = "";
		StringBuilder sB = new StringBuilder(path);
		for (int i = 0; i < tab.length-3; i++) {
			sB.append(tab[i]+"\\");
		}
        path = sB.toString();
        path = path.substring(0, path.length()-1);
        
//        absoluteProjectPath = path;
        return path;
	}
	
	
	private static void setAbsolutePath(File f){
		
	    try {
	        File[] files = f.listFiles();
	        for (File file : files) {
	            if (file.isDirectory()) {
//	                System.out.println("Directory Name==>:" + file.getCanonicalPath());
	            	if(file.getCanonicalPath().equals(absoluteProjectPath+corridorMaskName+pathWithoutSolName)){
	            		pathToCorridorMask = absoluteProjectPath + corridorMaskName + pathWithoutSolName;
	            		System.out.println("Œcie¿ka do masek ustawiona");
	            		System.out.println(pathToCorridorMask);
	            	}else if(file.getCanonicalPath().equals(absoluteProjectPath+corridorName+pathWithoutSolName)){
	            		pathToCorridor = absoluteProjectPath + corridorName + pathWithoutSolName;
	            		System.out.println("Œcie¿ka do zdjêc ustawiona");
	            		System.out.println(pathToCorridor);
	            	}
	            	setAbsolutePath(file);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
