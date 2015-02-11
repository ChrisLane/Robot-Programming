package rp.Listener;

import java.util.ArrayList;

import lejos.nxt.ADSensorPort;
import lejos.nxt.LightSensor;

public class BlackLineSensor extends LightSensor implements Runnable {
	private ArrayList<LineListener> listeners;

	private Thread pollThread;
	private boolean isRunning;

	private boolean onLine;
	private int lightValue, oldLightValue;
	private final int darkTolerance;

	public BlackLineSensor(ADSensorPort port, boolean floodlight, int tolerance) {
		super(port, floodlight);
		this.darkTolerance = tolerance;
		this.listeners = new ArrayList<LineListener>();
		this.startPolling();
	}

	@Override
	public void run() {
		while (this.isRunning) {
			this.oldLightValue = this.lightValue;
			this.lightValue = this.getLightValue();
			this.onLine = (this.lightValue < this.darkTolerance);

			if (this.lightValue != this.oldLightValue)
				for (LineListener ls : this.listeners)
					ls.lineChanged(this.onLine, this.lightValue);
		}
	}

	protected void startPolling() {
		this.pollThread = new Thread(this);
		this.pollThread.setDaemon(true);
		this.isRunning = true;
		this.pollThread.start();
	}

	public void stop() throws InterruptedException {
		this.isRunning = false;
		this.pollThread.join();
	}

	public BlackLineSensor addChangeListener(LineListener listener) {
		this.listeners.add(listener);
		return this;
	}
}