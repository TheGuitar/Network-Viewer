import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

//import org.graphstream.ui;
//import org.graphstream.graph.*;
//import org.graphstream.graph.implementations.*;
//import org.graphstream.ui.swingViewer.Viewer;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUI extends JFrame implements ActionListener{

    public JButton handw,painting,about,home,chooseFiles,train,test,clear,sample,export,load, applyChanges, matrixView, viewNetwork, showPart;
    public JTextArea startText;
    public JLabel header1,imagNN,accuracy,neuronLabel,layerLabel,afLabel,label1,label2,label3,label4, trainDataLabel, batchLabel, epochLabel, ratioLabel,freezeLabel,epochsTrained,testLoss,trainLoss,sampleOutput;
    public JPanel drawPanel;
    public JSlider ratioSlider,freezeSlider,dataSlider;
    public Font stdFont,smallFont;
    public Graphics g;
    public JFileChooser filech;
    public JTextField hlayerField1, hlayerField2, hlayerField3,lrField,dataField,epochField,sampleField,batchField,sampleInput1,sampleInput2;
    public JComboBox lrBox,afBox;
    public GraphPanel gp;
    public int eT = 0;
    public int oldFreezeRatio=0;

    
    private ArrayList<Point> points;
    private int tool = 1;
    private String currentScreen = "";
    private String prevScreen;
    int currentX, currentY, oldX, oldY;
    Network currentNetwork;

