// 	$Id: EnvironmentBeanInfo.java,v 1.2 1997/11/08 19:27:56 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import java.beans.SimpleBeanInfo;

/**
  A <code>BeanInfo</code> class for <code>Environment</code>. 
  Currently provides no explicit information, forcing the introspector to
  use reflection. Subject to change on further development, when class
  <code>Environment</code> becomes a real bean.
  @author Pawel Cichosz
  @see rl.Environment
  @see java.beans.SimpleBeanInfo
 */
public class EnvironmentBeanInfo extends SimpleBeanInfo
{
}
