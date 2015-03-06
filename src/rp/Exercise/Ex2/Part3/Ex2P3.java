package rp.Exercise.Ex2.Part3;

import rp.GeoffBot;
import rp.Listener.ArcRadiusChangeListener;
import rp.Listener.BumperHitListener;
import rp.Listener.WallApproachListener;
import rp.Listener.WallFalloffListener;
import rp.Sensor.BumperSensor;
import rp.Sensor.InfraredSideSensor;
import rp.Sensor.UltrasonicFrontSensor;

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
		final Ex2P3 program = new Ex2P3(GeoffBot.getTouchPort(), GeoffBot.getSideInfraredPort(), GeoffBot.getFrontUltrasonicPort());
		program.run();
	}
}