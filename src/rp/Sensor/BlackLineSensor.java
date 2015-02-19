package rp.Sensor;

import java.util.ArrayList;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

import rp.Listener.LineListener;

public class BlackLineSensor extends LightSensor implements SensorPortListener {
	private ArrayList<LineListener> listeners;

	private final int lightThreshold;

	public BlackLineSensor(SensorPort port, boolean floodlight, int lightThreshold) {
		super(port, floodlight);
		this.lightThreshold = lightThreshold;
		this.listeners = new ArrayList<>();

		port.addSensorPortListener(this);
	}

	public BlackLineSensor addChangeListener(LineListener listener) {
		this.listeners.add(listener);
		return this;
	}

	@Override
	public void stateChanged(SensorPort sensorPort, int i, int i1) {
		int lightValue = this.getLightValue();
		boolean onLine = (lightValue < this.lightThreshold);

		for (LineListener ls : this.listeners)
			ls.lineChanged(onLine, lightValue);
	}
}