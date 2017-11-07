package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Tx192temperature
	extends PComponent
{
	public Tx192temperature(PClient client,PComponent parent,
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
