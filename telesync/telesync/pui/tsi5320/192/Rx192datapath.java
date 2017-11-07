package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Rx192datapath extends PComponent {

	PComponent mChild;

	public Rx192datapath(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"setupLock",
					"copyActive",
					
					"b3Errors",
					"ptErrors",
				}, new byte[] {
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		if (name.equals("oc192c")) {
			if (mChild == null)
				mChild = new Oc192c_rx(mClient,this,name,id);
			return mChild;
		}
		return null;
	}
}