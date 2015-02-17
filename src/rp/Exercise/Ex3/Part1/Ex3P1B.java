package rp.Exercise.Ex3.Part1;

import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;

public class Ex3P1B extends RunSystem {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final BlackLineSensor lsLeft, lsRight;
	private final byte darkThreshold = 75;

	public Ex3P1B() {
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, darkThreshold);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, darkThreshold);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);
	}

	@Override
	public void run() {
		lsLeft.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				LCD.clear(0);
				LCD.drawString(Integer.toString(lightValue), 0, 0);

				if (onLine)
					pilot.steer(200, -1, true);
				else
					pilot.forward();
			}
		});

		lsRight.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				LCD.clear(1);
				LCD.drawString(Integer.toString(lightValue), 0, 1);

				if (onLine)
					pilot.steer(200, 1, true);
				else
					pilot.forward();
			}
		});

		pilot.forward();
		while (isRunning)
			Thread.yield();
	}

	public static void main(String[] args) {
		final Ex3P1B program = new Ex3P1B();
		program.run();
	}
}