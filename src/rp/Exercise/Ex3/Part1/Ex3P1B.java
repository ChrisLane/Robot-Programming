package rp.Exercise.Ex3.Part1;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;

import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1B extends RunSystem {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final BlackLineSensor lsLeft, lsRight;
	private final byte lightThreshold = 75;

	public Ex3P1B() {
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		lsLeft.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				if (onLine)
					pilot.steer(200, -10, true);
				else
					pilot.forward();
			}
		});
		lsRight.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				if (onLine)
					pilot.steer(200, 10, true);
				else
					pilot.forward();
			}
		});
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