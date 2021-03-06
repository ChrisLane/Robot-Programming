package rp.sensor;

import rp.listener.UltrasonicDistanceListener;
import rp.listener.WallApproachListener;

import lejos.nxt.UltrasonicSensor;

public class UltrasonicFrontSensor extends UltrasonicDistanceListener {
	private final WallApproachListener listener;

	public UltrasonicFrontSensor(UltrasonicSensor sensor, WallApproachListener listener) {
		super(sensor, 2);
		this.listener = listener;
	}

	@Override
	public void stateChanged(int value, int oldValue) {
		if (oldValue - value > 0 && value < InfraredSideSensor.TARGETDISTANCE * 0.75) // if getting closer to wall
			listener.wallApproaching(value);
	}
}
