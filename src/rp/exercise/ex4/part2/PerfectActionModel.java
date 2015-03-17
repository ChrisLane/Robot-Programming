package rp.exercise.ex4.part2;

import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.Heading;
import search.Coordinate;

/**
 * Example structure for an action model that should move the probabilities 1 cell in the requested direction.
 * In the case where the move would take the robot into an obstacle or off the map, this model assumes the robot stayed in one place.
 * This is the same as the model presented in Robot
 * Programming lecture on action models. Note that this class doesn't actually do this, instead it shows you a <b>possible</b> structure for your action model.
 *
 * @author Matthias Casula
 */
public class PerfectActionModel implements ActionModel {

	@Override
	public GridPositionDistribution updateAfterMove(GridPositionDistribution from, Heading heading) {
		// Create the new distribution that will result from applying the action model
		GridPositionDistribution to = new GridPositionDistribution(from);

		// iterate through points updating as appropriate
		for (int y = 0; y < to.getGridHeight(); y++)
			for (int x = 0; x < to.getGridWidth(); x++)
				// make sure to respect obstructed grid points
				if (!to.isObstructed(x, y)) {

					// the action model should work out all of the different
					// ways (x,y) in the to grid could've been reached based on
					// the from grid and the move taken (in this case
					// HEADING.PLUSX)

					// for example if the only way to have got to to (x,y) was
					// from from (x-1, y) (i.e. there was a PLUSX move from
					// (x-1, y) then you write the value from from (x-1, y) to
					// the to (x, y) value

					// The below code does now translate the value

					// position before move
					Coordinate hd = heading.toCoordinate();

					// set probability for position after move
					to.setProbability(x + hd.x, y + hd.y, from.getProbability(x, y));
				}
		to.normalise();
		return to;
	}
}
