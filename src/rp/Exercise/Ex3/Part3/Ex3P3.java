package rp.Exercise.Ex3.Part3;

import java.awt.geom.Rectangle2D;

import lejos.nxt.addon.NXTCam;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.GeoffBot;
import rp.Util.RunSystem;

public class Ex3P3 extends RunSystem {
	private DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private NXTCam cam;

	// double radius = 30;
	private double leftT = 118; // right and left thresholds before turning
								// either side
	private double rightT = 50;
	private double radius;

	public Ex3P3() {
		this.cam = new NXTCam(GeoffBot.getCameraPort());
		this.cam.setTrackingMode(NXTCam.COLOR);
		this.cam.sortBy(NXTCam.SIZE);
		this.cam.enableTracking(true);
	}

	@Override
	public void run() {

		while (this.isRunning) {
			Rectangle2D rec = this.cam.getRectangle(0);
			this.pilot.forward();
			if (rec.getX() < this.rightT) {
				this.radius = 0.7 * (this.rightT - rec.getX());
				this.pilot.arcForward(-this.radius);
				Delay.msDelay(30);
			} else if (rec.getX() > this.leftT) {
				this.radius = 0.7 * (rec.getX() - this.leftT);
				this.pilot.arcForward(this.radius);
				Delay.msDelay(30);
			} else {
				this.pilot.forward();
			}
			System.out.println(rec.getWidth());
			// System.out.println(numberOfObjects);
			// Delay.msDelay(300);
		}
	}

	public static void main(String[] args) {
		Ex3P3 program = new Ex3P3();
		program.run();
	}
}
