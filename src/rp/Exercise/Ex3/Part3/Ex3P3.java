package rp.Exercise.Ex3.Part3;

import rp.GeoffBot;
import rp.Util.RunSystem;

import lejos.nxt.addon.NXTCam;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

import java.awt.geom.Rectangle2D;

public class Ex3P3 extends RunSystem {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final NXTCam cam;

	private final double leftT = 136; // right and left thresholds before turning either side
	private final double rightT = 50;    // range of x is (15 - 171) therefore middle is 93 + 43 on each side
	private double radius;
	private final double constant = 0.7;
	private double speed = 10;

	public Ex3P3() {
		cam = new NXTCam(GeoffBot.getCameraPort());
		cam.setTrackingMode(NXTCam.COLOR);
		cam.sortBy(NXTCam.SIZE);
		cam.enableTracking(true);
	}

	public static void main(String[] args) {
		Ex3P3 program = new Ex3P3();
		program.run();
	}

	@Override
	public void run() {

		while (isRunning) {
			Rectangle2D rec = cam.getRectangle(0);

			double setPoint = 40;    // this is the width of the rectangle containing the largest tracked object
			double width = rec.getWidth();
			double error = (width <= setPoint ? setPoint - width : -(width - setPoint));
			speed += (0.001 * error);
			pilot.setTravelSpeed(speed);

			if (width >= 55 || width <= 17) {        // if too close or lost target stop and reset speed to default
				pilot.stop();
				pilot.setTravelSpeed(10);

				if (width >= 65)
					pilot.backward();
			}

			pilot.forward();

			if (rec.getCenterX() < rightT) {
				radius = constant * (rightT - rec.getCenterX());
				pilot.arcForward(-radius);
				Delay.msDelay(30);
			}
			else if (rec.getCenterX() > leftT) {
				radius = constant * (rec.getCenterX() - leftT);
				pilot.arcForward(radius);
				Delay.msDelay(30);
			}
			System.out.println(rec.getWidth());
			// Delay.msDelay(30);
		}
	}
}
