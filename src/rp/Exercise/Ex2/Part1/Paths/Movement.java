package rp.Exercise.Ex2.Part1.Paths;

import lejos.robotics.navigation.DifferentialPilot;

public abstract class Movement {
	protected DifferentialPilot pilot;
	private boolean isRunning;

	public Movement() {
		this.isRunning = true;
	}

	public void run(DifferentialPilot pilot) {
		this.pilot = pilot;
		this.startMoving(pilot);
		while (this.isRunning())
			;
	}

	protected abstract void startMoving(DifferentialPilot pilot);

	public void stop() {
		this.pilot.stop();
		this.isRunning = false;
	}

	public boolean isRunning() {
		return this.isRunning || this.pilot.isMoving();
	}
}