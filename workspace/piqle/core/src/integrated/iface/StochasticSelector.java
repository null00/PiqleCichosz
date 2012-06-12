package integrated.iface;

import environment.ActionList;
import environment.IAction;

public interface StochasticSelector {

	public IAction choice(ActionList l);
	
}
