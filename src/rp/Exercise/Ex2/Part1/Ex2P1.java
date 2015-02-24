package rp.Exercise.Ex2.Part1;

import rp.Exercise.Ex2.Part1.Paths.BackForthPath;
import rp.Exercise.Ex2.Part1.Paths.CirclePath;
import rp.Exercise.Ex2.Part1.Paths.MovementPath;
import rp.Exercise.Ex2.Part1.Paths.SquarePath;
import rp.Exercise.Ex2.Part1.Paths.TrianglePath;
import rp.GeoffBot;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P1 {
	public void run() {
		// GeoffBot.connectRemote();

		// Draw "Hello World" string to display
		LCD.clear();
		LCD.drawString("   Hello World\n\n\n  Press Enter to\n    continue...", 0, 2);

		final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

		final MovementPath[] paths = {
				new SquarePath(), new BackForthPath(), new CirclePath(), new TrianglePath()
		};

		Button.ENTER.addButtonListener(new ButtonListener() {
			private MovementPath path = paths[0];
			private int index = -1;

			@Override
			public void buttonPressed(Button b) {
				/*
				 * / Don't fire on first if (index < 0) { index++; return; }
				 */

				// Stop the current path if it is running and wait for termination
				if (this.path.isRunning()) {
					LCD.drawString("Stopping...", 3, 5);
					this.path.waitStop();
				}

				// Loop path index
				if (++this.index == paths.length)
					System.exit(0);
				// index = 0;

				this.path = paths[this.index];
			}

			@Override
			public void buttonReleased(Button b) {
				// Draw path name in middle of screen
				LCD.clear();
				LCD.drawString(this.path.getName(), (int) Math.ceil((LCD.DISPLAY_CHAR_WIDTH - this.path.getName().length()) / 2.0), 2);
				this.path.start(pilot);
			}
		});

		// Exit program when Escape button is pressed
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.exit(1);
			}

			@Override
			public void buttonReleased(Button b) {
			}
		});

		while (!Button.ESCAPE.isDown());
	}

	public static void main(String[] args) {
		final Ex2P1 program = new Ex2P1();
		program.run();
	}
}
