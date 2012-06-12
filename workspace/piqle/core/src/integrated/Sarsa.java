package integrated;

import java.util.Iterator;

import qlearning.IDefaultValueChooser;
import qlearning.RewardMemorizer;
import algorithms.AbstractMemorySelector;
import environment.ActionList;
import environment.IAction;
import environment.IState;

public class Sarsa extends AbstractMemorySelector {

	private static final long serialVersionUID = 1L;

	public Sarsa() {
		memory = new RewardMemorizer();
	}

	public Sarsa(IDefaultValueChooser dvc) {
		memory = new RewardMemorizer(dvc);
		bs = new BoltzmannSelector(memory);
	}

	public void learn(IState s1, IState s2, IAction a, double reward) {
		if (geometricDecay)
			alpha *= decayAlpha;
		else {
			alpha = 1 / Math.pow(count + 1.0, this.alphaDecayPower);
		}

		count++;
		double qsap;
		double qsa = memory.get(s1, a);
		ActionList la = s2.getActionList();
		if (la.size() != 0) {
			Iterator<IAction> iterator = la.iterator();
			IAction aprime = iterator.next();
			qsap = memory.get(s2, aprime);
			// D := r+γ maxa Qt(xt+1, a) - Qt(xt, at)
			qsa += alpha * (reward + gamma * qsap - qsa);
			memory.put(s1, a, s2, qsa);
		} else {
			memory.put(s1, a, s2, qsa + alpha * (reward - qsa));
		}
	}

	public void showHistogram() {
		((RewardMemorizer) memory).makeHistogram();
		((RewardMemorizer) memory).displayHistogram();
	}

}
