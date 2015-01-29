package rp.Ex2.Part3;

import lejos.nxt.LCD;
import lejos.nxt.addon.OpticalDistanceSensor;

public class InfraredSideListener extends OpticalDistanceListener {
	public static final double TARGETDISTANCE = 18.0;
	private final double THRESHOLD = 0;

	private final ArcRadiusChangeListener arcl;
	private final WallApproachListener wal;
	private final WallFalloffListener wfl;

	private double currentDistance;
	private double arcRadius;

	public InfraredSideListener(OpticalDistanceSensor sensor, ArcRadiusChangeListener arcl, WallApproachListener wal, WallFalloffListener wfl) {
		super(sensor, 0);
		this.arcl = arcl;
		this.wal = wal;
		this.wfl = wfl;
	}

	@Override
	public void stateChanged(double value, double oldValue) {
		this.currentDistance = value;

		LCD.clear();
		LCD.drawString(Double.toString(this.currentDistance), 0, 0);

		final double offset = TARGETDISTANCE - this.currentDistance;
		LCD.drawString(Double.toString(offset), 0, 1);
		if (-offset >= TARGETDISTANCE) {
			LCD.drawString("Wall falloff!", 0, 4);
			this.wfl.wallFalloff(this.currentDistance);
		}
		else {
			double radius = this.calcRadius(offset);
			radius = Math.abs(radius);
			if (this.currentDistance > TARGETDISTANCE) // Turn
				radius *= -1;

			final double oldAR = this.arcRadius;
			this.arcRadius = radius;

			if (Math.abs(oldAR - this.arcRadius) > this.THRESHOLD)
				this.arcl.arcRadiusChanged(this.arcRadius);
		}
	}

	private double calcRadius(double offset) {
		// Calculated with online graphing app
		// http://fooplot.com/plot/far84l18y1
		// x-axis: offset
		// y-axis: turn radius
		return (400 / ((0.65 * offset) - 1)) + TARGETDISTANCE - 8.2;
	}
	public double getArcRadius() {
		return this.arcRadius;
	}

}
