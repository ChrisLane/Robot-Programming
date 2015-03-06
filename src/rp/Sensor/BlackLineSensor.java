package rp.Sensor;

import rp.Listener.LineListener;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

import java.util.ArrayList;

public class BlackLineSensor extends LightSensor implements SensorPortListener {
	private final ArrayList<LineListener> listeners;

	private final int lightThreshold;

	public BlackLineSensor(SensorPort port, boolean floodlight, int lightThreshold) {
		super(port, floodlight);
		this.lightThreshold = lightThreshold;
		listeners = new ArrayList<>();

		port.addSensorPortListener(this);
	}

	public BlackLineSensor addChangeListener(LineListener listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public void stateChanged(SensorPort sensorPort, int ov, int nv) {
		int lightValue = getLightValue();
		boolean onLine = (lightValue < lightThreshold);

		for (LineListener ls : listeners)
			ls.lineChanged(this, onLine, lightValue);
	}
}