// 	$Id: EpsilonGreedySelector.java,v 1.14 1997/09/09 18:05:39 pawel Exp $
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft Pawel Cichosz, 1997

package rl;

import util.Numerical;
import java.util.Random;

/**
  Epsilon-greedy stochastic selection mechanism. One of the simplest
  possible (non-trivial) implementations of
  the <code>StochasticSelector</code> interface. The greedy
  choice is made with probability 1-epsilon and a random choice
  with probability epsilon.
  @author Pawel Cichosz
  @see rl.StochasticSelector
  */
public class EpsilonGreedySelector implements StochasticSelector
{
  /**
    Construct this selector with the given epsilon value.
    @param e the epsilon value to use for selection.
    @exception IllegalArgumentException if <code>e</code> is not in [0..1].
    */
  public EpsilonGreedySelector (double e)
  {
    if (e < 0.0 || e > 1.0)
      throw new IllegalArgumentException ();
    epsilon = e;
  }

  /**
    Construct this selector with a default epsilon value of 0.1.
    */
  public EpsilonGreedySelector ()
  {
    this (0.1);
  }

  // accessors and modifiers to EpsilonGreedySelector parameters

  /**
    Get the epsilon value.
    @return the epsilon value.
    */
  public final synchronized double getEpsilon ()
  {
    return epsilon;
  }

  /**
    Set the epsilon value.
    @param e the new epsilon value to set.
    @exception IllegalArgumentException if <code>e</code> is not in [0..1].
    */
  public final synchronized void setEpsilon (double e)
  {
    if (e < 0.0 || e > 1.0)
      throw new IllegalArgumentException ();
    epsilon = e;
  }

  /**
    Make a choice: the possibility with the greatest chance with
    probability <code>1-eps</code> and a random possibility with
    probability <code>eps</code>. Due to a bug in the current implementation
    always the first greedy possibility is taken into account (if there
    are many).
    @see rl.StochasticSelector#select(double[])
    */
  public synchronized int select (double[] p)
  {
    if (rnd.nextDouble () > epsilon)
      return Numerical.maxIndex (p);
    else
      return Numerical.randomInRange (rnd, 0, p.length - 1);
  }

  /**
    Produce a textual description of this algorithm and its parameter.
    @return the string of the form "EpsilonGreedySelector (eps)".
    */
  public String toString ()
  {
    return "EpsilonGreedySelector (" + epsilon + ")";
  }

  /**
    The epsilon value.
    */
  private double epsilon;

  /**
    A random number generator, shared by all instances and intialized
    to a <code>Random</code> object.
    @see java.util.Random
    */
  private static Random rnd = new Random ();

  /**
    Testing.
    */
  public static void main (String[] args)
  {
    double[] p = new double[args.length];
    int[] c = new int[args.length];
    for (int i = 0; i < args.length; i ++)
    {
      p[i] = Double.valueOf (args[i]).doubleValue ();
      c[i] = 0;
    }

    EpsilonGreedySelector sel = new EpsilonGreedySelector (0.1);

    for (int t = 0; t < 1000; t ++)
    {
      int s = sel.select (p);
      System.err.println (s);
      c[s] ++;
    }
      
    System.out.println ("Selections: " + new util.ArrayOfInt (c));
  }
}
