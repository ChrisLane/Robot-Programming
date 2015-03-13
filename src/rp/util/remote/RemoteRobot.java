package rp.util.remote;

import rp.robotics.LocalisedRangeScanner;

import lejos.robotics.RangeFinder;
import lejos.robotics.RangeReadings;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;

/**
 * A simple simulated robot for testing.
 *
 * @author nah
 */
public class RemoteRobot implements LocalisedRangeScanner {

	private Pose m_pose;
	private RangeReadings m_readings;
	private final float[] m_readingAngles;

	/**
	 * @param _pose The initial pose of the robot
	 * @param _map The map on which the robot exists
	 * @param _readingAngles The angles to take readings from, relative to 0 for this robot
	 * @param _sensorMaxRange Minimum sensor reading to return
	 * @param _sensorMinRange Maximum sensor reading to return
	 * @param _sensorOfOutRange The value to return if the sensor would fall outside the max or min values.
	 */
	public RemoteRobot(Pose _pose, LineMap _map, float[] _readingAngles) {
		m_pose = _pose;
		m_readings = new RangeReadings(_readingAngles.length);
		m_readingAngles = _readingAngles;
	}

	@Override
	public Pose getPose() {
		return m_pose;
	}

	@Override
	public void setPose(Pose _pose) {
		m_pose = _pose;
	}

	public void setRange(int sensorIndex, float range) {
		if (sensorIndex >= m_readingAngles.length)
			throw new IllegalArgumentException("There is no range sensor at index " + sensorIndex);
		m_readings.setRange(sensorIndex, m_readingAngles[sensorIndex], range);
	}

	@Override
	public RangeReadings getRangeValues() {
		return m_readings;
	}

	@Override
	public void setAngles(float[] _angles) {
		// does nothing as these are fixed in constructor
	}

	@Override
	public RangeFinder getRangeFinder() {
		// returns null as there is no actual reading
		return null;
	}

	/***
	 * Rotate the robot by this angle.
	 *
	 * @param _angle
	 */
	public void rotate(int _angle) {
		m_pose.rotateUpdate(_angle);
	}

	/***
	 * Move the robot forward by this amount. Currently does not collision checking.
	 *
	 * @param _junctionSeparation
	 */
	public void translate(float _distance) {
		m_pose.moveUpdate(_distance);
	}
}