package rp.Exercise.Ex3.Part1;

import rp.GeoffBot;
import rp.Util.RunSystem;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1BSimple extends RunSystem {

	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final byte lightThreshold = 75;

	public void run() {
		pilot.setTravelSpeed(20);

		final LightSensor lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.getLightSensorLeftPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				LCD.clear(0);
				LCD.drawString(Integer.toString(lsLeft.getLightValue()), 0, 0);

				if (lsLeft.getLightValue() < lightThreshold)
					pilot.steer(200, -10);
				else
					pilot.forward();
			}
		});

		final LightSensor lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);
		GeoffBot.calibrateRightLS(lsRight);
		GeoffBot.getLightSensorRightPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				LCD.clear(1);
				LCD.drawString(Integer.toString(lsRight.getLightValue()), 0, 1);

				if (lsRight.getLightValue() < lightThreshold)
					pilot.steer(200, 10);
				else
					pilot.forward();
			}
		});

		pilot.forward();
		while (isRunning)
			Thread.yield();
	}

	public static void main(String[] args) {
		Ex3P1BSimple program = new Ex3P1BSimple();
		program.run();
	}
}
