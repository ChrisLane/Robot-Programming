package rp.Ex2.Part1.Paths;

import lejos.robotics.navigation.DifferentialPilot;

public abstract class Movement {
	// protected ArrayList<MoveListener> listeners;
	protected DifferentialPilot pilot;
	private boolean isRunning;

	public Movement(DifferentialPilot pilot) {
		this.pilot = pilot;
		this.isRunning = true;
		// pilot.addMoveListener(this);
		// this.listeners = new ArrayList<MoveListener>(1);
	}

	public void run() {
		this.startMoving();
		while (this.isRunning());
	}
	protected abstract void startMoving();
	public void stop() {
		pilot.stop();
		this.isRunning = false;
	}
	public boolean isRunning() {
		return this.isRunning && pilot.isMoving();
	}

	/*
	 * public Movement addMoveListener(MoveListener listener) { if (!listeners.contains(listener))
	 * this.listeners.add(listener); return this; }
	 * @Override public void moveStarted(Move e, MoveProvider mp) { for (MoveListener l : this.listeners)
	 * l.moveStarted(e, mp); }
	 * @Override public void moveStopped(Move e, MoveProvider mp) { for (MoveListener l : this.listeners)
	 * l.moveStopped(e, mp); }
	 */
}