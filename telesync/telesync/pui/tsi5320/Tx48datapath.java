package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Tx48datapath
	extends PComponent
{
	PComponent mChild;

	public Tx48datapath(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
				}, new byte[] {
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		System.out.println("Tx48Datapath.makeSubComponent: type=" +name+
						"  id=" +id);

		if (name.equals("oc48c")) {
			if (mChild == null)
				mChild = new Oc48c(mClient,this,name,id);
			return mChild;
		}
		return null;
	}
}
