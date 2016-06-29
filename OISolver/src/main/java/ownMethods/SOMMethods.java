package ownMethods;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationElliott;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodSingle;

public class SOMMethods {
	
	public static double SOM_INPUT[][] = { 
			{ -1.0, -1.0, 1.0, 1.0 }, 
			{ 1.0, 1.0, -1.0, -1.0 } };
	public static void test1() {
		// create the training set
				MLDataSet training = new BasicMLDataSet(SOM_INPUT,null);
				
				// Create the neural network.
				SOM network = new SOM(4,2);
				network.reset();
				
				BasicTrainSOM train = new BasicTrainSOM(
						network,
						0.7,
						training,
						new NeighborhoodSingle());
				
				for(int i = 0; i < 10; i++) {
					train.iteration();
					System.out.println("Iteration: " + i + ", Error:" + train.getError());
				}
				
				MLData data1 = new BasicMLData(SOM_INPUT[0]);
				MLData data2 = new BasicMLData(SOM_INPUT[1]);
				System.out.println("Pattern 1 winner: " + network.classify(data1));
				System.out.println("Pattern 2 winner: " + network.classify(data2));
				Encog.getInstance().shutdown();
	}

	
	/*
	public static void test2() {
// read datasets
		MLDataSet trainingSet = getDataSet(
		    "c:\\MNIST\\train-labels.idx1-ubyte", 
		    "c:\\MNIST\\train-images.idx3-ubyte");
		 
		MLDataSet validationSet = getDataSet(
		    "c:\\MNIST\\t10k-labels.idx1-ubyte", 
		    "c:\\MNIST\\t10k-images.idx3-ubyte");
		
// configure the neural network
		BasicNetwork network = new BasicNetwork();
		 
		int hiddenLayerNeuronsCount = 100;
		
		network.addLayer(new BasicLayer(null, true, 28*28));
		network.addLayer(new BasicLayer(new ActivationElliott(), true, hiddenLayerNeuronsCount));
		network.addLayer(new BasicLayer(new ActivationElliott(), false, 10));
		
		network.getStructure().finalizeStructure();
		network.reset();
		
// train the neural network	 
		final Propagation train = new ResilientPropagation(network, trainingSet);
		 
		int epochsCount = 100;
		 
		for(int i = 1; i <= epochsCount; i++)
		{
		    train.iteration();
		}
		 
		train.finishTraining();
		 
		// calculate error on validation set
		 
		double error = network.calculateError(validationSet);
		 
		Encog.getInstance().shutdown();
	}
	*/
	
	
	
}































