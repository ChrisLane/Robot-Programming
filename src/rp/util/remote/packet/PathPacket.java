package rp.util.remote.packet;

import search.Coordinate;
import search.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
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
		os.write(ID);											// path id

		for (Node<Coordinate> n : data) {
			os.write(NodeID);									// node id
			new CoordPacket(n.payload).write(os);				// node payload *the coordinate*

			os.write((byte) n.getSuccessors().size());			// node successor count
			for (Node<Coordinate> succ : n.getSuccessors())
				os.writeShort(succ.payload.hashCode());			// all successor hashcodes
		}
	}
	@Override
	protected List<Node<Coordinate>> read(DataInputStream is) throws IOException {
		short size = is.readShort();							// node count

		List<short[]> allSuccessors = new ArrayList<short[]>(size);
		Map<Short, Node<Coordinate>> nodes = new HashMap<Short, Node<Coordinate>>(size);

		for (int i = 0; i < size; i++) {
			if (is.readByte() != NodeID)							// node id
				throw new IOException("Data received is not Path format");

			Coordinate c = new CoordPacket(is).getData();			// node payload *the coordinate*
			Node<Coordinate> n = new Node<Coordinate>(c);
			short nodeHash = (short) c.hashCode();
			nodes.put(nodeHash, n);

			short[] nSucs = new short[is.readByte() + 1];			// node successor count
			nSucs[0] = nodeHash;
			for (int j = 1; j < nSucs.length; j++)
				nSucs[j] = is.readShort();							// all successor hashcodes
			allSuccessors.add(nSucs);
		}

		data = new ArrayList<Node<Coordinate>>(size);
		for (int i = 0; i < size; i++) {							// Reconstruct Nodes
			short[] nodeSuccs = allSuccessors.get(i);				// with successors
			Node<Coordinate> node = nodes.get(nodeSuccs[0]);
			for (short successorHash : nodeSuccs)
				node.addSuccessor(nodes.get(successorHash));		// Doesn't matter if Node is null here
			data.add(node);
		}
		return data;
	}
}