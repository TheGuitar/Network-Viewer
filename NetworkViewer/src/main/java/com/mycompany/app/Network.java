import org.ejml.simple.SimpleMatrix;
import org.ejml.dense.row.CommonOps_DDRM;
import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;

public class Network {

    public int inputLayerSize, outputLayerSize, hiddenAmount, hiddenSize, layers;
    public int numberOfWeights = 0;
    public int numberOfBias = 0;
    public double lastCost, avgCost;
    public double errorRate = 0;
    public double learningRate = 0.0001;
    public int[] size;
    public String actF = "relu";
    public SimpleMatrix[] weights, activation, input, bias,avgGradient;
    public int[][] freezeW;
    //public SimpleMatrix[][] weightGradient;
    public double[] cost;
    public int epochsTrained = 0;
    //public ArrayList<double[]> testLossTrend;
    public ArrayList<Double> trainLossTrend,epochList,emptyList,testLossTrend,epochList2,emptyList2;
    public boolean frozen;

    public Network(int inputLayerSize, int outputLayerSize, int hiddenAmount, int hiddenSize) {
        this.inputLayerSize = inputLayerSize;
        this.outputLayerSize = outputLayerSize;
        this.hiddenAmount = hiddenAmount;
        this.hiddenSize = hiddenSize;
        layers = hiddenAmount + 2;

        weights = new SimpleMatrix[layers];
        activation = new SimpleMatrix[layers];
        input = new SimpleMatrix[layers];
        bias = new SimpleMatrix[layers];
        input = new SimpleMatrix[layers];
        size = new int[layers];

        //Input and Output Layers
        activation[0]= new SimpleMatrix(inputLayerSize,1);
        //bias[0]= new SimpleMatrix(inputLayerSize,1);
        size[0] = inputLayerSize;

        //Hidden Layers
        for(int i=1;i<=hiddenAmount;i++) {
            size[i]=hiddenSize;
            weights[i] = new SimpleMatrix(hiddenSize,size[i-1]);
            input[i] = new SimpleMatrix(hiddenSize,1);
            activation[i] = new SimpleMatrix(hiddenSize,1);
            bias[i] = new SimpleMatrix(hiddenSize,1);
        }
        weights[layers-1] = new SimpleMatrix(outputLayerSize,size[layers-2]);
        input[layers-1] = new SimpleMatrix(outputLayerSize,1);
        activation[layers-1] = new SimpleMatrix(outputLayerSize,1);
        bias[layers-1] = new SimpleMatrix(outputLayerSize,1);
        size[layers-1] = outputLayerSize;

        iniBias();
        iniWeights();
        trainLossTrend = new ArrayList<Double>();
        epochList = new ArrayList<Double>();
        emptyList = new ArrayList<Double>();

        testLossTrend = new ArrayList<Double>();
        epochList2 = new ArrayList<Double>();
        emptyList2 = new ArrayList<Double>();

        
    }

    public Network(int inputLayerSize, int outputLayerSize, int[] hidden) {
        this.inputLayerSize = inputLayerSize;
        this.outputLayerSize = outputLayerSize;
        this.hiddenAmount = 0;
        for(int i=0;i<hidden.length;i++) {
            if(hidden[i]!=0){this.hiddenAmount++;}
        }
        //this.hiddenSize = hiddenSize;
        layers = hiddenAmount + 2;

        weights = new SimpleMatrix[layers];
        activation = new SimpleMatrix[layers];
        input = new SimpleMatrix[layers];
        bias = new SimpleMatrix[layers];
        input = new SimpleMatrix[layers];
        size = new int[layers];

        //Input and Output Layers
        activation[0]= new SimpleMatrix(inputLayerSize,1);
        //bias[0]= new SimpleMatrix(inputLayerSize,1);
        size[0] = inputLayerSize;

        //Hidden Layers
        for(int i=1;i<=hiddenAmount;i++) {
            size[i]=hidden[i-1];
            weights[i] = new SimpleMatrix(hidden[i-1],size[i-1]);
            input[i] = new SimpleMatrix(hidden[i-1],1);
            activation[i] = new SimpleMatrix(hidden[i-1],1);
            bias[i] = new SimpleMatrix(hidden[i-1],1);
        }
        weights[layers-1] = new SimpleMatrix(outputLayerSize,size[layers-2]);
        input[layers-1] = new SimpleMatrix(outputLayerSize,1);
        activation[layers-1] = new SimpleMatrix(outputLayerSize,1);
        bias[layers-1] = new SimpleMatrix(outputLayerSize,1);
        size[layers-1] = outputLayerSize;

        iniBias();
        iniWeights();
        trainLossTrend = new ArrayList<Double>();
        epochList = new ArrayList<Double>();
        emptyList = new ArrayList<Double>();

        testLossTrend = new ArrayList<Double>();
        epochList2 = new ArrayList<Double>();
        emptyList2 = new ArrayList<Double>();
    }

