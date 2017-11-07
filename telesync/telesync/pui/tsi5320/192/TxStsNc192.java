package telesync.pui.tsi5320;


import pippin.pui.*;
import java.util.*;
import java.io.*;

/**
 * TxStsNc 192 impl
 * 05/28/2001
 */
 
public class TxStsNc192
	extends PComponent
{
	Hashtable mChildren;
	int mRate;
	int mSts3num;
	int mSts1num;
	
	private TxStsNc192(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"rateEnabled",

					"payloadType",
					"pattern",
					"patternInvert",
				}, new byte[] {
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BOOLEAN,
				}));
	}

	public TxStsNc192(PClient client,PComponent parent,
			String name,int id, int rate,int s3,int s1) {
		this(client,parent,name,id);
	
		// System.out.print("TxStsNc: " +name+ "\n");

		mRate = rate;
		mSts3num = s3;
		mSts1num = s1;

		mChildren = new Hashtable();

		if (rate == 1) {
		} else if (rate == 3) {
			for (int i=0 ; i<3 ; i++) {
				mChildren.put("sts1-" +s3+ "," +(i+1),
						new StsID(s3,i+1));
				System.out.println("    sts1-" +s3+ "," +(i+1));
			}
		} else {
			for (int i=0 ; i<4 ; i++) {
				int x = s3 + i*(rate/12);
				mChildren.put("sts" +(rate/4)+ "c-" + x,
						new StsID(x,s1));
				System.out.println("    sts" +(rate/4)+ "c-" + x);
			}
		}
	}

	protected PComponent makeSubComponent(String name,int id) {
		if (mChildren.containsKey(name)) {
			Object tmp = (Object)mChildren.get(name);
			TxStsNc192 sts = null;

			if (tmp instanceof StsID) {
				StsID sid = (StsID)tmp;
				sts = new TxStsNc192(mClient,this,name,id,
						(mRate == 3) ? 1 : mRate/4,
						sid.mSts3number,sid.mSts1number);
				mChildren.put(name,sts);
			} else {
				sts = (TxStsNc192)tmp;
			}
			if (sts != null)
				return sts;
		}
		return null;
	}
	
	
	
} //end class TxStsNc - 192
