package rp.Listener;

public interface StateChanged {
	/**
	 * Returns distance in cm
	 * @param value Current distance
	 * @param oldValue Previous distance
	 */
	public void stateChanged(double value, double oldValue);
}
