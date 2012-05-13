// 	$Id: FunctionApproximator_1.java,v 1.12 1997/09/09 18:06:11 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;
import java.io.Serializable;

/**
  A single-output function approximation interface.
  @author Pawel Cichosz
  @see rl.FunctionApproximator_n
  @see rl.FunctionApproximator
  */
public interface FunctionApproximator_1 extends Serializable
{
  /**
    Restore the function value for the given input vector.
    @param x the input vector for which the function value is to be restored.
    @return the function value for <code>x</code>.
    */
  double restore (NumberArray x);

  /**
    Update the function value for the given input vector.
    @param x the input vector for which the function value is to be updated.
    @param delta the error value for the update operation.
    @param beta the step-size value for the update operation.
    @return the function value for <code>x</code>.
    */
  void update (NumberArray x, double delta, double beta);

  /**
    Initialize the approximated function to the given fixed value
    for arbitrary inputs.
    @param v the initial function value to set.
    */
  void initialize (double v);
}
