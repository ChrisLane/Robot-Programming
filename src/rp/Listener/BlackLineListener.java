package rp.Listener;

import lejos.nxt.LightSensor;

public class BlackLineListener extends SensorListener {
	private final LightSensor sensor;
	private BlackLineChangeListener blackLineChangeListener;

	private boolean onLine;
	private int lightValue, oldLightValue;
	private final int darkTolerance;

	public BlackLineListener(LightSensor sensor, int tolerance) {
		super();
		this.sensor = sensor;
		this.darkTolerance = tolerance;
		this.startPolling();
	}

	public BlackLineListener setChangeListener(BlackLineChangeListener listener) {
		this.blackLineChangeListener = listener;
		return this;
	}

	@Override
	protected void pollTick() {
		this.oldLightValue = this.lightValue;
		this.lightValue = this.sensor.getLightValue();
		this.onLine = (this.lightValue < this.darkTolerance);

		if (this.lightValue != this.oldLightValue && this.blackLineChangeListener != null)
			this.blackLineChangeListener.lineChanged(this.onLine, this.lightValue);
	}
}