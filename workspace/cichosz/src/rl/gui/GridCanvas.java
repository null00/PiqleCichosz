// 	$Id: GridCanvas.java,v 1.24 1997/09/11 12:51:52 pawel Exp $	
// This code may be used/distributed/modified under the terms of the GPL
// Copyleft 1997 Pawel Cichosz

package rl.gui;

import rl.Environment;
import rl.Grid;
import util.NumberArray;
import java.util.Observable;
import java.awt.*;
import java.awt.event.*;

/**
  A canvas for drawing a graphical representation of a grid environment.
  Handles mouse events by toggling the contents of the clicked cell.
  When resized, attempts to preserve the aspect ration implied by the
  grid size.
  @author Pawel Cichosz
  @see rl.Grid
  */
public class GridCanvas extends EnvironmentCanvas
{
  /**
    Construct a new grid canvas for drawing the given grid environment.
    @param g the grid environment to draw.
    @exception IllegalArgumentException if <code>g</code>
    is <code>null</code>.
    */
  public GridCanvas (Grid g)
  {
    if (g == null)
      throw new IllegalArgumentException ();

    grid = g;
    state = (NumberArray) grid.state ().clone ();
    grid.addObserver (this); // to find out when to repaint

    addMouseListener (new GridMouseListener ()); // mouse click handler
    addComponentListener (new GridComponentListener ()); // resize handler
  }

  /**
    Get the minimum reasonable size for this grid canvas.
    @return a new <code>Dimension</code> object representing the minimum
    reasonable size for this grid canvas (5 pixels per cell).
    */
  public Dimension getMinimumSize ()
  {
    return new Dimension (5 * grid.getXSize () + 2,
			  5 * grid.getYSize () + 2); // 2, not 1
  }

  /**
    Get the preferred size for this grid canvas.
    @return a new <code>Dimension</code> object the preferred
    size for this grid canvas (10 pixels per cell).
    */
  public Dimension getPreferredSize ()
  {
    return new Dimension (10 * grid.getXSize () + 2,
			  10 * grid.getYSize () + 2); // 2, not 1
  }

  /**
    Update the canvas by drawing a graphical representation of the
    grid environment specified on construction. The displayed state
    is as set by the most recent call to
    <code>update (Observable, Object)</code>
    or during construction if no such a call has been made.
    <code>Component.update ()</code> is overriden to
    avoid erasing the background before painting.
    @param g the graphics context to use for painting.
    */
  public synchronized void update (Graphics g)
  {
    final int xSize = grid.getXSize (), ySize = grid.getYSize ();
    final int cw =  cellWidth (); // graphical cell width
    final int ch = cellHeight (); // graphical cell height
    final int gw = cw * xSize; // graphical grid width
    final int gh = ch * ySize; // graphical grid height

    Rectangle bounds = g.getClipBounds (); // what grid region to draw
    int x0 = Math.max (graphToLogX (bounds.x), 0); // redundant max ()?
    int y0 = Math.max (graphToLogY (bounds.y), 0); // redundant max ()?
    int x1 = Math.min (graphToLogX (bounds.x + bounds.width - 1),
		       xSize - 1);
    int  y1 = Math.min (graphToLogY (bounds.y + bounds.height - 1),
			ySize - 1);

    // x, y -- logical cell coordinates, gX, gY -- graphical cell coordinates
    for (int x = x0, gX = logToGraphX (x); x <= x1; x ++, gX += cw)
    {
      for (int y = y0, gY = logToGraphY (y); y <= y1; y ++, gY += ch)
      {
	g.setColor (cellColor (x, y));
	g.fillRect (gX + 1, gY + 1, cw - 1, ch - 1); // leave 2 pix for lines
	if (y > 0)
	{
	  g.setColor (Color.black);
	  g.drawLine (gX, gY, gX + cw - 1, gY);
	}
      }
      if (x > 0)
      {
	g.setColor (Color.black);
	g.drawLine (gX, 0, gX, gh - 1);
      }
    }
    g.setColor (Color.black);
    g.drawRect (0, 0, gw, gh);
    upToDate = true;
    notifyAll (); // notify all that wait until we repaint
  }

  /**
    Paint the canvas by drawing a graphical representation of the
    grid environment specified on construction. Just calls
    <code>update</code>.
    @param g the graphics context to use for painting.
    @see rl.gui.GridCanvas#update(java.lang.awt.Graphics)
    */
  public void paint (Graphics g)
  {
    update (g);
  }

