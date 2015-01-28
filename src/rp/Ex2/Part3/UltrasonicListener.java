package rp.Ex2.Part3;

import lejos.nxt.UltrasonicSensor;

public abstract class UltrasonicListener implements Runnable {
	private UltrasonicSensor sensor;

	private Thread pollThread;
	private boolean isRunning;

	private int previous, current;
	private double tolerance = 0;

	public UltrasonicListener(UltrasonicSensor sensor, double tolerance) {
		this.sensor = sensor;
		this.tolerance = tolerance;
		sensor.setMode(UltrasonicSensor.MODE_CONTINUOUS);

		pollThread = new Thread(this);
		pollThread.setDaemon(true);
		this.isRunning = true;
		pollThread.start();
	}

	@Override
	public void run() {
		while (this.isRunning) {
			previous = current;
			current = sensor.getDistance();
			if (Math.abs(previous - current) >= tolerance)
				this.stateChanged(current, previous);

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
