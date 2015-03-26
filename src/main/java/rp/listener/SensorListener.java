package rp.listener;

public abstract class SensorListener implements Runnable {
	private Thread pollThread;
	private boolean isRunning;

	@Override
	public void run() {
		while (isRunning)
			pollTick();
	}

	protected abstract void pollTick();

	protected void startPolling() {
		pollThread = new Thread(this);
		pollThread.setDaemon(true);
		isRunning = true;
		pollThread.start();
	}
	public void stop() throws InterruptedException {
		isRunning = false;
		pollThread.join();
	}
}
