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
		// JeffRobot.connectRemote();
		
		LCD.clear();
		LCD.drawString("   Hello World\n\n\n  Press Enter to\n    continue...", 0, 2);

		DifferentialPilot pilot = JeffRobot.getDifferentialPilot();
		pilot.setTravelSpeed(35);

		MovementPath[] paths = {
				new SquarePath(pilot), new Figure8Path(pilot), new BackForthPath(pilot), new CirclePath(pilot)
		};

		Button.ENTER.addButtonListener(new ButtonListener() {
			private MovementPath path = paths[0];
			private int index = -1;

			@Override
			public void buttonPressed(Button b) {
				LCD.clear();
				LCD.drawString("Stopping...", 3, 5);
				path.stop();
				if (++index == paths.length)
					index = 0;
				path = paths[index];
				LCD.clear();
			}

			@Override
			public void buttonReleased(Button b) {
				LCD.drawString(path.getName(), (int) Math.ceil((LCD.DISPLAY_CHAR_WIDTH - path.getName().length()) / 2.0), 2);
				path.start();
			}
		});

		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
	}
}
