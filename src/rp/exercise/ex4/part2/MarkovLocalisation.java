package rp.exercise.ex4.part2;

import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.simulation.SimulatedRobot;
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.robotics.visualisation.KillMeNow;

import lejos.geom.Point;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MarkovLocalisation {

	// Maps
	private final LineMap lineMap;
	// Robot to drive around
	private final SimulatedRobot robot;
	private final float m_translationAmount;
	// Probability distribution over the position of a robot on the given
	// grid map
	private GridPositionDistribution distribution;
	// Visualisation
	private GridPositionDistributionVisualisation m_mapVis;

	public MarkovLocalisation(SimulatedRobot robot, LineMap lineMap, GridMap gridMap, float translationAmount) {
		this.robot = robot;
		this.lineMap = lineMap;
		distribution = new GridPositionDistribution(gridMap);
		m_translationAmount = translationAmount;
	}

	public static void main(String[] args) {
		RPLineMap lineMap = MapUtils.create2015Map1();
		GridMap gridMap = new GridMap(12, 8, 15, 15, 30, lineMap);

		// this converts the grid position into the underlying continuous
		// coordinate frame
		Point startPoint = gridMap.getCoordinatesOfGridPosition(10, 1);
		Pose startPose = new Pose(startPoint.x, startPoint.y, Heading.PLUS_Y.toDegrees());

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

	/**
	 * Optionally run the visualisation of the robot and localisation process. This is not necessary to run the localisation and could be removed once on the real robot.
	 */
	public void visualise() {
		JFrame frame = new JFrame("Map Viewer");
		frame.addWindowListener(new KillMeNow());

		// visualise the distribution on top of a line map
		m_mapVis = new GridPositionDistributionVisualisation(distribution, lineMap, 2, true);

		// Visualise the robot
		m_mapVis.addRobot(robot);

		frame.add(m_mapVis);
		frame.pack();
		frame.setSize(820, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Move the robot and update the distribution with the action model
	 *
	 * @param heading
	 * @param sensorModel
	 */
	private void move(Heading heading, ActionModel actionModel, SensorModel sensorModel) {
		Delay.msDelay(300);
		// move robot
		//if (m_gridMap.isValidGridPosition((int) robot.getPose().getX(), (int) robot.getPose().getY()))
		robot.translate(m_translationAmount);

		// now update estimate of position using the action model
		distribution = actionModel.updateAfterMove(distribution, heading);

		// if visualising, update the shown distribution
		if (m_mapVis != null)
			m_mapVis.setDistribution(distribution);

		// A short delay so we can see what's going on
		Delay.msDelay(300);

		distribution = sensorModel.updateAfterSensing(distribution, heading, robot.getRangeValues());

		// if visualising, update the shown distribution
		if (m_mapVis != null)
			m_mapVis.setDistribution(distribution);

		// A short delay so we can see what's going on
	}

	public void run() {
		ActionModel actionModel = new PerfectActionModel();
		SensorModel sensorModel = new PerfectSensorModel();

		while (true) {
			for (int i = 0; i < 5; i++)
				move(Heading.degreesToHeading(robot.getPose().getHeading()), actionModel, sensorModel);
			robot.rotate(90);

			for (int i = 0; i < 9; i++)
				move(Heading.degreesToHeading(robot.getPose().getHeading()), actionModel, sensorModel);
			robot.rotate(90);
		}
	}
}
