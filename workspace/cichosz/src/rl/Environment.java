// 	$Id: Environment.java,v 1.14 1997/09/09 18:03:58 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;
import util.Parameterized;
import util.PropertiesManager;
import java.io.Serializable;
import java.util.Observable;
import java.util.Properties;

/**
  An abstract class that specifies simple reinforcement learning.
  The main reason why it is a class rather than an interface (which might
  be sometimes preferrable) is that it extends class
  <code>java.util.Observable</code>. It also implements
  the <code>util.Parameterized</code> interface.
  @author Pawel Cichosz
  @see util.Parameterized
  @see java.util.Observable
  */
public abstract class Environment
extends Observable implements Parameterized, Serializable
{
  // notification messages sent to observers

  /**
    Notification argument when notifying about state change.
    */
  public static final String STATE = "State";

  /**
    Notification argument when notifying about properties change.
    */
  public static final String PROPERTIES = "Properties";

  /**
    Construct this environment. Empty default constructor.
    */
  public Environment ()
  {
  }

  /**
    Get the properties of this grid environment. Includes <code>xSize</code>,
    <code>ySize</code>, <code>startX</code>, and <code>startY</code>.
    @param the destination for the properties to get.
    */
  public void getProperties (Properties p)
  {
    PropertiesManager.getAllProperties (p, this);
  }

  /**
    Set the properties of this environment. May include <code>xSize</code>,
    <code>ySize</code>, <code>startX</code>, and <code>startY</code>.
    @param the source for the properties to set.
    */
  public void setProperties (Properties p)
  {
    PropertiesManager.setProperties (p, this);
    setChanged ();
    notifyObservers (PROPERTIES);
  }

  // public environment interface

  /**
    Get the curremt environment state.
    @return the current state.
    */
  public final synchronized NumberArray state ()
  {
    return currentState;
  }
  
  /**
    Make a state transition due to action execution, produce a reward,
    and notify observers.
    @param a the executed action.
    @return the reward value for the executed action.
    */
  public final double execute (int a)
  {
    synchronized (this) {
      stateTransition (a);
    }
    setChanged ();
    notifyObservers (STATE);
    return reward ();
  }
  
  /**
    The end of trial flag.
    @return <code>true</code> if a terminal state has been reached
    and <code>false</code> otherwise.
    */
  public abstract boolean terminalState ();

  /**
    Reset the environment to an initial state and notify observers.
    */
  public final void reset ()
  {
    synchronized (this) {
      stateInit ();
    }
    setChanged ();
    notifyObservers (STATE);
  }

  // auxliary (abstract) methods

  /**
    Perform a state transition for the given action.
    @param a the executed action.
    */
  protected abstract void stateTransition (int a);

  /**
    Set an initial state.
    */
  protected abstract void stateInit ();

  /**
    Generate a reward value for the current state.
    @return the reward value for the current state.
    */
  protected abstract double reward ();

  /**
    The current environment state.
    */
  protected NumberArray currentState;
}
