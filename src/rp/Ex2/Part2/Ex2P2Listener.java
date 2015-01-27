package rp.Ex2.Part2;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;

public class Ex2P2Listener {
	public static void main(String[] args) {
		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

		GeoffBot.getTouchPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort aSource, int oldVal, int newVal) {
				if (oldVal - newVal < 60)
					return;
				pilot.stop();
				pilot.travel(-20);
				pilot.rotate(90);
				pilot.forward();
			}
		});

		pilot.forward();
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
	}
}