package rp.Listener;

import lejos.nxt.LightSensor;

public class BlackLineListener extends SensorListener {
	private final LightSensor sensor;
	private BlackLineChangeListener blcl;

	private boolean onLine;
	private int lightValue;
	private final int darkTolerance;

	public BlackLineListener(LightSensor sensor, int tolerance) {
		super();
		this.sensor = sensor;
		this.darkTolerance = tolerance;
		this.startPolling();
	}

	public BlackLineListener setChangeListener(BlackLineChangeListener listener) {
		this.blcl = listener;
		return this;
	}

	@Override
	protected void pollTick() {
		final boolean oldVal = this.onLine;
		this.lightValue = this.sensor.getLightValue();
		this.onLine = (this.lightValue < this.darkTolerance);

		if (oldVal != this.onLine && this.blcl != null)
			this.blcl.lineChanged(this.onLine, this.lightValue);
	}
}