package rp.util.remote.packet;

import search.Coordinate;
import search.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathPacket extends RobotPacket<List<Node<Coordinate>>> {
	public static final byte ID = 15;
	public static final byte NodeID = 16;

	public PathPacket(List<Node<Coordinate>> data) {
		super(data);
	}
	public PathPacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);										// path id
		os.writeShort(data.size());								// node count
		for (Node<Coordinate> n : data)
			new CoordPacket(n.getPayload()).write(os);				// node payload *the coordinate*
	}
	@Override
	protected List<Node<Coordinate>> read(DataInputStream is) throws IOException {
		short size = is.readShort();							// node count

		data = new ArrayList<Node<Coordinate>>(size);
		for (int i = 0; i < size; i++) {
			if (is.readByte() != CoordPacket.ID)					// coordinate id
				throw new IOException("Data received is not Coordinate format");
			Coordinate c = new CoordPacket(is).getData();			// node payload *the coordinate*
			data.add(new Node<Coordinate>(c));
		}
		return data;
	}
}