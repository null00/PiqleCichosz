// 	$Id: LookUpTable.java,v 1.13 1997/09/08 16:48:27 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft Pawel Cichosz, 1997

package rl;

import util.NumberArray;
import util.ArrayOfNumber;
import util.ArrayOfInt;
import util.Numerical;

/**
  A look-up table function approximator. It is capable of handling
  both discrete and contionuous inputs (the latter with a "boxes"
  representation.
  @author Pawel Cichosz
  @see rl.FunctionApproximator
  */
public class LookUpTable implements FunctionApproximator
{
  /**
    Construct this object as a multi-output look-up table with specified
    input quantization.
    @param nQn an array containg the number of quants for each input.
    @oaram lo an array containing the lower boundaries for each
    input.
    @param hi an array containing the higher boundaries for each
    input.
    @param nOut the number of outputs.
    @exception IllegalArgumentException if <code>nQn</code>,
    <code>lo</code>, or </code>hi</code> are </code>null</code>, empty,
    or have different length, or <code>nOut</code> is less than 1.
    */
  public LookUpTable (int[] nQn, NumberArray lo, NumberArray hi, int nOut)
  {
    if (nQn == null || nQn.length == 0 ||
	lo == null || lo.length () != nQn.length ||
	hi == null || hi.length () != nQn.length ||
	nOut < 1)
      throw new IllegalArgumentException ();

    table = new double[nOut][];
    nQuants = (int []) nQn.clone ();
    low = (NumberArray) lo.clone ();
    high = (NumberArray ) hi.clone ();

    int nEntries = Numerical.product (nQuants);
    for (int o = 0; o < table.length; o ++)
      table[o] = new double[nEntries];
    initialize (0.0);
  }

  /**
    Construct this object as a multi-output look-up table with uniform
    input quantization (one quant corresponds to an interval of length 1).
    @param nIn the number of inputs.
    @oaram lo an array containing the lower boundaries for each
    input.
    @param hi an array containing the higher boundaries for each
    input.
    @param nOut the number of outputs.
    @exception IllegalArgumentException if <code>nIn</code> is less than 1,
    <code>lo</code> or </code>hi</code> are </code>null</code>, empty,
    or have different length, or <code>nOut</code> is less than 1.
    */
  public LookUpTable (int nIn, NumberArray lo, NumberArray hi, int nOut)
  {
    if (nIn < 1 ||
	lo == null || lo.length () != nIn ||
	hi == null || hi.length () != nIn ||
	nOut < 1)
      throw new IllegalArgumentException ();

    table = new double[nOut][];
    nQuants = new int[nIn];
    for (int i = 0; i < nIn; i ++)
      nQuants[i] = hi.getInt (i) - lo.getInt (i);
    low = (NumberArray) lo.clone ();
    high = (NumberArray ) hi.clone ();

    int nEntries = Numerical.product (nQuants);
    for (int o = 0; o < table.length; o ++)
      table[o] = new double[nEntries];
    initialize (0.0);
  }

  /**
    Construct this object as a multi-output look-up table with the
    same input quantization and ranges for each input.
    @param nIn the number of inputs.
    @param nQn the number of quants for each input.
    @oaram lo a <code>Number</code>-based wrapper object containing
    the lower boundary for each input.
    @param hi a <code>Number</code>-based wrapper object containing
    the higher boundary for each input.
    @param nOut the number of outputs.
    @exception IllegalArgumentException if <code>nIn</code> or
    <code>nQn</code> are less than 1,
    <code>lo</code> or </code>hi</code> are </code>null</code>,
    or <code>nOut</code> is less than 1.
    */
  public LookUpTable (int nIn, int nQn, Number lo, Number hi, int nOut)
  {
    if (nIn < 1 || nQn < 1 ||
	lo == null || hi == null ||
	nOut < 1)
      throw new IllegalArgumentException ();

    table = new double[nOut][];
    nQuants = new int[nIn];
    for (int i = 0; i < nIn; i ++)
      nQuants[i] = nQn;
    low = new ArrayOfNumber (nIn);
    high = new ArrayOfNumber (nIn);
    for (int i = 0; i < nIn; i ++)
    {
      low.set (i, lo);
      high.set (i, hi);
    }

    int nEntries = Numerical.product (nQuants);
    for (int o = 0; o < table.length; o ++)
      table[o] = new double[nEntries];
    initialize (0.0);
  }

  /**
    Construct this object as a multi-output look-up table with specified
    input quantization on [0..1] ranges.
    @param nQn an array containg the number of quants for each input.
    @param nOut the number of outputs.
    @exception IllegalArgumentException if <code>nQn</code>
    is <code>null</code> or empty, or </code>nOut</code> is less than 1.
    */
  public LookUpTable (int[] nQn, int nOut)
  {
    if (nQn == null || nQn.length == 0 ||
	nOut < 1)
      throw new IllegalArgumentException ();

    table = new double[nOut][];
    nQuants = (int []) nQn.clone ();
    low = new ArrayOfInt (nQuants.length);
    high = new ArrayOfInt (nQuants.length);
    for (int i = 0; i < nQuants.length; i ++)
    {
      low.set (i, 0);
      high.set (i, 1);
    }

    int nEntries = Numerical.product (nQuants);
    for (int o = 0; o < table.length; o ++)
      table[o] = new double[nEntries];
    initialize (0.0);
  }

