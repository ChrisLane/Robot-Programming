package rp.util.remote.packet;

import search.Coordinate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CoordPacket extends RobotPacket<Coordinate> {
	public static final byte ID = 17;

	public CoordPacket(Coordinate coord) {
		super(coord);
	}
	public CoordPacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.writeByte(data.x);
		os.writeByte(data.y);
	}

	@Override
	protected Coordinate read(DataInputStream is) throws IOException {
		return new Coordinate(is.readByte(), is.readByte());
	}

}
