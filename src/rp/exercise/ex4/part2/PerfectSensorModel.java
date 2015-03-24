package rp.exercise.ex4.part2;

import lejos.robotics.RangeReadings;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.mapping.Heading;

public class PerfectSensorModel implements SensorModel {
    @Override
    public GridPositionDistribution updateAfterSensing(GridPositionDistribution _dist, Heading _heading, RangeReadings _readings) {

        for (int x = 0; x < _dist.getGridWidth(); x++) {
            for (int y = 0; y < _dist.getGridHeight(); y++) {

                if (_dist.isValidGridPosition(x, y)) {
                    float obstacleDistance = _dist.getGridMap().rangeToObstacleFromGridPosition(x, y, (float) -_heading.toDegrees());

                    if (obstacleDistance == _readings.getRange(0))
                        _dist.setProbability(x, y, 1);
                    else if (_readings.getRange(0) < 255) {
                        _dist.setProbability(x, y, 0);
                        System.out.println("X: " + x + " " + "Y: " + y + " " + "Heading: " + _heading);
                        System.out.println("obstacle distance " + obstacleDistance);
                        System.out.println("reading " + _readings.getRange(0));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        _dist.normalise();

        return _dist;
    }
}