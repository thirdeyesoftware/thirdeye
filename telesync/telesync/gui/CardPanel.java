////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;
import pippin.pui.*;
import pippin.binder.*;
import telesync.pui.tsi5320.*;



public abstract class CardPanel extends JPanel
		implements PStateChangeListener, ClientView.DisposeListener, Scrollable {

	protected ClientView mClientView;
	protected Card mCard;
	private JPanel mTitlePanel;
	protected BasicForm mTitleForm;
	private JViewport mViewport;
	private PStateChangeConnector mStateChangeConnector;
	protected JLabel titleLabel;
	protected JLabel cardNameLabel;
	protected JButton clearFlagsButton;
	protected int mSlotNumber;
	
	private String mCardName = "";
	
	static private int cInstanceID = 0;
	private int mInstanceID = getNextInstanceID();
	
	private Vector mClearFlagListeners = new Vector();
	
	static private CardPanel cCoverPanel = null;

	protected CardPanel(ClientView clientView, Card card,
			int slotNumber) throws Exception {
		
		System.out.println("CardPanel.<init> slotNumber = " + slotNumber);
		long sTime = System.currentTimeMillis();
		
		mClientView = clientView;
		mCard = card;
		System.out.println("card is null?" + (mCard == null));
		mSlotNumber = slotNumber;
		
		mCard.addStateChangeListener(mStateChangeConnector =
				new PStateChangeConnector(this));

		setLayout(new BorderLayout());

		mTitlePanel = new JPanel();
		mTitlePanel.setLayout(new BoxLayout(mTitlePanel, BoxLayout.X_AXIS));
		titleLabel = new JLabel(getLabelText(),
				getCardIcon().getIcon(CardState.BASE), SwingConstants.LEFT);
		mTitlePanel.add(titleLabel);
		mTitlePanel.setBorder(new CompoundBorder(
				new EtchedBorder(),
				new EmptyBorder(5,5,5,5)));
		//mTitlePanel.setBackground(
		//		UIManager.getColor("pippin.colors.titlePanelBG"));
		
		add(mTitlePanel, BorderLayout.NORTH);
		System.out.println("CardPanel.<init> - before create peers.");
		createPeers();
		
		clearFlagsButton = ButtonMaker.createPButton("Clear Flags", true);
		
		clearFlagsButton.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				fireClearFlagEvent();
			}
		});
		clearFlagsButton.setIcon(new ImageIcon(DefaultBinder.class.getResource("icons/event_small_white.gif")));
		mTitleForm = new BasicForm(new TitleTypeSet());

		mTitleForm.addField("slotNumber", new IntegerBinder("Slot #"));
		//mTitleForm.addField("status", new FormField("Status"));
		mTitleForm.setEnabled(false);
		mTitleForm.setUseSeparators(false);

		add(createContentComponent(), BorderLayout.CENTER);

		/* jb-01070201 */
		/* add clear button flag and notification/listener mechanism for cardpanels */
		
		//mTitleForm.add(Box.createVerticalStrut(5));
		//mTitleForm.add(clearFlagsButton, BorderLayout.SOUTH);
		//mTitlePanel.add(Box.createHorizontalGlue());
		
		mTitlePanel.add(mTitleForm);
		

		mClientView.addDisposeListener(this);


		mTitleForm.setBoundValue("slotNumber", new Integer(slotNumber));

		createPeerListeners();
		System.out.println("CardPanel.<init> time=" + (System.currentTimeMillis()-sTime)+"ms");
		
	}


	static synchronized private int getNextInstanceID() {
		System.out.println("CardPanel.getNextInstanceID: " + cInstanceID);
		return cInstanceID++;
	}


	protected int getInstanceID() {
		return mInstanceID;
	}



	/**
	* CoverPanel constructor.
	*/
	protected CardPanel() {
		/*
		setLayout(new BorderLayout());

		JLabel l = new JLabel("TSI");
		l.setFont(new Font("SansSerif", Font.PLAIN, 72));
		add(l, BorderLayout.CENTER);
		*/

		ImageIcon icon = new ImageIcon(
				GClient.class.getResource("icons/TSI5320Splash.gif"));
		JLabel l = new JLabel(icon);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// JLabel l = new JLabel("TSI");
		// l.setFont(new Font("SansSerif", Font.BOLD, 128));
		l.setAlignmentX(.5f);
		l.setBorder(new BevelBorder(BevelBorder.RAISED));
		l.setMaximumSize(l.getPreferredSize());

		add(Box.createVerticalGlue());
		add(l);
		add(Box.createVerticalGlue());
	}


	protected abstract void createPeers() throws Exception;


	protected abstract void createPeerListeners() throws Exception;


	protected abstract JComponent createContentComponent() throws Exception;


	static protected CardPanel createCardPanel(ClientView clientView, Card card,
				int slotNumber) throws Exception {
		CardPanel cp = null;
		CardType ct = card.getCardType();
		
		
		if (ct.equals(CardType.SYSTEM)) {
			cp = new SystemCardPanel(clientView, card, slotNumber);
		} else
		if (ct.equals(CardType.REF_CLOCK)) {
			cp = new RefClockCardPanel(clientView, card, slotNumber);
		} else
		if (ct.equals(CardType.OC48_TRANSMIT)) {
			cp = new OC48TransmitCardPanel(clientView, card, slotNumber);
		} else
		if (ct.equals(CardType.OC48_RECEIVE)) {
			cp = new OC48ReceiveCardPanel(clientView, card, slotNumber);
		} else
		if (ct.equals(CardType.OC192_TRANSMIT)) {
				cp = new OC192TransmitCardPanel( clientView, card, slotNumber);
		}
		else
		if (ct.equals(CardType.OC192_RECEIVE)) {
			cp = new OC192ReceiveCardPanel( clientView, card, slotNumber);
		}
		else {
			cp = getCoverPanel();
		}

		return cp;
	}


	protected Card getCard() {
		if (mCard == null) {
			try {
				mCard = getClientView().getBusPanel().getSlotAt(mSlotNumber).getCard();
			}
			catch (Exception e) {}
		}
		return mCard;
	}


	private CardIcon getCardIcon() {
		return CardIcon.getCardIcon(mCard.getCardType());
	}
	
	protected void refreshTitleLabel() {
		titleLabel.setText(getLabelText());
	}
	
	public String getLabelText(boolean markup) {
		StringBuffer sb = new StringBuffer(0);
		if (markup) 
			sb.append("<HTML><B><font size=+0 face=arial>");
		
		try {
			if (mCard != null) {
				String label = "";
				if (mCard.getMode().equals(Mode.US_SONET)) label = "OC-48";
				else label = "STM-16";
				if (mCard.getCardType().equals(CardType.OC48_RECEIVE)) {
					sb.append(label);
					if (markup) sb.append("<BR>");
					sb.append("Receive ");
				}
				else if (mCard.getCardType().equals(CardType.OC48_TRANSMIT)) {
					sb.append(label);
					if (markup) sb.append("<BR>");
					sb.append("Transmit ");
				}
				else sb.append(mCard.getCardType().getCardName());
				if (mCardName != null && !mCardName.trim().equals("")) {
					sb.append(" - ");
					sb.append(mCardName);
				}
			} else {
				// coverPanel case
				sb.append(mCard.getCardType().getCardName());
			}

		}
		catch (Exception ex) {
			sb.append(mCard.getCardType().getCardName());
		}

		if (markup) sb.append("</font></b><html>");
				
		return sb.toString();
	}
	
	protected String getLabelText() {
		return getLabelText(true);
				
	}


	public static CardPanel getCoverPanel() {
		if (cCoverPanel == null) {
			cCoverPanel = new CardPanel() {
				protected void createPeers() {
				}
				protected void createPeerListeners() {
				}
				protected JComponent createContentComponent() {
					return null;
				}
				public void doStateChange(PStateChangeEvent event) {
				}
			};
		}
		return cCoverPanel;
	}


	/**
	* @see telesync.gui.ClientView.DisposeListener
	* @see telesync.gui.CardButton#setCard
	*/
	public void dispose() throws Exception {
		if (mCard != null) {
			mClientView.removeDisposeListener(this);
			mCard.removeStateChangeListener(mStateChangeConnector);
		}
		mCard = null;
		System.out.println("card is null");
	}


	/**
	* @see pippin.pui.PStateChangeListener
	*/
	public void stateChanged(PStateChangeEvent event) {
		if (SwingUtilities.isEventDispatchThread()) {
			doStateChange(event);
		} else {
			SwingUtilities.invokeLater(new PEventRunnable(event) {
				public void run() {
					stateChanged((PStateChangeEvent) mPEvent);
				}
			});
		}
	}


	/**
	* Guaranteed to be in the AWT event dispatch thread.
	*/
	public abstract void doStateChange(PStateChangeEvent event);


	public String getTitle() {
		String label = "";
		try {
			
			if (mCard != null) {
				if (mCard.getMode().equals(Mode.US_SONET)) label = "OC-48";
				else label = "STM-16";
				if (mCard.getCardType().equals(CardType.OC48_RECEIVE)) 
					label += " Receive ";
				else if (mCard.getCardType().equals(CardType.OC48_TRANSMIT)) 
					label += " Transmit ";
				else label = 
					mCard.getCardType().getCardName();
			} else {
				// coverPanel case
				label =  "TSI 5320";
			}
			String name = getCardName();
			if (name.length() > 10) {
				name = name.substring(0,12);
			}
			
			
			label += " - " + name;
			
			return label;	
		
		}
		catch (Exception ex) {
			return "TSI 5320";
		}
		
	}


	protected static class TitleTypeSet extends pippin.binder.TypeSet {
		protected TitleTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("slotNumber", Integer.class),
				new TypeSetElement("status", String.class),

			});
		}
	}


	public Dimension getPreferredSize() {
		return getMinimumSize();
	}


	public Dimension getPreferredScrollableViewportSize() {
		return getMinimumSize();
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 60;
	}
	public boolean getScrollableTracksViewportHeight() {
		return true;
	}
	public boolean getScrollableTracksViewportWidth() {
		if (mViewport.getExtentSize().width > getMinimumSize().width) {
			return true;
		} else {
			return false;
		}
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 30;
	}

	protected void setViewport(JViewport viewport) {
		mViewport = viewport;
	}
	
	public void showTitlePanel( boolean visible ) {
		if (mTitlePanel != null) mTitlePanel.setVisible( visible );
	}
	
	public void addClearFlagEventListener(ClearFlagEventListener listener) {
		if (mClearFlagListeners == null) mClearFlagListeners = new Vector();
		if (!mClearFlagListeners.contains(listener)) {
			mClearFlagListeners.addElement( listener );
		}
	}
	
	public void removeClearFlagEventListener( ClearFlagEventListener listener) {
		if (mClearFlagListeners.size() > 0) {
			if (mClearFlagListeners.contains(listener)) {
				mClearFlagListeners.removeElement(listener);
			}
		}
	}
	
	public void fireClearFlagEvent() {
		
		ClearFlagEvent event = new ClearFlagEvent((CardPanel)this);
		for (Enumeration en = mClearFlagListeners.elements();en.hasMoreElements();) {
			ClearFlagEventListener listener = (ClearFlagEventListener)en.nextElement();
			listener.clearFlags();
		}
	}
	protected JPanel getTitlePanel() {
		return mTitlePanel;
	}
	
	/* jb-01092101 - Add Naming functions to CardPanel */
	public String getCardName() {
		return mCardName;
	}
	public void setCardName(String name) {
		mCardName = name;
	}
	public ClientView getClientView() {
		return mClientView;
	}
	public void addFlagListener(FlagListener l) {
	}
	public void removeFlagListener(FlagListener l) {
	}
	
	
}
