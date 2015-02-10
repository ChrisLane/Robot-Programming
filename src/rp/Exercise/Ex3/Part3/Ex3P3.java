package rp.Exercise.Ex3.Part3;

import java.awt.geom.Rectangle2D;

import rp.GeoffBot;
import rp.RunSystem;

import lejos.nxt.addon.NXTCam;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class Ex3P3 extends RunSystem{
	//double radius = 30;
	private double leftT = 118; 		//right and left thresholds before turning either side
	private double rightT = 50;
	private double radius;
	public void run(){
		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
		NXTCam cam = new NXTCam(GeoffBot.getCameraPort());
		cam.setTrackingMode(NXTCam.COLOR);
		cam.sortBy(NXTCam.SIZE);
		cam.enableTracking(true);
		
		while (isRunning){
			Rectangle2D rec = cam.getRectangle(0);
			pilot.forward();
			if(rec.getX() < rightT){
				radius = 0.7 * (rightT - rec.getX());
				pilot.arcForward(-radius);
				Delay.msDelay(300);
			}
			else if(rec.getX() > leftT){
				radius = 0.7 * (rec.getX() - leftT);
				pilot.arcForward(radius);
				Delay.msDelay(300);
			}
			else
				pilot.forward();
				System.out.println(rec.getWidth());
			//System.out.println(numberOfObjects);
			//Delay.msDelay(300);
			
		}
	}

	public static void main(String[] args) {
		Ex3P3 program = new Ex3P3();
		program.run();

	}
}
