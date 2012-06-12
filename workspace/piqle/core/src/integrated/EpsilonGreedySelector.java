package integrated;

import java.util.Random;

import qlearning.IRewardStore;
import environment.ActionList;
import environment.IAction;
import environment.IState;
import integrated.iface.StochasticSelector;

public class EpsilonGreedySelector implements StochasticSelector {

	protected IRewardStore memory;
	private double epsilon = 0.5;
	
	private boolean epsilonGreedy = false;
	private Random generator = new Random();
	
	public EpsilonGreedySelector(IRewardStore memory) {
		this.memory = memory;
	}
	
	@Override
	public IAction choice(ActionList l) {
		if (l.size() == 0)
			return null;
		IState s = l.getState();
		IAction meilleure = l.get(0);
		double maxqsap = memory.get(s, meilleure);
		// TODO : might use an iterator
		for (int i = 1; i < l.size(); i++) {
			IAction a = l.get(i);
			double qsap = memory.get(s, a);
			if (qsap > maxqsap) {
				maxqsap = qsap;
				meilleure = a;
			}
		}
		// TODO Beginning the method with this test should speed up the program
		if (generator.nextDouble() > this.getEpsilon())
			return meilleure;
		else
			return l.get(generator.nextInt(l.size()));
	}

	public boolean isEpsilonGreedy() {
		return epsilonGreedy;
	}

	public void setEpsilonGreedy(boolean epsilonGreedy) {
		this.epsilonGreedy = epsilonGreedy;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

}
