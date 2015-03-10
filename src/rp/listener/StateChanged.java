package rp.listener;

public interface StateChanged {
	/**
	 * Returns distance in cm
	 *
	 * @param value Current distance
	 * @param oldValue Previous distance
	 */
	void stateChanged(double value, double oldValue);
}
