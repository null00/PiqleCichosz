// 	$Id: FunctionApproximator.java,v 1.4 1997/08/14 09:43:51 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

/**
  A general function approximation interface. It combines the interfaces
  <code>FunctionApproximator_1</code> and
  <code>FunctionApproximator_n</code>.
  @author Pawel Cichosz
  @see rl.FunctionApproximator_1
  @see rl.FunctionApproximator_n
  */
public interface FunctionApproximator
extends FunctionApproximator_1, FunctionApproximator_n  
{
}
