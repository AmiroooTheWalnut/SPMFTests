/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spmftest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.database.Item;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.database.Sequence;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.database.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.database.SequenceStatsGenerator;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.predictor.CPT.CPTPlus.CPTPlusPredictor;
import ca.pfv.spmf.algorithms.sequential_rules.rulegrowth.AlgoERMiner;
import ca.pfv.spmf.test.MainTestERMiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class SPMFTest {

    SPMFTest() {
        try {
            runCPTPlus();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SPMFTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SPMFTest.class.getName()).log(Level.SEVERE, null, ex);
        }

//        try {
//            //        MainTestERMiner mainTestERMiner = new MainTestERMiner();
//            runERMiner();
//        } catch (IOException ex) {
//            Logger.getLogger(SPMFTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void runCPTPlus() throws UnsupportedEncodingException, IOException {
        // Load the set of training sequences
//		String inputPath = fileToPath("Data.txt");
        String inputPath = "Data.txt";

        SequenceDatabase trainingSet = new SequenceDatabase();
        trainingSet.loadFileSPMFFormat(inputPath, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

        // Print the training sequences to the console
        System.out.println("--- Training sequences ---");
        for (Sequence sequence : trainingSet.getSequences()) {
            System.out.println(sequence.toString());
        }
        System.out.println();

        // Print statistics about the training sequences
        SequenceStatsGenerator.prinStats(trainingSet, " training sequences ");

        // The following line is to set optional parameters for the prediction model. 
        // We want to 
        // activate the CCF and CBS strategies which generally improves its performance (see paper)
        String optionalParameters = "CCF:true CBS:true CCFmin:1 CCFmax:6 CCFsup:2 splitMethod:0 splitLength:4 minPredictionRatio:1.0 noiseRatio:1.0";
        // Here is a brief description of the parameter used in the above line:
        //  CCF:true  --> activate the CCF strategy
        //  CBS:true -->  activate the CBS strategy
        //  CCFmax:6 --> indicate that the CCF strategy will not use pattern having more than 6 items
        //  CCFsup:2 --> indicate that a pattern is frequent for the CCF strategy if it appears in at least 2 sequences
        //  splitMethod:0 --> 0 : indicate to not split the training sequences    1: indicate to split the sequences
        //  splitLength:4  --> indicate to split sequence to keep only 4 items, if splitting is activated
        //  minPredictionRatio:1.0  -->  the amount of sequences or part of sequences that should match to make a prediction, expressed as a ratio
        //  noiseRatio:1.0  -->   ratio of items to remove in a sequence per level (see paper). 

        // Train the prediction model
        CPTPlusPredictor predictionModel = new CPTPlusPredictor("CPT+", optionalParameters);
        predictionModel.Train(trainingSet.getSequences());

        // Now we will make a prediction.
        // We want to predict what would occur after the sequence <1, 2>.
        // We first create the sequence
        Sequence sequence = new Sequence(0);
        sequence.addItem(new Item(3));
//        sequence.addItem(new Item(2));

        // Then we perform the prediction
        Sequence thePrediction = predictionModel.Predict(sequence);
        System.out.println("For the sequence <(1),(2)>, the prediction for the next symbol is: +" + thePrediction);
        
        System.out.println(thePrediction.get(0).val);
        
        System.out.println();
        System.out.println("To make the prediction, the scores were calculated as follows:");
        Map<Integer, Float> countTable = predictionModel.getCountTable();
        for (Entry<Integer, Float> entry : countTable.entrySet()) {
            System.out.println("symbol" + entry.getKey() + "\t score: " + entry.getValue());
        }
        
        sequence.addItem(new Item(thePrediction.get(0).val));
        
        thePrediction = predictionModel.Predict(sequence);
        System.out.println("For the sequence <(1),(2)>, the prediction for the next symbol is: +" + thePrediction);
        
        System.out.println(thePrediction.get(0).val);
        
        System.out.println();
        System.out.println("To make the prediction, the scores were calculated as follows:");
        countTable = predictionModel.getCountTable();
        for (Entry<Integer, Float> entry : countTable.entrySet()) {
            System.out.println("symbol" + entry.getKey() + "\t score: " + entry.getValue());
        }
        
        sequence.addItem(new Item(thePrediction.get(0).val));
        
        thePrediction = predictionModel.Predict(sequence);
        System.out.println("For the sequence <(1),(2)>, the prediction for the next symbol is: +" + thePrediction);
        
        System.out.println(thePrediction.get(0).val);
        
        System.out.println();
        System.out.println("To make the prediction, the scores were calculated as follows:");
        countTable = predictionModel.getCountTable();
        for (Entry<Integer, Float> entry : countTable.entrySet()) {
            System.out.println("symbol" + entry.getKey() + "\t score: " + entry.getValue());
        }
        
        sequence.addItem(new Item(thePrediction.get(0).val));
        
        thePrediction = predictionModel.Predict(sequence);
        System.out.println("For the sequence <(1),(2)>, the prediction for the next symbol is: +" + thePrediction);
        
        System.out.println(thePrediction.get(0).val);
        
        System.out.println();
        System.out.println("To make the prediction, the scores were calculated as follows:");
        countTable = predictionModel.getCountTable();
        for (Entry<Integer, Float> entry : countTable.entrySet()) {
            System.out.println("symbol" + entry.getKey() + "\t score: " + entry.getValue());
        }
        
        sequence.addItem(new Item(thePrediction.get(0).val));
        
        thePrediction = predictionModel.Predict(sequence);
        System.out.println("For the sequence <(1),(2)>, the prediction for the next symbol is: +" + thePrediction);

        // If we want to see why that prediction was made, we can also 
        // ask to see the count table of the prediction algorithm. The
        // count table is a structure that stores the score for each symbols
        // for the last prediction that was made.  The symbol with the highest
        // score was the prediction.
        System.out.println();
        System.out.println("To make the prediction, the scores were calculated as follows:");
        countTable = predictionModel.getCountTable();
        for (Entry<Integer, Float> entry : countTable.entrySet()) {
            System.out.println("symbol" + entry.getKey() + "\t score: " + entry.getValue());
        }
    }

    public void runERMiner() throws IOException {
//        String input = fileToPath("contextPrefixSpan.txt");  // the database
        String output = ".//output.txt";  // the path for saving the frequent itemsets found

        //  Applying ERMiner algorithm with minsup = 3 sequences and minconf = 0.5
        int minsup_relative = 3;
        double minconf = 0.8;
        AlgoERMiner algo = new AlgoERMiner();

//		// This optional parameter allows to specify the maximum number of items in the 
//		// left side (antecedent) of rules found:
//		algo.setMaxAntecedentSize(1);  // optional
////
////		// This optional parameter allows to specify the maximum number of items in the 
////		// right side (consequent) of rules found:
//		algo.setMaxConsequentSize(3);  // optional
        algo.runAlgorithm("Data.txt", output, minsup_relative, minconf);

        // If you want to use an absolute support (percentage value), use
        // the following lines instead:
//		   double minsup_absolute = 0.75;  // it means 75 %
//		   AlgoERMiner algo = new AlgoERMiner();
//		   algo.runAlgorithm(minsup_absolute, minconf, input, output);
        // print statistics
        algo.printStats();

    }

    public static String fileToPath(String filename)
            throws UnsupportedEncodingException {
        URL url = MainTestERMiner.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SPMFTest sPMFTest = new SPMFTest();
    }
}
