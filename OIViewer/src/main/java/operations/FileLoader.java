package operations;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileLoader {
	
	private String destinationFolder = ".\\";
	private String chooserTitle = "Wybierz katalog ze zdjêciami";
	private String startPath;
	
	
	public FileLoader(){}

	private void createFilter(JFileChooser jf){
		FileFilter wavFilter = new FileNameExtensionFilter(".wav","wav");
		jf.addChoosableFileFilter(wavFilter);
		jf.setFileFilter(wavFilter);
	}
	
	private void selectDestinationFolder(JFileChooser fc){
		fc.setCurrentDirectory(new java.io.File(destinationFolder));
		setStartPath(fc.getCurrentDirectory().toString());
		fc.setDialogTitle(chooserTitle);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
	
	public File fileOpener(){
		JFileChooser jf = new JFileChooser();
		selectDestinationFolder(jf);
		File file = null;
		if(jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			file = jf.getSelectedFile();
		}
		return file;
	}
	
	public static void displayDirectoryContents(File dir) {
	    try {
	        File[] files = dir.listFiles();
	        for (File file : files) {
	            if (file.isDirectory()) {
//	                System.out.println("Directory Name==>:" + file.getCanonicalPath());
	                displayDirectoryContents(file);
	            } else {
	                System.out.println("file Not Acess===>" + file.getCanonicalPath());
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	public static String getSubPath(String path){
		StringTokenizer token = new StringTokenizer(path, "\\");
		String[] tab = new String[token.countTokens()];
		int p = 0;
		String cos = "";
		while (token.hasMoreTokens()) {
			tab[p] = token.nextToken();
			p++;
		}
		
		for (int i = 1; i < tab.length; i++) {
			cos += tab[i] + " ";
		}

		return cos;
	}

	public static String getPath(String path){
		String endPath = "";
		StringTokenizer token = new StringTokenizer(path, "\\");
		String[] tab = new String[token.countTokens()];
		int p = 0;
		while (token.hasMoreTokens()) {
			tab[p] = token.nextToken();
			p++;
		}

		for(int i = 0; i < tab.length; i++){
			if(!tab[i].equals("sounds")){
				tab[i] = "";
			}else{
				break;
			}
		}
		for (String string : tab) {
			if(!string.equals("")){
				endPath += string + "\\";
			}
		}
		
		return endPath.substring(0, endPath.length()-1);
	}

	public String getStartPath() {
		return startPath;
	}

	public void setStartPath(String startPath) {
		this.startPath = startPath;
	}
	
}
