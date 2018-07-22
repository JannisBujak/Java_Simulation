package de.uni_hannover.sim.jannisbujak.profiling;

public class Statistics {
  /**
   * 100 normal distributed random double values; already sorted.
   */
  static final double[] data = { -4.486836197, -4.340119298, -3.950329468, -3.928681555, -3.693109245, -3.612294837,
      -3.475395561, -3.375282963, -3.310802716, -3.096677187, -3.033571915, -3.025696454, -3.010237475, -2.976678557,
      -2.947922503, -2.92134875, -2.914453069, -2.87483646, -2.850836066, -2.817448289, -2.817353815, -2.808943641,
      -2.782600671, -2.761758917, -2.760476106, -2.734283127, -2.689135211, -2.648366454, -2.648167969, -2.593656567,
      -2.583552211, -2.582493836, -2.557547197, -2.502916739, -2.485635176, -2.474747159, -2.456896575, -2.438016661,
      -2.435795719, -2.393350017, -2.374976974, -2.361686628, -2.351080604, -2.330734966, -2.311592961, -2.303140576,
      -2.264348945, -2.258228561, -2.248520164, -2.246221753, -2.194306887, -2.179712076, -2.112205216, -2.104824417,
      -2.076201277, -2.058741497, -1.993243148, -1.990088903, -1.973261957, -1.906240964, -1.890816021, -1.86640224,
      -1.852263587, -1.841187912, -1.828000078, -1.809048788, -1.767311841, -1.758956634, -1.699335584, -1.686776351,
      -1.646962016, -1.636389687, -1.62697457, -1.61612997, -1.515287465, -1.490784545, -1.486230654, -1.390562305,
      -1.370638796, -1.26036549, -1.220558631, -1.139360145, -1.031815893, -1.022122131, -0.9644988451, -0.8712443841,
      -0.8358686985, -0.7732202261, -0.5722449902, -0.5718077888, -0.476727832, -0.3639774984, -0.3034239054,
      -0.2826210014, -0.1576029452, -0.1221852599, 0.03271974846, 0.6624309855, 0.7079703219, 1.088398684, };

	  
  public static void main(String[] args) {
    System.out.print("maximum: ");
    System.out.println(calculateMax(data));
	
	 System.out.print("minimum : ");
    System.out.println(calculateMin(data));
	
	System.out.print("mean: ");
    System.out.println(calculateMean(data));
	
	System.out.print("median: ");
    System.out.println(calculateMedian(data));
	
	System.out.print("standard deviation: ");
    System.out.println(calculateStandardDeviation(data));
	
	System.out.print("range: ");
    System.out.println(calculateRange(data));
	
	System.out.print("lower quartile: ");
    System.out.println(calculateLowerQuartile(data));
	
	System.out.print("upper quartile: ");
    System.out.println(calculateUpperQuartile(data));
	
	
    // example use of Math.sqrt:
    //double two = Math.sqrt(4.0);
  }

  /**
   * Finds the greatest value in data. This method assumes that the array is already sorted in ascending order.
   *
   * @param data An arbitrary number of sorted double values
   * @return A greatest value in data
   */
  public static double calculateMax(double[] data) {
    return data[data.length - 1]; 
	/*double max = data[0];
    for (int i = 1; i < data.length; i++) {
      max = Math.max(max, data[i]);
    }
    return max;*/
  }
  
  public static double calculateMin(double[] data) {
    return data[0]; 
  }
  
  public static double calculateMean(double[] data) {
    double sum = 0;
	for(int i = 0; i < data.length; i++){
		sum += data[i];
	}
	return (sum/data.length);
  }
  
  public static double calculateMedian(double[] data) {
    return (data[(data.length-1)/2]);
  }
  
  public static double calculateStandardDeviation(double[] data) {
	double sum = 0;
	for(int i = 0; i < data.length; i++){
		sum += (data[i] - calculateMean(data)) * (data[i] - calculateMean(data));
	}
	double squared = sum/data.length;
	return Math.sqrt(squared);
  }
  
  
  public static double calculateRange(double[] data) {
    return (data[data.length - 1] - data[0]); 
  }
  
  public static double calculateLowerQuartile(double[] data) {
	int position = (data.length - 1)/4;
	return data[position];
  }
  
  public static double calculateUpperQuartile(double[] data) {
    int position = (data.length - 1)*3/4;
	return data[position];
  }
}