  /**
    Initialize function values to the given fixed value.
    @param v the function value to initialize to.
    @see rl.FunctionApproximator_1#initialize(double)
    */
  public synchronized void initialize (double v)
  {
    for (int o = 0; o < table.length; o ++)
      for (int e = 0; e < table[o].length; e ++)
	table[o][e] = v;
  }

  // function approximator interface

  /**
    Restore the single-output function value for the given input vector.
    If this is a multi-output approximator, the first output is used.
    @param x the input vector for which the function value is to be restored.
    @return the function value for <code>x</code>.
    @see rl.FunctionApproximator_1#restore(util.NumberArray)
    */
  public double restore (NumberArray x)
  {
    return restore (x, 0); // just return the first output
  }

  /**
    Restore the function value for the given input vector and all outputs.
    @param x the input vector for which the function value is to be restored.
    @param y the array where the restored values are put.
    @see rl.FunctionApproximator_n#restore(util.NumberArray, double[])
    */
  public void restore (NumberArray x, double[] y)
  {
    for (int o = 0; o < y.length; o ++)
      y[o] = restore (x, o);
  }

  /**
    Restore the function value for the given input vector and output number.
    @param x the input vector for which the function value is to be restored.
    @param o the number of output to be restored.
    @return the restored function value for <code>x</code> and
    output number <code>o</code>.
    @see rl.FunctionApproximator_n#restore(util.NumberArray, int)
    */
  public synchronized double restore (NumberArray x, int o)
  {
    return table[o][entryNum (x)];
  }

  /**
    Update the single-output function value for the given input vector.
    If this is a multi-output approximator, the first output is used.
    @param x the input vector for which the function value is to be updated.
    @param delta the error value for the update operation.
    @param beta the step-size value for the update operation.
    @return the function value for <code>x</code>.
    @see rl.FunctionApproximator_1#update(util.NumberArray, double, double)
    */
  public void update (NumberArray x, double delta, double beta)
  {
    update (x, 0, delta, beta); // update the first output
  }

  /**
    Update the function value for the given input vector and all outputs.
    @param x the input vector for which the function value is to be updated.
    @param delta the array of error values for each output.
    @param beta the step-size value for the update operation.
    @see rl.FunctionApproximator_n#update(util.NumberArray, double[], double)
    */
  public void update (NumberArray x, double[] delta, double beta)
  {
    for (int o = 0; o < delta.length; o ++)
      update (x, o, delta[o], beta);
  }

  /**
    Update the function value for the given input vector and output number.
    @param x the input vector for which the function value is to be updated.
    @param o the number of output to be updated.
    @param delta the error value for the updated output.
    @param beta the step-size value for the update operation.
    @see rl.FunctionApproximator_n#update(util.NumberArray, int,
    double, double)
    */
  public synchronized void update (NumberArray x, int o,
				   double delta, double beta)
  {
    table[o][entryNum (x)] += beta * delta;
  }

  // auxiliary functions

  /**
    Compute the table entry number for the given input. Subclasses
    might use some more sophisticated state-to-entry mapping, hence
    declared protected.
    @param x the input for which the entry number is to be computed.
    @return the table entry number for <code>x</code>.
    */
  protected int entryNum (NumberArray x)
  {
    int e = 0;
    for (int i = 0; i < x.length (); i ++)
      e = e * nQuants[i] + quantNum (i, x);
    return e;
  }

  /**
    Compute the quant number for the given input dimension.
    @param i the number of input vector dimension for which the quant
    number is to be computed.
    @param x the input vector for which the quant number is to be computed.
    @return the quant number for dimension <code>i</code> of
    <code>x</code>.
    */
  protected int quantNum (int i, NumberArray x)
  {
    int q = (int) ((x.getDouble (i) - low.getDouble (i)) /
		   (high.getDouble (i) - low.getDouble (i)) * nQuants[i]);
    return q < 0 ? 0 : q < nQuants[i] ? q : nQuants[i] - 1;
  }

  // data

  /**
    The look-up table data. For each output there is an array of double
    entries.
    */
  protected double[][] table;

  /**
    The array with quantization density for each input dimension.
    */
  protected int[] nQuants; // no. of quants for all inputs

  /**
    The array with lower boundaries for each input dimension.
    */
  protected NumberArray low;

  /**
    The array with higher boundaries for each input dimension.
    */
  protected NumberArray high;

  /**
    Testing.
    */
  public static void main (String[] args)
  {
    int[] nQn = {3, 3, 3};
    int[] lo = {0, 0, 0};
    int[] hi = {2, 2, 2};
    LookUpTable lut = new LookUpTable (nQn,
				       new ArrayOfInt (lo),
				       new ArrayOfInt (hi), 1);
    int[] x = {0, 1, 2};
    System.out.println ("Before update: " +
			lut.restore (new ArrayOfInt (x)));
    lut.update (new ArrayOfInt (x), 1.0, 0.5);
    System.out.println ("After update: " +
			lut.restore (new ArrayOfInt (x)));
  }
}
