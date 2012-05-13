// 	$Id: EnvironmentCanvas.java,v 1.4 1997/09/08 17:45:56 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl.gui;

import java.awt.Canvas;
import java.util.Observer;

/**
  An abstract base class for specific classes implementing
  environment visualization. By implementing the <code>Observer</code>
  interface it provides a convenient way to be notified about environment
  state changes that require repainting.
  */
public abstract class EnvironmentCanvas extends Canvas implements Observer
{
}
