import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.ops.SimpleOperations_DDRM;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Tiat {
    public static SimpleMatrix A,B;
    public static void main(String[] args) {
        double[][] a = {{1},{-2}};
        A = new SimpleMatrix(a);
        //A = A.plus(A);
        //B = A;
        //System.out.println(derivSigmoid(A));
        System.out.println(A);
    }

    public static SimpleMatrix sigmoid(SimpleMatrix S) {
        for(int i=0;i<S.numRows();i++) {
            for(int j=0;j<S.numCols();j++) {
                S.set(i,j,1/(1+Math.exp(-S.get(i,j))));
            }
        }
        return S;
    }
    public static SimpleMatrix derivSigmoid(SimpleMatrix S) {
        SimpleMatrix Id = new SimpleMatrix(S.numRows(),S.numCols());
        Id.fill(1);
        //return (sigmoid(S) * (1 - sigmoid(S)));
        //return S.minus(sigmoid(S).elementMult(sigmoid(S)));
        SimpleMatrix sig = sigmoid(S);
        System.out.println("S:" + sig);
        System.out.println("I-S:" + Id.minus(sig));
        return sig.elementMult(Id.minus(sig));
    }
}