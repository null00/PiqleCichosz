// 	$Id: RunnerPanel.java,v 1.22 1997/11/08 21:06:00 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl.gui;

import rl.Runner;
import java.util.Observer;
import java.util.Observable;
import java.awt.*;
import java.awt.event.*;

/**
  A GUI control panel for running reinforcement learning simulations.
  Features include setting the number of steps and trials to run (not
  implemented yet) and starting/stopping or suspending/resuming
  the simulation.
  @author Pawel Cichosz
  @see rl.Runner
  */
public class RunnerPanel extends Panel implements Observer
{
  /**
    Construct this panel. Adds control widgets (buttons and text fields)
    and registers event listeners for them.
    @param r the <code>Runner</code> object to use for running the simulation.
    @exception IllegalArgumentException if <code>r</code> is
    <code>null</code>.
    */
  public RunnerPanel (Runner r)
  {
    if (r == null)
      throw new IllegalArgumentException ();
    runner = r;
    runner.addObserver (this); // to sleep when notified

    buttonStart.addActionListener (new StartListener ());
    buttonStop.addActionListener (new StopListener ());
    buttonSuspend.addActionListener (new SuspendListener ());
    buttonResume.addActionListener (new ResumeListener ());
    scrollbarDelay.addAdjustmentListener (new DelayListener ());

    setLayout (new GridLayout (3, 4, 5, 10));

    add (buttonStart);
    add (buttonStop);
    add (labelTrial);
    add (textFieldTrial);

    add (buttonSuspend);
    add (buttonResume);
    add (labelStep);
    add (textFieldStep);

    add (labelDelay);
    add (scrollbarDelay);

    textFieldStep.setEditable (false);
    textFieldTrial.setEditable (false);
    buttonStart.setEnabled (true);
    buttonStop.setEnabled (false);
    buttonSuspend.setEnabled (false);
    buttonResume.setEnabled (false);
  }

  /**
    Start the simulation. Creates and starts a new runner thread.
    */
  public synchronized void start ()
  {
    if (runnerThread == null)
    {
      stopIt = false;
      step = 0;
      trial = 0;
      runner.getLearner ().clearKnowledge ();
      runnerThread = new Thread (runner);
      runnerThread.setPriority (Thread.MAX_PRIORITY); // any sense?
      runnerThread.start ();
    }
  }

  /**
    Stop the simulation (at the nearest appropriate moment).
    Sets the <code>stopIt</code> flag.
    */
  public synchronized void stop ()
  {
    if (runnerThread != null)
      stopIt = true;
  }

  /**
    Suspend the simulation (at the nearest appropriate moment).
    Sets the <code>suspendIt</code> flag.
    */
  public synchronized void suspend ()
  {
    if (runnerThread != null)
      suspendIt = true;
  }

  /**
    Resume the simulation, if it is currently suspended.
    */
  public synchronized void resume ()
  {
    if (runnerThread != null && suspendIt)
    {
      suspendIt = false;
      runnerThread.resume ();
    }
  }

  /**
    Get notified by the runner about step or trial completion. Normally just
    sleeps for a while, but may also suspend or stop the runner thread
    if it was requested.
    @see java.util.Observer#update(java.util.Observable, java.lang.Object)
    */
  public void update (Observable o, Object arg)
  {
    if (o != runner)
      throw new IllegalStateException ();

    if (arg == Runner.STEP)
      step ++;
    else if (arg == Runner.TRIAL)
      trial ++;
    else if (arg == Runner.RESET)
      step = 0;

    /* DISABLED: too memory consuming to do at each step
      updateStatus ();
      */

    if (stopIt)
    {
      updateStatus ();
      Thread auxThread = runnerThread;
      runnerThread = null; // should this be synchronized?
      auxThread.stop ();
    }
    else if (suspendIt)
    {
      updateStatus ();
      runnerThread.suspend ();
    }
    else
    {
      try {
	runnerThread.sleep (delay);
      }
      catch (InterruptedException e) {}
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
    Draw a rectangle border around this panel.
    @see java.awt.Container#paint(java.awt.Graphics)
    */
  public void paint (Graphics g)
  {
    Dimension size = getSize ();
    g.drawRect (0, 0, size.width - 1, size.height - 1);
  }

  // simulation-related data -- protected (subclasses may need it)

  /**
    The <code>Runner</code> object used for running the simulation.
    */
  protected Runner runner;

  /**
    The thread used for running the simulation.
    */
  protected Thread runnerThread = null;

  // event listener inner classes

  /**
    The event listener for "Start" button actions (start a simulation
    runner thread). The learner's knowledge and the numbers of steps
    and trials completed so far are cleared.
    @see rl.gui.RunnerPanel#start()
    */
  private class StartListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      textFieldTrial.setEnabled (false);
      textFieldStep.setEnabled (false);
      buttonStart.setEnabled (false);
      buttonStop.setEnabled (true);
      buttonSuspend.setEnabled (true);
      start ();
    }
  }

