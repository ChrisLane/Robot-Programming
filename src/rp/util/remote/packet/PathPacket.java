package rp.util.remote.packet;

import search.Coordinate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathPacket extends RobotPacket<List<Coordinate>> {
	public static final byte ID = 15;
	public static final byte NodeID = 16;

	public PathPacket(List<Coordinate> data) {
		super(data);
	}
	public PathPacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);								// path id
		os.writeShort(data.size());						// node count
		for (Coordinate c : data)
			new CoordPacket(c).write(os);				// the coordinate
	}
	@Override
	protected List<Coordinate> read(DataInputStream is) throws IOException {
		short size = is.readShort();					// node count

		data = new ArrayList<Coordinate>(size);
		for (int i = 0; i < size; i++) {
			if (is.readByte() != CoordPacket.ID)		// coordinate id
				throw new IOException("Data received is not Coordinate format");
			data.add(new CoordPacket(is).getData());	// the coordinate
		}
		return data;
	}
}