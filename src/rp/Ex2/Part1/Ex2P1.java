package rp.Ex2.Part1;

import rp.Ex2.Part1.Paths.BackForthPath;
import rp.Ex2.Part1.Paths.CirclePath;
import rp.Ex2.Part1.Paths.Figure8Path;
import rp.Ex2.Part1.Paths.MovementPath;
import rp.Ex2.Part1.Paths.SquarePath;
import rp.Ex2.Part1.Paths.TrianglePath;
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

		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

		// Paths to be used in next movement
		MovementPath[] paths = {
				new SquarePath(pilot), new Figure8Path(pilot), new BackForthPath(pilot), new CirclePath(pilot), new TrianglePath(pilot)
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
				if (path.isRunning()) {
					LCD.drawString("Stopping...", 3, 5);
					path.waitStop();
				}

				// Loop path index
				if (++index == paths.length)
					System.exit(0);
				// index = 0;

				path = paths[index];
			}

			@Override
			public void buttonReleased(Button b) {
				// Draw path name in middle of screen
				LCD.clear();
				LCD.drawString(path.getName(), (int) Math.ceil((LCD.DISPLAY_CHAR_WIDTH - path.getName().length()) / 2.0), 2);
				path.start();
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

		while (!Button.ESCAPE.isDown()) ;
	}

	public static void main(String[] args) {
		Ex2P1 program = new Ex2P1();
		program.run();
	}
}