  /**
    Update the environment drawing to reflect a state transition or
    properties change, if notified by the grid environment for which
    this canvas has been constructed.
    @see java.util.Observer#update(java.util.Observable, java.lang.Object)
    */
  public synchronized void update (Observable o, Object arg)
  {
    if (o != grid)
      return;

    if (arg.equals (Environment.STATE))
    {
      NumberArray newState = grid.state ();
      int oldX = logToGraphX (state.getInt (0));
      int oldY = logToGraphY (state.getInt (1));
      int newX = logToGraphX (newState.getInt (0));
      int newY = logToGraphY (newState.getInt (1));
      state.set (newState);
      upToDate = false;
      repaint (oldX + 1, oldY + 1, cellWidth () - 1, cellHeight () - 1);
      repaint (newX + 1, newY + 1, cellWidth () - 1, cellHeight () - 1);
      if (! upToDate)
      {
	try {
	  wait (); // wait for the canvas to get repainted
	}
	catch (InterruptedException e) {}
      }
    }
    else if (arg.equals (Environment.PROPERTIES))
    {
      getParent ().validate ();
      adjustSize (); // to be sure
      repaint ();
    }
  }

  // some auxiliary methods

  /**
    Select cell color for grid drawing. Uses <code>Color.red</code>
    for the current cell, <code>Color.white</code> for
    <code>Grid.EMPTY</code> cells, <code>Color.green</code> for
    <code>Grid.GOAL</code> cells, and <code>Color.white/code> otherwise.
    Subclasses may override this method to define their own coloring scheme
    (e.g., to draw subclasses of <code>Grid</code>).
    @param x the x coordinate of the cell to select color for.
    @param y the y coordinate of the cell to select color for.
    @return the color selected for drawing the cell
    at (<code>x</code>,<code>y</code>).
    @see rl.Grid#cellAt(int, int)
    */
  protected Color cellColor (int x, int y)
  {
    if (x == state.getInt (0) && y == state.getInt (1))
      return Color.red;
    byte c = grid.cellAt (x, y);
    return c == Grid.EMPTY ? Color.white :
      c == Grid.GOAL ? Color.green : Color.black;
  }

  /**
    Translate the given logical x cell coordinate to its graphical x
    coordinate.
    @param x the logical x cell coordinates to translate.
    @return the graphical x cell coordinates corresponding to <code>x</code>.
    */
  protected final int logToGraphX (int x)
  {
    return x * cellWidth ();
  }

  /**
    Translate the given logical y cell coordinate to its graphical y
    coordinate.
    @param y the logical y cell coordinates to translate.
    @return the graphical y cell coordinates corresponding to <code>y</code>.
    */
  protected final int logToGraphY (int y)
  {
    return y * cellHeight ();
  }

  /**
    Translate the given graphical x cell coordinate to its logical x
    coordinate.
    @param x the graphical x cell coordinate to translate.
    @return the logical x cell coordinate corresponding to <code>x</code>.
    */
  protected final int graphToLogX (int x)
  {
    return x / cellWidth ();
  }

  /**
    Translate the given graphical y cell coordinate to its logical  y
    coordinate.
    @param y the graphical y cell coordinate to translate.
    @return the logical y cell coordinate corresponding to <code>y</code>.
    */
  protected final int graphToLogY (int y)
  {
    return y / cellHeight ();
  }

  /**
    Get graphical cell width.
    @return the graphical width of a single cell in pixels.
    @see rl.gui.GridCanvas#cellHeight()
    */
  protected final int cellWidth ()
  {
    if (size == null)
      size = getSize ();
    return size.width / grid.getXSize ();
  }

  /**
    Get graphical cell height.
    @return the graphical height of a single cell in pixels.
    @see rl.gui.GridCanvas#cellWidth()
    */
  protected final int cellHeight ()
  {
    if (size == null)
      size = getSize ();
    return size.height / grid.getYSize ();
  }

  // protected data

  /**
    The grid environment drawn by this canvas.
    */
  protected Grid grid;

  /**
    The environment state used for the last drawing. Set up by
    <code>update (Observable, Object)</code>.
    */
  protected NumberArray state;

