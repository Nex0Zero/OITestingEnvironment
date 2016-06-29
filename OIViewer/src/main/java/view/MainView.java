package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent.KeyBinding;

import operations.FileLoader;
import operations.ImageCollector;
import operations.ImageOperations;
import operations.PathOperations;

import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class MainView extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuItem selectFolderItem;
	private JMenuItem closeItem;
	private JLabel imageLabel;
	private JLabel lblcieka;
	private JLabel lblTodociezka;
	private JComboBox<String> comboBox;
	
	BufferedImage loadBufferImage;
	File image;							//wczytane/wyswietlone zdjêcie 
	ImageCollector ic;
	
	private static int a ;
	private static int selection=0;
	
	String[] dziedzina = {"Pod³oga","Drzwi"};

	public MainView() {
		initialize();
	}
	
	public void initialize(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a=0;
		setBounds(100, 100, 700, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 684, 21);
		contentPane.add(menuBar);
		
		JMenu mnOpcje = new JMenu("Opcje");
		menuBar.add(mnOpcje);
		
		selectFolderItem = new JMenuItem("Wybierz folder");
		mnOpcje.add(selectFolderItem);
		selectFolderItem.addActionListener(this);
		
		JSeparator separator = new JSeparator();
		mnOpcje.add(separator);
		
		closeItem = new JMenuItem("Zamknij");
		closeItem.addActionListener(this);
		mnOpcje.add(closeItem);
		
		imageLabel = new JLabel("");
		imageLabel.setBounds(10, 72, 535, 478);
		contentPane.add(imageLabel);
		imageLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		lblcieka = new JLabel("\u015Acie\u017Cka:");
		lblcieka.setBounds(10, 32, 73, 14);
		contentPane.add(lblcieka);
		
		lblTodociezka = new JLabel("");
		lblTodociezka.setBounds(93, 32, 581, 14);
		contentPane.add(lblTodociezka);
		
		comboBox = new JComboBox<String>();
		for (String string : dziedzina) {
			comboBox.addItem(string);
		}
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				selection = comboBox.getSelectedIndex();
			}
		});
		comboBox.setBounds(569, 94, 105, 20);
		contentPane.add(comboBox);
		comboBox.setFocusable(false);
		
		JLabel lblCoSprawdzamy = new JLabel("Co sprawdzamy");
		lblCoSprawdzamy.setHorizontalAlignment(SwingConstants.CENTER);
		lblCoSprawdzamy.setBounds(569, 72, 105, 14);
		contentPane.add(lblCoSprawdzamy);
		
		JButton btnNastpny = new JButton("Nast\u0119pny");
		btnNastpny.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				buttonCheck(1);
			}
		});
		btnNastpny.setBounds(555, 486, 89, 23);
		contentPane.add(btnNastpny);
		
		JButton button = new JButton("Poprzedni");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonCheck(-1);
			}
		});
		button.setBounds(555, 520, 89, 23);
		contentPane.add(button);
		
		addKeyListener(new KeyAdapter() {
			   public void keyReleased(KeyEvent evt) {
					int code = evt.getKeyCode();
			        keysCheck(code);
			   }
		});
		setFocusable(true);

	}

	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		
		if(z == closeItem){
			System.exit(0);
		}else if(z == selectFolderItem){
			FileLoader fl = new FileLoader();
			File file = fl.fileOpener();
			lblTodociezka.setText(file.getAbsolutePath());
			PathOperations.cutPathToRelativePath(file.getAbsolutePath());
			ic = new ImageCollector(new File(PathOperations.pathToCorridor));
			refreshImage(a);
		}
	}
	
	private void keysCheck(int c) {
		// nastepny
		if( c == 39) {
			if(ic.images.length-1 <= a)
				a = 0;
			else
				a++;
			refreshImage(a);
		}
		// poprzedni
		if( c == 37) {
			if(a <= 0)
				a = ic.images.length-1;
			else
				a--;
			refreshImage(a);
		}
	}
	
	private void buttonCheck(int c) {
		// nastepny
		if( c == 1) {
			if(ic.images.length-1 <= a)
				a = 0;
			else
				a++;
			refreshImage(a);
//			loadImage(a);
		}
		// poprzedni
		if( c == -1) {
			if(a <= 0)
				a = ic.images.length-1;
			else
				a--;
			refreshImage(a);
//			loadImage(a);
		}
	}
	
	private void refreshImage(int i){
		if(selection == 0){
			try {
				File image = new File(ic.images[i].getCanonicalPath());		
				BufferedImage imageBuffered = ImageIO.read(image);				//zdjêcie g³ówne
				System.out.println("Wczytano "+image.getAbsolutePath());
				
				List<String> maskList = new ArrayList<String>();
				ImageCollector.displayDirectoryContents(new File(PathOperations.pathToCorridorMask), selection, i, ImageCollector.corridorMaskString, maskList);
				File mask = new File(maskList.get(0));
				BufferedImage maskBuffered = ImageIO.read(mask);				//maska
				System.out.println("Wczytano "+mask.getAbsolutePath());
				
				List<String> maskOurList = new ArrayList<String>();
				ImageCollector.displayDirectoryContents(new File(PathOperations.absolutePathToPart+"\\corridor"), selection, i, "",maskOurList);
				File ourMask = new File(maskOurList.get(0));
				BufferedImage ourMaskBuffered = ImageIO.read(ourMask);				//maska
				System.out.println("Wczytano "+ourMask.getAbsolutePath());
				
				ImageOperations.compare2Mask(maskBuffered, ourMaskBuffered, imageBuffered);
				
				BufferedImage temporaryImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_RGB);
				temporaryImage.getGraphics().drawImage(imageBuffered, 0, 0, imageLabel.getWidth(), imageLabel.getHeight(), null);
				Icon loadIcon = new ImageIcon(temporaryImage);
				imageLabel.setIcon(loadIcon);
				imageLabel.updateUI();
				imageBuffered.flush();
				ourMaskBuffered.flush();
				ourMaskBuffered.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(selection == 1){
			try {
				File image = new File(ic.images[i].getCanonicalPath());		
				BufferedImage imageBuffered = ImageIO.read(image);				//zdjêcie g³ówne
				System.out.println("Wczytano "+image.getAbsolutePath());
				
				List<String> maskList = new ArrayList<String>();
				ImageCollector.displayDirectoryContents(new File(PathOperations.pathToCorridorMask), selection, i, "", maskList);
				BufferedImage maskBuffered = ImageOperations.mergeDoors(maskList);
				
				List<String> maskOurList = new ArrayList<String>();
				ImageCollector.displayDirectoryContents(new File(PathOperations.absolutePathToPart+"\\door0"), selection, i, "",maskOurList);
				File ourMask = new File(maskOurList.get(0));
				BufferedImage ourMaskBuffered = ImageIO.read(ourMask);				//maska
				System.out.println("Wczytano "+ourMask.getAbsolutePath());
				
				ImageOperations.compare2Mask(maskBuffered, ourMaskBuffered, imageBuffered);
				
				BufferedImage temporaryImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_RGB);
				temporaryImage.getGraphics().drawImage(imageBuffered, 0, 0, imageLabel.getWidth(), imageLabel.getHeight(), null);
				Icon loadIcon = new ImageIcon(temporaryImage);
				imageLabel.setIcon(loadIcon);
				imageLabel.updateUI();
				imageBuffered.flush();
				ourMaskBuffered.flush();
				ourMaskBuffered.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			

	}
	
	private void loadImage(int i){
		try {
			image = new File(ic.images[i].getCanonicalPath());
			loadBufferImage = ImageIO.read(image);
			BufferedImage temporaryImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_RGB);
			temporaryImage.getGraphics().drawImage(loadBufferImage, 0, 0, imageLabel.getWidth(), imageLabel.getHeight(), null);
			Icon loadIcon = new ImageIcon(temporaryImage);
			imageLabel.setIcon(loadIcon);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
