// 	$Id: ArrayOfInt.java,v 1.10 1997/08/15 10:33:19 pawel Exp $
// This code may be used/distributed/modified under the terms of the GPL.
// Copyleft 1997 Pawel Cichosz

package util;

import java.lang.reflect.Array;

/**
  An efficient implementation of the <code>NumberArray</code> interface
  based on an ordinary array of primitive <code>int</code> values.
  @author Pawel Cichosz
  @see util.NumberArray
  */
public class ArrayOfInt implements NumberArray
{
  // constructors

  /**
    Construct this to be an array with the given length.
    @param length the length of the array to be constructed
    (must be non-negative).
    */
  public ArrayOfInt (int length)
  {
    array = new int[length];
  }

  /**
    Construct this to be a copy of the given array of <code>int</code>.
    @param a the array of <code>int</code> to which this is to be set.
    */
  public ArrayOfInt (int[] a)
  {
    array = (int[]) a.clone ();
  }

  /**
    Get the array length.
    @return the length of this array.
    @see util.NumberArray#length()
    */
  public int length ()
  {
    return array.length;
  }

  // accessors

  /**
    Get the array element with the given index as a <code>byte</code>
    value. Implemented using explicit cast from <code>int</code>
    to <code>byte</code>.
    @param i an array index.
    @return the <code>byte</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getByte(int)
    */
  public byte getByte (int i)
  {
    return (byte) array[i];
  }

  /**
    Get the array element with the given index as a <code>short</code>
    value. Implemented using explicit cast from <code>int</code>
    to <code>short</code>.
    @param i an array index.
    @return the <code>short</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getShort(int)
    */
  public short getShort (int i)
  {
    return (short) array[i];
  }

  /**
    Get the array element with the given index as an <code>int</code>
    value.
    @param i an array index.
    @return the <code>int</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getInt(int)
    */
  public int getInt (int i)
  {
    return array[i];
  }

  /**
    Get the array element with the given index as a <code>long</code>
    value. Implemented using explicit cast from <code>int</code>
    to <code>long</code>.
    @param i an array index.
    @return the <code>long</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getLong(int)
    */
  public long getLong (int i)
  {
    return (long) array[i];
  }

  /**
    Get the array element with the given index as a <code>float</code>
    value. Implemented using explicit cast from <code>int</code>
    to <code>float</code>.
    @param i an array index.
    @return the <code>float</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getFloat(int)
    */
  public float getFloat (int i)
  {
    return (float) array[i];
  }

  /**
    Get the array element with the given index as a <code>double</code>
    value. Implemented using explicit cast from <code>int</code>
    to <code>double</code>.
    @param i an array index.
    @return the <code>double</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getDouble(int)
    */
  public double getDouble (int i)
  {
    return (double) array[i];
  }

  /**
    Get the array element with the given index as a wrapper object
    of an appropriate (implementation-dependent) subclass of
    <code>Number</code>. Creates and returns a new <code>Integer</code>
    object with the value of the element with the given index.
    @param i an array index.
    @return a <code>Number</code>-based wrapper object with the
    value of the element at index <code>i</code>.
    @see util.NumberArray#get(int)
    @see java.lang.Integer
    */
  public Number get (int i)
  {
    return new Integer (array[i]);
  }

  // mutators

  /**
    Set the array element with the given index to the given
    <code>byte</code> value. Implemented using explicit cast from
    <code>byte</code> to <code>int</code>.
    @param i an array index.
    @param v the <code>byte</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, byte)
    */
  public void set (int i, byte v)
  {
    array[i] = (int) v;
  }

  /**
    Set the array element with the given index to the given
    <code>short</code> value. Implemented using explicit cast from
    <code>short</code> to <code>int</code>.
    @param i an array index.
    @param v the <code>short</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, short)
    */
  public void set (int i, short v)
  {
    array[i] = (int) v;
  }

  /**
    Set the array element with the given index to the given
    <code>int</code> value.
    @param i an array index.
    @param v the <code>int</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, int)
    */
  public void set (int i, int v)
  {
    array[i] = v;
  }

