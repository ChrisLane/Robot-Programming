package rp.sensor;

import rp.listener.BumperHitListener;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

public class BumperSensor implements SensorPortListener {
	private final BumperHitListener listener;

	public BumperSensor(SensorPort port, BumperHitListener listener) {
		this.listener = listener;
		port.addSensorPortListener(this);
	}

	@Override
	public void stateChanged(SensorPort aSource, int oldVal, int newVal) {
		if (oldVal - newVal >= 40)
			listener.onBumperHit();
		aSource.reset();
	}
}
