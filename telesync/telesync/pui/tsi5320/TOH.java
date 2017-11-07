package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class TOH
	extends PComponent
{
	public TOH(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {

					"A1", "A2", "J0", "B1", "E1", "F1", "D1", "D2", "D3",
					"H1", "H2", "H3", "B2", "K1", "K2", "D4", "D5", "D6",
					"D7", "D8", "D9", "Da", "Db", "Dc", "S1", "Mx", "E2",

				}, new byte[] {

PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,
PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,
PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,
PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,
PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,
PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,
PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,
PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,
PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY, PAttribute.TYPE_MEMORY,

				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}
}
