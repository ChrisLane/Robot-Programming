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

	public Compass getHeadingFrom(Coord a, Coord b) {
		return getRelativeHeading(a.getDelta(b));
	}

	public Compass getRelativeHeading(Coord delta) {
		return getRelativeHeading(delta.getX(), delta.getY(), this);
	}

	public int toDegrees() {
		if (this.val < 3)
			return this.val * 90;
		else
			return -90;
	}

	public static Compass getRelativeHeading(int x, int y, Compass heading) {
		return getCompass(getHeading(x, y).val - heading.val);
	}

	public static Compass getHeading(int x, int y) {
		if (x != 0)
			switch ((short) Math.signum(x)) {
				case -1:
					return LEFT;
				case 1:
					return RIGHT;
			}
		else if (y != 0)
			switch ((short) Math.signum(y)) {
				case -1:
					return DOWN;
				case 1:
					return UP;
			}

		throw new IllegalArgumentException("X or Y must be 0");
	}

	public static Compass getCompass(int heading) {
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

	@Override
	public String toString() {
		switch (val) {
			case 0:
				return "North";
			case 1:
				return "East";
			case 2:
				return "South";
			case 3:
				return "West";
			default:
				return "No Direction";
		}
	}

	public Compass add(Compass heading) {
		int val = heading.val + this.val;
		System.out.println(heading.val + " + " + this.val + " = " + val);
		return getCompass(val);
	}
}
