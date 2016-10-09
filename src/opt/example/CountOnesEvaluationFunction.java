package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * A function that counts the ones in the data
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CountOnesEvaluationFunction implements EvaluationFunction {
    /**
	 * Allows tracking of the number of times this function has been evaluated.
	 */
    int evaluations = 0;

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        Vector data = d.getData();
        double val = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == 1) {
                val++;
            }
        }
        evaluations++;
        return val;
    }
    
	/**
	 * Returns the number of times this function has been evaluated.
	 */
    public int getEvaluations()
    {
      return evaluations;
    }
}