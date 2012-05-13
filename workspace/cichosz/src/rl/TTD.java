// 	$Id: TTD.java,v 1.21 1997/09/05 11:33:16 pawel Exp $
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;

/**
  Credit assignement for reinforcement learning (an implementation of
  the <code>CreditAssigner</code> interface) using the TTD
  (Truncated Temporal Differences) algorithm of Cichosz.
  @author Pawel Cichosz
  @see rl.CreditAssigner
  @see rl.Learner
 */
public class TTD implements CreditAssigner
{
  // constructors

  /**
    Construct a TTD object with default parameter settings:
    lambda=0.5, m=10, gamma=0.95.
    */
  public TTD ()
  {
    this (0.5, 10, 0.95); // default settings
  }

  /**
    Construct a TTD object with the specified parameter settings.
    @param lambda the recency factor.
    @param m the experience buffer length.
    @param gamma the discount factor.
    @exception IllegalArgumentException if the parameters are not in
    their allowed ranges: lambda and gamma in [0..1], m in [1..oo).
    */
  public TTD (double lambda, int m, double gamma)
  {
    if (lambda < 0.0 || lambda > 1.0 ||
	m < 1 ||
	gamma < 0.0 || gamma > 1.0)
      throw new IllegalArgumentException ();

    this.lambda = lambda;
    this.m = m;
    this.gamma = gamma;
    ebuff = new ExperienceBuffer (m);
  }

  /**
    Perform some necessary bookkeeping for the given state-action pair
    and run the TTD algorithm.
    @exception IllegalStateException if called for the 2nd time
    consecutively.
    @see rl.CreditAssigner#stateAndAction(NumberArray, int,
    rl.UtilityFunction)
    */
  public synchronized void stateAndAction (NumberArray x, int a,
					   UtilityFunction utilFun)
  {
    if (lastCall == STATE_AND_ACTION)
      throw new IllegalStateException ();
    lastCall = STATE_AND_ACTION;
    ebuff.insert (x, a, utilFun.utility1 (x, a));
    ttd (1, utilFun);
    ebuff.tick ();
  }

  /**
    Perform some necessary TTD bookkeeping for the  given reinforcement
    value.
    @exception IllegalStateException if not called directly after
    the last call to <code>stateAndAction ()</code>.
    @see rl.CreditAssigner#stateAndAction(util.NumberArray, int,
    rl.UtilityFunction)
    */
  public synchronized void reward (double r, UtilityFunction utilFun)
       throws IllegalStateException
  {
    if (lastCall != STATE_AND_ACTION)
      throw new IllegalStateException ();
    lastCall = REWARD;
    ebuff.insert (r);
  }

  /**
    Perform the TTD reset operation.
    @exception IllegalStateException if not called directly after
    the last call to <code>reward ()</code>.
    @see rl.CreditAssigner#reset(rl.UtilityFunction)
    */
  public synchronized void reset (UtilityFunction utilFun)
       throws IllegalStateException
  {
    if (lastCall == STATE_AND_ACTION)
      throw new IllegalStateException ();
    lastCall = RESET;
    ebuff.insert ();
    for (int t0 = 1; t0 <= m; t0 ++)
    {
      ttd (t0, utilFun);
      ebuff.tick ();
    }
    ebuff.reset ();
  }

  // accessors and mutators for TTD parameters

  /**
    Get the recency factor lambda.
    @return the lambda value.
    */
  public final synchronized double getLambda ()
  {
    return lambda;
  }

  /**
    Get the experience buffer length m.
    @return m the m value.
    */
  public final synchronized int getM ()
  {
    return m;
  }

  /**
    Get the discount factor gamma.
    @return the gamma value.
    */
  public final synchronized double getGamma ()
  {
    return gamma;
  }

  /**
    Set the recency factor lambda to the given value.
    @param l the new lambda value to set.
    @exception IllegalArgumentException if <code>l</code> is not in [0..1].
    */
  public final synchronized void setLambda (double l)
  {
    if (l < 0 || l > 1)
      throw new IllegalArgumentException ();
    lambda = l;
  }

  /**
    Set the experience buffer length m to the given value. 
    Copies as many most recent elements from the old buffer to the new
    buffer them. Any experiences that do not fit are lost.
    @param m the new m value to set.
    @exception IllegalArgumentException if <code>m</code> is not in [1..oo).
    */
  public final synchronized void setM (int m)
  {
    if (m < 1)
      throw new IllegalArgumentException ();
    ExperienceBuffer newEbuff = new ExperienceBuffer (m, ebuff);
    this.m = m;
    ebuff = newEbuff;  // all done
  }

