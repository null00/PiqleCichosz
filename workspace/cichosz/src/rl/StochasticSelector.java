// 	$Id: StochasticSelector.java,v 1.9 1997/11/08 19:13:23 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import java.io.Serializable;

/**
  An interface for stochastic selection of one possibility out of several
  options. The most important usage is for action selection in reinforcement
  learning.
  @author Pawel Cichosz
  */
public interface StochasticSelector extends Serializable
{
  /**
    Select one possibility, given an array of "chances" for all
    possibilities.
    @param p the array of chances.
    @return the selected possibility (between 0 and <code>p.length</code>).
    */
  int select (double[] p);
}
