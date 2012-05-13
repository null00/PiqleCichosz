// 	$Id: FunctionApproximator_n.java,v 1.12 1997/09/09 18:06:28 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;
import java.io.Serializable;

/**
  A multi-output function approximation interface.
  @author Pawel Cichosz
  @see rl.FunctionApproximator_1
  @see rl.FunctionApproximator
  */
public interface FunctionApproximator_n extends Serializable
{
  /**
    Restore the function value for the given input vector and all outputs.
    @param x the input vector for which the function value is to be restored.
    @param y the array where the restored values are put.
    @see rl.FunctionApproximator_n#restore(util.NumberArray, int)
    */
  void restore (NumberArray x, double[] y);

  /**
    Restore the function value for the given input vector and output number.
    @param x the input vector for which the function value is to be restored.
    @param o the number of output to be restored.
    @return the restored function value for <code>x</code> and
    output number <code>o</code>.
    @see rl.FunctionApproximator_n#restore(util.NumberArray, double[])
    */
  double restore (NumberArray x, int o);

  /**
    Update the function value for the given input vector and all outputs.
    @param x the input vector for which the function value is to be updated.
    @param delta the array of error values for each output.
    @param beta the step-size value for the update operation.
    @see rl.FunctionApproximator_n#update(util.NumberArray, int,
    double, double)
    */
  void update (NumberArray x, double[] delta, double beta);

    /**
    Update the function value for the given input vector and output number.
    @param x the input vector for which the function value is to be updated.
    @param o the number of output to be updated.
    @param delta the error value for the updated output.
    @param beta the step-size value for the update operation.
    @see rl.FunctionApproximator_n#update(util.NumberArray,
    double[], double)
    */
  void update (NumberArray x, int o, double delta, double beta);
}
