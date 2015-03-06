package rp.Listener;

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

		pollThread = new Thread(this);
		pollThread.setDaemon(true);
		isRunning = true;
		pollThread.start();
	}

	@Override
	public void run() {
		while (isRunning) {
			previous = current;
			current = sensor.getDistance();
			if (Math.abs(previous - current) >= tolerance)
				stateChanged(current, previous);

			/*
			 * try { Thread.sleep(50); } catch (InterruptedException e) {
			 * System.out.println("Why sleeping would be bad I'll never know..."); e.printStackTrace(); }
			 */
		}
	}

	public int getLastValue() {
		return current;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public abstract void stateChanged(int value, int oldValue);

	public void stop() throws InterruptedException {
		isRunning = false;
		pollThread.join();
	}
}
