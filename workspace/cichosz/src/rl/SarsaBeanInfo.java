// 	$Id: SarsaBeanInfo.java,v 1.2 1997/11/08 19:21:13 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import java.beans.SimpleBeanInfo;

/**
  A <code>BeanInfo</code> class for <code>Sarsa</code>. 
  Currently provides no explicit information, forcing the introspector to
  use reflection. Subject to change on further development, when class
  <code>Sarsa</code> becomes a real bean.
  @author Pawel Cichosz
  @see rl.Sarsa
  @see java.beans.SimpleBeanInfo
 */
public class SarsaBeanInfo extends SimpleBeanInfo
{
}
