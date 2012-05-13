// 	$Id: BoltzmannSelector.java,v 1.8 1997/09/09 18:05:23 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft Pawel Cichosz, 1997

package rl;

import util.Numerical;
import java.util.Random;

/**
  Boltzmann distribution stochastic selection mechanism. This
  implementation of
  the <code>StochasticSelector</code> interface makes choices with
  probabilities proportional to the exponential function of the original
  chances divided by a positive temperature value.
  @author Pawel Cichosz
  @see rl.StochasticSelector
  */
public class BoltzmannSelector implements StochasticSelector
{
  /**
    Construct this selector with the temperature value.
    @param t the temperature value to use for selection.
    @exception IllegalArgumentException if <code>t</code><=0.
    */
  public BoltzmannSelector (double t)
  {
    if (t <= 0.0)
      throw new IllegalArgumentException ();
    temperature = t;
  }

  /**
    Construct this selector with a default temperature value of 0.1.
    */
  public BoltzmannSelector ()
  {
    this (0.1);
  }

  // accessors and modifiers for BoltzmannSelector parameters

  /**
    Get the temperature value.
    @return the temperature value.
    */
  public final synchronized double getTemperature ()
  {
    return temperature;
  }

  /**
    Set the temperature value.
    @param t the new temperature value to set.
    @exception IllegalArgumentException if <code>t</code><=0.
    */
  public final synchronized void setTemperature (double t)
  {
    if (t <= 0.0)
      throw new IllegalArgumentException ();
    temperature = t;
  }

  /**
    Make a choice based on a Boltzmann distribution specified by
    the given "chances" vector.
    @param p the chances vector.
    @return the selected possibility (between 0 and <code>p.length</code>).
    @see rl.StochasticSelector#select(double[])
    */
  public synchronized int select (double[] p)
  {
    if (distribution == null || distribution.length != p.length)
      distribution = new double[p.length];
    for (int i = 0; i < p.length; i ++)
      distribution[i] = Math.exp ((p[i] - p[0]) / temperature);
    return Numerical.randomChoice (rnd, distribution);
  }

  /**
    Produce a textual description of this algorithm and its parameter.
    @return the string of the form "EpsilonGreedySelector (eps)".
    */
  public String toString ()
  {
    return "BoltzmannSelector (" + temperature + ")";
  }

  /**
    The temperature value. Declared protected to allow subclasses
    to implement some adaptive strategies.
    */
  private double temperature;

  /**
    The array for storing the Boltzmann distribution.
    */
  private double[] distribution;

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

    BoltzmannSelector sel = new BoltzmannSelector (0.1);

    for (int t = 0; t < 1000; t ++)
    {
      int s = sel.select (p);
      System.err.println (s);
      c[s] ++;
    }
      
    System.out.println ("Selections: " + new util.ArrayOfInt (c));
  }
}
