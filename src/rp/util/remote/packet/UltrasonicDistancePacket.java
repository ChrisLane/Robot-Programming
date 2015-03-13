package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UltrasonicDistancePacket implements RobotPacket<Float> {
	public static final byte IDENTIFIER = 2;
	private final float f;

	public UltrasonicDistancePacket(float distance) {
		f = distance;
	}
	public UltrasonicDistancePacket(DataInputStream is) throws IOException {
		f = read(is);
	}

	@Override
	public Float read(DataInputStream is) throws IOException {
		return is.readFloat();
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(IDENTIFIER);
		os.writeFloat(f);
	}
	public float getDistance() {
		return f;
	}
}