  /**
    Set the array element with the given index to the given
    <code>long</code> value. Implemented using explicit cast from
    <code>long</code> to <code>int</code>.
    @param i an array index.
    @param v the <code>long</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, long)
    */
  public void set (int i, long v)
  {
    array[i] = (int) v;
  }

  /**
    Set the array element with the given index to the given
    <code>float</code> value. Implemented using explicit cast from
    <code>float</code> to <code>int</code>.
    @param i an array index.
    @param v the <code>float</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, float)
    */
  public void set (int i, float v)
  {
    array[i] = (int) v;
  }

  /**
    Set the array element with the given index to the given
    <code>double</code> value. Implemented using explicit cast from
    <code>double</code> to <code>int</code>.
    @param i an array index.
    @param v the <code>byte</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, double)
    */
  public void set (int i, double v)
  {
    array[i] = (int) v;
  }

  /**
    Set the array element with the given index to the value
    represented by the given wrapper object of a subclass of
    <code>Number</code>. Implemented using <code>Number.intValue ()</code>.
    @param i an array index.
    @param v the <code>Number</code>-base wrapper object the value
    of which is to be assigned to the element at index <code>i</code>.
    @see util.NumberArray#set(int, java.lang.Number)
    */
  public void set (int i, Number v)
  {
    array[i] = v.intValue ();
  }

  /**
    Make this array to a copy of another <code>NumberArray</code>.
    Implemented using <code>NumberArray.getInt ()</code>.
    @param na the <code>NumberArray</code> object to be copied to this.
    @see util.NumberArray#set(util.NumberArray)
    @see util.NumberArray#getInt(int)
    */
  public void set (NumberArray na)
  {
    for (int i = 0; i < length (); i ++)
      set (i, na.getInt (i));
  }

  /**
    Make this array a copy of the given array of wrapper objects of
    subclasses of <code>Number</code>. Implemented using element-by-element
    <code>set (int, Number)</code>.
    @param na the array of <code>Number</code>-based wrapper objects to
    be copied to this.
    @see util.NumberArray#set(util.NumberArray)
    @see util.ArrayOfInt#set(int, java.lang.Number)
    */
  public void set (Number[] na)
  {
    for (int i = 0; i < length (); i ++)
      set (i, na[i]);
  }

  /**
    Make this array a copy of the given ordinary array of <code>int</code>.
    @param o the array object to be copied to this.
    @see util.NumberArray#set(java.lang.Object)
    */
  public void set (Object o)
  {
    for (int i = 0; i < length (); i ++)
      set (i, Array.getInt (o, i));
  }

  /**
    Clone this array.
    @return a clone of this array.
    @see util.NumberArray#clone()
    */
  public Object clone ()
  {
    ArrayOfInt ai;
    try {
      ai = (ArrayOfInt) super.clone ();
    }
    catch (CloneNotSupportedException e) {
      System.err.println ("This is not supposed to ever happen:" + e);
      return null;
    }
    ai.array = (int[]) array.clone ();
    return ai;
  }

  /**
    Return a string representation of this array: a sequence of numbers
    separated by the tab character.
    @return a <code>String</code> object representing this array.
    @see java.lang.Object#toString()
    */
  public String toString ()
  {
    StringBuffer sb = new StringBuffer ();
    for (int i = 0; i < length (); i ++)
      sb.append (String.valueOf (array[i])).append ('\t');
    return sb.toString ();
  }

  /**
    Compare this array to another <code>NumberArray</code>.
    @param o the object of a class implementing
    the <code>NumberArray</code> interface to be compared to this.
    The comparison is implemented using operator <code>==</code> and
    the <code>getInt ()<code> function of the </code>NumberArray</code>
    interface.
    @return <code>true</code> if the values stored in the two arrays
    are equal and <code>false</code> otherwise.
    */
  public boolean equals (Object o)
  {
    NumberArray na = (NumberArray) o;
    boolean eq = true;
    for (int i = 0; i < length (); i ++)
      if (array[i] != na.getInt (i))
	eq = false;
    return eq;
  }

  /**
    The array of <code>int</code> which stores the data. Subclasses
    of <code>ArrayOfInt</code> are likely to direct need access to
    this field, hence it is <code>protected</code>.
    */
  protected int[] array;
}
