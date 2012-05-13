// 	$Id: SimulationPanel.java,v 1.16 1997/09/11 13:07:15 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl.gui;

import rl.Learner;
import rl.Environment;
import rl.Runner;
import java.lang.reflect.Constructor;
import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.Insets;

/**
  The main GUI control panel for running and visualizing reinforcement
  learning simulations. Contains <code>LearnerPanel</code>,
  <code>EnvironmentPanel</code>, <code>EnvironmentCanvas</code>,
  and <code>RunnerPanel</code>. If the subclass of <code>Environment</code>
  used in the simulation is of the form "package.foo.bar.Dummy", then
  there must be a subclass of <code>EnvironmentCanvas</code> called
  "package.foo.bar.gui.DummyCanvas".
  @author Pawel Cichosz
  @see rl.gui.LearnerPanel
  @see rl.gui.EnvironmentPanel
  @see rl.gui.EnvironmentCanvas
  @see rl.gui.RunnerPanel
  */
public class SimulationPanel extends Panel
{
  /**
    Construct this Simulation Panel using the given runner.
    Performs also GUI initialization: adding and laying out components.
    @param r the runner object to use for simulation.
    @exception IllegalArgumentException if <code>r</code> is
    <code>null</code>.
    */
  public SimulationPanel (Runner r)
  {
    if (r == null)
      throw new IllegalArgumentException ();
    runner = r;

    runPanel = new RunnerPanel (runner);
    learnPanel = new LearnerPanel (runner.getLearner ());
    envPanel = new EnvironmentPanel (runner.getEnvironment ());

    // use RTTI reflection to create the proper environment canvas
    Class envClass = runner.getEnvironment ().getClass ();
    String envName = envClass.getName ();
    StringBuffer auxName = new StringBuffer (envName);
    auxName.insert (envName.lastIndexOf ('.') + 1, "gui.");
    String envCanvasName = auxName + "Canvas";

    Class envCanvasClass = null;
    try {
      envCanvasClass = Class.forName (envCanvasName);
    }
    catch (ClassNotFoundException e) {
      System.err.println ("Class " + envCanvasName + " not found");
      System.exit (3);
    }

    Class[] constrTypes = {envClass};
    Constructor envCanvasConstr = null;
    try {
      envCanvasConstr = envCanvasClass.getConstructor (constrTypes);
    }
    catch (NoSuchMethodException e) {
      System.err.println ("Constructor for " + envCanvasClass +
			  "parameterized by " + envClass + " not found");
      System.exit (3);
    }

    Object[] constrArgs = {runner.getEnvironment ()};
    try {
      envCanvas = (EnvironmentCanvas) envCanvasConstr.newInstance (constrArgs);
    }
    catch (Exception e) {
      System.err.println ("Instantiation of " + envCanvasConstr + " failed");
      System.exit (3);
    }

    /* NOW Environment is observable!
    runner.addObserver (envCanvas); // to allow envCanvas to repaint itself
    */

    // add components
    setLayout (new BorderLayout (5, 5));
    add (BorderLayout.EAST, learnPanel);
    add (BorderLayout.WEST, envPanel);
    add (BorderLayout.CENTER, envCanvas);
    add (BorderLayout.SOUTH, runPanel);
  }

  /**
    Start the simulation.
    @see rl.gui.RunnerPanel#start()
    */
  public void start ()
  {
    runPanel.start ();
  }

  /**
    Stop the simulation.
    @see rl.gui.RunnerPanel#stop()
    */
  public void stop ()
  {
    runPanel.stop ();
  }

  /**
    Suspend the simulation.
    @see rl.gui.RunnerPanel#suspend()
    */
  public void suspend ()
  {
    runPanel.suspend ();
  }

  /**
    Resume the simulation.
    @see rl.gui.RunnerPanel#resume()
    */
  public void resume ()
  {
    runPanel.resume ();
  }

  /**
    Get the insets of this panel.
    @see java.awt.Container#getInsets(java.awt.Insets)
    */
  public Insets getInsets ()
  {
    return insets;
  }

  // protected simulation-related data

  /**
    The <code>Runner</code> object used for the simulation.
    */
  protected Runner runner;

  // private GUI-related data

  /**
    The runner panel (used for simulation run control).
    */
  private RunnerPanel runPanel;

  /**
    The learner panel (used for learning parameters control).
    @see rl.gui.LearnerPanel
    */
  private LearnerPanel learnPanel;

  /**
    The environment panel (used for environment parameters control).
    @see rl.gui.EnvironmentPanel
    */
  private EnvironmentPanel envPanel;

  /**
    The environment canvas (used for simulation visualization).
    @see rl.gui.EnvironmentCanvas
    */
  private EnvironmentCanvas envCanvas;

  /**
    The insets of this panel.
    */
  private final Insets insets = new Insets (5, 5, 5, 5);

  /**
    Testing.
    */
  public static void main (String[] args)
  {
    byte[][] m = new byte[10][10]; // a grid map
    for (int i = 0; i < m.length; i ++)
      for (int j = 0; j < m.length; j ++)
	m[i][j] = rl.Grid.EMPTY;
    m[1][2] = m[2][2] =
      m[3][2] = m[3][3] = m[3][4] = m[3][5] = m[3][6] = m[3][7] =
      m[2][7] = m[1][7] =
      m[8][2] = m[7][2] =
      m[6][2] = m[6][3] = m[6][4] = m[6][5] = m[6][6] = m[6][7] =
      m[7][7] = m[8][7] = rl.Grid.OBSTACLE;
    m[7][3] = rl.Grid.GOAL;

    rl.Grid grid = new rl.Grid (m, -1, -1);
    rl.LookUpTable[] qf = new rl.LookUpTable[4];
    for (int i = 0; i < 4; i ++)
      qf[i] = new rl.LookUpTable (2, 10, new Integer (0), new Integer (10), 1);
    rl.Sarsa sarsa = new rl.Sarsa (new rl.TTD (0.5, 10, 0.95),
				   new rl.BoltzmannSelector (0.1),
				   qf, 0.5);
    Runner runner = new Runner (sarsa, grid);

    java.awt.Frame window = new java.awt.Frame ("RL in Grid World");
    window.add (new SimulationPanel (runner));
    window.addWindowListener
      (new java.awt.event.WindowAdapter ()
       {
	 public void windowClosed (java.awt.event.WindowEvent e)
	   {
	     System.exit (0);
	   }
       });
    window.pack ();
    window.show ();
  }
}
