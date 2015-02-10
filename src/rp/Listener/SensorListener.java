package rp.Listener;

public abstract class SensorListener implements Runnable {
	private Thread pollThread;
	protected boolean isRunning;

	@Override
	public void run() {
		while (this.isRunning)
			this.pollTick();
	}

	protected abstract void pollTick();

	protected void startPolling() {
		this.pollThread = new Thread(this);
		this.pollThread.setDaemon(true);
		this.isRunning = true;
		this.pollThread.start();
	}
	public void stop() throws InterruptedException {
		this.isRunning = false;
		this.pollThread.join();
	}
}
