package rp.Ex2.Part3;

import lejos.nxt.addon.OpticalDistanceSensor;

public abstract class OpticalDistanceListener implements Runnable {
	private final OpticalDistanceSensor sensor;

	private final Thread pollThread;
	private boolean isRunning;

	private double previous, current;
	private double tolerance = 0;

	public OpticalDistanceListener(OpticalDistanceSensor sensor, double tolerance) {
		this.sensor = sensor;
		this.tolerance = tolerance;

		this.pollThread = new Thread(this);
		this.pollThread.setDaemon(true);
		this.isRunning = true;
		this.pollThread.start();
	}

	@Override
	public void run() {
		while (this.isRunning) {
			this.previous = this.current;
			this.current = this.sensor.getDistance() / 10.0;
			if (Math.abs(this.previous - this.current) >= this.tolerance)
				this.stateChanged(this.current, this.previous);
		}
	}
	public double getLastValue() {
		return this.current;
	}
	public double getTolerance() {
		return this.tolerance;
	}
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
	/**
	 * Returns distance in cm
	 * @param value Current distance
	 * @param oldValue Previous distance
	 */
	public abstract void stateChanged(double value, double oldValue);

	public void stop() throws InterruptedException {
		this.isRunning = false;
		this.pollThread.join();
	}
}
