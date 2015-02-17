package rp.Exercise.Ex3.Part2;

public class Coord {
	private int x, y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Coord getDelta(Coord c) {
		return new Coord(c.x - this.x, c.y - this.y);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
