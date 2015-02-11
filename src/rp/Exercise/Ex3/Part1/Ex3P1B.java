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

	public Ex3P1B() {
		this.lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, GeoffBot.LSThreshold);
		this.lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, GeoffBot.LSThreshold);

		GeoffBot.calibrateLeftLS(this.lsLeft);
		GeoffBot.calibrateRightLS(this.lsRight);
	}

	@Override
	public void run() {
		this.lsLeft.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				LCD.drawString("Left:  " + Integer.toString(lightValue) + onLine, 0, LCD.DISPLAY_CHAR_DEPTH - 2);
				if (onLine)
					Ex3P1B.this.pilot.steer(-180, 7, true);
				else
					Ex3P1B.this.pilot.forward();

				System.out.println("Left:\t" + lightValue);
			}
		});

		this.lsRight.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				LCD.drawString("Right: " + Integer.toString(lightValue) + onLine, 0, LCD.DISPLAY_CHAR_DEPTH - 1);
				if (onLine)
					Ex3P1B.this.pilot.steer(180, 7, true);
				else
					Ex3P1B.this.pilot.forward();
				System.out.println("Right:\t" + lightValue);
			}
		});

		this.pilot.forward();
		while (this.isRunning)
			Thread.yield();
	}

	public static void main(String[] args) {
		// GeoffBot.connectRemote();
		final Ex3P1B program = new Ex3P1B();
		program.run();
	}
}