// 	$Id: Learner.java,v 1.27 1997/09/09 18:03:35 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;
import util.Parameterized;
import util.PropertiesManager;
import java.io.Serializable;
import java.util.Properties;

/**
  An abstract class representing a generic reinforcement learning algorithm.
  Specifies a simple, but easy to use and sufficiently powerful RL
  interface as well as some provides some support for the implementation.
  It should be subclasses to get particular algorithms, such as AHC or
  Q-learning.
  @author Pawel Cichosz
  @see rl.QLearning
 */
public abstract class Learner implements Parameterized, Serializable
{
  /**
    Construct the learner using the given helper objects for credit
    assignement and action selection.
    @param ca the object of a class implementing
    the <code>CreditAssigner</code> interface to be used by this learner
    for credit assignment.
    @param as the object of a class implementing
    the <code>StochasticSelector</code> interface to be used by this learner
    for action selection.
    @see rl.CreditAssigner
    @see rl.StochasticSelector
    */
  public Learner (CreditAssigner ca, StochasticSelector as)
  {
    creditAssigner = ca;
    actionSelector = as;
  }

  /**
    Get the credit assignment object used by this learner.
    @return the <code>CreditAssigner</code> object used by this learner
    for credit assignment.
    */
  public final CreditAssigner getCreditAssigner ()
  {
    return creditAssigner;
  }

  /**
    Get the action selection object used by this learner.
    @return the <code>StochasticSelector</code> object used by this learner
    for action selection.
    */
  public final StochasticSelector getActionSelector ()
  {
    return actionSelector;
  }

  /**
    Get the properties of this learner. Includes the parameters of the learner
    object itself (that may be added by subclasses) and the parameters
    of the credit assignemnent and action selection objects. Subclasses
    should override this method if and only if they add new object fields
    with some properties.
    @param the destination for the properties to get.
    */
  public void getProperties (Properties p)
  {
    PropertiesManager.getAllProperties (p, this);
    PropertiesManager.getAllProperties (p, creditAssigner);
    PropertiesManager.getAllProperties (p, actionSelector);
  }

  /**
    Set the properties of this learner. May include the parameters of
    the learner object itself (that may be added by subclasses) and
    the parameters of the credit assignemnent and action selection objects.
    Subclasses should override this method if and only if they add new
    object fields with some properties.
    @param the source for the properties to set.
    */
  public void setProperties (Properties p)
  {
    PropertiesManager.setProperties (p, this);
    PropertiesManager.setProperties (p, creditAssigner);
    PropertiesManager.setProperties (p, actionSelector);
  }

  // public reinforcement learning interface

  /**
    React to the given state.
    @param x the current environment.
    @return the action selected for state <code>x</code>.
    @see rl.Learner#reinforce(double)
    @see util.NumberArray
    */
  public int react (NumberArray x)
  {
    int a = actionSelector.select (policy (x));
    creditAssigner.stateAndAction (x, a, utilFun);
    return a;
  }

  /**
    Reinforce the previous state-action pair.
    @param r the reinforcement value for the previous (i.e., corresponding
    to the previous call to <code>react ()</code>) state-action pair.
    @see rl.Learner#react(util.NumberArray)
    */
  public void reinforce (double r)
  {
    creditAssigner.reward (r, utilFun);
  }

  /**
    Reset the learner on end of trial/learning.
    */
  public void reset ()
  {
    creditAssigner.reset (utilFun);
  }

  // other public methods

  /**
    Clear the learner's knowledge. Depends on knowledge representation and
    therefore must be defined by subclasses.
    */
  public abstract void clearKnowledge ();

  /* DISABLED: waiting until the IO library is consistent
     public abstract void saveKnowledge (OutputStream os); // Writer?
     public abstract void loadKnowledge (InputStream is); // Writer?
     */

  /**
    Get a string representation of this learner (some text
    specifying in a human-readable form) what RL algorithm with
    what parameters it uses.
    @return a string representation of this.
    */
  public String toString ()
  {
    return "Learner\n"
      + "\tusing " + creditAssigner + " for credit assignment\n"
      + "\tusing " + actionSelector + " for action selection\n";
  }

  // protected abstract methods to be defined by particular RL algorithms

  /**
    Compute the policy function for the given state. Must be defined
    by subclasses.
    @param x the state for which the policy is to be computed.
    @return the array of <code>double</code> representing the
    policy for state <code>x</code> (each array element corresponds
    to one possible action).
    */
  protected abstract double[] policy (NumberArray x);

  /**
    Compute the utility of the given state-action pair assuming
    it represents the current experience. Must be defined by subclasses.
    @param x the (current) state.
    @param a the (current) action.
    @return the utility of <code>(x, a)</code>.
    @see rl.Learner#utility1(NumberArray, int)
    */
  protected abstract double utility0 (NumberArray x, int a);

  /**
    Compute the utility of the given state-action pair assuming
    it represents the next experience. Must be defined by subclasses.
    @param x the (successor) state.
    @param a the (successor) action.
    @return the utility of <code>(x, a)</code>.
    @see rl.Learner#utility0(NumberArray, int)
    */
  protected abstract double utility1 (NumberArray x, int a);

  /**
    Perform utility & policy update for the given state-action pair
    using the given error value. Must be defined by subclasses.
    @param x the state for the update operation.
    @param a the action for the update operation.
    @param delta the error value for the update operation.
    */
  protected abstract void update (NumberArray x, int a, double delta);

// private stuff

  /**
    The object of a class implementing the <code>CreditAssigner</code>
    interface used by this learner for credit asssignment.
    */
  private CreditAssigner creditAssigner;

  /**
    The object of a class implementing the <code>StochasticSelector</code>
    interface used by this learner for action selection.
    */
  private StochasticSelector actionSelector;

  /**
    The object of an anonymous class implementig
    the <code>UtilityFunction</code> interface used to provide
    the credit assignment object with a callback access to
    <code>utility0 ()</code>, <code>utility1 ()</code>, and
    <code>update ()</code>.
    */
  private  UtilityFunction utilFun = new UtilityFunction ()
  { // anonymous inner class giving access to selected functions
    public double utility0 (NumberArray x, int a)
      {
	return Learner.this.utility0 (x, a);
      }
    public double utility1 (NumberArray x, int a)
      {
	return Learner.this.utility1 (x, a);
      }
    public void update (NumberArray x, int a, double delta)
      {
	Learner.this.update (x, a, delta);
      }
  };
}
