// 	$Id: PropertiesPanel.java,v 1.8 1997/11/08 21:07:36 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package util;

import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
  A GUI panel for setting the properties of an object in editable textfields.
  It  creates a labeled editable textfield for each property. The object
  must implement the <code>Parameterized</code> interface.
  @author Pawel Cichosz
  @see util.Parameterized
  */
public class PropertiesPanel extends Panel
{
  /**
    Construct this panel for the given object. Performs GUI initialization:
    adding and laying out components.
    @param o the object for which this panel will be used.
    @exception IllegalArgumentException if <code>o</code> is
    <code>null</code>.
    */
  public PropertiesPanel (Parameterized o)
  {
    if (o == null)
      throw new IllegalArgumentException ();
    object = o;

    object.getProperties (properties);
    int numProperties = properties.size ();
    propNames = new Vector (numProperties);
    propTextFields = new Vector (numProperties);
    setLayout (new GridLayout (numProperties, 2, 5, 5));

    Enumeration names = properties.propertyNames ();
    int i = 0;
    while (names.hasMoreElements ())
    {
      String name = (String) names.nextElement ();
      propNames.addElement (name);
      add (new Label (name, Label.RIGHT));
      TextField textField = new TextField (properties.getProperty (name), 4);
      propTextFields.addElement (textField);
      textField.addActionListener (new PropertyListener ());
      add (textField);
    }
  }

  /**
    Get the insets of this panel.
    @see java.awt.Container#getInsets(java.awt.Insets)
    */
  public Insets getInsets ()
  {
    return insets;
  }

  /**
    Set the enabled status of the property textfields.
    @param e the enabled status to set (<code>true</code> -- enabled,
    <code>false</code> -- disabled.
    */
  public void setEnabled (boolean e)
  {
    for (int i = 0; i < propTextFields.size (); i ++)
      ((TextField) propTextFields.elementAt (i)).setEnabled (e);
  }

  /**
    Draw a rectangle border around this panel.
    @see java.awt.Container#paint(java.awt.Graphics)
    */
  public void paint (Graphics g)
  {
    Dimension size = getSize ();
    g.drawRect (0, 0, size.width - 1, size.height - 1);
  }

  /**
    The object controlled by this panel.
    */
  protected Parameterized object;

  /**
    The properties of the controlled object.
    */
  protected Properties properties = new Properties ();

  /**
    The event listener for property textfields actions (set the corresponding
    property to a new value).
    */
  private class PropertyListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      int i = propTextFields.indexOf (e.getSource ());
      if (i == -1)
	throw new RuntimeException ("Internal bug!");

      TextField textField = (TextField) propTextFields.elementAt (i);
      String name = (String) propNames.elementAt (i);
      
      properties.put (name, textField.getText ());
      object.setProperties (properties); // set
      object.getProperties (properties); // and get to verify
      textField.setText (properties.getProperty (name));
    }
  }

  // property textfields and names

  /**
    The vector of property textfields.
    */
  //private Vector propTextFields;
  protected Vector propTextFields;

  /**
    The vector of property names.
    */
  //private Vector propNames;
  protected Vector propNames;

  /**
    The insets of this panel.
    */
  private final Insets insets = new Insets (5, 5, 5, 5);
}
