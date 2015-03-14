package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataPacket extends RobotPacket<byte[]> {
	public static final byte ID = 2;
	private byte length;

	public DataPacket(byte[] data) {
		super(data);
		length = (byte) data.length;
	}
	public DataPacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public byte[] read(DataInputStream is) throws IOException {
		length = is.readByte();
		byte[] dat = new byte[length];
		is.read(dat);
		return dat;
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.writeByte(length);
		os.write(data);
	}
}
