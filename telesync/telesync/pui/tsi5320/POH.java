package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class POH
	extends PComponent
{
	public POH(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"C2",
					"G1",
					"F2",
					"H4",
					"Z3",
					"Z4",
					"Z5",
					"ptrace",
				}, new byte[] {
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,

					PAttribute.TYPE_STRING,
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}
}
