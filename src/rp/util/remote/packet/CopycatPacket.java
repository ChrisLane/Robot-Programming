package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CopycatPacket extends RobotPacket<Integer> {
	public static final byte ID = 3;

	public CopycatPacket(Integer data) {
		super(data);
	}
	public CopycatPacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public Integer read(DataInputStream is) throws IOException {
		return is.readInt();
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.write(data);
	}
}
