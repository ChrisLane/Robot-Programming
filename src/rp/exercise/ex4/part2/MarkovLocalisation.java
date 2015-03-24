package rp.exercise.ex4.part2;

import lejos.geom.Point;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;
import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.simulation.SimulatedRobot;
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.robotics.visualisation.KillMeNow;

import javax.swing.*;

public class MarkovLocalisation {

	// Maps
	private final LineMap m_lineMap;
	private final IGridMap m_gridMap;

	// Probability distribution over the position of a robot on the given
	// grid map
	private GridPositionDistribution m_distribution;

	// Robot to drive around
	private final SimulatedRobot m_robot;

	// Visualisation
	private GridPositionDistributionVisualisation m_mapVis;
	private final float m_translationAmount;

	public MarkovLocalisation(SimulatedRobot _robot, LineMap _lineMap, GridMap _gridMap, float _translationAmount) {

		m_robot = _robot;
		m_lineMap = _lineMap;
		m_gridMap = _gridMap;
		m_distribution = new GridPositionDistribution(m_gridMap);
		m_translationAmount = _translationAmount;
	}

	/**
	 * Optionally run the visualisation of the robot and localisation process. This is not necessary to run the localisation and could be removed once on the real robot.
	 */
	public void visualise() {
		JFrame frame = new JFrame("Map Viewer");
		frame.addWindowListener(new KillMeNow());

		// visualise the distribution on top of a line map
		m_mapVis = new GridPositionDistributionVisualisation(m_distribution, m_lineMap, 2, true);

		// Visualise the robot
		m_mapVis.addRobot(m_robot);

		frame.add(m_mapVis);
		frame.pack();
		frame.setSize(820, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/***
	 * Move the robot and update the distribution with the action model
	 *
	 * @param distance
	 * @param _heading
	 * @param _sensorModel
	 */
	private void move(float distance, Heading _heading, ActionModel _actionModel, SensorModel _sensorModel) {
		// move robot
		//if (m_gridMap.isValidGridPosition((int) m_robot.getPose().getX(), (int) m_robot.getPose().getY()))
			m_robot.translate(m_translationAmount);

		// now update estimate of position using the action model
		m_distribution = _actionModel.updateAfterMove(m_distribution, _heading);

		// if visualising, update the shown distribution
		if (m_mapVis != null)
			m_mapVis.setDistribution(m_distribution);

		// A short delay so we can see what's going on
		Delay.msDelay(500);

		m_distribution = _sensorModel.updateAfterSensing(m_distribution, _heading, m_robot.getRangeValues());

		// if visualising, update the shown distribution
		if (m_mapVis != null)
			m_mapVis.setDistribution(m_distribution);

		// A short delay so we can see what's going on
		Delay.msDelay(500);
	}

	public void run() {

		ActionModel actionModel = new PerfectActionModel();
		SensorModel sensorModel = new PerfectSensorModel();

		int horizontal = 5;
		int vertical = 1;

		// Assuming all the moves go in this direction. This will not work once
		// the robot turns...
		Heading movementHeading;
		int moves;

		while (true) {

			movementHeading = Heading.RIGHT;
			moves = horizontal;
			for (int i = 0; i < moves; i++)
				move(m_translationAmount, movementHeading, actionModel, sensorModel);
			m_robot.rotate(90);

			movementHeading = Heading.UP;
			moves = vertical;
			for (int i = 0; i < moves; i++)
				move(m_translationAmount, movementHeading, actionModel, sensorModel);
			m_robot.rotate(90);

			movementHeading = Heading.LEFT;
			moves = horizontal;
			for (int i = 0; i < moves; i++)
				move(m_translationAmount, movementHeading, actionModel, sensorModel);
			m_robot.rotate(90);

			movementHeading = Heading.DOWN;
			moves = vertical;
			for (int i = 0; i < moves; i++)
				move(m_translationAmount, movementHeading, actionModel, sensorModel);
			m_robot.rotate(90);


		}
	}

	public static void main(String[] args) {
		RPLineMap lineMap = MapUtils.create2015Map1();
		GridMap gridMap = new GridMap(12, 8, 15, 15, 30, lineMap);

		// the starting position of the robot for the simulation. This is not
		// known in the action model or position distribution
		int startGridX = 10;
		int startGridY = 1;

		// this converts the grid position into the underlying continuous
		// coordinate frame
		Point startPoint = gridMap.getCoordinatesOfGridPosition(startGridX, startGridY);

		// starting heading
		float startTheta = Heading.RIGHT.toDegrees();

		Pose startPose = new Pose(startPoint.x, startPoint.y, startTheta);

		// This creates a simulated robot with single, forward pointing distance
		// sensor with similar properties to the Lego ultrasonic sensor but
		// without the noise
		SimulatedRobot robot = SimulatedRobot.createSingleNoiseFreeSensorRobot(startPose, lineMap);

		// This does the same as above but adds noise to the range readings
		// SimulatedRobot robot = SimulatedRobot.createSingleSensorRobot(
		// startPose, lineMap);

		MarkovLocalisation ml = new MarkovLocalisation(robot, lineMap, gridMap, 30);
		ml.visualise();
		ml.run();

	}
}
