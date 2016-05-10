package extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Nex0Zero
 *
 */
public class Extractor {
	
	public void readResourceTxtFile() {
		String path = "src/main/resources/";
		String fileName = "corridor";
		
		readTxtFile(path+"test.txt");
	}
	
	private void readTxtFile(String path) {
		if(!path.contains(".txt"))
			path = path + ".txt";
		
		String line;
		try(Scanner sc = new Scanner(new File(path+".txt"), "UTF-8")) {
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				System.out.println(line);
			}
			
			if(sc.ioException() != null) {
				throw sc.ioException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}

/* TODO
 * 1. Wczytaæ plik .txt
 * 2. Wyci¹gn¹æ dane (interpretacja)
 * 3. ? Wczytaæ zdjêcia ? rozmiar ?
 * 4. Stworzyæ maski z punktów
 * 4. Zapisaæ do pliku .png uzyskane maski
 */
