package rp.Ex3.Part1;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;

public class Ex3P1B {

	public static void main(String[] args) {
		final Ex3P1B program = new Ex3P1B();
		program.run();
	}

	final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

	public void run() {
		final LightSensor lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		GeoffBot.getLightSensorLeftPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				if (lsLeft.getLightValue() < GeoffBot.leftLSThreshold)
					Ex3P1B.this.pilot.steer(200, -10);
				else
					Ex3P1B.this.pilot.forward();
			}
		});

		final LightSensor lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);
		GeoffBot.getLightSensorRightPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				if (lsRight.getLightValue() < GeoffBot.rightLSThreshold)
					Ex3P1B.this.pilot.steer(200, 10);
				else
					Ex3P1B.this.pilot.forward();
			}
		});

		this.pilot.forward();
		while (true)
			Thread.yield();
	}
}
