// 	$Id: ArrayOfNumber.java,v 1.16 1997/08/19 15:15:49 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL.
// Copyleft 1997 Pawel Cichosz

package util;

import java.lang.reflect.Array;

/** The most straightforward implementation of the
  <code>NumberArray</code> interface, using an ordinary array of
  wrapper objects of subclasses of <code>Number</code>.
  @author Pawel Cichosz
  @see util.NumberArray
  @see java.lang.Number
  */
public class ArrayOfNumber implements NumberArray
{
  // constructors

  /**
    Construct this to be an array with the given length and with
    uninitialized contents.
    @param length the length of the array to be constructed
    (must be non-negative).
    */
  public ArrayOfNumber (int length)
  {
    array = new Number[length];
  }

  /**
    Construct this to be a copy of the given array of wrapper objects of
    subclasses of <code>Number</code>.
    @param na the array of <code>Number</code>-based wrapper objects
    of which this is to be made a copy.
    @see java.lang.Array
    */
  public ArrayOfNumber (Number[] na)
  {
    array = (Number[]) na.clone ();
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
    value. Implemented using <code>Number.byteValue ()</code>.
    @param i an array index.
    @return the <code>byte</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getByte(int)
    @see java.lang.Number#byteValue()
    */
  public byte getByte (int i)
  {
    return array[i].byteValue ();
  }

  /**
    Get the array element with the given index as a <code>short</code>
    value. Implemented using <code>Number.shortValue ()</code>.
    @param i an array index.
    @return the <code>short</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getShort(int)
    */
  public short getShort (int i)
  {
    return array[i].shortValue ();
  }

  /**
    Get the array element with the given index as an <code>int</code>
    value. Implemented using <code>Number.intValue ()</code>.
    @param i an array index.
    @return the <code>int</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getInt(int)
    */
  public int getInt (int i)
  {
    return array[i].intValue ();
  }

  /**
    Get the array element with the given index as a <code>long</code>
    value. Implemented using <code>Number.longValue ()</code>.
    @param i an array index.
    @return the <code>long</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getLong(int)
    */
  public long getLong (int i)
  {
    return array[i].longValue ();
  }

  /**
    Get the array element with the given index as a <code>float</code>
    value. Implemented using <code>Number.floatValue ()</code>.
    @param i an array index.
    @return the <code>float</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getFloat(int)
    */
  public float getFloat (int i)
  {
    return array[i].floatValue ();
  }

  /**
    Get the array element with the given index as a <code>double</code>
    value. Implemented using <code>Number.doubleValue ()</code>.
    @param i an array index.
    @return the <code>double</code> value of the element at index
    <code>i</code>.
    @see util.NumberArray#getDouble(int)
    */
  public double getDouble (int i)
  {
    return array[i].doubleValue ();
  }

  /**
    Get the array element with the given index as a wrapper object
    of a subclass of <code>Number</code>. The exact type of the return
    object depends on which <code>set ()</code> method has been used to
    set it.
    @param i an array index.
    @return the <code>Number</code>-based wrapper object stored in the array
    at index <code>i</code>.
    @exception NullPointerException if the element with the specified index
    is <code>null</code>.
    @see util.NumberArray#get(java.lang.Number)
    */
  public Number get (int i)
  {
    if (array[i] == null)
      throw new NullPointerException ();
    return array[i];
  }

  // mutators

  /**
    Create a new <code>Byte</code> object and assign it to the element
    with the given index.
    @param i an array index.
    @param v the <code>byte</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, byte)
    @see java.lang.Byte
    */
  public void set (int i, byte v)
  {
    array[i] = new Byte (v);
  }

  /**
    Create a new <code>Short</code> object and assign it to the element
    with the given index.
    @param i an array index.
    @param v the <code>short</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, short)
    @see java.lang.Short
    */
  public void set (int i, short v)
  {
    array[i] = new Short (v);
  }

  /**
    Create a new <code>Integer</code> object and assign it to the element
    with the given index.
    @param i an array index.
    @param v the <code>int</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, int)
    @see java.lang.Integher
    */
  public void set (int i, int v)
  {
    array[i] = new Integer (v);
  }

  /**
    Create a new <code>Long</code> object and assign it to the element
    with the given index.
    @param i an array index.
    @param v the <code>long</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, long)
    @see java.lang.Long
    */
  public void set (int i, long v)
  {
    array[i] = new Long (v);
  }

  /**
    Create a new <code>Float</code> object and assign it to the element
    with the given index.
    @param i an array index.
    @param v the <code>float</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, float)
    @see java.lang.Float
    */
  public void set (int i, float v)
  {
    array[i] = new Float (v);
  }

  /**
    Create a new <code>Double</code> object and assign it to the element
    with the given index.
    @param i an array index.
    @param v the <code>byte</code> value to be assigned to the element
    at index <code>i</code>.
    @see util.NumberArray#set(int, double)
    @see java.lang.DOuble
    */
  public void set (int i, double v)
  {
    array[i] = new Double (v);
  }

  /**
    Assign the given wrapper object of a subclass of <code>Number</code>
    to the element with the given index.
    @param i an array index.
    @param v the <code>Number</code>-base wrapper object the value
    of which is to be assigned to the element at index <code>i</code>.
    @see util.NumberArray#set(int, java.lang.Number)
    @see java.lang.Number
    */
  public void set (int i, Number v)
  {
    if (v == null)
      throw new NullPointerException ();
    array[i] = v;
  }

  /**
    Make this array to a copy of another <code>NumberArray</code>.
    Implemented using <code>get ()</code> and <code>set ()</code>
    methods with <code>Number</code> arguments.
    @param na the <code>NumberArray</code> object to be copied to this.
    @see util.NumberArray#set(util.NumberArray)
    @see util.ArrayOfNumber#get(int)
    @see util.ArrayOfNumber#set(int, java.lang.Number)
    */
  public void set (NumberArray na)
  {
    for (int i = 0; i < length (); i ++)
      set (i, na.get (i));
  }

  /**
    Make this array a copy of the given array of wrapper objects of
    subclasses of <code>Number</code>. Implemented using
    the <code>set ()</code> method with a <code>Number</code> argument.
    @param na the array of <code>Number</code>-based wrapper objects to
    be copied to this.
    @see util.NumberArray#set(java.lang.Number[])
    @see util.ArrayOfNumber#set(int, java.lang.Number)
    */
  public void set (Number[] na)
  {
    for (int i = 0; i < length (); i ++)
      set (i, na[i]);
  }

  /**
    Make this array a copy of the given ordinary array of primitive
    number types. Implemented using the <code>set ()</code> method with
    a <code>Number</code> argument.
    @param o the array object to be copied to this.
    @see util.NumberArray#set(java.lang.Object)
    @see util.ArrayOfNumber#set(int, java.lang.Number)
    */
  public void set (Object o)
  {
    for (int i = 0; i < length (); i ++)
      set (Array.get (o, i));
  }

  /**
    Clone this array.
    @return a clone of this array.
    @see util.NumberArray#clone()
    */
  public Object clone ()
  {
    ArrayOfNumber an;
    try {
      an = (ArrayOfNumber) super.clone ();
    }
    catch (CloneNotSupportedException e) {
      System.err.println ("This is not supposed to ever happen:" + e);
      return null;
    }
    an.array = (Number[]) array.clone ();
    return an;
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
      sb.append (array[i].toString ()).append ('\t');
    return sb.toString ();
  }

  /**
    Compare this array to another <code>NumberArray</code>.
    @param o the object of a class implementing
    the <code>NumberArray</code> interface to be compared to this.
    The comparison is implemented using the <code>equals ()</code>
    function of standard number wrappers.
    @return <code>true</code> if the values stored in the two arrays
    are equal and <code>false</code> otherwise.
    */
  public boolean equals (Object o)
  {
    NumberArray na = (NumberArray) o;
    boolean eq = true;
    for (int i = 0; i < length (); i ++)
      if (! array[i].equals (na.get (i)))
	eq = false;
    return eq;
  }

  /**
    The array of wrapper objects of subclasses of <code>Number</code>
    which stores the data. Subclasses of <code>ArrayOfNumbers</code>
    are likely to need direct access to this field, hence it is
    <code>protected</code>.
    @see java.lang.Number
    */
  protected Number[] array;
}
