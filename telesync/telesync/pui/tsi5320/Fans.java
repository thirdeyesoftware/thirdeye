package telesync.pui.tsi5320;


import pippin.pui.*;
import java.util.*;


public class Fans extends PComponent {

	public Fans(PClient client, PComponent parent, String name, int id) {
		super(client, parent, name, id,
				new PTypeSet(new String[] {
					"fanCount",
					"fan1",
					"fan2",
					"fan3",
					"fan4",
				}, new byte[] {
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}
}
