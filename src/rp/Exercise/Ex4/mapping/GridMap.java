package rp.Exercise.Ex4.mapping;

import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.RPLineMap;

import lejos.geom.Point;
import lejos.robotics.navigation.Pose;

public class GridMap implements IGridMap {

	private final int xSize;
	private final int ySize;
	private float xStart;
	private float yStart;
	private float cellSize;
	private final RPLineMap lineMap;

	public GridMap(int xSize, int ySize, float xStart, float yStart, float cellSize, RPLineMap lineMap) {
		super();
		this.xSize = xSize;
		this.ySize = ySize;
		this.xStart = xStart;
		this.yStart = yStart;
		this.cellSize = cellSize;
		this.lineMap = lineMap;
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
		return lineMap.inside(new Point(x, y));
	}

	@Override
	public Point getCoordinatesOfGridPosition(int x, int y) {
		return new Point(xStart + x * cellSize, yStart + y * cellSize);
	}

	@Override
	public boolean isValidTransition(int x1, int y1, int x2, int y2) {
		Point pos1 = new Point(x1, y1);
		Point pos2 = new Point(x2, y2);

		// TODO: Discuss if this should be between two points more than 1 distance from each other#
		// SearchFunction<Point> euclidean = (a, b) -> Math.abs(a.contents.x - b.contents.x) + Math.abs(a.contents.y - b.contents.y);
		// SearchFunction<Point> manhattan = (a, b) -> (float) Math.sqrt(Math.pow(a.contents.x - b.contents.x, 2) + Math.pow(a.contents.y - b.contents.y, 2));
		// return !AStar.findPathFrom(new Node<Point>(pos1), new Node<Point>(pos2), manhattan, euclidean).isNothing();
		return lineMap.inside(pos1) && lineMap.inside(pos2);
	}
	@Override
	public float rangeToObstacleFromGridPosition(int x, int y, float heading) {
		return lineMap.range(new Pose(x, y, heading));
	}
}
