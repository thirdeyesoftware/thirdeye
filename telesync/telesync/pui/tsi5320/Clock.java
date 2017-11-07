package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Clock
	extends PComponent
{
	public Clock(PClient client,PComponent parent,String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"seconds",
					"msecs",
				}, new byte[] {
					PAttribute.TYPE_LONG,
					PAttribute.TYPE_SHORT,
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}

	public synchronized void addStateChangeListener(PStateChangeListener lsnr)
			throws IOException,PApplianceException {
		throw new Error(
			"The 'clock' component must not be subscribed, as there are\n"+
			"no event change notifications to update the time attributes");
	}
}
