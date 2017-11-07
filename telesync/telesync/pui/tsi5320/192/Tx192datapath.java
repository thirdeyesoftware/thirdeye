package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Tx192datapath
	extends PComponent
{
	PComponent mChild;

	public Tx192datapath(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
				}, new byte[] {
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		if (name.equals("oc192c")) {
			if (mChild == null)
				mChild = new Oc192c(mClient,this,name,id);
			return mChild;
		}
		return null;
	}
}