  /**
    Set the discount factor gamma to the given value.
    @param g the new gamma value to set.
    @exception IllegalArgumentException if <code>g</code> is not in [0..1].
    */
  public final synchronized void setGamma (double g)
  {
    if (g < 0 || g > 1)
      throw new IllegalArgumentException ();
    gamma = g;
  }

  // other public methods

  /**
    Get a textual representation of this object.
    @return <code>String</code> of the form "TTD(lambda,m,gamma)", with
    the parameter settings of this.
    */
  public String toString ()
  {
    return "TTD (" + lambda + ", " + m + ", " + gamma + ")";
  }

  /**
    The experience buffer.
    */
  protected ExperienceBuffer ebuff;

  /**
    The TTD algorithm. Protected to allow subclasses to extend this
    implementation.
    @param t0 the first step (i.e., experience buffer index) taken into
    account by TTD (must be in [1..m]).
    @param utilFun the utility function callback object, used to compute
    state-action utilities and to perform update operations.
    @see rl.TTD#ttd_return(int, rl.UtilityFunction)
    @see rl.UtilityFunction
    */
  protected void ttd (int t0, UtilityFunction utilFun)
  {
    double z = ttd_return (t0, utilFun);
    if (ebuff.nsteps () >= m)
      utilFun.update (ebuff.x (m), ebuff.a (m),
		      z - utilFun.utility0 (ebuff.x (m), ebuff.a (m)));
  }

  /**
    The iterative version of the TTD return computation algorithm.
    @param t0 the first step (i.e., experience buffer index) used for
    the computation (must be in [1..m]).
    @param utilFun the utility function callback object, used to compute
    state-action utilities.
    @return the TTD return computed for experiences [t0..m].
    @see rl.TTD#ttd(int, rl.UtilityFunction)
    @see rl.UtilityFunction
    */
  protected double ttd_return (int t0, UtilityFunction utilFun)
  {
    if (ebuff.nsteps () < m)
      return 0.0;
    double z = ebuff.u1 (t0);
    for (int t = t0; t <= m; t ++)
      z = ebuff.r (t) + gamma * (lambda * z + (1.0 - lambda) * ebuff.u1 (t));
    return z;
  }

  // TTD parameters

  /**
    The recency factor parameter.
    */
  private double lambda;

  /**
    The experience buffer length (truncation period) parameter.
    */
  private int m;

  /**
    The discount factor parameter.
    */
  private double gamma;

  // auxiliary private stuff

  /**
    Constants: possible values of <code>lastCall</code>.
    */
  private final short NONE = -1, STATE_AND_ACTION = 0, REWARD = 1, RESET = 2;
  
  /**
    Indicates which method of the <code>CreditAssigner</code> interface has
    been most recently called for this object.
    */
  private short lastCall = NONE;
}

/**
  The TTD experience buffer. There is no use subclassing it, since TTD
  constructs and maintains an object of this particular class (hence
  declared final).
  @author Pawel Cichosz
  @see rl.TTD
  */
final class ExperienceBuffer
{
  /**
    Construct an experience buffer of given length.
    @param m the length of the buffer to construct.
    */
  ExperienceBuffer (int m)
  {
    buff = new Experience[m + 1];
    for (int t = 0; t < buff.length; t ++)
      buff[t] = new Experience ();
  }

  /**
    Construct an experience buffer of given length and copy into it as many
    "most recent" experiences of the given other buffer as possible.
    Sets <code>ns</code> to the number of experiences copied minus 1
    (which is a safe simplification).
    @param m the length of the buffer to construct.
    @param ebuff the experience buffer from which to copy experiences.
    */
  ExperienceBuffer (int m, ExperienceBuffer ebuff)
  {
    buff = new Experience[m + 1];
    int t;
    for (t = 0; t < buff.length && t < ebuff.buff.length &&
	   t < ebuff.nsteps (); t ++)  // copy as much as possible
      buff[t] = (Experience) ebuff.experienceAt (t).clone ();
    ns = t - 1; // a simplification (sometimes should be ns = t)
    for ( ; t < buff.length; t ++) // create new experiences in free space
      buff[t] = new Experience ();
  }

  // "public" (actually, package) interface

  /**
    Report the number of steps made since the beginning or the last reset.
    @return the number of steps made so far.
    */
  long nsteps ()
  {
    return ns;
  }

