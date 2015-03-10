package rp.exercise.Ex2.Part1.Paths;

import lejos.robotics.navigation.DifferentialPilot;

public abstract class Movement {
	protected DifferentialPilot pilot;
	private boolean isRunning;

	public Movement() {
		isRunning = true;
	}

	public void run(DifferentialPilot pilot) {
		this.pilot = pilot;
		startMoving(pilot);
		while (isRunning());
	}

	protected abstract void startMoving(DifferentialPilot pilot);

	public void stop() {
		pilot.stop();
		isRunning = false;
	}

	public boolean isRunning() {
		return isRunning || pilot.isMoving();
	}
}