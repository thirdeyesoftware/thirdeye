package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Slot
	extends PComponent
	implements PStateChangeListener
{
	int mSlot;
	int mCardTypeID;
	PComponent mChild;

	public Slot(PClient client,PComponent parent,String name,int id,
			int slot) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"cfgdType",
					"cardType",
					"reset",
					"reserver",
				}, new byte[] {
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_STRING,
					
				}));

		mSlot = slot;
		mCardTypeID = mTypeSet.getID("cardType");

		try {
			addStateChangeListener(this);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Error(e.toString());
		}
	}

	protected PComponent makeSubComponent(String name,int id) {
		//System.out.println("Slot.makeSubComponent: type=" +name+
		//		"  id=" +id);
		if (mChild == null) {
			if (name.equals("booting")) {
				mChild = new Booting(mClient,this,name,id);
			} else if (name.equals("invalid")) {
				mChild = new Invalid(mClient,this,name,id);
			} else if (name.equals("tx48")) {
				
				mChild = new Tx48(mClient,this,name,id);
				
			} else if (name.equals("rx48")) {
				
				mChild = new Rx48(mClient,this,name,id);
				
			} else if (name.equals("tx192")) {
				mChild = new Tx192(mClient, this, name, id);
			} else if (name.equals("rx192")) {
				//System.out.println("Slot.makeSubComponent() - Rx192");
				mChild = new Rx192(mClient,this,name,id);
			}
			
			
		}
		
		return mChild;
	}


	protected synchronized void dispatch(PMessage msg) {
		if (msg.getType() == PMessage.TYPE_STATE_CHG) {
			PAttributeSet attrs = msg.getAttributeSet();
						
			if (attrs.getAttribute("cardType",false) != null) {
				mSubsLoaded = false;
				mSubComps = null;
				mChild = null;
				try {
					loadSubComponents();
				} catch (PApplianceException ex) {
				} catch (IOException ex) {
				}
			}
		}

		super.dispatch(msg);
	}
	public void reset() throws Exception {
		put("reset",Boolean.TRUE);
	}
	
	public void stateChanged(PStateChangeEvent e) {
		//System.out.println("Slot.stateChanged: " + e);
	}


	public CardType getCardType() throws Exception {
		Integer cardTypeID = (Integer) get("cardType");
		if (cardTypeID == null) {
			throw new NullPointerException("cardType attribute");
		}

		CardType cardType = CardType.getTypeFor(cardTypeID);

		if (cardType == null) {
			throw new RuntimeException("no CardType for id " + cardTypeID);
		}

		return cardType;
	}


	public CardType getConfiguredType() throws Exception {
		return CardType.getTypeFor((Integer) get("cfgdType"));
	}


	/**
	* @return
	* The Card instance for the currently resident card.
	* Returns <code>null</code> if no card is present.
	*/
	public Card getCard() throws Exception {
		Card card = null;
		CardType cardType = getCardType();

		if (cardType.equals(CardType.NOT_PRESENT)) {
			return null;
		} else
		if (cardType.equals(CardType.BOOT_IN_PROCESS)) {
			return (Card) getSubComponent("booting");
		} else
		if (cardType.equals(CardType.INVALID)) {
			return (Card) getSubComponent("invalid");
		} else
		if (cardType.equals(CardType.OC48_TRANSMIT)) {
			return (Card) getSubComponent("tx48");
		} else
		if (cardType.equals(CardType.OC48_RECEIVE)) {
			return (Card) getSubComponent("rx48");
		} else
		if (cardType.equals(CardType.OC192_TRANSMIT)) {
			return (Card) getSubComponent("tx192");
		} else 
		if (cardType.equals(CardType.OC192_RECEIVE)) {
			return (Card) getSubComponent("rx192");
		} else {
			throw new Error("unsupported CardType " + cardType);
		}
	}
}
