package rp.Ex2.Part1.Paths;

import lejos.robotics.navigation.DifferentialPilot;

public class MovementPath implements Runnable {
	private final DifferentialPilot pilot;
	private final String name;
	private boolean isRunning;

	private final Movement[] movements;
	private Movement current;
	private byte curMv;
	private Thread runThread;

	public MovementPath(DifferentialPilot pilot, String name, Movement[] moves) {
		this.pilot = pilot;
		this.name = name;
		this.movements = moves;
		// for (Movement m : moves)
		// m.addMoveListener(this);
	}

	public void start() {
		this.isRunning = true;
		this.curMv = 0;
		//System.out.println("Starting " + this.getName() + " thread. Is already running? " + runThread.isAlive());
		runThread = new Thread(this);
		runThread.start();
	}

	@Override
	public void run() {
		System.out.println("In thread of " + this.getName() + "\n");
		while (this.isRunning) {
			current = movements[curMv];
			current.run();

			if (++curMv == movements.length)
				curMv = 0;
		}
	}

	public void stop() {
		this.isRunning = false;
		current.stop();
		synchronized (runThread) {
			try {
				runThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitStop() {
		this.stop();
		while (current.isRunning()) ;
	}

	public boolean isRunning() {
		return !(current == null || !current.isRunning());
	}

	public String getName() {
		return this.name;
	}
}
