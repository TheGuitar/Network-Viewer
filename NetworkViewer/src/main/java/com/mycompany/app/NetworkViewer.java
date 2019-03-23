import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
//import org.graphstream.ui.swing_viewer;
import org.ejml.simple.SimpleMatrix;
import org.ejml.dense.row.DMatrixVisualization;
import org.ejml.simple.SimpleBase;
import java.util.Iterator;

public class NetworkViewer {

    public static void viewWholeNetwork(Network net, boolean big) {
        //System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph graph = new SingleGraph("Network Viewer");
        graph.addAttribute("ui.stylesheet",
        "url('file:./styles.css')");
        //graph.addAttribute("ui.stylesheet", "fill-color:blue");
        graph.display(false);

        for(int i=0;i<net.layers;i++) {
            for(int j=0;j<net.size[i];j++) {
                graph.addNode(Integer.toString(i)+":"+Integer.toString(j));
                if(big) {
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("x", i*300);
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("y", j*1/2);
                }
                else {
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("x", i*10);
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("y", j);
                }
                if(i==0) {
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).addAttribute("ui.class", "in");
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).addAttribute("ui.label", "In");
                }
                if(i==net.layers-1) {
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).addAttribute("ui.class", "out");
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).addAttribute("ui.label", "Out");
                }
            }
        }

        for(int i=1;i<net.weights.length;i++) {
            for(int r=0;r<net.weights[i].numRows();r++) {
                for(int c=0;c<net.weights[i].numCols();c++) {
                    graph.addEdge(Integer.toString(i-1)+":"+Integer.toString(c)+"-"+Integer.toString(i)+":"+Integer.toString(r), Integer.toString(i-1)+":"+Integer.toString(c), Integer.toString(i)+":"+Integer.toString(r));
                    graph.getEdge(Integer.toString(i-1)+":"+Integer.toString(c)+"-"+Integer.toString(i)+":"+Integer.toString(r)).addAttribute("ui.label",Double.toString(((double)(int)(net.weights[i].get(r,c)*100))/100));
                }
            }
        }

        /*for(Edge e:graph.getEachEdge()) {
            System.out.println(e.getId());
        }*/
        /*for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }*/
    
    }

    public static void viewNetwork(Network net, int max) {
        //System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph graph = new SingleGraph("Network Viewer");
        graph.addAttribute("ui.stylesheet",
        "url('file:./styles.css')");
        //graph.addAttribute("ui.stylesheet", "fill-color:blue");
        graph.display(false);

        for(int i=0;i<net.layers;i++) {
            for(int j=0;j<max;j++) {
                graph.addNode(Integer.toString(i)+":"+Integer.toString(j));
                if(i==0) { //input Layer
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).addAttribute("ui.class", "in");
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).addAttribute("ui.label", "In");
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("x", i*10);
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("y", j);
                }
                else if(i==net.layers-1) { //Output Layer
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).addAttribute("ui.class", "out");
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).addAttribute("ui.label", "Out");
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("x", i*10);
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("y", j);
                }
                else {
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("x", i*10);
                    graph.getNode(Integer.toString(i)+":"+Integer.toString(j)).setAttribute("y", -(max/2)+j*2);
                }
            }
        }

        for(int i=1;i<net.weights.length;i++) {
            for(int r=0;r<max;r++) {
                for(int c=0;c<max;c++) {
                    graph.addEdge(Integer.toString(i-1)+":"+Integer.toString(c)+"-"+Integer.toString(i)+":"+Integer.toString(r), Integer.toString(i-1)+":"+Integer.toString(c), Integer.toString(i)+":"+Integer.toString(r));
                    graph.getEdge(Integer.toString(i-1)+":"+Integer.toString(c)+"-"+Integer.toString(i)+":"+Integer.toString(r)).addAttribute("ui.label",Double.toString(((double)(int)(net.weights[i].get(r,c)*100))/100));
                }
            }
        }

        /*for(Edge e:graph.getEachEdge()) {
            System.out.println(e.getId());
        }*/
        /*for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }*/
    
    }

    public static void matrixView(SimpleMatrix mat, String titel) {
        DMatrixVisualization.show(mat.getMatrix(),titel);
    }

    public static void viewWeights(SimpleMatrix weights, int neuron) {
        SimpleMatrix mat = new SimpleMatrix(28,28);
        int counter = 0;
        for(int r = 0;r<28;r++) {
            for(int c=0;c<28;c++) {
                mat.set(r,c,weights.get(neuron,counter));
                counter++;
            }
        }
        matrixView(mat,Integer.toString(neuron));
    }

}