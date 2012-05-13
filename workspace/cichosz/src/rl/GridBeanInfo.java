// 	$Id: GridBeanInfo.java,v 1.2 1997/11/08 19:29:06 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import java.beans.SimpleBeanInfo;

/**
  A <code>BeanInfo</code> class for <code>Grid</code>. 
  Currently provides no explicit information, forcing the introspector to
  use reflection. Subject to change on further development, when class
  <code>Grid</code> becomes a real bean.
  @author Pawel Cichosz
  @see rl.Grid
  @see java.beans.SimpleBeanInfo
 */
public class GridBeanInfo extends SimpleBeanInfo
{
}
