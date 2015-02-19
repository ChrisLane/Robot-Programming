package rp.Sensor;

import java.util.ArrayList;

import lejos.nxt.ADSensorPort;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

import rp.Listener.LineListener;

public class BlackLineSensor extends LightSensor implements SensorPortListener {
	private ArrayList<LineListener> listeners;

	private final int darkTolerance;

	public BlackLineSensor(SensorPort port, boolean floodlight, int tolerance) {
		super(port, floodlight);
		this.darkTolerance = tolerance;
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
		boolean onLine = (lightValue < this.darkTolerance);

		for (LineListener ls : this.listeners)
			ls.lineChanged(onLine, lightValue);
	}
}