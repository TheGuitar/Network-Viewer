//package mynetwork.decoder;
import org.ejml.simple.SimpleMatrix;
import java.io.IOException;

public class MyDecoder {

    /*public static void main(String[] args) throws IOException {
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData("../data/MNIST/train-images.idx3-ubyte", "../data/MNIST/train-labels.idx1-ubyte");
        //printMnistMatrix(mnistMatrix[0]);
        //printMnistMatrix(mnistMatrix[1]);
        System.out.println(mnistMatrix[0].getValue(5, 12));
        /*mnistMatrix = new MnistDataReader().readData("../data/MNIST/t10k-images.idx3-ubyte", "../data/MNIST/t10k-labels.idx1-ubyte");
        printMnistMatrix(mnistMatrix[0]);
}*/

    public static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.println("label: " + matrix.getLabel());
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                System.out.print(matrix.getValue(r, c) + " ");
            }
            System.out.println();
        }
    }

    public static double[] matrix2Array(SimpleMatrix matrix) {
        int counter = 0;
        double[] res = new double[matrix.numRows()*matrix.numCols()];
        for (int r = 0; r < matrix.numRows(); r++ ) {
            for (int c = 0; c < matrix.numCols(); c++) {
                res[counter] = matrix.get(r, c);
                counter++;
            }
        }
        return res;
    }
}