  /**
    Insert a partial experience to the buffer.
    @param x the state to insert at position 0 (for the current experience).
    @param a the action to insert at position 0 (for the current experience).
    @param u1 the successor state utility to insert at position 1
    (for the previous experience).
    @see rl.ExperienceBuffer#insert(double)
    @see rl.ExperienceBuffer#insert()
    */
  void insert (NumberArray x, int a, double u1)
  {
    experienceAt (1).u1 = u1; // for the previous experience
    
    if (experienceAt (0).x == null)
      experienceAt (0).x = (NumberArray) x.clone ();
    else
      experienceAt (0).x.set (x);

    experienceAt (0).a = a;
  }

  /**
    Insert a reward value for the previous (already partially inserted)
    experience.
    @param r the reward value to insert at position 1 (for the previous
    experience).
    @see rl.ExperienceBuffer#insert(util.NumberArray, int, double)
    @see rl.ExperienceBuffer#insert()
    */
  void insert (double r)
  {
    experienceAt (1).r = r; // for the previous experience
  }

  /**
    Insert an empty fictious experience with the successor state utility
    of 0 (used for the reset operation).
    @see rl.ExpwerienceBuffer#insert(util.NumberArray, int, double)
    @see rl.ExperienceBuffer#insert(double)
    */
  void insert ()
  {
    experienceAt (1).u1 = 0.0;
  }

  /**
    Reset this buffer.
   */
  void reset ()
  {
    ns = 0;
  }

  /**
    Make a single time tick, i.e., shift the buffer's indices cyclically.
    */
  void tick ()
  {
    curp = (curp - 1 + buff.length) % buff.length;
    ns ++;
  }

  /**
    Get the state field of the specified buffer record.
    @param t the buffer index for which to get the state.
    @return the state field of the buffer record with index <code>t</code>.
    @see rl.ExperienceBuffer#a(int)
    @see rl.ExperienceBuffer#r(int)
    @see rl.ExperienceBuffer#u1(int)
    */
  NumberArray x (int t)
  {
    return experienceAt (t).x;
  }

  /**
    Get the action field of the specified buffer record.
    @param t the buffer index for which to get the action.
    @return the action field of the buffer record with index <code>t</code>.
    @see rl.ExperienceBuffer#x(int)
    @see rl.ExperienceBuffer#r(int)
    @see rl.ExperienceBuffer#u1(int)
    */
  int a (int t)
  {
    return experienceAt (t).a;
  }

  /**
    Get the reward field of the specified buffer record.
    @param t the buffer index for which to get the reward.
    @return the reward field of the buffer record with index <code>t</code>.
    @see rl.ExperienceBuffer#x(int)
    @see rl.ExperienceBuffer#a(int)
    @see rl.ExperienceBuffer#u1(int)
    */
  double r (int t)
  {
    return experienceAt (t).r;
  }

  /**
    Get the successor utility field of the specified buffer record.
    @param t the buffer index for which to get the successor utility.
    @return the successor utility field of the buffer record
    with index <code>t</code>.
    @see rl.ExperienceBuffer#x(int)
    @see rl.ExperienceBuffer#a(int)
    @see rl.ExperienceBuffer#r(int)
    */
  double u1 (int t)
  {
    return experienceAt (t).u1;
  }

  // private stuff

  /**
    The single experience record.
    */
  private final class Experience implements Cloneable
  {
    NumberArray x; // state
    int a;         // action
    double r;      // reward
    double u1;     // successor utility

    /**
      Clone this experience.
      */
    protected Object clone ()
    {
      Experience ex;
      try {
	ex = (Experience) super.clone ();
      }
      catch (CloneNotSupportedException e) {
	System.err.println ("This is not supposed to ever happen: " + e);
	return null;
      }
      if (x != null)
	ex.x = (NumberArray) x.clone ();
      return ex;
    }
  }

  /**
    The actual buffer, array of experience objects.
    */
  private Experience[] buff; // the actual buffer

  /**
    The number of steps made so far (since the beginning or the last reset).
    */
  private long ns = 0;

  /**
    The current buffer position to which index 0 is translated when
    calling experience buffer accessor methods <code>x ()</code>,
    <code>a ()</code>, <code>r ()</code>, <code>u1 ()</code>.
    Used internally to implement virtual cyclical indexing.
    @see rl.ExperienceBuffer#experienceAt(int)
    */
  private int curp = 0;

  /**
    The virtual indexing mechanism.
    @param t the buffer index for the record to access.
    @return the experience record stored at the buffer position obtained
    after translating <code>t</code> to the true internal index.
    @see rl.ExperienceBuffer#curp
    */
  private Experience experienceAt (int t)
  {
    return buff[(t + curp) % buff.length];
  }
}
