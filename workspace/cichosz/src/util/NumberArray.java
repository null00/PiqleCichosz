// 	$Id: NumberArray.java,v 1.17 1997/08/14 17:03:08 pawel Exp $
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package util;

import java.io.Serializable;

/**
  An interface for an array of numbers. The basic idea is similar to
 that of standard wrapper classes subclassing the abstract class
 <code>Number</code>. However, <code>NumberArray</code> is an
 interface rather than an abstract class to enable different unrelated
 implementations.
 @author Pawel Cichosz
 @see java.lang.Number
 */
public interface NumberArray extends Cloneable, Serializable
{
  /**
    Get the array length.
    @return the length of this array.
   */
  int length (); // length of the array

  // accessors

  /**
    Get the array element with the given index as a <code>byte</code>
    value.
    @param i an array index.
    @return the <code>byte</code> value of the element at index
    <code>i</code>.
    */
  byte getByte (int i);

  /**
    Get the array element with the given index as a <code>short</code>
    value.
    @param i an array index.
    @return the <code>short</code> value of the element at index
    <code>i</code>.
    */
  short getShort (int i);

  /**
    Get the array element with the given index as an <code>int</code>
    value.
    @param i an array index.
    @return the <code>int</code> value of the element at index
    <code>i</code>.
    */
  int getInt (int i);

  /**
    Get the array element with the given index as a <code>long</code>
    value.
    @param i an array index.
    @return the <code>long</code> value of the element at index
    <code>i</code>.
    */
  long getLong (int i);

  /**
    Get the array element with the given index as a <code>float</code>
    value.
    @param i an array index.
    @return the <code>float</code> value of the element at index
    <code>i</code>.
    */
  float getFloat (int i);

  /**
    Get the array element with the given index as a <code>double</code>
    value.
    @param i an array index.
    @return the <code>double</code> value of the element at index
    <code>i</code>.
    */
  double getDouble (int i);

  /**
    Get the array element with the given index as a wrapper object
    of an appropriate (implementation-dependent) subclass of
    <code>Number</code>.
    @param i an array index.
    @return a <code>Number</code>-based wrapper object with the
    value of the element at index <code>i</code>.
    see @java.lang.Number
    */
  Number get (int i);

  // mutators

  /**
    Set the array element with the given index to the given
    <code>byte</code> value.
    @param i an array index.
    @param v the <code>byte</code> value to be assigned to the element
    at index <code>i</code>.
    */
  void set (int i, byte v);

  /**
    Set the array element with the given index to the given
    <code>short</code> value.
    @param i an array index.
    @param v the <code>short</code> value to be assigned to the element
    at index <code>i</code>.
    */
  void set (int i, short v);

  /**
    Set the array element with the given index to the given
    <code>int</code> value.
    @param i an array index.
    @param v the <code>int</code> value to be assigned to the element
    at index <code>i</code>.
    */
  void set (int i, int v);

  /**
    Set the array element with the given index to the given
    <code>long</code> value.
    @param i an array index.
    @param v the <code>long</code> value to be assigned to the element
    at index <code>i</code>.
    */
  void set (int i, long v);

  /**
    Set the array element with the given index to the given
    <code>float</code> value.
    @param i an array index.
    @param v the <code>float</code> value to be assigned to the element
    at index <code>i</code>.
    */
  void set (int i, float v);

  /**
    Set the array element with the given index to the given
    <code>double</code> value.
    @param i an array index.
    @param v the <code>byte</code> value to be assigned to the element
    at index <code>i</code>.
    */
  void set (int i, double v);

  /**
    Set the array element with the given index to the value
    represented by the given wrapper object of a subclass of
    <code>Number</code>.
    @param i an array index.
    @param v the <code>Number</code>-base wrapper object the value
    of which is to be assigned to the element at index <code>i</code>.
    @see java.lang.Number
    */
  void set (int i, Number v);

  // copying

  /**
    Make this array to a copy of another <code>NumberArray</code>.
    @param na the <code>NumberArray</code> object to be copied to this.
    */
  void set (NumberArray na);

  /**
    Make this array a copy of the given array of wrapper objects of
    subclasses of <code>Number</code>.
    @param na the array of <code>Number</code>-based wrapper objects to
    be copied to this.
    @see java.lang.Number
    */
  void set (Number[] na);

  /**
    Make this array a copy of the given ordinary array of primitive
    number types.
    @param o the array object to be copied to this.
    */
  void set (Object o);
  
  // cloning
  /**
    Clone this array.
    @return a clone of this array.
    */
  Object clone ();
}
