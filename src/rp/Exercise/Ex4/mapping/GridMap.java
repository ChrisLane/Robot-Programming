package rp.Exercise.Ex4.mapping;

import rp.robotics.mapping.IGridMap;

import lejos.geom.Point;

public class GridMap implements IGridMap {

	private final int xSize;
	private final int ySize;

	public GridMap(int xSize, int ySize) {
		super();
		this.xSize = xSize;
		this.ySize = ySize;
	}

	@Override
	public int getXSize() {
		return xSize;
	}

	@Override
	public int getYSize() {
		return ySize;
	}

	@Override
	public boolean isValidGridPosition(int x, int y) {
		return x > 0 && y > 0 && x <= xSize && y <= ySize;
	}

	@Override
	public boolean isObstructed(int x, int y) {
		return false;
	}

	@Override
	public Point getCoordinatesOfGridPosition(int x, int y) {
		return null;
	}

	@Override
	public boolean isValidTransition(int x1, int y1, int x2, int y2) {
		return false;
	}

	@Override
	public float rangeToObstacleFromGridPosition(int x, int y, float heading) {
		return 0;
	}
}
