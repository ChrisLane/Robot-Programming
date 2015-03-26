package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RangePacket extends RobotPacket<Float> {
	public static final byte ID = 11;
	public byte rangeId;

	public RangePacket(float range, int id) {
		super(range);
		this.rangeId = (byte) id;
	}
	public RangePacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public Float read(DataInputStream is) throws IOException {
		rangeId = is.readByte();
		return is.readFloat();
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.writeByte(rangeId);
		os.writeFloat(data);
	}
}