// 	$Id: QLearning.java,v 1.19 1997/09/08 17:42:34 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;

/**
  An implementation of the Q-learning algorithm derived from
  <code>Learner</code>.
  @author Pawel Cichosz
  @see rl.Learner
  */
public class QLearning extends Learner
{
  /**
    Construct an object using given helper objects for credit assignment,
    action selection, and function representation, and a step-size value
    for function update.
    @param ca the object to use for credit assignement.
    @param sa the object to use for action selection.
    @param qf the array of objects to use for function representation,
    one for each action.
    @param b the step-size value for function update.
    @exception IllegalArgumentException if <code>qf</code> is
    <code>null</code> or empty, or </code>b</code> is not in [0..1].
    @see rl.Learner#Learner(rl.CreditAssigner, rl.StochasticSelector)
    */
  public QLearning (CreditAssigner ca, StochasticSelector sa,
		    FunctionApproximator_1[] qf, double b)
  {
    super (ca, sa);

    if (qf == null || qf.length == 0 ||
	b < 0.0 || b > 1.0)
      throw new IllegalArgumentException ();

    qfun = qf;
    q = new double[qf.length];
    beta = b;
  }

  // accessors and modifiers for QLearning parameters

  /**
    Get the step-size beta.
    @return the beta value.
    */
  public final synchronized double getBeta ()
  {
    return beta;
  }

  /**
    Set the step-size beta.
    @param b the new beta value to set.
    @exception IllegalArgumentException if <code>b</code> is not
    in [0..1].
    */
  public final synchronized void setBeta (double b)
  {
    if (b < 0.0 || b > 1.0)
      throw new IllegalArgumentException ();
    beta = b;
  }

  // redefinitions of inherited abstract functions

  /**
    Clear the learner's knowledge. Re-intializes the Q-function to 0.
    @see rl.Learner#clearKnowledge()
    @see rl.FunctionApproximator_1#initialize(double)
    */
  public void clearKnowledge ()
  {
    for (int i = 0; i < qfun.length; i ++)
      qfun[i].initialize (0.0);
  }

  /**
    Compute the policy function for the given state. Implemented by
    retrieving the Q-values for the given state and all actions.
    @param x the state for which the policy is to be computed.
    @return the reference to an internally stored array of
    <code>double</code> Q-values for state <code>x</code> (each array
    element corresponds to one possible action).
    @see rl.Learner#policy(util.NumberArray)
    */
  protected synchronized double[] policy (NumberArray x)
  {
    for (int a = 0; a < qfun.length; a ++)
      q[a] = qfun[a].restore (x);
    return q;
  }

  /**
    Compute the utility of the given state-action pair assuming
    it represents the current experience. Implemented by retrieving
    the Q-value for the given state-action pair.
    @param x the (current) state.
    @param a the (current) action.
    @return the utility of <code>(x, a)</code>
    @see rl.Learner#utility0(util.NumberArray, int)
    */
  protected double utility0 (NumberArray x, int a)
  {
    return qfun[a].restore (x);
  }

  /**
    Compute the utility of the given state-action pair assuming
    it represents the next experience. Implemented by finding
    the maximum Q-value for the given state (ignoring the action). 
    @param x the (successor) state.
    @param a the (successor) action.
    @return the utility of <code>(x, a)</code>.
    @see rl.Learner#utility1(rl.NumberArray, int)
    */
  protected double utility1 (NumberArray x, int a)
  {
    double qmax = - Double.MAX_VALUE;
    for (int a1 = 0; a1 < qfun.length; a1 ++)
      qmax = Math.max (qmax, qfun[a1].restore (x));
    return qmax;
  }

  /**
    Perform utility & policy update for the given state-action pair
    using the given error value. Implemented by updating the Q-value for
    the given state-action pair using the given error value.
    @param x the state for the update operation.
    @param a the action for the update operation.
    @param delta the error value for the update operation.
    @see rl.Learner#update(util.NumberArray, int, double)
    @see rl.FunctionApproximator_1#update(util.NumberArray, double, double)
    */
  protected synchronized void update (NumberArray x, int a, double delta)
  {
    qfun[a].update (x, delta, beta);
  }

  // data fields

  /**
    The array of function approximators representing the Q-function
    for each action.
    @see rl.FunctionApproximator_1
    */
  protected FunctionApproximator_1 qfun[];

  /**
    The step-size value used for function update.
    @see rl.QLearning#update(util.NumberArray, int, double)
    */
  private double beta;

  /**
    An auxiliary field for storing internally the Q-values returned by
    <code>policy</code>. This allows us to avoid creating a new
    array each time.
    @see rl.QLearning#policy(util.NumberArray)
    */
  private double q[]; // storage for values returned by policy ()
}
