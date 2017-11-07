////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import java.util.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import pippin.pui.*;
import telesync.pui.tsi5320.*;

import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.View;



public class CardIcon {
	private static Hashtable cCardIcons = new Hashtable();
	private static Hashtable cImageNames = new Hashtable();

	private CardType mCardType;
	private ImageIcon mIcon;
	private ImageIcon mEventIcon = null;
	private ImageIcon mAlertIcon = null;
	private ImageIcon mAlarmIcon = null;
	private ImageIcon mBigIcon;
	private ImageIcon mBigEventIcon = null;
	private ImageIcon mBigAlertIcon = null;
	private ImageIcon mBigAlarmIcon = null;

	static {
		cImageNames.put(CardType.SYSTEM, "system");
		cImageNames.put(CardType.REF_CLOCK, "clock");
		cImageNames.put(CardType.OC48_TRANSMIT, "transmit");
		cImageNames.put(CardType.OC48_RECEIVE, "receive");
		//OC192 impl
		cImageNames.put(CardType.OC192_TRANSMIT,"transmit");
		cImageNames.put(CardType.OC192_RECEIVE,"receive");
		//
		cImageNames.put(CardType.NOT_PRESENT, "empty");
		cImageNames.put(CardType.BOOT_IN_PROCESS, "empty");
		cImageNames.put(CardType.INVALID, "empty");

		loadCardIcons();
	}


	protected CardIcon(CardType cardType) {
		mCardType = cardType;

		String imageBaseName = (String) cImageNames.get(mCardType);
		System.out.println(imageBaseName);
		if (imageBaseName == null) {
			throw new Error("unknown CardType " + mCardType);
		}


		URL url;

		url = CardButton.class.getResource("icons/cards/" +
				imageBaseName + ".gif");
		mIcon = new ImageIcon(url);

		url = CardButton.class.getResource("icons/cards/" +
				imageBaseName + "_f.gif");
		mEventIcon = new ImageIcon(url);

		url = CardButton.class.getResource("icons/cards/" +
				imageBaseName + "_q.gif");
		mAlertIcon = new ImageIcon(url);

		url = CardButton.class.getResource("icons/cards/" +
				imageBaseName + "_x.gif");
		mAlarmIcon = new ImageIcon(url);

		url = CardButton.class.getResource("icons/cards/" +
				imageBaseName + "_b.gif");
		mBigIcon = new ImageIcon(url);
	}


	public ImageIcon getIcon(CardState state) {
		if (state.equals(CardState.BASE)) {
			return mIcon;
		} else
		if (state.equals(CardState.EVENT)) {
			return mEventIcon;
		} else
		if (state.equals(CardState.ALERT)) {
			return mAlertIcon;
		} else
		if (state.equals(CardState.ALARM)) {
			return mAlarmIcon;
		} else {
			throw new Error("unknown state " + state);
		}
	}

	public ImageIcon getBigIcon(CardState state) {
		if (state.equals(CardState.BASE)) {
			return mBigIcon;
		} else
		if (state.equals(CardState.EVENT)) {
			return mBigEventIcon;
		} else
		if (state.equals(CardState.ALERT)) {
			return mBigAlertIcon;
		} else
		if (state.equals(CardState.ALARM)) {
			return mBigAlarmIcon;
		} else {
			throw new Error("unknown state " + state);
		}
	}

	private static void loadCardIcons() {
		for (Enumeration en = CardType.getTypes();
				en.hasMoreElements(); ) {
			CardType cardType = (CardType) en.nextElement();

			try {
				cCardIcons.put(cardType, new CardIcon(cardType));
			}
			catch (Throwable t) {
				System.out.println("Loading " + cardType);
				t.printStackTrace();
				System.exit(-1);
			}
		}
	}

	protected static Icon getIcon(CardType cardType, CardState state) {
		CardIcon cardIcon = getCardIcon(cardType);

		if (cardIcon != null) {
			return cardIcon.getIcon(state);
		} else {
			throw new Error("unknown CardType " + cardType);
		}
	}

	public static CardIcon getCardIcon(CardType cardType) {
		return (CardIcon) cCardIcons.get(cardType);
	}
}
