package rp.Exercise.Ex3.Part2;

public class Coord {
	private byte x, y;

	public Coord(byte x, byte y) {
		this.x = x;
		this.y = y;
	}

	public byte getX() {
		return this.x;
	}

	public byte getY() {
		return this.y;
	}

	public void setX(byte x) {
		this.x = x;
	}

	public void setY(byte y) {
		this.y = y;
	}

	public Coord getDelta(Coord c) {
		return new Coord((byte) (c.x - this.x), (byte) (c.y - this.y));
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
