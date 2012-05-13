// 	$Id: Parameterized.java,v 1.4 1997/09/04 09:18:51 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package util;

import java.util.Properties;

/**
  The interface for classes the instances of which can be parameterizedby
  properties passed to or from using the <code>Properties</code> data
  structure.
  @author Pawel Cichosz
  @see util.PropertiesManager
  @see java.util.Properties
  */
public interface Parameterized
{
  /**
    Get the properties of this object.
    @param the destination for the properties to get.
    */
  void getProperties (Properties p);

  /**
    Set the properties of this learner.
    @param the source for the properties to set.
    */
  void setProperties (Properties p);
}
