// 	$Id: Numerical.java,v 1.8 1997/08/27 15:19:13 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package util;

import java.util.Random;

/**
  A collection of various simple but handy numerical routines implemented
  as static methods.
  @author Pawel Cichosz
  */
public final class Numerical
{
  /**
    Find the maximum element in the given array of integer numbers.
    @param a the array of <code>int</code> the maximum element of which
    is to be found.
    @return the maximum element in <code>a</code>.
    @see util.Numerical#min(int[])
    @see util.Numerical#maxIndex(int[])
    @see util.Numerical#minIndex(int[])
    */
  public static int max (int[] a)
  {
    return a[maxIndex (a)];
  }

  /**
    Find the maximum element in the given array of double numbers.
    @param a the array of <code>double</code> the maximum element of which
    is to be found.
    @return the maximum element in <code>a</code>.
    @see util.Numerical#min(double[])
    @see util.Numerical#maxIndex(double[])
    @see util.Numerical#minIndex(double[])
    */
  public static double max (double[] a)
  {
    return a[maxIndex (a)];
  }

  /**
    Find the maximum element in the given array of standard number wrapper
    objects of subclasses of <code>Number</code>.
    The <code>Number.doubleValue ()</code> function is used for comparing
    elements.
    @param a the array of <code>Number</code>-based wrapper objects
    the maximum element of which is to be found.
    @return the maximum element in <code>a</code>.
    @see util.Numerical#min(java.lang.Number[])
    @see util.Numerical#maxIndex(java.lang.Number[])
    @see util.Numerical#minIndex(java.lang.Number[])
    @see java.lang.Number
    */
  public static Number max (Number[] a)
  {
    return a[maxIndex (a)];
  }

  /**
    Find the maximum element in the given <code>NumberArray</code> object.
    The <code>NumberArray.getDouble ()</code> function is used for comparing
    elements.
    @param a the </cite>NumberArray</cite> object the maximum element
    of which is to be found.
    @return the maximum element in <code>a</code>.
    @see util.Numerical#min(util.NumberArray)
    @see util.Numerical#maxIndex(util.NumberArray)
    @see util.Numerical#minIndex(util.NumberArray)
    @see util.NumberArray
    */
  public static Number max (NumberArray a)
  {
    return a.get (maxIndex (a));
  }

  /**
    Find the minimum element in the given array of integer numbers.
    @param a the array of <code>int</code> the minimum element of which
    is to be found.
    @return the minimum element in <code>a</code>.
    @see util.Numerical#max(int[])
    @see util.Numerical#minIndex(int[])
    @see util.Numerical#maxIndex(int[])
    */
  public static int min (int[] a)
  {
    return a[minIndex (a)];
  }

  /**
    Find the minimum element in the given array of double numbers.
    @param a the array of <code>double</code> the minimum element of which
    is to be found.
    @return the minimum element in <code>a</code>.
    @see util.Numerical#max(double[])
    @see util.Numerical#minIndex(double[])
    @see util.Numerical#maxIndex(double[])
    */
  public static double min (double[] a)
  {
    return a[minIndex (a)];
  }

  /**
    Find the minimum element in the given array of standard number wrapper
    objects of subclasses of <code>Number</code>.
    The <code>Number.doubleValue ()</code> function is used for comparing
    elements.
    @param a the array of <code>Number</code>-based wrapper objects
    the minimum element of which is to be found.
    @return the minimum element in <code>a</code>.
    @see util.Numerical#max(java.lang.Number[])
    @see util.Numerical#minIndex(java.lang.Number[])
    @see util.Numerical#maxIndex(java.lang.Number[])
    @see java.lang.Number
    */
  public static Number min (Number[] a)
  {
    return a[minIndex (a)];
  }

  /**
    Find the minimum element in the given <code>NumberArray</code> object.
    The <code>NumberArray.getDouble ()</code> function is used for comparing
    elements.
    @param a the </cite>NumberArray</cite> object the minimum element
    of which is to be found.
    @return the minimum element in <code>a</code>.
    @see util.Numerical#max(util.NumberArray)
    @see util.Numerical#minIndex(util.NumberArray)
    @see util.Numerical#maxIndex(util.NumberArray)
    @see util.NumberArray
    */
  public static Number min (NumberArray a)
  {
    return a.get (minIndex (a));
  }

  /**
    Find the (least) index of the  maximum element in the given array of
    integer numbers.
    @param a the array of <code>int</code> the index of the maximum
    element of which is to be found.
    @return the index of the maximum element in <code>a</code>.
    @see util.Numerical#minIndex(int[])
    @see util.Numerical#max(int[])
    @see util.Numerical#min(int[])
    */
  public static int maxIndex (int[] a)
  {
    if (a.length < 1)
      return -1;
    int m = 0, v = a[0];
    for (int i = 0; i < a.length; i ++)
      if (a[i] > v)
	v = a[m = i];
    return m;
  }

