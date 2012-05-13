// 	$Id: PropertiesManager.java,v 1.5 1997/09/04 12:15:22 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package util;

import java.util.Properties;
import java.util.Enumeration;
import java.lang.reflect.Method;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

/**
  A collection for methods for managing (getting/setting) object properties
  (as in beans) with name-value pairs represented by
  the <code>Properties</code> class. For a property named "foo" there
  should be "getFoo ()" and "setFoo ()" methods available in the classs
  objects of which are to be processed this way. Each property should
  have a single value of a primitive type or <code>String</code> (this
  limitation may be somehow relaxed in the future), represented by a string
  in a <code>Properties</code> object.
  @author Pawel Cichosz
  @see java.util.Properties;
  */
public final class PropertiesManager
{
  /**
    Get the values of the specified properties of the specified object.
    For each property "foo" its value is set to the value retrieved by
    the object's <code>getFoo ()</code> method converted to a string, or
    left unchanged if the object has no such a method. Properties that
    are not of a primitive or <code>String</code> type are ignored.
    @param the properties to get.
    @param o the object of which to get the properties.
    */
  public static void getProperties (Properties p, Object o)
  {
    Enumeration names = p.propertyNames ();
    while (names.hasMoreElements ())
    { // for each property
      String propName = (String) names.nextElement ();
      PropertyDescriptor propDescr = null;
      try {
	propDescr = new PropertyDescriptor (propName, o.getClass ());
      }
      catch (IntrospectionException e) {}
      if (propDescr == null)
	continue; // probably no such property

      getProperty (p, o, propDescr);
    }
  }

  /**
    Get the values of all the properties of the specified object.
    For each property "foo" its value is set to the value retrieved by
    the object's <code>getFoo ()</code> method converted to a string, or
    left unchanged if the object has no such a method. Properties that
    are not of a primitive or <code>String</code> type are ignored.
    @param the destination for the properties to get.
    @param o the object of which to get the properties.
    */
  public static void getAllProperties (Properties p, Object o)
  {
    PropertyDescriptor[] propDescrArr = {};
    try {
      propDescrArr = Introspector.
	getBeanInfo (o.getClass ()).getPropertyDescriptors ();
    }
    catch (IntrospectionException e) {}

    for (int i = 0; i < propDescrArr.length; i ++) // for each property
      getProperty (p, o, propDescrArr[i]);
  }

  /**
    Get the values of all the properties of the specified object,
    including to subclasses only to the specified level.
    For each property "foo" its value is set to the value retrieved by
    the object's <code>getFoo ()</code> method converted to a string, or
    left unchanged if the object has no such a method. Properties that
    are not of a primitive or <code>String</code> type are ignored.
    @param the destination for the properties to get.
    @param o the object of which to get the properties.
    @param stopClass the <code>Class</code> object specifying the level
    to which subclass properties are to be included, if <code>null</code>
    defaults to the superclass of the class of <code>o</code>.
    */
  public static void getAllProperties (Properties p, Object o, Class stopClass)
  {
    if (stopClass == null)
      stopClass = o.getClass ().getSuperclass ();

    PropertyDescriptor[] propDescrArr = {};
    try {
      propDescrArr = Introspector.
	getBeanInfo (o.getClass (), stopClass).getPropertyDescriptors ();
    }
    catch (IntrospectionException e) {}

    for (int i = 0; i < propDescrArr.length; i ++) // for each property
      getProperty (p, o, propDescrArr[i]);
  }

  /**
    Set the values of the specified properties of the specified object.
    For each property "foo" its value, converted to a proper
    <em>primitive</em> type (unless "foo" is of type <code>String</code>),
    is used to set by the object's <code>setFoo ()</code> method, or ignored
    if the object has no such a method. Properties that are not of
    a primitive or <code>String</code> type are ignored.
    @param the properties to set.
    @param o the object of which to set the properties.
    */
  public static void setProperties (Properties p, Object o)
  {
    Enumeration names = p.propertyNames ();
    while (names.hasMoreElements ())
    { // for each property
      String propName = (String) names.nextElement ();
      PropertyDescriptor propDescr = null;
      try {
	propDescr = new PropertyDescriptor (propName, o.getClass ());
      }
      catch (IntrospectionException e) {}
      if (propDescr == null)
	continue; // probably no such property

      setProperty (p, o, propDescr);
    }
  }

  /**
    Get the value of the specified property of the specified object.
    Nothing is done if the property type is not a primitive type or
    <code>String</code>.
    @param the destination for the property to get.
    @param o the object of which to get the property.
    @param propDesc the descriptor of the property to get.
   */
  private static void getProperty (Properties p, Object o,
				   PropertyDescriptor propDescr)
  {
    Class propType = propDescr.getPropertyType ();
    if (propType == null ||
	! (propType.isPrimitive () || propType.equals (String.class)))
      return;

    Method getter = propDescr.getReadMethod (); 
    Object[] args = {};
    try {
      p.put (propDescr.getName (), getter.invoke (o, args).toString ());
    }
    catch (Exception e) {
      System.err.println ("Invocation of " + getter + " failed");
    }
  }

  /**
    Set the value of the specified property of the specified object.
    Nothing is done if the property type is not a primitive type or
    <code>String</code>. Conversion from string errors are ignored
    (no value set).
    @param the source for the property to get.
    @param o the object of which to set the property.
    @param propDesc the descriptor of the property to set.
   */
  private static void setProperty (Properties p, Object o,
				   PropertyDescriptor propDescr)
  {
    String propValue = p.getProperty (propDescr.getName ()).trim ();
    if (propValue == null || propValue.length () == 0)
     return; // no property value

    Class propType = propDescr.getPropertyType ();
    if (propType == null ||
	! (propType.isPrimitive () || propType.equals (String.class)))
      return;

    Method setter = propDescr.getWriteMethod ();
    Object[] args = {null};

    // convert the string property value to a proper primitive type
    try {
      if (propType.equals (boolean.class))
	args[0] = Boolean.valueOf (propValue);
      else if (propType.equals (char.class))
	args[0] = new Character (propValue.charAt (0));
      else if (propType.equals (byte.class))
	args[0] = Byte.valueOf (propValue);
      else if (propType.equals (short.class))
	args[0] = Short.valueOf (propValue);
      else if (propType.equals (int.class))
	args[0] = Integer.valueOf (propValue);
      else if (propType.equals (long.class))
	args[0] = Long.valueOf (propValue);
      else if (propType.equals (float.class))
	args[0] = Float.valueOf (propValue);
      else if (propType.equals (double.class))
	args[0] = Double.valueOf (propValue);
      else if (propType.equals (String.class))
	args[0] = propValue;
    }
    catch (NumberFormatException e) {
      return;
    }
    try {
      setter.invoke (o, args);
    }
    catch (Exception e) {
      System.err.println ("Invocation of " + setter + " failed");
    }
  }

  /**
    Testing.
    */
  public static void main (String[] args)
  {
    rl.TTD ttd = new rl.TTD (0.5, 10, 0.9);
    Properties p = new Properties ();
    PropertiesManager.getAllProperties (p, ttd);
    System.out.println ("List:");
    p.list (System.out);

    Properties p1 = new Properties ();
    try {
      p1.load (System.in);
    }
    catch (java.io.IOException e) {
      System.err.println ("Caught " + e);
    }
    PropertiesManager.setProperties (p1, ttd);
    PropertiesManager.getProperties (p, ttd);
    System.out.println ("Save:");
    p.save (System.out, "TTD Properties");
  }
}
