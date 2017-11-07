package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Bus
	extends PComponent
{
	PComponent[] mSlot;

	public Bus(PClient client,PComponent parent,String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"slotCount",
					"phaseA",
					"phaseB",
				}, new byte[] {
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
				}));

		try {
			Integer sc = (Integer)get("slotCount");
			mSlot = new PComponent[sc.intValue()];
		} catch (Exception ex) {
			throw new RuntimeException(this.toString() + "  " + ex);
		}
	}


	public Slot getSlotAt(int i) throws IOException, PApplianceException {
		String slotName = "slot" + i;
		return (Slot) getSubComponent(slotName);
	}


	protected PComponent makeSubComponent(String name,int id) {
		if (name.startsWith("slot")) {
			int slot = -1;
			try {
				slot = Integer.parseInt(name.substring(4));
			} catch (Exception ex) {
				System.out.print("Bus.makeSubComponent: " +ex);
			}
			int slotndx = slot - 2;
			if (slotndx < 0 || slotndx >= mSlot.length) {
				return null;
			}

			if (mSlot[slotndx] == null)
				mSlot[slotndx] = new Slot(mClient,this,name,id,slot);
			return mSlot[slotndx];
		}
		return null;
	}
}