    public Network(int inputLayerSize, int outputLayerSize, int hiddenAmount, int hiddenSize, SimpleMatrix[] setWeights) {
        this.inputLayerSize = inputLayerSize;
        this.outputLayerSize = outputLayerSize;
        this.hiddenAmount = hiddenAmount;
        this.hiddenSize = hiddenSize;
        layers = hiddenAmount + 2;

        weights = new SimpleMatrix[layers];
        activation = new SimpleMatrix[layers];
        input = new SimpleMatrix[layers];
        bias = new SimpleMatrix[layers];
        input = new SimpleMatrix[layers];
        size = new int[layers];

        //Input and Output Layers
        activation[0]= new SimpleMatrix(inputLayerSize,1);
        //bias[0]= new SimpleMatrix(inputLayerSize,1);
        size[0] = inputLayerSize;

        weights[layers-1] = setWeights[layers-1];
        input[layers-1] = new SimpleMatrix(outputLayerSize,1);
        activation[layers-1] = new SimpleMatrix(outputLayerSize,1);
        bias[layers-1] = new SimpleMatrix(outputLayerSize,1);
        size[layers-1] = outputLayerSize;

        //Hidden Layers
        for(int i=1;i<=hiddenAmount;i++) {
            size[i]=hiddenSize;
            weights[i] = setWeights[i];
            input[i] = new SimpleMatrix(hiddenSize,1);
            activation[i] = new SimpleMatrix(hiddenSize,1);
            bias[i] = new SimpleMatrix(hiddenSize,1);
        }

        iniBias();
        //setWeights();
        trainLossTrend = new ArrayList<Double>();
        epochList = new ArrayList<Double>();
        emptyList = new ArrayList<Double>();

        testLossTrend = new ArrayList<Double>();
        epochList2 = new ArrayList<Double>();
        emptyList2 = new ArrayList<Double>();
    }

    public void iniWeights() {  //we initialize all weights 
        for(int i = 1; i<layers;i++) {
            for(int j = 0; j<size[i];j++) {
                for(int k=0;k<size[i-1];k++) { 
                    weights[i].set(j,k,((double)((int)(Math.random()*100))/100)); //standard normal dist
                    //weights[i].set(j,k,((double)((int)(Math.random()*100))/100)*Math.sqrt(1/(size[i]+size[i-1])));
                    numberOfWeights++;
                }
            }
        }
        
    }

