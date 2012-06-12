package integrated;

import java.util.Random;

import qlearning.IRewardStore;
import environment.ActionList;
import environment.IAction;
import environment.IState;
import integrated.iface.StochasticSelector;

public class RouletteWheelSelector implements StochasticSelector {

	protected IRewardStore memory;
	private boolean rouletteWheel = false;
	private Random generator = new Random();
	
	public RouletteWheelSelector(IRewardStore memory) {
		this.memory = memory;
	}
	
	/**
	 * Roulette Wheel selection of the next action : the probability for an
	 * action to be chosen is relative to its Q(s,a) value.
	 * 
	 * TODO DEBUG : not valid if Q(s,a) can be negative !!!
	 */
	
	@Override
	public IAction choice(ActionList l) {
		if (l.size() == 0)
			return null;
		IState s = l.getState();
		double sum = 0;
		for (int i = 0; i < l.size(); i++)
			sum += memory.get(s, l.get(i)) + 1;
		double choix = generator.nextDouble() * sum;
		int indice = 0;
		double partialSum = memory.get(s, l.get(indice)) + 1;
		while (choix > partialSum) {
			indice++;
			partialSum += 1 + memory.get(s, l.get(indice));
		}
		return l.get(indice);
	}

	public boolean isRouletteWheel() {
		return rouletteWheel;
	}

	public void setRouletteWheel(boolean rouletteWheel) {
		this.rouletteWheel = rouletteWheel;
	}

}
