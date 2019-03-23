//package mynetwork;

public class Layer {
    
    private int size;
    public Neuron[] neuron;

    public Layer(int s){
        size = s;
        neuron = new Neuron[size];

        //System.out.println(neuron.length);
        //System.out.println(size);

        createNeurons();
    }

    public void createNeurons() {
        for(int i = 0; i<size; i++){
            neuron[i] = new Neuron();
        }
    }

    public int getSize() {
        return size;
    }
    /*public void setWeights() {
        for(int i=0,i<size,i++) {
            weight[i] = 0;
        }
    }*/

}