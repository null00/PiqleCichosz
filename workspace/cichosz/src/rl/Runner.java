// 	$Id: Runner.java,v 1.27 1997/09/09 18:07:54 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Observable;

/**
  A  framework for running reinforcement learning simulations. It is
  <code>Runnable</code>, so that the simulation can be run in a separate
  thread easily. It also extends <code>Observable</code> and notifies
  the registered observed after each simulation time step and trial.
  @author Pawel Cichosz
  @see rl.Learner
  @see rl.Environment
  */
public class Runner extends Observable implements Runnable, Serializable
{
  // notification messages sent to observers

  /**
    Notification argument when notifying about completed step.
    */
  public static final String STEP = "Step";

  /**
    Notification argument when notifying about completed trial.
    */
  public static final String TRIAL = "Trial";

  /**
    Notification argument when notifying about reset.
    */
  public static final String RESET = "Reset";
  
  // constructors

  /**
    Construct this runner object using the given learner and environment,
    as well as simulation length specification and report writers.
    @param l the learner to simulate.
    @param e the environment to simulate.
    @param nTr the number of trials to run the simulation for.
    @param nSt the maximum number of steps in a trial.
    @param trWriter the print writer object to which trial-based
    reports are to be printed (no trial reporting if <code>null</code>).
    @param stWriter the print writer object to which step-based
    reports are to be printed (no step reporting if <code>null</code>).
    @exception IllegalArgumentException if <code>l</code> or <code>e</code>
    are <code>null</code> or if <code>nTr</code> or <code>nSt</code> are
    less than 0.
    */
  public Runner (Learner l, Environment e, int nTr, long nSt,
		 PrintWriter trWriter, PrintWriter stWriter)
  {
    if (l == null || e == null || nTr < 0 || nSt < 0)
      throw new IllegalArgumentException ();

    learner = l;
    environment = e;
    nTrials = nTr;
    nSteps = nSt;
    trialWriter = trWriter;
    stepWriter = stWriter;
  }

  /**
    Construct this runner object without any limits for the number of steps
    in a trial (it is actually limited by <code>Long.MAX_VALUE</code>) and
    no step-based reports.
    @param l the learner to simulate.
    @param e the environment to simulate.
    @param nTr the maximum number of trials ot run.
    @param trWriter the print writer object to which trial-based
    reports are to be printed (no trial reporting if <code>null</code>).
    @see rl.Runner#Runner(rl.Learner, rl.Environment, int, long)
    */
  public Runner (Learner l, Environment e, int nTr,
		 PrintWriter trWriter)
  {
    this (l, e, nTr, Long.MAX_VALUE, trWriter, null);
  }

  /**
    Construct this runner object without any limits for the number of trials
    to run (it is actually limited by <code>Integer.MAX_VALUE</code>)
    and with no reports.
    @param l the learner to simulate.
    @param e the environment to simulate.
    @see rl.Runner#Runner(rl.Learner, rl.Environment, int, long)
    */
  public Runner (Learner l, Environment e)
  {
    this (l, e, Integer.MAX_VALUE, Long.MAX_VALUE, null, null);
  }

  /**
    Get the learner used for the simulation.
    @return the learner used by this runner for the simulation.
    */
  public final Learner getLearner ()
  {
    return learner;
  }

  /**
    Get the environment used for the simulation.
    @return the environment used by this runner for simulation.
    */
  public final Environment getEnvironment ()
  {
    return environment;
  }

  /**
    Run the simulation.
    */
  public void run ()
  {
    for (int trial = 0; trial < nTrials; trial ++)
      runTrial (trial);
  }

  // auxiliary functions

  /**
    Run a trial. The registered observers are notified about trial completion.
    @param trial the number of the trial to run.
    */
  protected void runTrial (int trial)
  {
    boolean inTrial = true; // trial is continued
    for (long step = 0; step < nSteps && inTrial; step ++)
      inTrial = runStep (trial, step);
    setChanged ();
    notifyObservers (TRIAL); // notify about completed trial
  }

