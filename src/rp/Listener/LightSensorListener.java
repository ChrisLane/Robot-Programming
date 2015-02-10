package rp.Listener;

import lejos.nxt.LightSensor;

public abstract class LightSensorListener implements Runnable {
	private final LightSensor sensor;

	private final Thread pollThread;
	private boolean isRunning;

	private int previous, current;
	private int tolerance = 0;

	public LightSensorListener(LightSensor sensor, int tolerance) {
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
			this.current = this.sensor.getLightValue();
			if (Math.abs(this.previous - this.current) >= this.tolerance)
				this.stateChanged(this.current, this.previous);
		}
	}

	public double getLastValue() {
		return this.current;
	}

	public int getTolerance() {
		return this.tolerance;
	}

	public void setTolerance(int tolerance) {
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