    public void iniBias() {  //we initialize all biases randomly
        for(int i = 1; i<layers;i++) {
            for(int j = 0; j<size[i];j++) {
                bias[i].set(j,(double)((int)(Math.random()*10))/10);
                numberOfBias++;
            }
        }
        
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    public void trainNetwork(double[][] in, double[][] res) {
        //Convert res to a vector:
        epochsTrained++;
        SimpleMatrix truth = new SimpleMatrix(res);
        avgCost = 0;
        cost = new double[in.length];
        //weightGradient = new SimpleMatrix[in.length][layers]; //For Batchwise training
        
        for(int n = 0;n<in.length;n++) {
            /*if(n%100 == 0) {
            System.out.println("---------- Input " + n + " -----------");
            }*/
            SimpleMatrix[] weightGradient = new SimpleMatrix[layers]; //For piece wise backprop
            cost[n] = 0;
            //Input Layer
            for(int i = 0;i<inputLayerSize;i++) {
                activation[0].set(i,in[n][i]);
            }
            //Hidden Layers and Output Layer
            for(int i = 1; i<layers;i++) { 
                //input[i] = (weights[i].mult(activation[i-1])).plus(bias[i]);
                input[i] = (weights[i].mult(activation[i-1]));
                switch (actF) {
                    case "sigmoid":
                        activation[i] = sigmoid(input[i]);
                    break;
                    case "relu": 
                        activation[i] = leakyRELU(input[i]);
                    break;
                    default:break;
                }
            }
            
            //Compute Cost 
            for(int i =0;i<outputLayerSize;i++) { // Prints out output info after every input 
                cost[n]+= Math.pow((activation[layers-1].get(i) - res[n][i]),2);
            }
            lastCost = cost[n];
            avgCost += lastCost/in.length;


            /////// We begin Back Prop
            for(int i=layers-1;i>0;i--) {
                SimpleMatrix el = errorLoop(i,n,truth);
                //weightGradient[n][i] =el.mult(activation[i-1].transpose()); //For batchwise training
                weightGradient[i] = el.mult(activation[i-1].transpose()); //For piecewise training
            }
            
           adjust(weightGradient); //update online
           /*if((n%100 == 0)&& n!=0) { //update after n samples
                adjustWeights(Arrays.copyOfRange(weightGradient, n-100, n));
            }*/
        }   
        //adjustWeights(weightGradient); //update after 1 epoch
        trainLossTrend.add(avgCost);
        epochList.add((double)epochsTrained);
        emptyList.add(0.0);
    }

    public void adjust(SimpleMatrix[] wg) {
        if(frozen) {
            for(int i=0;i<freezeW.length;i++) {
                wg[freezeW[i][0]].set(freezeW[i][1],freezeW[i][2],0);
            }
        }
        for(int i=1;i<layers;i++) {
            weights[i] = weights[i].minus(wg[i].scale(learningRate));
        }
    }

    public void adjustWeights(SimpleMatrix[][] weightGradient) {
        avgGradient = new SimpleMatrix[layers];
        for(int i=1;i<weightGradient[0].length;i++) {
            avgGradient[i] = new SimpleMatrix(weightGradient[0][i].numRows(),weightGradient[0][i].numCols());
            for(int j=0;j<weightGradient.length;j++) {
                double divisor = (1.0)/(weightGradient.length);
                avgGradient[i] = avgGradient[i].plus(weightGradient[j][i].scale(divisor));
            }
            
        }
        for(int i=1;i<layers;i++) {
                weights[i] = weights[i].minus(avgGradient[i]);
        }
    }

    public SimpleMatrix errorLoop(int i,int n, SimpleMatrix truth) {
        if(i==layers-1){
            SimpleMatrix tv=truth.extractVector(true,n);
            //return (activation[i].minus(tv.transpose())).elementMult(ds);
            return (activation[i].minus(tv.transpose()));
        }
        else{
            switch (actF) {
                case "sigmoid":
                    return derivSigmoid(input[i]).elementMult((weights[i+1].transpose()).mult(errorLoop(i+1,n,truth)));
                case "relu": 
                    return derivRELU(input[i]).elementMult((weights[i+1].transpose()).mult(errorLoop(i+1,n,truth)));
                default:return null;
            }
        }
    }

    public SimpleMatrix sigmoid(SimpleMatrix S) {
        //System.out.println(S);
        SimpleMatrix res = new SimpleMatrix(S);
        for(int i=0;i<S.numRows();i++) {
            for(int j=0;j<S.numCols();j++) {
                //System.out.println(1.0/(1.0+Math.exp(-S.get(i,j))));
                res.set(i,j,1.0/(1.0+Math.exp(-S.get(i,j))));
            }
        }
        //System.out.println(res);
        return res;
    }

    public SimpleMatrix derivSigmoid(SimpleMatrix S) {
        SimpleMatrix Id = new SimpleMatrix(S.numRows(),S.numCols());
        Id.fill(1);
        SimpleMatrix sig = sigmoid(S);
        //System.out.println(sig);
        return sig.elementMult(Id.minus(sig));
    }

    public SimpleMatrix leakyRELU(SimpleMatrix S) {
        //System.out.println(S);
        SimpleMatrix res = new SimpleMatrix(S);
        for(int r=0;r<S.numRows();r++) {
            for(int c=0;c<S.numCols();c++) {
                if(S.get(r,c)<0) {
                    res.set(r,c,0.01*S.get(r,c));
                }
            }
        }
        //System.out.println(res);
        return res;
    }

    public SimpleMatrix derivRELU(SimpleMatrix S) {
        SimpleMatrix res = new SimpleMatrix(S);
        for(int r=0;r<S.numRows();r++) {
            for(int c=0;c<S.numCols();c++) {
                if(S.get(r,c)<0) {
                    res.set(r,c,0.01);
                }
                else {
                    res.set(r,c,1);
                }
            }
        }
        return res;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    public double[] sampleXOR(int[][] in) {
        double currentInput = 0;
        double sol = -1;
        double highscore = 0;
        for(int n = 0;n<in.length;n++) {
            //Input Layer
            for(int i = 0;i<inputLayerSize;i++) {
                activation[0].set(i,in[n][i]);
            }
            //Hidden Layers and Output Layer
            for(int i = 1; i<layers;i++) {  //for every hidden Layer
                //input[i] = weights[i].mult(activation[i-1]).plus(bias[i]);
                input[i] = weights[i].mult(activation[i-1]);
                switch (actF) {
                    case "sigmoid":
                        activation[i] = (sigmoid(input[i]));
                    break;
                    case "relu": 
                        activation[i] = leakyRELU(input[i]);
                    break;
                    default:break;
                }
            }
            highscore = 0;
            sol = -1;
            for(int i =0;i<outputLayerSize;i++) { 
                    //System.out.println(i + ":    " + ((double)(int)(activation[layers-1].get(i)*100))/100);
                    if(activation[layers-1].get(i)>highscore) {
                        highscore = activation[layers-1].get(i);
                        sol = i;
                    }
            }
        }
        double[] res = {sol,((double)(int)(highscore*100))/100};
        return res;
    }

    public void testSample(double[][] in) {
        double currentInput = 0;

        for(int n = 0;n<in.length;n++) {
            //Input Layer
            for(int i = 0;i<inputLayerSize;i++) {
                activation[0].set(i,in[n][i]);
            }
            //Hidden Layers and Output Layer
            for(int i = 1; i<layers;i++) {  //for every hidden Layer
                //input[i] = weights[i].mult(activation[i-1]).plus(bias[i]);
                input[i] = weights[i].mult(activation[i-1]);
                switch (actF) {
                    case "sigmoid":
                        activation[i] = (sigmoid(input[i]));
                    break;
                    case "relu": 
                        activation[i] = leakyRELU(input[i]);
                    break;
                    default:break;
                }
            }
            System.out.println();
            for(int i =0;i<outputLayerSize;i++) { 
                    //System.out.println(i + ":    " + activation[layers-1].get(i));
                    System.out.println(i + ":    " + ((double)(int)(activation[layers-1].get(i)*100))/100);
            }
        }
    }

    public void testNetwork(double[][] in, double[][] res) {
        SimpleMatrix truth = new SimpleMatrix(res);

        cost = new double[in.length];
        //weightGradient = new SimpleMatrix[in.length][layers]; //For Batchwise training
        
        for(int n = 0;n<in.length;n++) {
            SimpleMatrix[] weightGradient = new SimpleMatrix[layers]; //For piece wise backprop
            cost[n] = 0;
            //Input Layer
            for(int i = 0;i<inputLayerSize;i++) {
                activation[0].set(i,in[n][i]);
            }
            //Hidden Layers and Output Layer
            for(int i = 1; i<layers;i++) { 
                //input[i] = (weights[i].mult(activation[i-1])).plus(bias[i]);
                input[i] = (weights[i].mult(activation[i-1]));
                switch (actF) {
                    case "sigmoid":
                        activation[i] = (sigmoid(input[i]));
                    break;
                    case "relu": 
                        activation[i] = leakyRELU(input[i]);
                    break;
                    default:break;
                }
                
            }
            
            //Compute Cost 
            for(int i =0;i<outputLayerSize;i++) { // Prints out output info after every input 
                cost[n]+= Math.pow((activation[layers-1].get(i) - res[n][i]),2);
            }
            errorRate += cost[n];
        }
        errorRate = errorRate/in.length;
        
        testLossTrend.add(errorRate);
        epochList2.add((double)epochsTrained);
        emptyList2.add(0.0);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    public void freezeWeights(double ratio) {
        int amount = (int) (numberOfWeights*(ratio/100));
        freezeW = new int[amount][3];
        int counter = 0;
            for(int i=1;i<weights.length;i++) {
                for(int r=0;r<weights[i].numRows();r++) {
                    for(int c=0;c<weights[i].numCols();c++) {
                        if(counter<amount) {
                            if((int)(Math.random()*100) < ratio) {
                                int[] cur = {i,r,c};
                                freezeW[counter] = cur;
                                counter++;
                            }
                        }
                    }
                }
            }
        
            System.out.println(numberOfWeights + ", " +ratio + ", " + counter);
    }

    public void clearFreeze() {
        freezeW = new int[0][0];
    }


    public void setInputLayerSize(int n) {
        inputLayerSize = n;
    }

    public void setOutputLayerSize(int n) {
        outputLayerSize = n;
    }

    public int getInputLayerSize() {
        return inputLayerSize;
    }

    public int getOutputLayerSize() {
        return outputLayerSize;
    }
}