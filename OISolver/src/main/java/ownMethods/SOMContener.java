package ownMethods;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodSingle;

public class SOMContener {
	
	SOM network;
	BasicTrainSOM train;
	
	
	public SOMContener(int inputCnt, int outputCnt) {
		
		network = new SOM(inputCnt, outputCnt);
		
	}
	
	public void runBasicTrain(double learningRate, int iterations, 
			String pictureFilePath, String maskFilePath) {
		
		// get DataSet input
		double[][] input = getInputData(pictureFilePath);
		
		// get DataSet ideal
		double[][] ideal = getIdealData(maskFilePath);
				
		// create MLDataSet
		MLDataSet training = new BasicMLDataSet(input, ideal);
		
		// create train object
		BasicTrainSOM train = new BasicTrainSOM(
				network,
				learningRate,
				training,
				new NeighborhoodSingle());
		
		// learn iter times
		for(int i = 0; i < iterations; i++)
			train.iteration();
		
		// close Encog ???
		Encog.getInstance().shutdown();
	}
	private double[][] getInputData(String pictureFilePath) {
		
		// TODO
		
		return null;
	}
	private double[][] getIdealData(String maskFilePath) {
		
		// TODO
		
		return null;
	}
	
}




























