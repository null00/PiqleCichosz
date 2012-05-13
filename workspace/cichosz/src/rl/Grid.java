// 	$Id: Grid.java,v 1.27 1997/09/11 11:16:19 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;
import util.ArrayOfInt;
import util.Numerical;
import util.PropertiesManager;
import java.util.Random;
import java.util.Properties;

/**
  A two-dimensional grid environment for simple path-finding reinforcement
  learning tasks.
  @author Pawel Cichosz
  @see rl.Environment
  */
public class Grid extends Environment
{
  // constants used in grid map definitions

  /**
    An empty grid cell.
    */
  public static final byte EMPTY = 0;

  /**
    A grid cell containing an obstacle.
    */
  public static final byte OBSTACLE = 1;

    /**
      A goal grid cell.
    */
  public static final byte GOAL = 2;

  // allowed actions

  /**
    Action: left.
    */
  public static final int LEFT = 0;

  /**
    Action: right.
    */
  public static final int RIGHT = 1;

  /**
    Action: top.
    */
  public static final int TOP = 2;

  /**
    Action: bottom.
    */
  public static final int BOTTOM = 3;

  // constructors

  /**
    Construct a grid environment with the specified map and start location.
    @param m the grid map.
    @param x the x coordinate of the starting location (using a random
    starting location if illegal or occupied).
    @param y the y coordinate of the starting location (using random
    starting location if illegal or occupied).
    @exception IllegalArgumentException if <code>m</code> is not at least
    a 2x2 matrix.
    */
  public Grid (byte[][] m, int x, int y)
  {
    if (m == null || m.length <=1 || m[0].length <= 1)
      throw new IllegalArgumentException ();
    currentState = new ArrayOfInt (2);
    map = m;
    setStartX (x);
    setStartY (y);
    reset ();
  }

  /**
    Construct a grid environment with the specified map, start location, and
    scaling factor.
    @param m the grid map.
    @param x the x coordinate of the starting location (using a random
    starting location if illegal or occupied).
    @param y the y coordinate of the starting location (using random
    starting location if illegal or occupied).
    @param s the positive integer factor by which <code>m</code>,
    <code>x</code>, and <code>y</code> are to be scaled up.
    */
  public Grid (byte[][] m, int x, int y, int s)
  {
    this (m, x, y);
    rescale (s, s);
  }

  /**
    Construct a grid environment with the specified map and
    scaling factor, using random starting locations.
    @param m the grid map.
    @param s the positive integer factor by which <code>m</code>
    is to be scaled up.
    */
  public Grid (byte[][] m, int s)
  {
    this (m, -1, -1, s);
  }

  /**
    Construct a grid environment with the specified map, random starting
    locations and no scaling.
    @param m the grid map, containing values from the set of
    <code>EMPTY</code>, <code>OBSTACLE</code>, <code>GOAL</code>.
    */
  public Grid (byte[][] m)
  {
    this (m, -1, -1);
  }

  /**
    Indicate whether a goal state has been reached.
    @see rl.Environment#terminalState()
    */
  public synchronized boolean terminalState ()
  {
    return map[currentState.getInt (0)][currentState.getInt (1)] == GOAL;
  }

  // some additional public accessors

  /**
    Get the horizontal size of the grid.
    @return the number of grid cells in the x direction.
    */
  public final synchronized int getXSize ()
  {
    return xSize ();
  }

  /**
    Get the vertical size of the grid.
    @return the number of grid cells in the y direction.
    */
  public final synchronized int getYSize ()
  {
    return ySize ();
  }

  /**
    Set the horizontal size of the grid by rescaling.
    @param xs the new number of cells in the x direction.
    @exception IllegalArgumentException if <code>xs</code> is not above 1.
    */
  public final synchronized void setXSize (int xs)
  {
    if (xs <= 1)
      throw new IllegalArgumentException ();
    rescale ((float) xs / (float) xSize (), 1);
  }

  /**
    Set the vertical size of the grid by rescaling.
    @param ys the new number of cells in the y direction.
    @exception IllegalArgumentException if <code>ys</code> is less than 1.
    */
  public final synchronized void setYSize (int ys)
  {
    if (ys <= 1)
      throw new IllegalArgumentException ();
    rescale (1, (float) ys / (float) ySize ());
  }

  /**
    Get the x coordinate of the starting location.
    @return the x coordinate of the starting location.
    */
  public final synchronized int getStartX ()
  {
    return startX;
  }

