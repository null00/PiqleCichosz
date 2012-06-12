package integrated;

import java.util.Random;

import qlearning.IRewardStore;
import environment.ActionList;
import environment.IAction;
import environment.IState;
import integrated.iface.StochasticSelector;

public class BoltzmannSelector implements StochasticSelector {

	protected IRewardStore memory;
	private boolean boltzmann = false;
	protected double tau=0.5;

	private Random generator = new Random();
	
	public BoltzmannSelector(IRewardStore memory) {
		this.memory = memory;
	}

	public IAction choice(ActionList l) {
		if (l.size() == 0)
			return null;
		IState s = l.getState();
		double sum = 0;
		double tab[] = new double[l.size()];
		for (int i = 0; i < l.size(); i++) {
			sum += Math.exp(memory.get(s, l.get(i)) / this.tau);
			tab[i] = sum;
		}
		double choix = generator.nextDouble() * sum;
		for (int i = 0; i < l.size(); i++) {
			if (choix <= tab[i])
				return l.get(i);
		}
		System.err.println(choix + " " + "Wrong");
		System.exit(-1);
		return null;
	}

	public double getTau() {
		return tau;
	}

	public void setTau(double tau) {
		this.tau = tau;
	}

	public boolean isBoltzmann() {
		return boltzmann;
	}

	public void setBoltzmann(boolean boltzmann) {
		this.boltzmann = boltzmann;
	}

}