  /**
    The mouse listener used by this canvas. Handles mouse click events by
    toggling the contents of the clicked cell.
    */
  private class GridMouseListener extends MouseAdapter
  {
    /**
      Handle mouse click events for a grid canvas by toggling the contents
      of the clicked cell. For left button clicks
      <code>Grid.EMPTY</code> becomes <code>Grid.OBSTACLE</code> or
      vice versa. For right button clicks <code>Grid.EMPTY</code> becomes
      <code>Grid.GOAL</code> or vice versa.
      */
    public void mouseClicked (MouseEvent e)
    {
      int x = graphToLogX (e.getX ()), y = graphToLogY (e.getY ());
      
      if (x >= grid.getXSize () || y >= grid.getYSize ())
	return; // click out of drawing bounds

      if ((e.getModifiers () & MouseEvent.BUTTON1_MASK) != 0)
      { // left button click
	if (grid.cellAt (x, y) == Grid.EMPTY)
	  grid.setObstacle (x, y);
	else if (grid.cellAt (x, y) == Grid.OBSTACLE)
	  grid.setEmpty (x, y);
      }
      else if ((e.getModifiers () & MouseEvent.BUTTON3_MASK) != 0)
      { // right button click
	if (grid.cellAt (x, y) == Grid.EMPTY)
	  grid.setGoal (x, y);
	else if (grid.cellAt (x, y) == Grid.GOAL)
	  grid.setEmpty (x, y);
      }
      else
	return; // not handling other mouse clicks

      int gX = logToGraphX (x), gY = logToGraphY (y);
      repaint (gX + 1, gY + 1, cellWidth () - 1, cellHeight () - 1);
    }
  }

  /**
    The component listener for this canvas. When notified about resizing,
    enforces preserving the grid aspect ratio.
    */
  private class GridComponentListener extends ComponentAdapter
  {
    /**
      Handle resize notifications for a grid canvas to enforce
      the preservation of the grid aspect ratio. Just calls
      <code>adjustSize ()</code>.
      @see rl.gui.GridCanvas#adjustSize()
      */
    public void componentResized (ComponentEvent e)
    {
      adjustSize ();
    }
  }

  /**
    Adjust the size of this canvas so as to make grid cells square.
    Enforces the preservation of the grid aspect ratio by <em>reducing</em>
    either the x or the y dimension.
    */
  private void adjustSize ()
  {
    Dimension oldSize = getSize ();
    final int xSize = grid.getXSize (), ySize = grid.getYSize ();
    if ((oldSize.width - 1) / xSize != (oldSize.height - 1) / ySize ||
	(oldSize.width - 1) % xSize != 0 ||
	(oldSize.height - 1) % ySize != 0)  // 1: for right/bottom border
    {
      int newWidth = xSize * ((oldSize.height - 1) / ySize) + 1;
      int newHeight = ySize * ((oldSize.width - 1) / xSize) + 1;
      if (newWidth <= oldSize.width)
	newHeight = ySize * ((oldSize.height - 1) / ySize) + 1;
      else
	newWidth = xSize * ((oldSize.width - 1) / xSize) + 1;
      size = new Dimension (newWidth, newHeight);
      setSize (size);
    }
  }

  /**
    The size of this canvas.
    */
  private Dimension size = null;

  /**
    Flag indicating whether the drawing of the canvas is up to date
    with respect to the most recent environment state set up by
    <code>update (Observable, Object)</code>.
    @see rl.gui.GridCanvas#update(java.util.Observable, java.lang.Object)
    */
  private boolean upToDate = false;

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

    Grid grid = new Grid (m, 2, 6, 5);
    Frame window = new Frame ("Grid World");
    window.setLayout (new BorderLayout ());
    GridCanvas canvas = new GridCanvas (grid);
    window.add (canvas, BorderLayout.CENTER);
    window.addWindowListener (new WindowAdapter ()
			      {
				public void windowClosed (WindowEvent e)
				  {
				    System.exit (0);
				  }
			      });
    window.pack ();
    window.show ();

    java.util.Random rnd = new java.util.Random ();
    while (true)
    {
      grid.execute (util.Numerical.randomInRange (rnd, 0, 3));
      try {
 	Thread.currentThread ().sleep (10);
      }
      catch (InterruptedException e) {}
      canvas.update (null, null);
    }
  }
}
