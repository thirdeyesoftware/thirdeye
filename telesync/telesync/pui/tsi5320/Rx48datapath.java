package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Rx48datapath extends PComponent {

	PComponent mChild;

	public Rx48datapath(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"setupLock",
					"copyActive",
					
					"b3Errors",
					"ptErrors",
					"ptChange",
					
				}, new byte[] {
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		System.out.println("Rx48Datapath.makeSubComponent: type=" +name+
						"  id=" +id);

		if (name.equals("oc48c")) {
			if (mChild == null)
				mChild = new Oc48c_rx(mClient,this,name,id);
			return mChild;
		}
		return null;
	}
}
