package rp.Exercise.Ex3.Part1;

import rp.GeoffBot;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1BSimple {

	final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

	public void run() {
		final LightSensor lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		GeoffBot.getLightSensorLeftPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				LCD.clear(0);
				LCD.drawString(Integer.toString(lsLeft.getLightValue()), 0, 0);
				if (lsLeft.getLightValue() < 52)
					pilot.steer(200, -1, true);
				else
					pilot.forward();
			}
		});

		final LightSensor lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);
		GeoffBot.getLightSensorRightPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				LCD.clear(1);
				LCD.drawString(Integer.toString(lsRight.getLightValue()), 0, 1);
				if (lsRight.getLightValue() < 45)
					pilot.steer(200, 1, true);
				else
					pilot.forward();
			}
		});

		pilot.forward();
		while (pilot.isMoving())
			Thread.yield();
	}

	public static void main(String[] args) {
		Ex3P1B program = new Ex3P1B();
		program.run();
	}
}
