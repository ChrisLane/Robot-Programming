package rp.Exercise.Ex3.Part2;

import java.awt.geom.Point2D;

public class CompassHeading {

	public static final byte NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

	public static byte getRelativeHeading(int x, int y, byte heading) {
		return (byte) ((getHeading(x, y) + heading) % 4);
	}

	public static byte getRelativeHeading(Point2D.Float delta, byte heading) {
		return getRelativeHeading((int) delta.getX(), (int) delta.getY(), heading);
	}

	public static byte getHeading(int x, int y) {
		if (x != 0 && y != 0)
			throw new IllegalArgumentException("X or Y must be 0");
		switch ((int) Math.signum(x)) {
			case -1: return WEST;
			case 1: return EAST;
			default:
				switch ((int) Math.signum(y)) {
					case -1: return NORTH;
					case 1: return SOUTH;
				}
		}
		return 10;
	}
}
