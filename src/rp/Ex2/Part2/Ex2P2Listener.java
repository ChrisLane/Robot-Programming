package rp.Ex2.Part2;

import rp.GeoffBot;
import rp.Ex2.Part1.Ex2P1;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P2Listener {
	public void run() {
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
		while (Button.waitForAnyPress() != Button.ID_ESCAPE) ;
	}
	
	public static void main(String[] args) {
		Ex2P2Listener program = new Ex2P2Listener();
		program.run();
	}
}