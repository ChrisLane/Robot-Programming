package rp.exercise.ex2.part3;

import rp.GeoffBot;
import rp.listener.ArcRadiusChangeListener;
import rp.listener.BumperHitListener;
import rp.listener.WallApproachListener;
import rp.listener.WallFalloffListener;
import rp.sensor.BumperSensor;
import rp.sensor.InfraredSideSensor;
import rp.sensor.UltrasonicFrontSensor;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P3 implements ArcRadiusChangeListener, BumperHitListener, WallApproachListener, WallFalloffListener {
	private final DifferentialPilot pilot;

	private final UltrasonicSensor usSensorFront;
	private final OpticalDistanceSensor irSensorSide;
	private double arcRadius;
	private boolean bumperHit, wallFalloff;

	public Ex2P3(SensorPort touchPort, SensorPort irPortSide, SensorPort usPortFront) {
		new TouchSensor(touchPort);
		irSensorSide = new OpticalDistanceSensor(irPortSide);
		usSensorFront = new UltrasonicSensor(usPortFront);

		new BumperSensor(touchPort, this);
		new InfraredSideSensor(irSensorSide, this, this, this);
		new UltrasonicFrontSensor(usSensorFront, this);

		pilot = GeoffBot.getDifferentialPilot();
	}

	private void run() {
		pilot.forward();
		while (!Button.ESCAPE.isDown()) {
			if (wallFalloff) {
				while ((irSensorSide.getDistance() / 10) + 4 >= InfraredSideSensor.TARGETDISTANCE * 1.2 && wallFalloff && !bumperHit)
					pilot.arcForward(-InfraredSideSensor.TARGETDISTANCE);
				wallFalloff = false;
			}
			if (bumperHit) {
				pilot.stop();
				pilot.travel(-InfraredSideSensor.TARGETDISTANCE / 2);
				pilot.rotate(90);
				pilot.forward();
				bumperHit = false;
			}
		}
	}

	@Override
	public void onRadiusChanged(double arcRadius) {
		this.arcRadius = arcRadius;
		if (!bumperHit && !wallFalloff)
			pilot.arcForward(this.arcRadius);
	}

	@Override
	public void wallApproaching(double distance) {
		if (distance < 15)
			bumperHit = true;
	}

	@Override
	public void onWallFalloff(double wallDistance) {
		wallFalloff = true;
	}

	@Override
	public void onBumperHit() {
		bumperHit = true;
	}

	public static void main(String[] args) {
		// GeoffBot.connectRemote();
		final Ex2P3 program = new Ex2P3(GeoffBot.getTouchPort(), GeoffBot.getInfraredPort(), GeoffBot.getFrontUltrasonicPort());
		program.run();
	}
}