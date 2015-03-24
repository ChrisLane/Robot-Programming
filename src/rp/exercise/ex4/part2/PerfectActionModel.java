package rp.exercise.ex4.part2;

import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.Heading;
import search.Coordinate;

/**
 * An action model that should move the probabilities 1 cell in the requested direction.
 * In the case where the move would take the robot into an obstacle or off the map, this model assumes the robot stayed in one place.
 *
 * @author Matthias Casula
 */
public class PerfectActionModel implements ActionModel {

	@Override
	public GridPositionDistribution updateAfterMove(GridPositionDistribution from, Heading heading) {
		// Create the new distribution that will result from applying the action model
		GridPositionDistribution to = new GridPositionDistribution(from);

		// iterate through points updating as appropriate
		for (int y = 0; y < to.getGridHeight(); y++) {
			for (int x = 0; x < to.getGridWidth(); x++) {
				Coordinate hd = heading.headingToCoord();
				//Coordinates of where we going
				int dx = x + hd.getX();
				int dy = y + hd.getY();
				//Coordinated of where we coming from
				int prevX = x - hd.getX();
				int prevY = y - hd.getY();
				// make sure to respect obstructed grid points
				boolean validTransition = from.getGridMap().isValidTransition(x, y, dx, dy);
				boolean prev2CurrValid = from.getGridMap().isValidTransition(x, y, prevX, prevY);
				if (to.isValidGridPosition(dx, dy)) {
					//assigns points that cannot be accessed with the performed move probability 0
					if (validTransition) {
						// set probability for position after move
						to.setProbability(dx, dy, from.getProbability(x, y));

					} else {
						to.setProbability(dx, dy, 0);
					}

					if (from.isValidGridPosition(prevX, prevY) && from.isValidGridPosition(x, y) && prev2CurrValid) {
						to.setProbability(x, y, from.getProbability(prevX, prevY));
					} else
						to.setProbability(x, y, 0);
				}
			}
		}


		to.normalise();
		return to;
	}
}