  /**
    Find the (least) index of the maximum element in the given array of
    double numbers.
    @param a the array of <code>double</code> the index of the  maximum
    element of which is to be found.
    @return the index of the maximum element in <code>a</code>.
    @see util.Numerical#minIndex(double[])
    @see util.Numerical#max(double[])
    @see util.Numerical#min(double[])
    */
  public static int maxIndex (double[] a)
  {
    if (a.length < 1)
      return -1;
    int m = 0;
    double v = a[0];
    for (int i = 0; i < a.length; i ++)
      if (a[i] > v)
	v = a[m = i];
    return m;
  }

  /**
    Find the (least) index of the  maximum element in the given array of
    standard number wrapper objects of subclasses of <code>Number</code>.
    The <code>Number.doubleValue ()</code> function is used for comparing
    elements.
    @param a the array of <code>Number</code>-based wrapper objects
    the index of the maximum element of which is to be found.
    @return the index of the maximum element in <code>a</code>.
    @see util.Numerical#minIndex(java.lang.Number[])
    @see util.Numerical#max(java.lang.Number[])
    @see util.Numerical#min(java.lang.Number[])
    @see java.lang.Number
    */
  public static int maxIndex (Number[] a)
  {
    if (a.length < 1)
      return -1;
    int m = 0;
    double v = a[0].doubleValue (); // not very efficient, but simple
    for (int i = 0; i < a.length; i ++)
      if (a[i].doubleValue () > v)
	v = a[m = i].doubleValue ();
    return m;
  }

  /**
    Find the (least) index of the  maximum element in the given
    <code>NumberArray</code> object.
    The <code>NumberArray.getDouble ()</code> function is used for comparing
    elements.
    @param a the </cite>NumberArray</cite> object the index of
    the maximum element of which is to be found.
    @return the index of the maximum element in <code>a</code>.
    @see util.Numerical#minIndex(util.NumberArray)
    @see util.Numerical#max(util.NumberArray)
    @see util.Numerical#min(util.NumberArray)
    @see util.NumberArray
    */
  public static int maxIndex (NumberArray a)
  {
    if (a.length () < 1)
      return -1;
    int m = 0;
    double v = a.getDouble (0); // not very efficient, but simple
    for (int i = 0; i < a.length (); i ++)
      if (a.getDouble (i) > v)
	v = a.getDouble (m = i);
    return m;
  }

  /**
    Find the (least) index of the  minimum element in the given array of
    integer numbers.
    @param a the array of <code>int</code> the index of the minimum
    element of which is to be found.
    @return the index of the minimum element in <code>a</code>.
    @see util.Numerical#maxIndex(int[])
    @see util.Numerical#min(int[])
    @see util.Numerical#max(int[])
    */
  public static int minIndex (int[] a)
  {
    if (a.length < 1)
      return -1;
    int m = 0, v = a[0];
    for (int i = 0; i < a.length; i ++)
      if (a[i] < v)
	v = a[m = i];
    return m;
  }

  /**
    Find the (least) index of the minimum element in the given array of
    double numbers.
    @param a the array of <code>double</code> the index of the  minimum
    element of which is to be found.
    @return the index of the minimum element in <code>a</code>.
    @see util.Numerical#maxIndex(double[])
    @see util.Numerical#min(double[])
    @see util.Numerical#max(double[])
    */
  public static int minIndex (double[] a)
  {
    if (a.length < 1)
      return -1;
    int m = 0;
    double v = a[0];
    for (int i = 0; i < a.length; i ++)
      if (a[i] < v)
	v = a[m = i];
    return m;
  }

  /**
    Find the (least) index of the  minimum element in the given array of
    standard number wrapper objects of subclasses of <code>Number</code>.
    The <code>Number.doubleValue ()</code> function is used for comparing
    elements.
    @param a the array of <code>Number</code>-based wrapper objects
    the index of the minimum element of which is to be found.
    @return the index of the minimum element in <code>a</code>.
    @see util.Numerical#maxIndex(java.lang.Number[])
    @see util.Numerical#min(java.lang.Number[])
    @see util.Numerical#max(java.lang.Number[])
    @see java.lang.Number
    */
  public static int minIndex (Number[] a)
  {
    if (a.length < 1)
      return -1;
    int m = 0;
    double v = a[0].doubleValue (); // not very efficient, but simple
    for (int i = 0; i < a.length; i ++)
      if (a[i].doubleValue () < v)
	v = a[m = i].doubleValue ();
    return m;
  }

