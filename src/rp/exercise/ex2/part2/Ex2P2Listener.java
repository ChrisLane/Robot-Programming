package rp.exercise.ex2.part2;

import rp.GeoffBot;
import rp.exercise.ex2.part1.paths.MovementPath;
import rp.exercise.ex2.part1.paths.ReverseAndTurn;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

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
				while (path.isRunning());
				pilot.forward();
			}
		});

		pilot.forward();
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
	}

	public static void main(String[] args) {
		Ex2P2Listener program = new Ex2P2Listener();
		program.run();
	}
}