package rp.Ex2.Part3;

import lejos.nxt.UltrasonicSensor;

public abstract class UltrasonicDistanceListener implements Runnable {
	private final UltrasonicSensor sensor;

	private final Thread pollThread;
	private boolean isRunning;

	private int previous, current;
	private double tolerance = 0;

	public UltrasonicDistanceListener(UltrasonicSensor sensor, double tolerance) {
		this.sensor = sensor;
		this.tolerance = tolerance;
		sensor.setMode(UltrasonicSensor.MODE_CONTINUOUS);

		this.pollThread = new Thread(this);
		this.pollThread.setDaemon(true);
		this.isRunning = true;
		this.pollThread.start();
	}

	@Override
	public void run() {
		while (this.isRunning) {
			this.previous = this.current;
			this.current = this.sensor.getDistance();
			if (Math.abs(this.previous - this.current) >= this.tolerance)
				this.stateChanged(this.current, this.previous);

			/*
			 * try { Thread.sleep(50); } catch (InterruptedException e) {
			 * System.out.println("Why sleeping would be bad I'll never know..."); e.printStackTrace(); }
			 */
		}
	}

	public int getLastValue() {
		return this.current;
	}

	public double getTolerance() {
		return this.tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public abstract void stateChanged(int value, int oldValue);

	public void stop() throws InterruptedException {
		this.isRunning = false;
		this.pollThread.join();
	}
}
