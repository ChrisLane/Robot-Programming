package rp.util.gui;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class ProgressBar {
	private final byte X = 5;
	private final byte Y = 35;
	private final byte WIDTH = LCD.SCREEN_WIDTH - 5;
	private final byte HEIGHT = 13;
	private final double PERCENT = WIDTH / 100.0;

	private String label;
	private byte progress;

	public ProgressBar(String label, int initalValue) {
		this.label = label;
		progress = (byte) initalValue;

		redraw();
	}

	public void setLabel(String label) {
		this.label = label;
		drawLabel();
	}
	public void setProgress(int progress) {
		byte oldVal = this.progress;
		this.progress = (byte) progress;
		drawBar((int) (oldVal * PERCENT), (int) (progress * PERCENT));
	}
	private void redraw() {
		drawLabel();
		drawOutline();
		int val = (int) (progress * PERCENT);
		drawBar(0, val);
	}
	private void drawLabel() {
		LCD.drawString(label, (LCD.DISPLAY_CHAR_WIDTH - label.length()) + 1 / 2, 2);
	}
	private void drawBar(int from, int to) {
		for (int y = Y + 1; y < Y + HEIGHT; y++)
			for (int x = X + from; x <= X + to; x++)
				LCD.setPixel(x, y, 1);
	}
	private void drawOutline() {
		// Draw box
		for (int i = X + 1; i < WIDTH; i++) {
			LCD.setPixel(i, Y, 1);
			LCD.setPixel(i, Y + HEIGHT, 1);
		}
		for (int i = Y + 1; i < Y + HEIGHT; i++) {
			LCD.setPixel(X, i, 1);
			LCD.setPixel(WIDTH, i, 1);
		}
	}

	public static void main(String[] args) {
		new ProgressBar("Progress Test!", (byte) 82);
		Button.waitForAnyPress();
	}
}
