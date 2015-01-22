package Exercise2.Part1;

import lejos.robotics.navigation.DifferentialPilot;

public abstract class MovementPath implements Runnable {
	protected final String name;
	protected DifferentialPilot pilot;
	protected boolean isRunning = true;
	private Thread thread;
	
	public MovementPath(DifferentialPilot pilot, String name) {
		this.pilot = pilot;
		this.name = name;
	}
	
	@Override
	public void run() {
		while (this.isRunning)
			this.path();
	}
	
	protected abstract void path();
	
	public void start() {
		this.isRunning = true;
		if (thread == null || !thread.isAlive())
			this.thread = new Thread(this);
		thread.start();
	}
	
	@SuppressWarnings("static-access")
	public void stop() {
		// System.out.println("Waiting for path to complete");
		this.isRunning = false;
		pilot.stop();
		while (pilot.isMoving() || this.isRunning)
			thread.yield();
	}
	
	public String getName() {
		return this.name;
	}
}
