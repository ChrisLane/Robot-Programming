package rp.Ex2.Part3;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

public class TouchListener implements SensorPortListener {
	private BumperPressListener listener;

	public TouchListener(SensorPort port, BumperPressListener listener) {
		this.listener = listener;
		port.addSensorPortListener(this);
	}

	@Override
	public void stateChanged(SensorPort aSource, int oldVal, int newVal) {
		if (oldVal - newVal >= 60)
			this.listener.bumperHit();
	}
}
