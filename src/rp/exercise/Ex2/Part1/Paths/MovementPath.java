package rp.exercise.Ex2.Part1.Paths;

import lejos.robotics.navigation.DifferentialPilot;

public class MovementPath implements Runnable {
	private DifferentialPilot pilot;
	private final String name;
	private boolean isRunning, oneTime;

	private final Movement[] movements;
	private Movement current;
	private byte curMv;
	private Thread runThread;

	public MovementPath(String name, Movement[] moves) {
		this.name = name;
		movements = moves;
		// for (Movement m : moves)1
		// m.addMoveListener(this);
	}

	public void start(DifferentialPilot pilot) {
		this.start(pilot, false);
	}

	public void start(DifferentialPilot pilot, boolean oneTime) {
		this.pilot = pilot;
		this.oneTime = oneTime;
		isRunning = true;
		curMv = 0;
		// System.out.println("Starting " + this.getName() +
		// " thread. Is already running? " + runThread.isAlive());
		runThread = new Thread(this);
		runThread.start();
	}

	@Override
	public void run() {
		// System.out.println("In thread of " + this.getName() + "\n");
		while (isRunning) {
			current = movements[curMv];
			current.run(pilot);

			if (++curMv == movements.length)
				if (oneTime) {
					oneTime = false;
					return;
				}
				else
					curMv = 0;
		}
	}

	public void stop() {
		isRunning = false;
		current.stop();
		synchronized (runThread) {
			try {
				runThread.join();
			}
			catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitStop() {
		stop();
		while (current.isRunning());
	}

	public boolean isRunning() {
		return !(current == null || !current.isRunning());
	}

	public String getName() {
		return name;
	}
}
