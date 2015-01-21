import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;

public abstract class MovementPath implements Runnable {
	private Thread thread;

	protected DifferentialPilot pilot;
	protected boolean isRunning = true;

	public MovementPath(DifferentialPilot pilot) {
		this.pilot = pilot;
		this.thread = new Thread(this);
	}

	public void run() {
		while (this.isRunning)
			this.path();
	}

	protected abstract void path();

	public void start() {
		thread.start();
	}

	public void pause(long millis) throws InterruptedException {
		Thread.sleep(millis);
	}

	public void stop() {
		System.out.println("Waiting for path to complete");
		this.isRunning = false;
		pilot.stop();		
		try {
			thread.join();
		} catch (InterruptedException e) {
			LCD.clear();
			e.printStackTrace();
		}
	}
}
