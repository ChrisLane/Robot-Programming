package rp.util.remote.packet;

public interface PacketSender {
	<E> void send(RobotPacket<E> e);
}
