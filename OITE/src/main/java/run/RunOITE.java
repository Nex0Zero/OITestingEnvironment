package run;

import extractor.Extractor;

/**
 * @author Nex0Zero
 *
 */
public class RunOITE {

	public static void main(String[] args) {
		
		Extractor ext = new Extractor();
		ext.readResourceTxtFile();
		
	}

}

/* TODO
 * 1. Wyci�gn�� dane z pliku .txt i stworzy� maski .png
 * 2. �rodowisko testowe
 * 3. Metody wykrywania (jedna na �ebka)
 * 
 * 
 * n-1. Dokumentcja
 * n. Zrobi� skrypt uruchomieniowy run.sh w Linux
 * 
 */