  /**
    Get the y coordinate of the starting location.
    @return the y coordinate of the starting location.
    */
  public final synchronized int getStartY ()
  {
    return startY;
  }

  /**
    Set the x coordinate of the starting location. If illegal, starting at
    random.
    @param x the new x coordinate of the starting location.
    */
  public final synchronized void setStartX (int x)
  {
    startX = inRangeX (x) ? x : -1;
  }

  /**
    Set the y coordinate of the starting location. If illegal, starting at
    random.
    @param y the new y coordinate of the starting location.
    */
  public final synchronized void setStartY (int y)
  {
    startY = inRangeY (y) ? y : -1;
  }

  /**
    Get the contents of the specified grid cell.
    @param x the x coordinate of the cell of interest.
    @param y the y coordinate of the cell of interest.
    @return the contents of the cell at (<code>x</code>, <code>y</code>)
    as one of <code>EMPTY</code>, <code>OBSTACLE</code>, <code>GOAL</code>.
    @exception IllegalArgumentException  if <code>x</code> or <code>y</code>
    are out of the grid size.
    */
  public final synchronized byte cellAt (int x, int y)
  {
    if (! inRangeX (x) || ! inRangeY (y))
      throw new IllegalArgumentException ();
    return map[x][y];
  }

  /**
    Set the contents of the specified grid cell to <code>OBSTACLE</code>.
    @param x the x coordinate of the cell of interest.
    @param y the y coordinate of the cell of interest.
    @exception IllegalArgumentException  if <code>x</code> or <code>y</code>
    are out of the grid size.
    */
  public final synchronized void setObstacle (int x, int y)
  {
    if (! inRangeX (x) || ! inRangeY (y))
      throw new IllegalArgumentException ();
    map[x][y] = OBSTACLE;
  }

  /**
    Set the contents of the specified grid cell to <code>EMPTY</code>.
    @param x the x coordinate of the cell of interest.
    @param y the y coordinate of the cell of interest.
    @exception IllegalArgumentException  if <code>x</code> or <code>y</code>
    are out of the grid size.
    */
  public final synchronized void setEmpty (int x, int y)
  {
    if (! inRangeX (x) || ! inRangeY (y))
      throw new IllegalArgumentException ();
    map[x][y] = EMPTY;
  }

  /**
    Set the contents of the specified grid cell to <code>GOAL</code>.
    @param x the x coordinate of the cell of interest.
    @param y the y coordinate of the cell of interest.
    @exception IllegalArgumentException  if <code>x</code> or <code>y</code>
    are out of the grid size.
    */
  public final synchronized void setGoal (int x, int y)
  {
    if (! inRangeX (x) || ! inRangeY (y))
      throw new IllegalArgumentException ();
    map[x][y] = GOAL;
  }

  // overriden abstract auxiliary methods of Environment

  /**
    Make a move in the choosen direction, if possible.
    @param a the move direction, one of <code>LEFT</code>,
    <code>RIGHT</code>, <code>TOP</code>, and <code>BOTTOM</code>.
    @see rl.Environment#stateTransition(int)
    */
  protected void stateTransition (int a)
  {
    int newX = currentState.getInt (0), newY = currentState.getInt (1);
    switch (a)
    {
    case LEFT:
      newX --;
      break;

    case RIGHT:
      newX ++;
      break;

    case TOP:
      newY --;
      break;

    case BOTTOM:
      newY ++;
      break;

    default:
      throw new IllegalArgumentException ();
    }

    if (canMoveTo (newX, newY))
      moveTo (newX, newY);
  }

  /**
    Reset the agent to its fixed or random initial location.
    @see rl.Environment#stateInit()
    */
  protected void stateInit ()
  {
    int newX = startX, newY = startY;
    while (! canMoveTo (newX, newY))
    {
      newX = Numerical.randomInRange (rnd, 0, xSize () - 1);
      newY = Numerical.randomInRange (rnd, 0, ySize () - 1);
    }
    moveTo (newX, newY);
  }

  /**
    Generate a reward value for the current state.
    @return 0 if the current cell is a goal cell and -1 otherwise.
    */
  protected double reward ()
  {
    return map[currentState.getInt (0)][currentState.getInt (1)] == GOAL ?
      0 : -1;
  }

    /**
    The horizontal size of the grid. This is a protected and unsynchronized
    version of <code>getXSize ()</code>, to be called only from synchronized
    methods.
    @return the number of grid cells in the x direction.
    */
  protected final int xSize ()
  {
    return map.length;
  }

