package rp.Ex3.Part1;

import rp.GeoffBot;
import rp.RunUtil;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1B extends RunUtil{

	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

	@Override
	public void run() {
		final LightSensor lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.getLightSensorLeftPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				if (lsLeft.getLightValue() < GeoffBot.LSThreshold)
					Ex3P1B.this.pilot.steer(150, -10, true);
				else
					Ex3P1B.this.pilot.forward();
			}
		});

		final LightSensor lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);
		GeoffBot.calibrateRightLS(lsRight);
		GeoffBot.getLightSensorRightPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				if (lsRight.getLightValue() < GeoffBot.LSThreshold)
					Ex3P1B.this.pilot.steer(150, 10, true);
				else
					Ex3P1B.this.pilot.forward();
			}
		});

		this.pilot.forward();
		while (isRunning)
			Thread.yield();
	}

	public static void main(String[] args) {
		Ex3P1B program = new Ex3P1B();
		program.run();
	}
}