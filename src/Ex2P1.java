import Paths.BackForthPath;
import Paths.CirclePath;
import Paths.Figure8Path;
import Paths.MovementPath;
import Paths.SquarePath;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P1 {
	public static void main(String[] args) {
		GeoffBot.connectRemote();

		LCD.clear();
		LCD.drawString("   Hello World\n\n\n  Press Enter to\n    continue...", 0, 2);

		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
		pilot.setTravelSpeed(20);

		MovementPath[] paths = {
				new SquarePath(pilot), new Figure8Path(pilot), new BackForthPath(pilot), new CirclePath(pilot)
		};

		Button.ENTER.addButtonListener(new ButtonListener() {
			private MovementPath path = paths[0];
			private int index = -1;

			@Override
			public void buttonPressed(Button b) {
				// Don't fire on first
				if (index < 0) {
					index++;
					return;
				}

				LCD.drawString("Stopping...", 3, 5);

				// Stop the current path if it is running and wait for termination
				if (path.isRunning())
					path.waitStop();

				// Loop path index
				if (++index == paths.length)
					index = 0;
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

		// This doesn't work?!
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
}
