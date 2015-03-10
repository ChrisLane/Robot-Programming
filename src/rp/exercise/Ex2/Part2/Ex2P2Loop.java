package rp.exercise.Ex2.Part2;

import rp.sensor.InfraredSideSensor;

import rp.GeoffBot;

import lejos.nxt.Button;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P2Loop {
	public void run() {
		TouchSensor ts = new TouchSensor(GeoffBot.getTouchPort());
		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

		pilot.forward();
		while (!Button.ESCAPE.isDown())
			if (ts.isPressed()) {
				pilot.stop();
				pilot.travel(-InfraredSideSensor.TARGETDISTANCE);
				pilot.rotate(90);
				pilot.forward();
			}
	}

	public static void main(String[] args) {
		Ex2P2Loop program = new Ex2P2Loop();
		program.run();
	}
}