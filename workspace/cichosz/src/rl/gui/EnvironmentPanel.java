// 	$Id: EnvironmentPanel.java,v 1.3 1997/09/08 17:46:45 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl.gui;

import rl.Environment;
import util.PropertiesPanel;

/**
  The GUI panel for setting the parameters of a reinforcement learning
  environment. Currently just extends <code>util.PropertiesPanel</code>.
  @author Pawel Cichosz
  @see rl.Environment
  @see util.PropertiesPanel
  */
public class EnvironmentPanel extends PropertiesPanel
{
  /**
    Construct this panel for the given environment.
    @param e the environment to be controlled by this panel.
    @see util.PropertiesPanel#PropertiesPanel(util.Parameterized)
    */
  public EnvironmentPanel (Environment e)
  {
    super (e);
  }
}
