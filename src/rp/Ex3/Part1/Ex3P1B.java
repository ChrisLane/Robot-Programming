package rp.Ex3.Part1;

import rp.GeoffBot;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1B {

	final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

	public void run() {
		LightSensor lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		GeoffBot.getLightSensorLeftPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				if (lsLeft.getLightValue() < 45)
					pilot.arcForward(-10);
				else
					pilot.forward();
			}
		});

		LightSensor lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);
		GeoffBot.getLightSensorRightPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				if (lsRight.getLightValue() < 45)
					pilot.arcForward(10);
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
