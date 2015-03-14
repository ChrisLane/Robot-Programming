package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UltrasonicDistancePacket extends RobotPacket<Float> {
	public static final byte ID = 2;

	public UltrasonicDistancePacket(float range) {
		super(range);
	}
	public UltrasonicDistancePacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public Float read(DataInputStream is) throws IOException {
		return is.readFloat();
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.writeFloat(data);
	}
}