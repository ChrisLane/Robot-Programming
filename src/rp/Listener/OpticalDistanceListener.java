package rp.Listener;

import lejos.nxt.addon.OpticalDistanceSensor;

public abstract class OpticalDistanceListener extends SensorListener implements Runnable, StateChanged {
	private final OpticalDistanceSensor sensor;

	private double previous, current;
	private double tolerance = 0;

	public OpticalDistanceListener(OpticalDistanceSensor sensor, double tolerance) {
		this.sensor = sensor;
		this.tolerance = tolerance;
		startPolling();
	}

	@Override
	protected void pollTick() {
		previous = current;
		current = (sensor.getDistance() / 10.0) + 4.0;
		if (Math.abs(previous - current) >= tolerance)
			stateChanged(current, previous);
	}

	public double getLastValue() {
		return current;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
}
