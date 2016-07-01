package ownMethods;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodSingle;

import others.ArrayData;
import others.ImageProcess;

public class SOMContener2 {
	
	SOM network;
	BasicNetwork network2;
	BasicTrainSOM train;
	
	int INSIZE = 0;
	
	public SOMContener2(int inputCnt, int outputCnt) {
		
		network = new SOM(inputCnt, outputCnt);
		
		network2 = new BasicNetwork();
		network2.addLayer(new BasicLayer(new ActivationSigmoid(), false, 3));
		network2.addLayer(new BasicLayer(new ActivationSigmoid(), false, 4));
		network2.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		network2.getStructure().finalizeStructure();
		network2.reset();
		
		INSIZE = inputCnt;
		
	}
	
	public void runBasicTrain(double learningRate, int iterations, 
			BufferedImage picture, BufferedImage mask) {
		
		// create MLDataSet
		MLDataSet training = getDataSet(picture, mask, iterations);
		
		
		// create train object
		NeighborhoodRBF gaussian = new NeighborhoodRBF(RBFEnum.Gaussian,
				picture.getWidth(),
				picture.getHeight());
		BasicTrainSOM train = new BasicTrainSOM(
				network,
				learningRate,
				training,
				gaussian);
		
//		Train train = new Backpropagation(network2, training, 0.1, 0.0);
		
		// learn iter times
		for(int i = 0; i < iterations; i++) {
			train.iteration();
			
			if(i%(iterations/10)==0)
				System.out.println( train.getError() );
		}
		
		
		// close Encog ???
		Encog.getInstance().shutdown();
	}	
	
	public BufferedImage runBasic( BufferedImage picture ) {
		
		BufferedImage resultMask;
		
		// get DataSet input
		double[][] input = getInputData(picture);
		
		// create output Array
		int table[][][] = new int[3][picture.getHeight()][picture.getWidth()];
		ArrayData outputArray = new ArrayData(table, picture.getType());
		
		// generate Mask
		MLData data;
		int result;
		for(int h = 0; h < picture.getHeight(); h++)
			for(int w = 0; w < picture.getWidth(); w++) {
				data = new BasicMLData( input[h*picture.getWidth() + w ] );
				result = network.classify(data);

				if(result == 0) {
					table[0][h][w] = 0;
					table[1][h][w] = 0;
					table[2][h][w] = 0;
					
				}
				else if(result == 1) {
					table[0][h][w] = 60;
					table[1][h][w] = 60;
					table[2][h][w] = 60;
				}
				else if(result == 2) {
					table[0][h][w] = 120;
					table[1][h][w] = 120;
					table[2][h][w] = 120;
				}
				else if(result == 3) {
					table[0][h][w] = 255;
					table[1][h][w] = 255;
					table[2][h][w] = 255;
				}
				else
					System.out.println(result);
			}
				
		resultMask = ImageProcess.getImageFromData(outputArray);
		
		return resultMask;
	}
	
	
	
	/**
	 * [ R, G, B ];
	 */
	private double[][] getInputData(BufferedImage picture) {
		
		ArrayData image = ImageProcess.loadImageDataFromBI(picture);
		
		double[][] result = new double[image.getH()*image.getW()][INSIZE];

		for(int h = 0; h < image.getH(); h++)
			for(int w = 0; w < image.getW(); w++) {
				double R = image.get(0, h, w);
				double G = image.get(1, h, w);
				double B = image.get(2, h, w);
//				// R
//				result[ h*image.getW() + w ][0] = (double) R/255.0;
//				// G
//				result[ h*image.getW() + w ][1] = (double) G/255.0;
//				// B
//				result[ h*image.getW() + w ][2] = (double) B/255.0;
				double Lumi = 0.299*R + 0.587*G + 0.114*B;
//				result[ h*image.getW() + w ][2] = Lumi/255; 	// Luminancja
				double Odc = Math.atan2(Math.sqrt(3)*(G-B), 2*R-G-B);
				if( G==0 && B==0)
					Odc = 0;
//				result[ h*image.getW() + w ][4] = Odc;			// Odcieñ
				double maks = Math.max(R,G);
				maks = Math.max(maks,B);
				double min = Math.min(R, G);
				min = Math.min(min, B);
				double Nas = (maks-min)/maks;
				if(Nas > 1 || Nas < 0)
					Nas = 0.0;
//				result[ h*image.getW() + w ][1] = Nas; 			// Nasycenie
				
				result[ h*image.getW() + w ][0] = (double)h/(double)image.getH();				// h
				result[ h*image.getW() + w ][1] = (double)w/(double)image.getW();				// w
			}
		
		return result;
	}
	private MLDataSet getDataSet(BufferedImage pictureBI, BufferedImage maskBI, int size) {
		MLDataSet training = null;
		Random random = new Random();
		
		ArrayData picture = ImageProcess.loadImageDataFromBI(pictureBI);
		ArrayData mask = ImageProcess.loadImageDataFromBI(maskBI);
		
		int maxH = picture.getH();
		int maxW = picture.getW();
		
		double[][] rPicture = new double[size][INSIZE];
		double[][] rMask = new double[size][1];
		
		int h,w;
		for(int i = 0; i < size; ) {
			h = Math.abs( random.nextInt()%maxH );
			w = Math.abs( random.nextInt()%maxW );

			if((mask.get(0, h, w) >= 250)) {
				double R = picture.get(0, h, w);
				double G = picture.get(1, h, w);
				double B = picture.get(2, h, w);
//				rPicture[i][0] = (double) R/255.0;	//R
//				rPicture[i][1] = (double) G/255.0;	//G
//				rPicture[i][2] = (double) B/255.0;	//B
				double Lumi = 0.299*R + 0.587*G + 0.114*B;
//				rPicture[i][2] = Lumi/255; 			// Luminancja
				double Odc = Math.atan2(Math.sqrt(3)*(G-B), 2*R-G-B);
//				rPicture[i][4] = Odc; 				// odcieñ
				if(G==0 && B==0)
					Odc = 0;
				double maks = Math.max(R,G);
				maks = Math.max(maks,B);
				double min = Math.min(R, G);
				min = Math.min(min, B);
				double Nas = (maks-min)/maks;
				if(Nas > 1 || Nas < 0)
					Nas = 0.0;
//				rPicture[i][1] = Nas; 				// nasycenie
				
				rPicture[i][0] = (double)h/(double)maxH;				// h
				rPicture[i][1] = (double)w/(double)maxW;				// w
				
				if (mask.get(0, h, w) >= 250)
					rMask[i][0] = 1.0;
				else
					rMask[i][0] = 0.0;
				i++;
			}
			
			
			}
			
		
		training = new BasicMLDataSet(rPicture, rMask);		
//		training = new BasicMLDataSet(rPicture, null);
		
		//test(pictureBI, maskBI, size, rPicture, rMask);

		return training;
	}
	
}




























