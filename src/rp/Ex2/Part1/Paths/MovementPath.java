package rp.Ex2.Part1.Paths;

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
		this.movements = moves;
		// for (Movement m : moves)1
		// m.addMoveListener(this);
	}

	public void start(DifferentialPilot pilot) {
		this.start(pilot, false);
	}

	public void start(DifferentialPilot pilot, boolean oneTime) {
		this.pilot = pilot;
		this.oneTime = oneTime;
		this.isRunning = true;
		this.curMv = 0;
		// System.out.println("Starting " + this.getName() +
		// " thread. Is already running? " + runThread.isAlive());
		this.runThread = new Thread(this);
		this.runThread.start();
	}

	@Override
	public void run() {
		// System.out.println("In thread of " + this.getName() + "\n");
		while (this.isRunning) {
			this.current = this.movements[this.curMv];
			this.current.run(this.pilot);

			if (++this.curMv == this.movements.length) {
				if (this.oneTime) {
					this.oneTime = false;
					return;
				} else
					this.curMv = 0;
			}
		}
	}

	public void stop() {
		this.isRunning = false;
		this.current.stop();
		synchronized (this.runThread) {
			try {
				this.runThread.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitStop() {
		this.stop();
		while (this.current.isRunning())
			;
	}

	public boolean isRunning() {
		return !(this.current == null || !this.current.isRunning());
	}

	public String getName() {
		return this.name;
	}
}
