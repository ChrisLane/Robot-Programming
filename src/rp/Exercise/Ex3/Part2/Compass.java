package rp.Exercise.Ex3.Part2;

public class Compass {
	public static final Compass NONE = new Compass((byte) 255);
	public static final Compass UP = new Compass((byte) 0);
	public static final Compass RIGHT = new Compass((byte) 1);
	public static final Compass DOWN = new Compass((byte) 2);
	public static final Compass LEFT = new Compass((byte) 3);

	private final byte val;

	private Compass(byte val) {
		this.val = val;
	}

	public Compass getRelativeHeading(Coord delta) {
		return getRelativeHeading(delta, this);
	}
	public Compass getRelativeHeading(int x, int y) {
		return getRelativeHeading(x, y, this);
	}
	public int toDegrees() {
		if (this.val < 3)
			return this.val * 90;
		else
			return -90;
	}

	public static Compass getRelativeHeading(int x, int y, Compass heading) {
		return getCompass((byte) ((getHeading(x, y).val + heading.val) % 4));
	}

	public static Compass getRelativeHeading(Coord delta, Compass heading) {
		return getRelativeHeading(delta.getX(), delta.getY(), heading);
	}

	public static Compass getHeading(int x, int y) {
		if (x != 0 && y != 0)
			throw new IllegalArgumentException("X or Y must be 0");
		switch ((int) Math.signum(x)) {
			case -1:
				return LEFT;
			case 1:
				return RIGHT;
			default:
				switch ((int) Math.signum(y)) {
					case -1:
						return UP;
					case 1:
						return DOWN;
				}
		}
		return NONE;
	}

	public static Compass getCompass(byte heading) {
		switch (heading % 4) {
			case 0:
				return UP;
			case 1:
				return RIGHT;
			case 2:
				return DOWN;
			case 3:
				return LEFT;
			default:
				return NONE;
		}
	}
}