  /**
    Find the (least) index of the  minimum element in the given
    <code>NumberArray</code> object.
    The <code>NumberArray.getDouble ()</code> function is used for comparing
    elements.
    @param a the </cite>NumberArray</cite> object the index of
    the minimum element of which is to be found.
    @return the index of the minimum element in <code>a</code>.
    @see util.Numerical#maxIndex(util.NumberArray)
    @see util.Numerical#min(util.NumberArray)
    @see util.Numerical#max(util.NumberArray)
    @see util.NumberArray
    */
  public static int minIndex (NumberArray a)
  {
    if (a.length () < 1)
      return -1;
    int m = 0;
    double v = a.getDouble (0); // not very efficient, but simple
    for (int i = 0; i < a.length (); i ++)
      if (a.getDouble (i) < v)
	v = a.getDouble (m = i);
    return m;
  }

  /**
    Compute the sum of elements in the given array of integer numbers.
    @param a the array of <code>int</code> the sum of elements of
    which is to be computed.
    @return the sum of elements in <code>a</code>.
    @see util.Numerical#product(int[])
    */
  public static int sum (int[] a)
  {
    int s = 0;
    for (int i = 0; i < a.length; i ++)
      s += a[i];
    return s;
  }

  /**
    Compute the sum of elements in the given array of double numbers.
    @param a the array of <code>double</code> the sum of elements of
    which is to be computed.
    @return the sum of elements in <code>a</code>.
    @see util.Numerical#product(double[])
    */
  public static double sum (double[] a)
  {
    double s = 0.0;
    for (int i = 0; i < a.length; i ++)
      s += a[i];
    return s;
  }

  /**
    Compute the sum of elements in the <code>NumberArray</code> object.
    The <code>NumberArray.getDouble ()</code> function is used for
    adding elements.
    @param a the <code>NumberArray</code> object the sum of elements
    of which is to be computed.
    @return a new <code>Number</code>-based wrapper object containing
    sum of elements in <code>a</code>.
    @see util.Numerical#product(util.NumberArray)
    */
  public Number sum (NumberArray a)
  {
    double s = 0.0;
    for (int i = 0; i < a.length (); i ++)
      s += a.getDouble (i);
    return new Double (s);
  }

  /**
    Compute the product of elements in the given array of integer numbers.
    @param a the array of <code>int</code> the product of elements of
    which is to be computed.
    @return the product of elements in <code>a</code>.
    @see util.Numerical#sum(int[])
    */
  public static int product (int[] a)
  {
    int p = 1;
    for (int i = 0; i < a.length; i ++)
      p *= a[i];
    return p;
  }

  /**
    Compute the product of elements in the given array of double numbers.
    @param a the array of <code>double</code> the product of elements of
    which is to be computed.
    @return the product of elements in <code>a</code>.
    @see util.Numerical#sum(double[])
    */
  public static double product (double[] a)
  {
    double p = 1.0;
    for (int i = 0; i < a.length; i ++)
      p *= a[i];
    return p;
  }

  /**
    Compute the product of elements in the <code>NumberArray</code> object.
    The <code>NumberArray.getDouble ()</code> function is used for
    multiplying elements.
    @param a the <code>NumberArray</code> object the product of elements
    of which is to be computed.
    @return a new <code>Number</code>-based wrapper object containing
    product of elements in <code>a</code>.
    @see util.Numerical#sum(util.NumberArray)
    */
  public Number product (NumberArray a)
  {
    double p = 1.0;
    for (int i = 0; i < a.length (); i ++)
      p *= a.getDouble (i);
    return new Double (p);
  }

  // some random number routines

  /**
    Generate a random integer in the specified range using the given
    random number generator object.
    @param rnd the random number generator to use.
    @param low the lower range boundary.
    @param high the higher range boundary.
    @return a random number in the range between <code>low</code>
    and <code>high</code> (inclusive).
    */
  public static int randomInRange (Random rnd, int low, int high)
  {
    long l = (long) rnd.nextInt () - (long) Integer.MIN_VALUE;
    return (int) (l % (long) (high - low + 1) + (long) low);
  }


  /**
    Generate a random double number in the specified range using the given
    random number generator object.
    @param rnd the random number generator to use.
    @param low the lower range boundary.
    @param high the higher range boundary.
    @return a random number in the range between <code>low</code>
    and <code>high</code> (inclusive?).
    */
  public static double randomInRange (Random rnd, double low, double high)
  {
    return rnd.nextDouble () * (high - low) + low;
  }

  /**
    Make a random choice from the given discrete distribution, using
    the given random number generator. Not a very efficient implementation
    (could be improved using binary instead of linear search).
    @param rnd the random number generator to used.
    @param distrib the distribution to choose from.
    @return a value between <code>0</code> and <code>distrib.length</code>
    choosen according to the distribution specified by <code>distrib</code>.
    */
  public static int randomChoice (Random rnd, double[] distrib)
  {
    if (distrib.length < 1)
      throw new IllegalArgumentException ();

    double p = randomInRange (rnd, 0.0, sum (distrib)); // a random point
    int choice = 0;
    for (double partSum = distrib[0]; // partial sum [0..choice]
	 choice < distrib.length - 1 && partSum < p;
	 partSum += distrib[++ choice])
      ;
    return choice;
  }
}
