// 	$Id: CreditAssigner.java,v 1.9 1997/11/08 19:12:39 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;
import java.io.Serializable;

/**
  The interface for credit assignment algorithms for reinforcement
  learning. An object of a class implementing this interface must be
  passed to the constructor of <code>Learner</code> when
  instantiating any of its subclasses.
  @author Pawel Cichosz
  @see rl.Learner
 */
public interface CreditAssigner extends Serializable
{
  /**
    Perform whatever credit assignment is appropriate given the most
    recent state-action pair.
    @param x the current state.
    @param a the current action.
    @param utilFun the utility function callback object giving access
    to the learner's utility function.
    @see rl.UtilityFunction
    */
  void stateAndAction (NumberArray x, int a, UtilityFunction utilFun);

  /**
    Perform whatever credit assignment is appropriate given the most
    recent reward value.
    @param r the current reward.
    @param utilFun the utility function callback object giving access
    to the learner's utility function.
    @see rl.UtilityFunction
    */     
  void reward (double r, UtilityFunction utilFun);

  /**
    Perform whatever credit assignment is appropriate on end of
    trial/learning.
    @param utilFun the utility function callback object giving access
    to the learner's utility function.
    @see rl.UtilityFunction
    */       
  void reset (UtilityFunction uf);
}
