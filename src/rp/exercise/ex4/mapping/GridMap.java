package rp.exercise.ex4.mapping;

import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.RPLineMap;
import search.Coordinate;
import search.Node;

import lejos.geom.Line;
import lejos.geom.Point;
import lejos.robotics.navigation.Pose;

public class GridMap implements IGridMap {
	private final int xSize;
	private final int ySize;
	private final RPLineMap lineMap;
	private Object[] nodes;
	private float xStart;
	private float yStart;
	private float cellSize;

	public GridMap(int xSize, int ySize, float xStart, float yStart, float cellSize, RPLineMap lineMap) {
		super();
		this.xSize = xSize;
		this.ySize = ySize;
		this.xStart = xStart;
		this.yStart = yStart;
		this.cellSize = cellSize;
		this.lineMap = lineMap;

		nodes = new Object[xSize * ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				nodes[x + y * xSize] = new Node<Coordinate>(new Coordinate(x, y));

		// Set successors
		for (Object o : nodes) {
			@SuppressWarnings("unchecked")
			Node<Coordinate> node = (Node<Coordinate>) o;
			addSuccessor(node, 0, 1);
			addSuccessor(node, 0, -1);
			addSuccessor(node, 1, 0);
			addSuccessor(node, -1, 0);
		}
		System.out.println();
	}

	private void addSuccessor(Node<Coordinate> n, int dx, int dy) {
		int x = n.payload.x;
		int y = n.payload.y;
		if (isValidGridPosition(x + dx, y + dy) && isValidTransition(x, y, x + dx, y + dy))
			n.addSuccessor(getNodeAt(x + dx, y + dy));
	}

	@Override
	public int getXSize() {
		return xSize;
	}

	@Override
	public int getYSize() {
		return ySize;
	}

	public float getGridX(int x) {
		return xStart + x * cellSize;
	}

	public float getGridY(int y) {
		return yStart + y * cellSize;
	}

	@Override
	public boolean isValidGridPosition(int x, int y) {
		return x >= 0 && y >= 0 && x < xSize && y < ySize;
	}

	@Override
	public boolean isObstructed(int x, int y) {
		return !lineMap.inside(getCoordinatesOfGridPosition(x, y));
	}

	@Override
	public Point getCoordinatesOfGridPosition(int x, int y) {
		return new Point(getGridX(x), getGridY(y));
	}

	@Override
	public boolean isValidTransition(int x1, int y1, int x2, int y2) {
		if (isObstructed(x1, y1) || isObstructed(x2, y2) || !isValidGridPosition(x1, y1) || !isValidGridPosition(x2, y2))
			return false;

		Line line = new Line(getGridX(x1), getGridY(y1), getGridX(x2), getGridY(y2));
		for (Line l : lineMap.getLines())
			if (line.intersectsLine(l))
				return false;

		return true;
	}

	@Override
	public float rangeToObstacleFromGridPosition(int x, int y, float heading) {
		return lineMap.range(new Pose(x, y, heading));
	}

	@SuppressWarnings("unchecked")
	public Node<Coordinate> getNodeAt(int x, int y) {
		return (Node<Coordinate>) nodes[x + y * xSize];
	}
}