  /**
    The vertical size of the grid. This is a protected and unsynchronized
    version of <code>getYSize ()</code>, to be called only from synchronized
    methods.
    @return the number of grid cells in the y direction.
    */
  protected final int ySize ()
  {
    return xSize () > 0 ? map[0].length : 0;
  }

  /**
    Check whether the given x coordinate is within the grid area.
    @param x the x coordinate to check.
    @return <code>true</code> if <code>x</code> is between 0 and
    the maximum x coordinate and <code>false</code> otherwise.
    */
  protected boolean inRangeX (int x)
  {
    return x >= 0 && x < xSize ();
  }

  /**
    Check whether the given y coordinate is within the grid area.
    @param y the y coordinate to check.
    @return <code>true</code> if <code>y</code> is between 0 and
    the maximum y coordinate and <code>false</code> otherwise.
    */
  protected boolean inRangeY (int y)
  {
    return y >= 0 && y < ySize ();
  }

  /**
    Check whether a move to the given position is legal (withing the grid,
    an empty cell).
    @param newX the x coordinate of the desired new location.
    @param newY the y coordinate of the desired new location.
    @return <code>true</code> if the move is OK and <code>false</code>
    otherwise.
    */
  protected boolean canMoveTo (int newX, int newY)
  {
    return inRangeX (newX) && inRangeY (newY) && map[newX][newY] != OBSTACLE;
  }

  /**
    Make a move to the specified location, assuming it is legal.
    @param newX the x coordinate of the new location.
    @param newY the y coordinate of the new location.
    */
  protected void moveTo (int newX, int newY)
  {
    currentState.set (0, newX);
    currentState.set (1, newY);
  }

  /**
   Rescale the grid map by the given factors. Modifies the starting location
   coordinates accordingly and initializes the current state for
   the new grid.
   @param sX the scale factor for the x dimension.
   @param sY the scale factor for the y dimension.
   @exception IllegalArgumentException if <code>sX</code> or <code>sY</code>
   are not positive.
   */
  protected void rescale (float sX, float sY)
  {
    if (sX <= 0.0 || sY <= 0.0)
      throw new IllegalArgumentException ();

    int newXSize = (int) (sX * xSize ()); // new x size
    int newYSize = (int) (sY * ySize ()); // new y size
    newXSize = newXSize > 1 ? newXSize : 1;
    newYSize = newYSize > 1 ? newYSize : 1;
    byte[][] newMap = new byte[newXSize][newYSize];
    
    for (int si = 0; si < newXSize; si ++)
      for (int sj = 0; sj < newYSize; sj ++)
	newMap[si][sj] = map[(int) (si / sX)][(int) (sj / sY)];

    map = newMap;
    startX = inRangeX (startX) ?
      (int) (sX * startX + sX * (startX + 1) - 1) / 2 : -1;
    startY = inRangeY (startY) ?
      (int) (sY * startY + sY * (startY + 1) - 1) / 2 : -1;
    stateInit ();
  }

  // data

  /**
    The grid map, i.e., a 2-dimensional array of cell contents.
    */
  protected byte[][] map;

  /**
    The random number generator used for selecting random starting locations.
    */
  private static final Random rnd = new Random ();

  /**
    The x coordinate of the starting location (using random starting
    locations if illegal.
    */
  private int startX;

  /**
    The y coordinate of the starting location (using random starting
    locations if illegal.
    */
  private int startY;


  // some testing
  public static void main (String[] args)
  {
    byte[][] m = {{Grid.GOAL, Grid.EMPTY, Grid.EMPTY, Grid.EMPTY,
		   Grid.EMPTY},
		  {Grid.EMPTY, Grid.OBSTACLE, Grid.OBSTACLE, Grid.EMPTY,
		   Grid.EMPTY},
		  {Grid.EMPTY, Grid.EMPTY, Grid.EMPTY, Grid.OBSTACLE,
		   Grid.EMPTY},
		  {Grid.EMPTY, Grid.EMPTY, Grid.EMPTY, Grid.EMPTY,
		   Grid.EMPTY}};
    Grid grid = new Grid (m, 2, 2);

    int[] a = {Grid.LEFT, Grid.RIGHT, Grid.TOP, Grid.TOP,
	       Grid.LEFT, Grid.LEFT, Grid.LEFT};

    System.out.println (grid.state ());
    for (int i = 0; i < a.length && ! grid.terminalState (); i ++)
    {
      System.out.println (grid.execute (a[i]));
      System.out.println (grid.state ());
    }
    grid.reset ();
    System.out.println (grid.state ());
  }
}
