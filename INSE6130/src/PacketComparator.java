import java.util.Comparator;


public class PacketComparator implements Comparator<Packet> {

	@Override
	public int compare(Packet p1, Packet p2) {
		return p1.compareTo(p2);
	}

}