  /**
    Run a step. The registered observers are notified upon step completion.
    @param trial the number of the current trial.
    @param step the number of the step to run.
    @return <code>false</code> if the environment has reach a terminal
    state (the end of trial) and <code>true</code> otherwise.
    */
  protected synchronized boolean runStep (int trial, long step)
  {
    if (step == 0)
    {
      environment.reset ();
      learner.reset ();
      setChanged ();
      notifyObservers (RESET);
    }

    NumberArray x = environment.state ();
    int a = learner.react (x);
    double r = environment.execute (a); // x changes!
    learner.reinforce (r);
    boolean endOfTrial = environment.terminalState ();
    report (trial, step, x, a, r, endOfTrial || step == nSteps - 1);

    setChanged ();
    notifyObservers (STEP); // notify about completed step

    return ! endOfTrial;
  }

  /**
    Generate simulation reports for the current step. Step-based reports
    include the reward value for the current step. Trial-based reports
    include the average reinforcement and the number of steps in
    the recently completed trial.
    @param trial the current trial number.
    @param step the current step number.
    @param x the current environment state.
    @param a the learner's current action.
    @param r the current reward value.
    @param endOfTrial a flag indicating whether the end of the current
    trial has been just reached (<code>true</code>) or the current trial
    continues (<code>false</code>).
    @see rl.Runner#trialWriter
    @see rl.Runner#stepWriter
    @see rl.Runner#sumRewards
    */
  protected void report (int trial, long step, NumberArray x, int a, double r,
			 boolean endOfTrial)
  {
    if (trialWriter != null) // trial-based reports requested?
    {
      sumRewards += r; // update the bookkeeping data
      if (endOfTrial) 
      {
	trialWriter.println ((trial + 1) +
			     "\t" + (sumRewards / (step + 1)) +
			     "\t" + (step + 1));
	sumRewards = 0.0;
      }
    }

    if (stepWriter != null) // step-based reports requested?
      stepWriter.println (trial + "\t" + step + "\t" + r);
  }

  // data

  /**
    The learner.
    @see rl.Learner
    */
  protected Learner learner;

  /**
    The environment.
    @see rl.Environment
    */
  protected Environment environment;

  /**
    The number of trials to run (0 if no limits).
    */
  protected int nTrials;

  /**
    The maximum number of steps to run in a trial (0 if no limits).
    */
  protected long nSteps;

  /**
    The writer object for printing trial-based reports.
    @see rl.Runner#report()
    */
  protected PrintWriter trialWriter;

  /**
    The writer object stream for printing step-based reports.
    @see rl.Runner#report()
    */
  protected PrintWriter stepWriter;

  /**
    The sum of reward values in the current trial, maintained and
    used by <code>report ()</code>.
    @see rl.Runner#report()
    */
  private double sumRewards = 0.0;

  /**
    Testing.
    */
  public static void main (String[] args)
  {
    byte[][] m = new byte[10][10]; // a grid map
    for (int i = 0; i < m.length; i ++)
      for (int j = 0; j < m.length; j ++)
	m[i][j] = Grid.EMPTY;
    m[1][2] = m[2][2] =
      m[3][2] = m[3][3] = m[3][4] = m[3][5] = m[3][6] = m[3][7] =
      m[2][7] = m[1][7] =
      m[8][2] = m[7][2] =
      m[6][2] = m[6][3] = m[6][4] = m[6][5] = m[6][6] = m[6][7] =
      m[7][7] = m[8][7] = Grid.OBSTACLE;
    m[7][3] = Grid.GOAL;

    Grid grid = new Grid (m, 2, 6);
    LookUpTable[] qf = new LookUpTable[4];
    for (int i = 0; i < 4; i ++)
      qf[i] = new LookUpTable (2, 10, new Integer (0), new Integer (10), 1);
    Sarsa sarsa = new Sarsa (new TTD (0.5, 10, 0.95),
			     new BoltzmannSelector (0.1),
			     qf, 0.5);
    java.util.Properties p = new java.util.Properties ();
    sarsa.getProperties (p);
    p.save (System.err, "Learner properties");
    Runner runner = new Runner (sarsa, grid, Integer.MAX_VALUE,
				new PrintWriter (System.out, true));
    runner.run ();
  }
}
