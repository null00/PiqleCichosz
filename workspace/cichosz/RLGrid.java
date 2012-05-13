// 	$Id: RLGrid.java,v 1.6 1997/11/08 21:30:35 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

import rl.*;
import rl.gui.SimulationPanel;
import java.applet.Applet;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
  A demo reinforcement learning applet. Uses Sarsa with TTD in application
  to grid path finding and visualizes the learning process. The actual
  GUI used is implemented by <code>rl.gui.SimulationPanel</code>.
  The applet only implements the <code>init ()</code> method to instantiate
  the classes for the learning algorithm and its environment, and the
  abovementioned panel. It can be also used as an application, as it
  defines a <code>main ()</code> method.
  @author Pawel Cichosz
  @see rl.gui.SimulationPanel
  */
public class RLGrid extends Applet
{
  /**
    Initialize the applet by creating instances of the appropriate
    RL classes and an instance of <code>SimulationPanel</code> to show
    everything.
    */
  public void init ()
  {
    byte[][] m = new byte[20][20]; // a grid map
    for (int i = 0; i < m.length; i ++)
      for (int j = 0; j < m[i].length; j ++)
	m[i][j] = rl.Grid.EMPTY;

    m[1][1] =
      m[4][1] = m[3][1] = m[2][1] =
      m[1][4] = m[1][3] = m[1][2] =
      m[4][3] = m[4][4] = m[3][4] =
      m[6][0] = m[6][1] = m[6][2] = m[6][3] = m[6][4] =
      m[0][6] = m[1][6] = m[2][6] = m[3][6] = m[4][6] =
      m[9][1] = m[8][1] = m[8][2] = m[8][3] = m[8][4] =
      m[1][9] = m[1][8] = m[2][8] = m[3][8] = m[4][8] =
      m[8][6] = m[9][6] =
      m[6][8] = m[6][9] = Grid.OBSTACLE;
    for (int i = 0; i < m.length; i ++)
      for (int j = 0; j < m[i].length; j ++)
      {
	int ii = i, jj = j;
	if (i >= m.length / 2)
	  ii = m.length - i - 1;
	if (j >= m[i].length / 2)
	  jj = m[i].length - j - 1;
	m[i][j] = m[ii][jj];
      }
    m[9][8] = m[8][8] = m[8][10] = m[8][11] =
      m[10][11] = m[11][11] = m[11][9] = m[11][8] = Grid.OBSTACLE;
    m[16][2] = m[16][3] = m[17][3] = m[17][2] = rl.Grid.GOAL;

    Grid grid = new Grid (m, -1, -1);
    LookUpTable[] qf = new LookUpTable[4];
    for (int i = 0; i < 4; i ++)
      qf[i] = new LookUpTable (2, 50, new Integer (0), new Integer (50), 1);
    rl.Sarsa sarsa = new rl.Sarsa (new TTD (0.5, 10, 0.95),
				   new BoltzmannSelector (0.1),
				   qf, 0.5);
    Runner runner = new Runner (sarsa, grid);
    add (simPanel = new SimulationPanel (runner));
    validate ();
  }

  /**
    Resume the simulation (if has been suspended).
    @see java.applet.Applet#start()
    */
  public void start ()
  {
    simPanel.resume ();
  }

  /**
    Suspend the simulation.
    @see java.applet.Applet#stop()
    */
  public void stop ()
  {
    simPanel.suspend ();
  }

  /**
    The simulation panel displayed by this applet.
    @see rl.gui.SimulationPanel
    */
  private SimulationPanel simPanel;

  /**
    Run as an application. Just puts the applet in a frame. In the future
    more functionality may be added here.
    @param args ignored.
    */
  public static void main (String[] args)
  {
    Frame window = new Frame ("RL in Grid World");
    RLGrid applet = new RLGrid ();
    applet.init ();
    window.add (applet);
    window.addWindowListener (new WindowAdapter ()
			      {
				public void windowClosed (WindowEvent e)
				  {
				    System.exit (0);
				  }
			      });
    window.pack ();
    window.show ();
    applet.start (); // currently not needed
  }
}
