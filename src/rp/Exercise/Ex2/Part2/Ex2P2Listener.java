package rp.Exercise.Ex2.Part2;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;
import rp.GeoffBot;
import rp.Exercise.Ex2.Part1.Paths.MovementPath;
import rp.Exercise.Ex2.Part1.Paths.ReverseAndTurn;

public class Ex2P2Listener {
	public void run() {
		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
		MovementPath path = new ReverseAndTurn();

		GeoffBot.getTouchPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort aSource, int oldVal, int newVal) {
				if (oldVal - newVal < 60)
					return;
				if (path.isRunning())
					path.waitStop();
				path.start(pilot, true);
				while (path.isRunning())
					;
				pilot.forward();
			}
		});

		pilot.forward();
		while (Button.waitForAnyPress() != Button.ID_ESCAPE)
			;
	}

	public static void main(String[] args) {
		Ex2P2Listener program = new Ex2P2Listener();
		program.run();
	}
}