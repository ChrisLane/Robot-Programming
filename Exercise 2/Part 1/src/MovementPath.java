import lejos.robotics.navigation.DifferentialPilot;

public abstract class MovementPath implements Runnable {
	protected DifferentialPilot pilot;
	private Thread thread;

	public MovementPath(DifferentialPilot pilot) {
		this.pilot = pilot;
		this.thread = new Thread(this);
	}

	public abstract void run();

	public void start() {
		thread.start();
	}
	public void pause(long millis) throws InterruptedException {
		Thread.sleep(millis);
	}
	public void stop() {
		pilot.stop();
	}
}
