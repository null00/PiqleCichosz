// 	$Id: Sarsa.java,v 1.2 1997/08/27 12:01:04 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;

/**
  An implementation of the Sarsa algorithm by extending
  <code>QLearning</code>.
  @author Pawel Cichosz
  @see rl.QLearning
  */
public class Sarsa extends QLearning
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
    @see rl.QLearning#QLearning(rl.CreditAssigner, rl.StochasticSelector)
    */
  public Sarsa (CreditAssigner ca, StochasticSelector sa,
		FunctionApproximator_1[] qf, double b)
  {
    super (ca, sa, qf, b);
  }

  // redefinition of next-state-utility

  /**
    Compute the utility of the given state-action pair assuming
    it represents the next experience. For Sarsa it is the same as
    <code>QLearning.utility0 ()</code>.
    @param x the (successor) state.
    @param a the (successor) action.
    @return the utility of <code>(x, a)</code>.
    @see rl.Learner#utility1(rl.NumberArray, int)
    @see rl.QLearning#utility0(rl.NumberArray, int)
    */
  protected double utility1 (NumberArray x, int a)
  {
    return utility0 (x, a);
  }
}
