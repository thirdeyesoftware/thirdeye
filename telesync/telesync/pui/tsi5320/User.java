package telesync.pui.tsi5320;


import pippin.pui.*;
import java.util.*;


public class User extends PComponent {

	public User(PClient client, PComponent parent, String name, int id) {
		super(client, parent, name, id,
				new PTypeSet(new String[] {
					"authEnabled",
					"authenticated",
					"name",
					"challengeID",
					"challenge",
					"response",
				}, new byte[] {
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_MEMORY,
					PAttribute.TYPE_MEMORY,
				}));
	}
}
