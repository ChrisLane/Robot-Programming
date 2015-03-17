package rp.util;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public abstract class RunSystem implements Runnable, ButtonListener {
	protected boolean isRunning = true;

	public RunSystem() {
		Button.ESCAPE.addButtonListener(this);
	}
	@Override
	public void buttonReleased(Button _b) {
	}

	@Override
	public void buttonPressed(Button _b) {
		isRunning = false;
		System.exit(0);
	}
}
