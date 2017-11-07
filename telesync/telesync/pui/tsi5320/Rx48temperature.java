package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Rx48temperature extends PComponent {

	public Rx48temperature(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"topTemp",
					"botTemp",
				}, new byte[] {
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}
}
