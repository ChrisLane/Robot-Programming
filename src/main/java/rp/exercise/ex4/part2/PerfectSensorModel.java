package rp.exercise.ex4.part2;

import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.mapping.Heading;

import lejos.robotics.RangeReadings;

public class PerfectSensorModel implements SensorModel {
	@Override
	public GridPositionDistribution updateAfterSensing(GridPositionDistribution dist, Heading heading, RangeReadings readings) {

		for (int x = 0; x < dist.getGridWidth(); x++) {
			for (int y = 0; y < dist.getGridHeight(); y++) {

				float obstacleDistance = dist.getGridMap().rangeToObstacleFromGridPosition(x, y, heading.toDegrees());

				if (dist.getProbability(x, y) > 0) {
					if (obstacleDistance == readings.getRange(0))
						dist.setProbability(x, y, 1);
					else if (readings.getRange(0) < 255)
						dist.setProbability(x, y, 0);
				}
			}
		}
		dist.normalise();

		return dist;
	}
}