  /**
    The event listener for "Stop" button actions (stop the current
    simulation thread).
    @see rl.gui.RunnerPanel#stop()
    */
  private class StopListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      textFieldTrial.setEnabled (true);
      textFieldStep.setEnabled (true);
      buttonStart.setEnabled (true);
      buttonStop.setEnabled (false);
      buttonSuspend.setEnabled (false);
      stop ();
    }
  }

  /**
    The event listener for "Suspend" button actions (suspend the current
    simulation thread).
    @see rl.gui.RunnerPanel#suspend()
    */
  private class SuspendListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      textFieldTrial.setEnabled (true);
      textFieldStep.setEnabled (true);
      buttonStop.setEnabled (false);
      buttonSuspend.setEnabled (false);
      buttonResume.setEnabled (true);
      suspend ();
    }
  }

  /**
    The event listener for "Resume" button actions (resume the current
    simulation thread).
    @see rl.gui.RunnerPanel#resume()
    */
  private class ResumeListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      textFieldTrial.setEnabled (false);
      textFieldStep.setEnabled (false);
      buttonStop.setEnabled (true);
      buttonSuspend.setEnabled (true);
      buttonResume.setEnabled (false);
      resume ();
    }
  }

  /**
    The event listener for delay scrollbar actions (set the intra-step
    delay).
    */
  private class DelayListener implements AdjustmentListener
  {
    public void adjustmentValueChanged (AdjustmentEvent e)
    {
      delay = DELAY_SCALE * scrollbarDelay.getValue ();
    }
  }

  /**
    Update simulation status text fields.
    */
  private void updateStatus ()
  {
    textFieldTrial.setText (String.valueOf (trial));
    textFieldStep.setText (String.valueOf (step));
  }

  /**
    The value by which delay scrollbar values are multiplied to get
    the actual delay in milliseconds.
    */
  //private static final int DELAY_SCALE = 10;
  protected static final int DELAY_SCALE = 10;

  /**
    The delay between successive steps (in milliseconds). The runner thread
    sleeps for that long after each step.
    */
  //private int delay = 0;
  protected int delay = 0;

  // step & trial counters

  /**
    The current trial number (the number of trials completed so far).
    */
  private int trial = 0;

  /**
    The current step number (the number of steps completed so far).
    */
  private long step = 0;

  // stop & suspend flags

  /**
    The stop flag, set when the "Stop" button is clicked.
    */
  private boolean stopIt = false;
  
  /**
    The suspend flag, set when the "Suspend" button is clicked.
    */
  private boolean suspendIt = false;

  // control widgets

  /**
    The text field for reporting the current trial number.
    */
  //private final TextField textFieldTrial = new TextField ("0", 8);
  protected final TextField textFieldTrial = new TextField ("0", 8);

  /**
    The text field for reporting the current step number.
    */
  //private final TextField textFieldStep = new TextField ("0", 8);
  protected final TextField textFieldStep = new TextField ("0", 8);

  /**
    The "Trial" label.
    */
  private final Label labelTrial = new Label ("Trial:", Label.RIGHT);

  /**
    The "Step" label.
    */
  private final Label labelStep = new Label ("Step:", Label.RIGHT);

  /**
    The "Start" button.
    */
  //private final Button buttonStart = new Button ("Start");
  protected final Button buttonStart = new Button ("Start");

  /**
    The "Stop" button.
    */
  //private final Button buttonStop = new Button ("Stop");
  protected final Button buttonStop = new Button ("Stop");

  /**
    The "Suspend" button.
    */
  //private final Button buttonSuspend = new Button ("Suspend");
  protected final Button buttonSuspend = new Button ("Suspend");

  /**
    The "Resume" button.
    */
  //private final Button buttonResume = new Button ("Resume");
  protected final Button buttonResume = new Button ("Resume");

  /**
    The "Delay" label.
    */
  private final Label labelDelay = new Label ("Delay", Label.RIGHT);

  /**
    The srcollbar for adjusting the intra-step delay.
    */
  //private final Scrollbar scrollbarDelay =
  protected final Scrollbar scrollbarDelay =
  new Scrollbar (Scrollbar.HORIZONTAL, delay / DELAY_SCALE, 10, 0, 50);

  /**
    The insets of this panel.
    */
  private final Insets insets = new Insets (5, 5, 5, 5);
}
