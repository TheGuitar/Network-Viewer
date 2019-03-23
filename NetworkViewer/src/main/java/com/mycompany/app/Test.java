/*package mynetwork;

import mynetwork.decoder.Decoder;*/
import java.io.IOException;
import org.ejml.simple.SimpleMatrix;

public class Test {

    public static void testMnist(Network net, int set) throws IOException{
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData("../data/MNIST/t10k-images.idx3-ubyte", "../data/MNIST/t10k-labels.idx1-ubyte");
        int setSize = set;
        double[][] input = new double[setSize][28*28];
        double[][] res = new double[setSize][10];
        
        for(int i=0; i<setSize; i++) {
            int counter = 0;
            for (int r = 0; r < mnistMatrix[i].getNumberOfRows(); r++ ) {
                for (int c = 0; c < mnistMatrix[i].getNumberOfColumns(); c++) {
                    input[i][counter] = mnistMatrix[i].getValue(r,c)/255;
                    counter++;
                }
            }

            for(int j=0;j<=9;j++){
                if(j==mnistMatrix[i].getLabel()) {
                    res[i][j] =  1;
                }
                else {
                    res[i][j] = 0;
                }
            }
            
        }
        net.testNetwork(input,res);
        System.out.println("Error rate of the Network: " + net.errorRate);
        for(int i=0;i<10;i++) {
        //NetworkViewer.viewWeights(net.weights[1],i);
        }
    }

    public static void mnist(Network digitNet, int set, int epochs) throws IOException {
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData("../data/MNIST/train-images.idx3-ubyte", "../data/MNIST/train-labels.idx1-ubyte");
        int setSize;
        if(set==0) {
            setSize = mnistMatrix.length;
        }
        else {
            setSize = set;
        }
        
        double[][] input = new double[setSize][28*28];
        double[][] res = new double[setSize][10];
        
        //System.out.println("label: " + mnistMatrix.getLabel());
        for(int i=0; i<setSize; i++) {
            int counter = 0;
            for (int r = 0; r < mnistMatrix[i].getNumberOfRows(); r++ ) {
                for (int c = 0; c < mnistMatrix[i].getNumberOfColumns(); c++) {
                    //System.out.println(mnistMatrix[i].getValue(r, c) + " ");
                    input[i][counter] = ((double)mnistMatrix[i].getValue(r,c))/255;
                    //System.out.println(input[i][counter]);
                    counter++;
                }
                //System.out.println();
            }

            for(int j=0;j<=9;j++){
                if(j==mnistMatrix[i].getLabel()) {
                    res[i][j] =  1;
                }
                else {
                    res[i][j] = 0;
                }
            }
            
        }
        for(int i=0;i<epochs;i++) {
            if(i%10 == 0) {System.out.println(i);}
            digitNet.trainNetwork(input,res);
        }
        //System.out.println(digitNet.weights[1].get(0,0));
        //System.out.println(digitNet.weights[2].get(0,0));
        

        
        //System.out.println("Average Error of the Network: " + digitNet.avgCost/setSize);
        /*for(int i=0;i<4;i++) {
        //System.out.println("Cost of Run " + setSize + ": " + digitNet.lastCost);

        System.out.println("testing with digit " + mnistMatrix[i].getLabel() + ": ");
        //MyDecoder.printMnistMatrix(mnistMatrix[i]);
        }
        double [][] t = {input[0],input[1],input[2],input[3]};
        digitNet.testSample(t);
        System.out.println("Cost of last epoch: " + digitNet.avgCost);*/
    }

    public static void sampleMnist(Network net, int sample) throws IOException{
        int counter = 0;
        double[] input = new double[28*28];
        MnistMatrix mnistMatrix = new MnistDataReader().readMatrix("../data/MNIST/t10k-images.idx3-ubyte", "../data/MNIST/t10k-labels.idx1-ubyte",sample);
        System.out.println("testing with digit " + mnistMatrix.getLabel() + ": ");
        //MyDecoder.printMnistMatrix(mnistMatrix);
        SimpleMatrix sm = new SimpleMatrix(28,28);

        for (int r = 0; r < mnistMatrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < mnistMatrix.getNumberOfColumns(); c++) {
                input[counter] = mnistMatrix.getValue(r,c)/255;
                sm.set(r,c,mnistMatrix.getValue(r,c));
                counter++;
            }
        }
        //NetworkViewer.matrixView(sm,"test");

        double [][] t = {input};
        net.testSample(t);
    }

    public static void xor(Network test, int runs) { //XOR feste trainigszahl
        double[][] X = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[][] Y = {{1,0},{0,1},{0,1},{1,0}};
        //double[][] Y = {{0},{1},{1},{0}};
        //Network test = new Network(2,2,layers,size);
        for(int i=0;i<runs;i++){
            test.trainNetwork(X,Y);
        }
    }
}