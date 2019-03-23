public class Example {
 
  public static void main(String[] args) throws Exception {
 
    double[] xData = new double[] { 0.0, 1.0, 2.0 };
    double[] yData = new double[] { 2.0, 1.0, 0.0 };
 
    // Create Chart
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
 
    // Show it
    new SwingWrapper(chart).displayChart();
 
  }
}