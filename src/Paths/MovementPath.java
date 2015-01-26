package Paths;

import lejos.robotics.navigation.DifferentialPilot;

public abstract class MovementPath implements Runnable {
	protected final String name;
	protected DifferentialPilot pilot;
	protected boolean isRunning;
	private Thread thread;

	public MovementPath(DifferentialPilot pilot, String name) {
		this.pilot = pilot;
		this.name = name;
		this.thread = new Thread(this);
	}

	@Override
	public void run() {
		while (this.isRunning)
			this.path();
	}

	protected abstract void path();

	public synchronized void start() {
		if (thread.isAlive())
			this.stop();
		this.isRunning = true;
		thread.start();
	}

	@SuppressWarnings("static-access")
	public synchronized void stop() {
		// System.out.println("Waiting for path to complete");
		pilot.stop();
		while (pilot.isMoving() || this.isRunning)
			thread.yield();
		this.isRunning = false;
	}

	public String getName() {
		return this.name;
	}
}
