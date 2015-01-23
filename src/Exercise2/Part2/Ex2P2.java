package Exercise2.Part2;

import Exercise2.JeffRobot;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P2 {
	public static void main(String[] args) {
		TouchSensor ts = new TouchSensor(SensorPort.S4);
		DifferentialPilot pilot = JeffRobot.getDifferentialPilot();

		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				System.exit(0);
			}
		});

		pilot.forward();
		while (true) {
			while (!ts.isPressed()) {
			}
			pilot.travel(-20);
			pilot.rotate(90);
			pilot.forward();
		}
	}
}
