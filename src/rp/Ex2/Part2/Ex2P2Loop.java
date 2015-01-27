package rp.Ex2.Part2;

import lejos.nxt.Button;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;

public class Ex2P2Loop {
	public static void main(String[] args) {
		TouchSensor ts = new TouchSensor(GeoffBot.getTouchPort());
		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

		pilot.forward();
		while (!Button.ESCAPE.isDown()) {
			if (ts.isPressed()) {
				pilot.stop();
				pilot.travel(-20);
				pilot.rotate(90);
				pilot.forward();
			}
		}
	}
}