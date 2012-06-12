package algorithms;

/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation; either version 2.1 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA.
 */

/*
 *    AbstractMemorySelector.java
 *    Copyright (C) 2004 Francesco De Comite
 *
 */

import integrated.BoltzmannSelector;
import integrated.EpsilonGreedySelector;
import integrated.RouletteWheelSelector;

import java.util.Iterator;
import java.util.Random;

import qlearning.IRewardStore;
import dataset.Dataset;
import environment.ActionList;
import environment.IAction;
import environment.IState;

/**
 * The base of all Q-Learning-like algorithms :
 * 
 * <ul>
 * <li>Provides a structure to memorize or compute the Q(s,a)</li>
 * <li>Contains all the parameters used in the Q-Learning update rules</li>
 * <li>Contains all the parameters used to control convergence</li>
 * </ul>
 * 
 * 
 * 
 * @author Francesco De Comite (decomite at lifl.fr)
 * @version $Revision: 1.0 $
 * 
 */
/*
 * October 4th 2006 : return to the old version, where boltzmann, epsilon-greedy
 * and rouletteWheel are declared inside this class : too much problems in
 * defining and monitoring the parameters of each choosing strategy (epsilon,
 * tau, modifying epsilon...) (fd)
 */
abstract public class AbstractMemorySelector implements ISelector {

	/** Memorizing or computing Q(s,a) */
	protected IRewardStore memory;

	/** Learning rate */
	protected double alpha = 0.9;

	/** discount rate */
	protected double gamma = 0.9;

	private Random generator = new Random();

	/**
	 * Factor by which we multiply alpha at each learning step (geometric decay)<br>
	 * <i> Note : geometric decay does no insure convergence.</i>
	 */
	protected double decayAlpha = 0.999999999;

	/** Number of learning steps */
	protected double count = 1.0;

	/**
	 * Power of decay when alpha=1/(count^alphaDecayPower)
	 * 
	 * @see <a href="http://www.cs.tau.ac.il/~evend/papers/ql-jmlr.ps">Learning
	 *      rates for Q-Learning</a>
	 */
	protected double alphaDecayPower = 0.8;

	public void setAlpha(double a) {
		this.alpha = a;
	}

	public void setGamma(double g) {
		this.gamma = g;
	}

	public void setDecay(double d) {
		this.decayAlpha = d;
	}

	public void setAlphaDecayPower(double a) {
		this.alphaDecayPower = a;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getGamma() {
		return gamma;
	}

	public double getDecay() {
		return this.decayAlpha;
	}

	public double getAlphaDecayPower() {
		return this.alphaDecayPower;
	}

	/**
	 * Alpha decay methods
	 * <ul>
	 * <li>Geometric : use decayAlpha</li>
	 * <li>Exponential : use alphaDecayPower (default)</li>
	 * <ul>
	 */
	protected boolean geometricDecay = false;

	public void setGeometricAlphaDecay() {
		geometricDecay = true;
	}

	public void setExponentialAlphaDecay() {
		geometricDecay = false;
	}

	/**
	 * How convergence is controlled ?
	 * <ul>
	 * <li>true : alpha decays geometrically</li>
	 * <li>false : alpha decays exponentially</li>
	 * </ul>
	 */
	public boolean getGeometricDecay() {
		return geometricDecay;
	}

	/**
	 * How to implement randomness ?
	 * <ul>
	 * <li>epsilon-greedy</li>
	 * <li>Roulette wheel selection</li>
	 * <li>Boltzmann</li>
	 * <ul>
	 * Roulette wheel or Boltzmann selection makes epsilon useless.
	 */
	protected RouletteWheelSelector rws;

	protected EpsilonGreedySelector egs;

	protected BoltzmannSelector bs;

	public void setRouletteWheel() {
		rws.setRouletteWheel(true);
		egs.setEpsilonGreedy(false);
		bs.setBoltzmann(false);
	}

	/** Set the epsilon-greedy policy */
	public void setEpsilonGreedy() {
		egs.setEpsilonGreedy(true);
		rws.setRouletteWheel(false);
		bs.setBoltzmann(false);
	}

	/** Set Boltzmann selection */
	public void setBoltzmann() {
		egs.setEpsilonGreedy(false);
		rws.setRouletteWheel(false);
		bs.setBoltzmann(true);
	}

	/** Finding Q(s,a) */
	public double getValue(IState s, IAction a) {
		return memory.get(s, a);
	}

	/** Nothing to reset at this level. */
	public void newEpisode() {
	};

	/**
	 * Learning from experience.
	 * 
	 * @param s1
	 *            the start state.
	 * @param s2
	 *            the arrival state.
	 * @param a
	 *            the chosen action.
	 * 
	 *            <a href=
	 *            "http://www.cs.ualberta.ca/~sutton/book/ebook/node65.html"
	 *            >Sutton & Barto p 149 Q-Learning</a>
	 * @param reward
	 *            immediate reward.
	 */
	public void learn(IState s1, IState s2, IAction a, double reward) {
		if (geometricDecay)
			alpha *= decayAlpha;
		else {
			alpha = 1 / Math.pow(count + 1.0, this.alphaDecayPower);
		}

		count++;
		double qsa = memory.get(s1, a);
		ActionList la = s2.getActionList();
		if (la.size() != 0) {
			Iterator<IAction> iterator = la.iterator();
			double maxqsap = memory.get(s2, iterator.next());
			while (iterator.hasNext()) {
				IAction aprime = iterator.next();
				double qsap = memory.get(s2, aprime);
				if (qsap > maxqsap)
					maxqsap = qsap;
			}
			qsa += alpha * (reward + gamma * maxqsap - qsa);
			memory.put(s1, a, s2, qsa);
		} else {
			memory.put(s1, a, s2, qsa + alpha * (reward - qsa));
		}
	}

	/** Choose one of the legal moves */
	public IAction getChoice(ActionList l) {
		if (rws.isRouletteWheel())
			return rws.choice(l);
		if (egs.isEpsilonGreedy())
			return egs.choice(l);
		if (bs.isBoltzmann())
			return bs.choice(l);
		return null;
	}

	/** Auxiliary/debug method : find the best action from a state. */
	public IAction bestAction(IState s) {
		ActionList l = s.getActionList();
		Iterator<IAction> iterator = l.iterator();
		if (l.size() == 0)
			return null;
		IAction meilleure = iterator.next();
		double maxqsap = memory.get(s, meilleure);
		while (iterator.hasNext()) {
			IAction a = iterator.next();
			double qsap = memory.get(s, a);
			if (qsap > maxqsap) {
				maxqsap = qsap;
				meilleure = a;
			}
		}
		return meilleure;
	}

	public String toString() {
		return memory.toString();
	}

	public Dataset extractDataset() {
		return memory.extractDataset();
	}

	/**
	 * @return the epsilon
	 */
	public double getEpsilon() {
		return egs.getEpsilon();
	}

	/**
	 * @param epsilon
	 *            the epsilon to set
	 */
	public void setEpsilon(double epsilon) {
		this.egs.setEpsilon(epsilon);
	}

	/**
	 * @return the tau
	 */
	public double getTau() {
		return bs.getTau();
	}

	/**
	 * @param tau
	 *            the tau to set
	 */
	public void setTau(double tau) {
		this.bs.setTau(tau);
	}

}
