package rp.Exercise.Ex3.Part1;

import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;
import rp.Sensor.BlackLineSensor;
import rp.Util.LineFollower;
import rp.Util.RunSystem;

public class Ex3P1B extends RunSystem {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final BlackLineSensor lsLeft, lsRight;
	private final byte lightThreshold = 75;

	public Ex3P1B() {
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		new LineFollower(pilot, lsLeft, lsRight, 200, 10, false);
	}

	@Override
	public void run() {
		pilot.forward();
		while (isRunning)
			Thread.yield();
	}

	public static void main(String[] args) {
		final Ex3P1B program = new Ex3P1B();
		program.run();
	}
}