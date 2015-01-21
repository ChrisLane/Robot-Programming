package Exercise2.Part1;

import lejos.robotics.navigation.DifferentialPilot;

public abstract class MovementPath implements Runnable {
	private Thread thread;
	
	protected DifferentialPilot pilot;
	protected boolean isRunning = true;
	protected final String name;
	
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
