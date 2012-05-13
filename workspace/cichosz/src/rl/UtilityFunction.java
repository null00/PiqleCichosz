// 	$Id: UtilityFunction.java,v 1.7 1997/08/14 09:59:46 pawel Exp $	
// UtilityFunction.java: definition of interface UtilityFunction
// Copyleft 1997 Pawel Cichosz

package rl;

import util.NumberArray;

/**
  An interface for utility function operations, to be used to provide
  <code>CreditAssigner</code> callback access to the corresponding
  functions of <code>Learner</code>.
  @see rl.Learner
  @see rl.CreditAssigner
 */
public interface UtilityFunction
{
  /**
    Call <code>Learner.utility0 ()</code>.
    @see rl.Learner#utility0(util.NumberArray, int)
    */  
  double utility0 (NumberArray x, int a);

  /**
    Call <code>Learner.utility1 ()</code>.
    @see rl.Learner#utility1(util.NumberArray, int)
    */
  double utility1 (NumberArray x, int a);

  /**
    Call <code>Learner.update ()</code>.
    @see rl.Learner#update(util.NumberArray, int, double)
    */
  void update (NumberArray x, int a, double d);
}