///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////

    public GUI() {
        super("Neural Network Viewer");
        setSize(800,600);
        setLocationRelativeTo(null);
        //setUndecorated(true);
        setDefaultCloseOperation(3);
        setResizable(false);
        setFocusable(true);
        setLayout(null);
        getContentPane().setBackground(Color.GRAY);

        stdFont = new Font("Arial", Font.PLAIN, 20);
        smallFont = new Font("Arial", Font.PLAIN, 12);

        handw = new JButton("MNIST digits");
        handw.setBounds(25, 100, 300, 50);
        handw.addActionListener(this);
        add(handw);

        painting = new JButton("XOR");
        painting.setBounds(450,100,300,50);
        painting.addActionListener(this);
        add(painting);

        about = new JButton("?");
        about.setBounds(700,500,50,50);
        about.addActionListener(this);
        add(about);

        home = new JButton("<");
        home.setBounds(0,0,25,25);
        home.addActionListener(this);
        add(home);

        clear = new JButton("X");
        clear.setBounds(250,300,25,25);
        clear.addActionListener(this);
        add(clear);

        train = new JButton("Train");
        train.setBounds(200,225,125,25);
        train.addActionListener(this);
        add(train);

        test = new JButton("Test");
        test.setBounds(400,225,125,25);
        test.addActionListener(this);
        add(test);

        export = new JButton("Export");
        export.setBounds(600,55,125,25);
        export.addActionListener(this);
        add(export);

        load = new JButton("Import");
        load.setBounds(600,100,125,25);
        load.addActionListener(this);
        add(load);

        viewNetwork = new JButton("view Network");
        viewNetwork.setBounds(600,145,125,25);
        viewNetwork.addActionListener(this);
        add(viewNetwork);

        matrixView = new JButton("Matrix-View");
        matrixView.setBounds(600,200,125,25);
        matrixView.addActionListener(this);
        add(matrixView);

        showPart = new JButton("show extract");
        showPart.setBounds(600,245,125,25);
        showPart.addActionListener(this);
        add(showPart);

        chooseFiles = new JButton("choose Files");
        chooseFiles.setBounds(25,350,125,25);
        chooseFiles.addActionListener(this);
        add(chooseFiles);

        startText = new JTextArea();
        startText.setEditable(false);
        startText.setBounds(25,25,350,525);
        startText.setVisible(true);
        //JScrollPane l = new JScrollPane(left);
        //l.setBounds(25,25,350,525);
        //l.setVisible(true);
        //add(l);
        //add(startText);
        startText.setText("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        startText.setLineWrap(true);
        startText.setWrapStyleWord(true);
        startText.setFont(new Font("Arial", Font.PLAIN, 18));

        header1 = new JLabel();
        header1.setBounds(25,25,600,50);
        //header1.setVerticalAlignment(JLabel.TOP);
        //header1.setHorizontalTextPosition(JLabel.LEFT);
        //header1.setVisible(true);
        header1.setText("Choose a category to use on a Neural Network:");
        header1.setFont(stdFont);
        add(header1);

        sample = new JButton("XOR");
        sample.setBounds(550,400,50,50);
        sample.addActionListener(this);
        add(sample);

        sampleOutput = new JLabel("...");
        sampleOutput.setBounds(650,400,200,50);
        sampleOutput.setFont(stdFont);
        add(sampleOutput);

        sampleInput1 = new JTextField("");
        sampleInput1.setBounds(500,400,50,50);
        sampleInput1.setFont(stdFont);
        add(sampleInput1);

        sampleInput2 = new JTextField("");
        sampleInput2.setBounds(600,400,50,50);
        sampleInput2.setFont(stdFont);
        add(sampleInput2);

        hlayerField1 = new JTextField("16");
        hlayerField1.setBounds(25,75,30,25);
        hlayerField1.setFont(stdFont);
        add(hlayerField1);

        label1 = new JLabel();
        label1.setBounds(25,10,200,50);
        label1.setText("Network");
        label1.setFont(stdFont);
        add(label1);

        label2 = new JLabel();
        label2.setBounds(200,10,200,50);
        label2.setText("Training");
        label2.setFont(stdFont);
        add(label2);

        label3 = new JLabel();
        label3.setBounds(400,10,200,50);
        label3.setText("Testing");
        label3.setFont(stdFont);
        add(label3);

        label4 = new JLabel();
        label4.setBounds(600,10,200,50);
        label4.setText("Options");
        label4.setFont(stdFont);
        add(label4);

        ImageIcon nnpic = new ImageIcon("../img/background.png");
        imagNN = new JLabel();
        imagNN.setIcon(nnpic);
        imagNN.setBounds(0,0,800,400);
        add(imagNN);

        drawPanel = new GPanel();
        drawPanel.setBackground(Color.WHITE);
        drawPanel.setBounds(25,75,250,250);
        add(drawPanel);
        //drawPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        drawPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        ratioSlider = new JSlider(JSlider.HORIZONTAL,0,10000,10000);
        ratioSlider.setBounds(380,75,120,35);
        ratioSlider.setMajorTickSpacing(10000);
        ratioSlider.setMinorTickSpacing(5000);
        ratioSlider.setPaintTicks(true);
        ratioSlider.setPaintLabels(true);
        ratioSlider.setFont(smallFont);
        add(ratioSlider);

        freezeSlider = new JSlider(JSlider.HORIZONTAL,0,100,0);
        freezeSlider.setBounds(380,130,120,35);
        freezeSlider.setMajorTickSpacing(100);
        freezeSlider.setMinorTickSpacing(50);
        freezeSlider.setPaintTicks(true);
        freezeSlider.setPaintLabels(true);
        freezeSlider.setFont(smallFont);
        add(freezeSlider);

        neuronLabel = new JLabel("hidden"+'\n'+"layers:");
        neuronLabel.setBounds(25,55,100,25);
        neuronLabel.setFont(smallFont);
        add(neuronLabel);

        layerLabel = new JLabel("learning rate: ");
        layerLabel.setBounds(25,110,100,25);
        layerLabel.setFont(smallFont);
        add(layerLabel);

        afLabel = new JLabel("activation function:");
        afLabel.setBounds(25,165,100,25);
        afLabel.setFont(smallFont);
        add(afLabel);

        hlayerField1 = new JTextField("16");
        hlayerField1.setBounds(25,75,30,25);
        hlayerField1.setFont(stdFont);
        add(hlayerField1);

        hlayerField2 = new JTextField("0");
        hlayerField2.setBounds(60,75,30,25);
        hlayerField2.setFont(stdFont);
        add(hlayerField2);

        hlayerField3 = new JTextField("0");
        hlayerField3.setBounds(95,75,30,25);
        hlayerField3.setFont(stdFont);
        add(hlayerField3);

        lrField = new JTextField("1");
        lrField.setBounds(25,130,100,25);
        lrField.setFont(stdFont);
        add(lrField);

        lrBox = new JComboBox();
        lrBox.setModel(new DefaultComboBoxModel(new String[] {"1.0", "0.1", "0.001", "0.0001"}));
        lrBox.setBounds(25,130,100,25);
        add(lrBox);
        
        afBox = new JComboBox();
        afBox.setModel(new DefaultComboBoxModel(new String[] {"RELU", "Sigmoid"}));
        afBox.setBounds(25,185,100,25);
        add(afBox);
        
        applyChanges = new JButton("apply changes");
        applyChanges.setBounds(20,225,125,25);
        applyChanges.addActionListener(this);
        add(applyChanges);

        epochField = new JTextField("10");
        epochField.setBounds(200,185,100,25);
        epochField.setFont(stdFont);
        add(epochField);

        batchField = new JTextField("1");
        batchField.setBounds(200,130,100,25);
        batchField.setFont(stdFont);
        add(batchField);

        dataField = new JTextField("1");
        dataField.setBounds(150,250,100,50);
        dataField.setFont(stdFont);
        add(dataField);

        sampleField = new JTextField("10");
        sampleField.setBounds(25,500,100,50);
        sampleField.setFont(stdFont);
        add(sampleField);

        trainDataLabel = new JLabel("data amount:");
        trainDataLabel.setBounds(200,55,100,25);
        trainDataLabel.setFont(smallFont);
        add(trainDataLabel);

        batchLabel = new JLabel("batch size:");
        batchLabel.setBounds(200,110,100,25);
        batchLabel.setFont(smallFont);
        add(batchLabel);

        epochLabel = new JLabel("epochs:");
        epochLabel.setBounds(200,165,100,25);
        epochLabel.setFont(smallFont);
        add(epochLabel);

        dataSlider = new JSlider(JSlider.HORIZONTAL,0,60000,60000);
        dataSlider.setBounds(180,75,120,35);
        dataSlider.setMajorTickSpacing(60000);
        dataSlider.setMinorTickSpacing(30000);
        dataSlider.setPaintTicks(true);
        dataSlider.setPaintLabels(true);
        dataSlider.setFont(smallFont);
        add(dataSlider);

        ratioLabel = new JLabel("data amount:");
        ratioLabel.setBounds(400,55,100,25);
        ratioLabel.setFont(smallFont);
        add(ratioLabel);

        freezeLabel = new JLabel("freeze ratio:");
        freezeLabel.setBounds(400,110,100,25);
        freezeLabel.setFont(smallFont);
        add(freezeLabel);

        epochsTrained = new JLabel();
        epochsTrained.setBounds(25,300,400,50);
        epochsTrained.setText("Epochs Trained: " + Integer.toString(eT));
        epochsTrained.setFont(stdFont);
        add(epochsTrained);

        testLoss = new JLabel();
        testLoss.setBounds(25,400,400,50);
        testLoss.setText("Test loss: 0");
        testLoss.setFont(stdFont);
        add(testLoss);

        trainLoss = new JLabel();
        trainLoss.setBounds(25,500,400,50);
        trainLoss.setText("Training loss: 0");
        trainLoss.setFont(stdFont);
        add(trainLoss);


        points = new ArrayList<Point>();
        setVisible(true);
        homeScreen();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        new GUI();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void homeScreen() {
        prevScreen = currentScreen;
        currentScreen = "home";

        for (Component c : super.getContentPane().getComponents())
        {
        c.setVisible(false);
        }

        handw.setVisible(true);
        painting.setVisible(true);
        about.setVisible(true);
        header1.setVisible(true);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void demoScreen() {
        //System.out.println("\n"+ "\n"+ "\n");
        currentNetwork = new Network(28*28,10,Integer.parseInt(lrField.getText()), Integer.parseInt(hlayerField1.getText()));
        prevScreen = currentScreen;
        currentScreen = "hand";
        lrBox.setSelectedIndex(3);
        afBox.setSelectedIndex(0);
        for (Component c : super.getContentPane().getComponents())
        {
        c.setVisible(false);
        }

        home.setVisible(true);
        imagNN.setVisible(true);
        neuronLabel.setVisible(true);
        layerLabel.setVisible(true);
        afLabel.setVisible(true);
        train.setVisible(true);
        test.setVisible(true);
        hlayerField1.setVisible(true);
        hlayerField2.setVisible(true);
        hlayerField3.setVisible(true);
        label1.setVisible(true);
        label2.setVisible(true);
        label3.setVisible(true);
        label4.setVisible(true);
        lrBox.setVisible(true);
        afBox.setVisible(true);
        applyChanges.setVisible(true);
        dataSlider.setVisible(true);
        trainDataLabel.setVisible(true);
        epochLabel.setVisible(true);
        batchLabel.setVisible(true);
        batchField.setVisible(true);
        epochField.setVisible(true);
        ratioLabel.setVisible(true);
        freezeLabel.setVisible(true);
        ratioSlider.setVisible(true);
        freezeSlider.setVisible(true);
        load.setVisible(true);
        export.setVisible(true);
        matrixView.setVisible(true);
        viewNetwork.setVisible(true);
        showPart.setVisible(true);
        epochsTrained.setVisible(true);
        trainLoss.setVisible(true);
        testLoss.setVisible(true);
    }

       ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void xorScreen() {
        prevScreen = currentScreen;
        currentScreen = "xor";
        lrBox.setSelectedIndex(0);
        afBox.setSelectedIndex(1);
        for (Component c : super.getContentPane().getComponents())
        {
        c.setVisible(false);
        }
        imagNN.setVisible(true);
        home.setVisible(true);
        neuronLabel.setVisible(true);
        layerLabel.setVisible(true);
        afLabel.setVisible(true);
        train.setVisible(true);
        test.setVisible(true);
        hlayerField1.setVisible(true);
        hlayerField2.setVisible(true);
        hlayerField3.setVisible(true);
        label1.setVisible(true);
        label2.setVisible(true);
        label3.setVisible(true);
        label4.setVisible(true);
        lrBox.setVisible(true);
        afBox.setVisible(true);
        applyChanges.setVisible(true);
        epochLabel.setVisible(true);
        epochField.setVisible(true);
        freezeLabel.setVisible(true);
        freezeSlider.setVisible(true);
        load.setVisible(true);
        export.setVisible(true);
        matrixView.setVisible(true);
        viewNetwork.setVisible(true);
        showPart.setVisible(true);
        epochsTrained.setVisible(true);
        trainLoss.setVisible(true);
        testLoss.setVisible(true);
        sample.setVisible(true);
        sampleInput1.setVisible(true);
        sampleInput2.setVisible(true);
        sampleOutput.setVisible(true);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void aboutScreen() {
        prevScreen = currentScreen;
        currentScreen = "about";


    }
    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    private void paintComp() {

        drawPanel.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                drawPanelMousePressed(evt);
            }
            public void mouseReleased(MouseEvent evt) {
                drawPanelMouseReleased(evt);
            }
        });
        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                drawPanelMouseDragged(evt);
            }
        });
    }

    private void drawPanelMouseDragged(MouseEvent evt) {
        if(evt.getX()>=0 && evt.getX()<=250 && evt.getY()>=0 && evt.getX()<=250){
            points.add(new Point(evt.getX(), evt.getY()));
            repaint();
        }
    }

    private void drawPanelMousePressed(MouseEvent evt) {
        points.add(new Point(evt.getX(), evt.getY()));
        repaint();
    }

    private void drawPanelMouseReleased(MouseEvent evt) {
    }

    class GPanel extends JPanel {

        //@Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Point point : points) {
                if(point.x<5){point.x=5;}
                if(point.x>245){point.x=245;}
                if(point.y<5){point.y=5;}
                if(point.y>245){point.y=245;}

                g.fillOval(point.x-5, point.y-5, 10, 10);
            }
        }
    }

    public void saveImage(String name,String type) {
		BufferedImage image = new BufferedImage(drawPanel.getWidth(),drawPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
        drawPanel.paint(g2);
		try{
			ImageIO.write(image, type, new File(name+"."+type));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==handw) {
            demoScreen();
        }
        if(e.getSource()==painting) {
            xorScreen();
        }
        if(e.getSource()==home) {
            if(currentScreen=="about"){
                switch (prevScreen) {
                    case "hand": demoScreen(); break;
                    case "xor": xorScreen(); break;
                    default: homeScreen(); break;
                }
            }
            else {
                homeScreen();
            }
        }
        if(e.getSource()==clear) {
            points.clear();
            repaint();
        }
        if(e.getSource()==chooseFiles) {
            filech = new JFileChooser(".");
            filech.showOpenDialog(this);
            //filech.setFileFilter(FileFilter filter)
        }
        if(e.getSource()==about) {
            System.out.println(dataSlider.getValue());
            System.out.println(lrBox.getSelectedIndex());
        }
        if(e.getSource()==viewNetwork) {
            NetworkViewer.viewWholeNetwork(currentNetwork,false);
        }
        if(e.getSource()==showPart) {
            NetworkViewer.viewNetwork(currentNetwork,5);
        }
        if(e.getSource()==matrixView){
            for(int i=0;i<10;i++) {
                NetworkViewer.viewWeights(currentNetwork.weights[1],i);
            }
        }
        if(e.getSource()==train) { //Training Button
            //eT+=Integer.parseInt(epochField.getText());

            if(freezeSlider.getValue()!=oldFreezeRatio) {
                //currentNetwork.clearFreeze();
                oldFreezeRatio = freezeSlider.getValue();
                if(freezeSlider.getValue() > 0) {
                    currentNetwork.frozen = true;
                    currentNetwork.freezeWeights(freezeSlider.getValue());
                }
                else {
                    currentNetwork.frozen = false;
                }
            }

                switch (currentScreen) {
                    case "hand": try {
                        System.out.println(dataSlider.getValue());
                        Test.mnist(currentNetwork,dataSlider.getValue(),Integer.parseInt(epochField.getText()));
                        
                        //NetworkViewer.viewNetwork(currentNetwork,5);
                      }
                      catch(IOException f) {
                        f.printStackTrace();
                      }
                    
                    break;
                    case "xor": 
                          Test.xor(currentNetwork,Integer.parseInt(epochField.getText()) );
                          
                      
                    break;
                    default: homeScreen(); break;
                }
            eT = currentNetwork.epochsTrained;
            epochsTrained.setText("Epochs Trained: " + Integer.toString(eT));
            trainLoss.setText("Training loss: " + Double.toString(currentNetwork.avgCost));
            System.out.println("Training done");
            
        }
        if(e.getSource()==test) {
            switch (currentScreen) {
                case "hand": try {
                     currentNetwork.errorRate=0;
                     Test.testMnist(currentNetwork,ratioSlider.getValue());
                  }
                  catch(IOException f) {
                    f.printStackTrace();
                  }
                
                break;
                case "xor": 
                      //saveImage("sample", "png");
                        currentNetwork.errorRate = 0;
                        double[][] X = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
                        double[][] Y = {{1,0},{0,1},{0,1},{1,0}};
                        currentNetwork.testNetwork(X, Y);
                        //System.out.println("Error Rate of the Network: " + currentNetwork.errorRate);
                break;
                default: homeScreen(); break;
            }
            testLoss.setText("Test loss: " + Double.toString(currentNetwork.errorRate));
        }
        if(e.getSource()==sample) {
            switch (currentScreen) {
                case "hand": try {
                    Test.sampleMnist(currentNetwork, Integer.parseInt(sampleField.getText()));
                }
                catch(IOException f) {
                    f.printStackTrace();
                }
                
                break;
                case "xor": 
                    int[][] in = {{Integer.parseInt(sampleInput1.getText()),Integer.parseInt(sampleInput2.getText())}};
                    double[] res = currentNetwork.sampleXOR(in);
                    sampleOutput.setText("= " + Integer.toString((int)res[0]) + " : " + Double.toString(res[1]));
                  
                break;
                default: homeScreen(); break;
            }
        }

        if(e.getSource()==load) {
            
            filech = new JFileChooser("Import Network");
            filech.setCurrentDirectory(new File("../savefiles/"));
            filech.showOpenDialog(this);
            File file = filech.getSelectedFile();
            currentNetwork=NetworkIO.importNetwork(file.getPath());
        }
        if(e.getSource()==export) {
            filech = new JFileChooser("Export Network");
            filech.setCurrentDirectory(new File("../savefiles/"));
            filech.showSaveDialog(this);
            String name = filech.getSelectedFile().getName();

            NetworkIO.exportNetwork(currentNetwork,name);
        }
        if(e.getSource()==applyChanges) {
            int[] hidden = new int[3];
            hidden[0]=Integer.parseInt(hlayerField1.getText());
            hidden[1]=Integer.parseInt(hlayerField2.getText());
            hidden[2]=Integer.parseInt(hlayerField3.getText());

            freezeSlider.setValue(0);
            double[] lrList = {1,0.1,0.001,0.0001};
            eT = 0;
            

            switch (currentScreen) {
                case "hand":
                     
                     currentNetwork = new Network(28*28,10,hidden);
                     currentNetwork.learningRate=lrList[lrBox.getSelectedIndex()];
                     if(afBox.getSelectedIndex()==0){
                        currentNetwork.actF = "relu";
                     }
                     else{
                         currentNetwork.actF = "sigmoid";
                     }
                
                break;
                case "xor": 
                    currentNetwork = new Network(2,2,hidden);
                    currentNetwork.learningRate=lrList[lrBox.getSelectedIndex()];
                    if(afBox.getSelectedIndex()==0){
                    currentNetwork.actF = "relu";
                    }
                    else{
                        currentNetwork.actF = "sigmoid";
                    }
                break;
                default: homeScreen(); break;
            }
            epochsTrained.setText("Epochs Trained: " + Integer.toString(eT));
            trainLoss.setText("Training loss: " + Double.toString(currentNetwork.avgCost));
            testLoss.setText("Test loss: " + Double.toString(currentNetwork.errorRate));
        }
    }
}

