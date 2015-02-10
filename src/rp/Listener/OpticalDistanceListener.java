package rp.Listener;

import lejos.nxt.addon.OpticalDistanceSensor;

public abstract class OpticalDistanceListener extends SensorListener implements Runnable, StateChanged {
	private final OpticalDistanceSensor sensor;

	private double previous, current;
	private double tolerance = 0;

	public OpticalDistanceListener(OpticalDistanceSensor sensor, double tolerance) {
		this.sensor = sensor;
		this.tolerance = tolerance;
		this.startPolling();
	}

	@Override
	protected void pollTick() {
		this.previous = this.current;
		this.current = (this.sensor.getDistance() / 10.0) + 4.0;
		if (Math.abs(this.previous - this.current) >= this.tolerance)
			this.stateChanged(this.current, this.previous);
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
}
