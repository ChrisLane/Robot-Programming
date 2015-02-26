package rp.Exercise.Ex3.Part1;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;

import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1B extends RunSystem implements LineListener {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final BlackLineSensor lsLeft, lsRight;
	private boolean leftOnline, rightOnline;

	public Ex3P1B() {
		byte lightThreshold = 75;
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		lsLeft.addChangeListener(this);
		lsRight.addChangeListener(this);
	}

	public static void main(String[] args) {
		final Ex3P1B program = new Ex3P1B();
		program.run();
	}

	@Override
	public void run() {
		pilot.forward();

		while (isRunning) {
			if (leftOnline)
				pilot.steer(200, -10, false);
			else if (rightOnline)
				pilot.steer(200, 10, false);
		}
	}

	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnline = onLine;
		else
			rightOnline = onLine;
	}
}