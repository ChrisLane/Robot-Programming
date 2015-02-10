package rp;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public abstract class RunUtil implements Runnable {

	protected boolean isRunning = true;

	public RunUtil() {
		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button _b) {
				isRunning = false;
			}

			@Override
			public void buttonPressed(Button _b) {

			}
		});
	}
}
