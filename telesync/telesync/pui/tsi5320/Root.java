package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;

public class Root extends PComponent {
	private PComponent mSystem;
	private PComponent mRefclk;
	private PComponent mBus;
	private PComponent mUser;

	public Root(PClient client,int id) {
		super(client,null,"root",id,
				new PTypeSet(new String[] {
					"model",
				},new byte[] {
					PAttribute.TYPE_STRING,
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		if (name.equals("system")) {
			if (mSystem == null)
				mSystem = new CSystem(mClient,this,name,id);
			return mSystem;
		} else if (name.equals("refclk")) {
			if (mRefclk == null)
				mRefclk = new Refclk(mClient,this,name,id);
			return mRefclk;
		} else if (name.equals("bus")) {
			if (mBus == null)
				mBus = new Bus(mClient,this,name,id);
			return mBus;
		} else if (name.equals("user")) {
			if (mUser == null)
				mUser = new User(mClient,this,name,id);
			return mUser;
		} else {
			return null;
		}
	}
}
