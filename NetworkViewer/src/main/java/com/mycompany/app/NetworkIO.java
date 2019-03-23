import java.io.*;
import org.ejml.ops.ConvertDArrays;
import org.ejml.simple.SimpleMatrix;

public class NetworkIO {

    public static void exportNetwork(Network net,String filename) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../savefiles/"+filename+".nn"), "utf-8"))) {
            writer.write(Integer.toString(net.layers) + '\n');
                for(int i=0;i<net.layers;i++) {
                    writer.write(Integer.toString(net.size[i])+'\n');
                }
                writer.write(Integer.toString(net.numberOfWeights) + '\n' + Double.toString(net.learningRate) + '\n');
                //writer.write('\n');

                for(int i=1;i<net.weights.length;i++) {
                    double[]weights = MyDecoder.matrix2Array(net.weights[i]);
                    writer.write(Integer.toString(net.weights[i].numRows())+'\n');
                    writer.write(Integer.toString(net.weights[i].numCols())+'\n');
                    for(int j=0;j<weights.length;j++) {
                        writer.write(Double.toString(weights[j]) + '\n');
                    }
                    //writer.write('\n');
                }
            } 
         catch (IOException e) {
            e.printStackTrace();
        }  
    }
    
    public static Network importNetwork(String filePath) { //CHANGE FOR DIFFERENT HIDDEN SIZES
        try(DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)))){
            int layers = Integer.parseInt(dataInputStream.readLine());
            int hidden = layers-2;
            int hiddenSize = 0;
            int inputSize = Integer.parseInt(dataInputStream.readLine());
            for(int i=1;i<layers-1;i++) {
                hiddenSize = Integer.parseInt(dataInputStream.readLine());
            }
            int outputSize = Integer.parseInt(dataInputStream.readLine());
            int numberOfWeights = Integer.parseInt(dataInputStream.readLine());
            double learningRate = Double.parseDouble(dataInputStream.readLine());

            SimpleMatrix[] weights = new SimpleMatrix[layers];
            for(int i = 1; i<layers;i++) {
                int rows = Integer.parseInt(dataInputStream.readLine());
                int cols = Integer.parseInt(dataInputStream.readLine());
                weights[i] = new SimpleMatrix(rows,cols);
                for(int r=0;r<rows;r++) {
                    for(int c=0;c<cols;c++) {
                        weights[i].set(r,c,Double.parseDouble(dataInputStream.readLine()));
                    }
                }
            }
            Network net = new Network(inputSize,outputSize,hidden,hiddenSize,weights);
            return net;
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    
    }
}