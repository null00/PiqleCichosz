// 	$Id: LearnerPanel.java,v 1.5 1997/09/08 17:47:24 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl.gui;

import rl.Learner;
import util.PropertiesPanel;

/**
  The GUI panel for setting the parameters of a reinforcement learning system.
  In the future it may be extended to allow the user to select algorithms
  as well. Currently it just trivially extends
  <code>util.PropertiesPanel</code>.
  @author Pawel Cichosz
  @see rl.Learner
  @see util.PropertiesPanel
  */
public class LearnerPanel extends PropertiesPanel
{
  /**
    Construct this panel using the given learner.
    @param l the learner for which this panel will be used.
    @see util.PropertiesPanel#PropertiesPanel(util.Parameterized)
    */
  public LearnerPanel (Learner l)
  {
    super (l);
  }
}
