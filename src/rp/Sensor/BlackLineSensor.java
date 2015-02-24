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

	public void addChangeListener(LineListener listener) {
		listeners.add(listener);
	}

	@Override
	public void stateChanged(SensorPort sensorPort, int i, int i1) {
		int lightValue = this.getLightValue();
		boolean onLine = (lightValue < this.lightThreshold);

		for (LineListener ls : listeners)
			ls.lineChanged(onLine, lightValue);
	}
}