////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import pippin.pui.*;
import pippin.binder.*;

import telesync.pui.tsi5320.*;

import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.View;



public class CardButton extends JToggleButton
		implements PStateChangeListener,
		ClientView.DisposeListener, ActionListener, 
		CardEventListener, ClearFlagEventListener, FlagListener {
			
	private JLabel mTitleLabel;
	private JPanel mBorderPanel = null;
	public static int WIDTH = 100;
	private static Font cFont = new Font("SansSerif", Font.PLAIN, 10);

	public static Color cAlertYellow = new Color(255, 255, 204);
	public static Color cAlarmRed = new Color(255, 204, 204);
	private static Color cSelectedBorder = Color.darkGray;
	
  /* jb-01052008 */
  public static Color cDefaultBackground = new Color( 204, 204, 204 );
  /* jb-01062602 remove ToolTip during alarm condition */
  
  private static final String DEFAULT_TOOLTIP = "Right-Click for options";
  private boolean mShowToolTip = false;
  private String mToolTipText = "";
  
	private Color mStatusColor = null;
	private CardType mCardType;
	private CardType mConfiguredCardType;
	private Color mBaseColor;

	private BusPanel mBusPanel;
	private int mSlotNumber;
	private Slot mSlot = null;
	private Card mCard;
	private CardPanel mCardPanel = null;

	private Dimension mSize;
	private PStateChangeConnector mStateChangeConnector;
	private CardEventConnector mCardEventConnector = null;
	private CardState mCardState = null;
	private boolean mFlagRaised = false;
	

	protected CardButton(BusPanel busPanel, int slotNumber) throws Exception {
		mBusPanel = busPanel;
		mBusPanel.addDisposeListener(this);

		setCardType(CardType.NOT_PRESENT);

		mSize = new Dimension(WIDTH, super.getPreferredSize().height);

		mBaseColor = getBackground();

		setHorizontalAlignment(JToggleButton.LEFT);
		setMargin(new Insets(4,2,4,2));

		mSlotNumber = slotNumber;
		mSlot = mBusPanel.getSlotAt(mSlotNumber);

		
		setModel( new CardButtonModel() );
		
		// System and Refclk have no slot!
		//
		if (mSlot == null) {
			// hardcode system and refclock
			switch (mSlotNumber) {
			case 0:
				setCardType(CardType.SYSTEM);
				break;
			case 1:
				setCardType(CardType.REF_CLOCK);
				break;
			default:
				throw new NullPointerException();
			}
		} else {
			mSlot.addStateChangeListener(mStateChangeConnector =
					new PStateChangeConnector(this));
			setConfiguredCardType(mSlot.getConfiguredType());
			setCardType(mSlot.getCardType());
				
			
		}
		
		addActionListener(this);
	}

	public void setTitleLabel(JLabel l) {
		mTitleLabel = l;
	}
	public Dimension getPreferredSize() {
		return mSize;
	}
	public Dimension getMaximumSize() {
		return mSize;
	}
	public Dimension getMinimumSize() {
		return mSize;
	}

	private String getLabelText() {
		return getLabelText(true);
	}
	
	private String getLabelText(boolean markup) {
		StringBuffer sb = new StringBuffer(0);
		if (markup) sb.append("<html><font size=-2 face=arial>");
		Card card = null;
		String cardName = "";
		
		Mode mode = null;;
		try {
			card = mSlot.getCard();
			if (mCardType.equals(CardType.OC48_RECEIVE)) {
				mode = ((Rx48)card).getMode();
				if (mode.equals(Mode.US_SONET)) sb.append("OC-48 Receive");
				else sb.append("STM-16 Receive");
				cardName = (String)((Rx48)card).get("name");
				
			} else if (mCardType.equals(CardType.OC48_TRANSMIT)) {
				mode = ((Tx48)card).getMode();
				if (mode.equals(Mode.US_SONET)) sb.append("OC-48 Transmit");
				else sb.append("STM-16 Transmit");
				cardName = (String)((Tx48)card).get("name");
			}			
			else if (mCardType.equals(CardType.OC192_RECEIVE)) {
				mode = ((Rx192)card).getMode();
				if (mode.equals(Mode.US_SONET)) sb.append("OC-192 Receive");
				else sb.append("STM-16 Transmit");
				cardName = (String)((Rx192)card).get("name");
			}
			else if (mCardType.equals(CardType.OC192_TRANSMIT)) {
				mode = ((Tx192)card).getMode();
				if (mode.equals(Mode.US_SONET)) sb.append("OC-192 Transmit");
				else sb.append("STM-16 Transmit");
				cardName = (String)((Tx192)card).get("name");
			}
			else sb.append(mCardType.getText());
			
			
		}
		catch (Exception e) {
			sb.append(mCardType.getText());
			
		}
		
		
		if (markup) sb.append("</font><html>");
		
		return sb.toString();
	}

	public CardType getCardType() {
		try {
			return mSlot.getCardType();
		}
		catch (Exception e) {
			return null;
		}
		
	}
	public void setBorderTitle() {
		JLabel l = mBusPanel.getTitleLabelAt(mSlotNumber);
		l.setText(getTitle());
	}
	private String getTitle() {
		String title;
		try {
			
			if (mCardType.equals(CardType.OC48_RECEIVE)) {
				title = mSlotNumber + " - " + ((Rx48)mCard).get("name",false);
			}
			else if (mCardType.equals(CardType.OC48_TRANSMIT)) {
				title = mSlotNumber + " - " + ((Tx48)mCard).get("name",false);
			} else if (mCardType.equals(CardType.OC192_TRANSMIT)) {
				title = mSlotNumber + " - " + ((Tx192)mCard).get("name",false);
			} else if (mCardType.equals(CardType.OC192_RECEIVE)) {
				title = mSlotNumber + " - " + ((Rx192)mCard).get("name",false);
			} else if (mCardType.equals(CardType.SYSTEM)) {
				title = mSlotNumber + " - SYSTEM";
			} else if (mCardType.equals(CardType.REF_CLOCK)) {
				title = mSlotNumber + " - REF_CLOCK";
			}
			else {
				title = mSlotNumber + " - Unused";
			}
		}
		catch (Exception e) {
			return mSlotNumber + "";
		}
		if (title.length() > 20) title = title.substring(0,20);
		return title;
	}
	public void setBorderPanel(JPanel p) {
		mBorderPanel = p;		
		setBorderTitle();
		
		
	}
	
	/**
	* @see #stateChanged
	*/
	protected void setCardType(CardType cardType) throws Exception {
		mCardType = cardType;

		if (mCardType == null) {
			throw new NullPointerException("cardType");
		}

		setText(getLabelText());

		Card card = null;

		if (mSlot != null) {
			card = mSlot.getCard();
		}

		if (card != null) {
			setCard(card);
		} else {
			if (CardType.SYSTEM.equals(mCardType)) {
				setCard((Card)
						mBusPanel.getPClient().getRoot().getSubComponent("system"));
			} else
			if (CardType.REF_CLOCK.equals(mCardType)) {
				setCard((Card)
						mBusPanel.getPClient().getRoot().getSubComponent("refclk"));
			} else {
				setCard(null);
			}
		}
		
		if (CardType.NOT_PRESENT.equals(mCardType)) {
			setEnabled(false);
			
		} else {
			setEnabled(true);
		}
	}

	/* jb-01052008 change Color Theme for CardButton 
	   Jeff Blau : 06/05/2001
	*/
	public void setEnabled( boolean flag ) {
		super.setEnabled( flag );
		
	}
		
	/**
	* @see #stateChanged
	*/
	protected void setConfiguredCardType(CardType configuredCardType) {
		mConfiguredCardType = configuredCardType;

		refreshState();
	}


	protected void setCard(Card card) throws Exception {
		if (mCard != null) {
			mCard.removeCardEventListener(mCardEventConnector);
		}
		mCard = card;
		if (mCard != null) {
			mCard.addStateChangeListener(new PStateChangeListener() {
				public void stateChanged(PStateChangeEvent event) {
					
					if (!mBusPanel.getClientView().getTitle().equals(getTitle())) {
						if (getModel().isSelected()) {
							try {
								if (mCard.getCardType().equals(CardType.REF_CLOCK)) {
									mBusPanel.getClientView().setTitle("Reference Clock");
								}
								else if (mCard.getCardType().equals(CardType.SYSTEM)) {
									mBusPanel.getClientView().setTitle("System");
								} else {
									mBusPanel.getClientView().setTitle(getLabelText(false) + " - " + mCard.get("name"));
								}
							}
							catch (Exception iee) {
							}
						}
					}
					
					setText(getLabelText());
					setBorderTitle();
				}
			});
		}
		
		if (mCardPanel != null) {
			System.out.println("setCard() - card is null?" + mCard == null);
			if (mCard != null) mCardPanel.dispose();
			mCardPanel = null;
		}

		if (mCard != null) {
			mCard.addCardEventListener(mCardEventConnector =
					new CardEventConnector(this));
		}

		if (isSelected()) {
			mBusPanel.getClientView().setCardPanel(getCardPanel());
		}

		refreshState();
	}


	protected Card getCard() {
		return mCard;
	}

	
	
	protected CardPanel getCardPanel() {
	
		
		if (mCardPanel == null && mCard != null) {
			try {
				mBusPanel.getClientView().getGClient().setStatus("Waiting for <init>");
				mBusPanel.getClientView().getGClient().suspendCursor();
				mCardPanel = CardPanel.createCardPanel(mBusPanel.getClientView(),
					mCard, mSlotNumber);
				mCardPanel.addFlagListener(this);
				System.out.println("add flag listener");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				mBusPanel.getClientView().getGClient().resumeCursor();
			}
		}

		return mCardPanel;
	}


	/**
	* Recomputes the state of this CardButton based
	* on the current state of mSlot and mCard.
	*/
	protected void refreshState() {
		// if (mConfiguredCardType != null &&
		// 		!mConfiguredCardType.equals(mCardType)) {
		// 	setIconState(CardState.ALERT);
		// } else {
			
			if (CardType.BOOT_IN_PROCESS.equals(mCardType)) {
				setIconState(CardState.ALERT);
			} else
			if (CardType.INVALID.equals(mCardType)) {
				setIconState(CardState.ALARM);
			} else
			if (CardType.NOT_PRESENT.equals(mCardType)) {
				setIconState(CardState.BASE);
			} else {
				if (mCard != null) {
					if (mCard.hasAlarm()) {
						setIconState(CardState.ALARM);
					} else
					if (mFlagRaised) {
						setIconState(CardState.EVENT);
					} else {
						setIconState(CardState.BASE);
					}
				} else {
					setIconState(CardState.ALARM);
				}
			}
			setText(getLabelText());
		// }
	}
	
	public CardState getCardState() {
		return mCardState;
	}
	
	public void resetStatus() {
		setIconState( CardState.BASE );
		if (mCard != null) mCard.resetHasNewEvents();
	}
	
	public void resetCard() {
		try {
			mSlot.reset();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
	/* ClearFlagEventListener Methods */
	public void clearFlags() {
		if (!mCardState.equals(CardState.ALARM)) {
			mFlagRaised = false;
			resetStatus();
		}
			
	}
	
	public void setIconState(CardState state) {
		mCardState = state;
		setIcon(CardIcon.getIcon(mCardType, state));
		setToolTipText(mToolTipText);
		
		if (state.equals(CardState.BASE)) {
			setStatusColor(null);
		} else
		if (state.equals(CardState.EVENT)) {
			setStatusColor(null);
		} else
		if (state.equals(CardState.ALERT)) {
			setStatusColor(cAlertYellow);
		} else
		if (state.equals(CardState.ALARM)) {
			setStatusColor(cAlarmRed);
			super.setToolTipText("");
			
		} else {
			throw new Error("unknown state " + state);
		}
	}


	public void updateUI() {
		setUI(CardButtonUI.createUI(this));
	}


	public void setStatusColor(Color c) {
		mStatusColor = c;
		if (c != null) {
			setBackground(c.darker());
		} else {
			/* jb-01052008 */
			setBackground( cDefaultBackground.darker() );
			
		}
	}


	public Color getStatusColor() {
		return mStatusColor;
	}


	public boolean hasStatusColor() {
		return mStatusColor != null;
	}


	public Color getSelectStatusColor() {
		if (mStatusColor != null) {
			
			return mStatusColor;//.darker();
		} else {
			return null;
		}
	}


	/**
	* @see pippin.pui.PStateChangeListener
	*/
	public void stateChanged(PStateChangeEvent event) {
		
		Slot s = (Slot) event.getComponent();
		PAttributeSet attrs = event.getAttributes();
		//System.out.println("CardButton.stateChanged() - attrs = " + event.getAttributes());
		
		Integer cardTypeID = (Integer)
				attrs.getValue("cardType", false);

		Integer cfgdTypeID = (Integer)
				attrs.getValue("cfgdType", false);

		Integer cardMode = (Integer) attrs.getValue("mode", false);
		
		CardType type = null;
		
		if (cardTypeID != null) {
			try {
				type = CardType.getTypeFor(cardTypeID);
				setCardType(type);
			}
			catch (Exception e) {
				e.printStackTrace();
				mBusPanel.getGClient().showAlertDialog(e);
			}
		}
		
		if (cardMode != null) {
			setText(getLabelText());
		}
		
		if (cfgdTypeID != null) {
			setConfiguredCardType(CardType.getTypeFor(cfgdTypeID));
		}
	}

	public void setShowTooltip( boolean show ) {
		mShowToolTip = show;
	}
	public boolean showToolTip() {
		return mShowToolTip;
	}
	public void setToolTipText( String text ) {
		mToolTipText = text;
		super.setToolTipText( text );
	}
	
	public void cardEventNotify(CardEvent event) {
		
		refreshState();
		
	}


	/**
	* @see telesync.gui.ClientView.DisposeListener
	*/
	public void dispose() throws Exception {
		if (mSlot != null) {
			mSlot.removeStateChangeListener(mStateChangeConnector);
		}
		if (mCard != null) {
			mCard.removeCardEventListener(mCardEventConnector);
		}
		mSlot = null;
		mCard = null;
	}


	public void actionPerformed(ActionEvent event) {
		System.out.println("CardButton.actionPerformed() start");
		mBorderPanel.setBorder( new CompoundBorder( new LineBorder(cSelectedBorder, 2), new EmptyBorder(1,1,1,1)) );		
		mBusPanel.getClientView().setCardPanel(getCardPanel());
		getCardPanel().addClearFlagEventListener(this);
		System.out.println("CardButton.actionPerformed() end");
	}

	/* jb-01052009  add CardButton border
	   Jeff Blau : 06/07/2001
	*/
	protected class CardButtonModel extends JToggleButton.ToggleButtonModel {
		public void setSelected( boolean b ) {
			super.setSelected( b );
			if (!b) {
				mBorderPanel.setBorder( new CompoundBorder( new LineBorder(mBorderPanel.getBackground(), 2), new EmptyBorder(1,1,1,1)) );
				if (mCardPanel != null) {
					if (mCardPanel instanceof OC48ReceiveCardPanel) {
						((OC48ReceiveCardPanel)mCardPanel).deactivate();
					}
				}
				
			} else {
				if (mCardPanel instanceof OC48ReceiveCardPanel) {
					((OC48ReceiveCardPanel)mCardPanel).activate();
				}
			}
			
			
		}
	}


	protected static class CardButtonUI extends MetalToggleButtonUI {
		static CardButtonUI cCardButtonUI = new CardButtonUI();
		Color selectColor = cDefaultBackground;
		Color disabledTextColor = new Color(204,204,204);
		public static ComponentUI createUI(JComponent c) {
			return cCardButtonUI;
		}

		protected void paintButtonPressed(Graphics g, AbstractButton b) {
			CardButton cb = (CardButton) b;

			if ( b.isContentAreaFilled() ) {
				Dimension size = b.getSize();
				if (cb.hasStatusColor()) {
					g.setColor(cb.getSelectStatusColor());
					
				} else {
					/* jb-01052008 */
					g.setColor( cDefaultBackground );
					
					
				}
				g.fillRect(0, 0, size.width, size.height);
											
			}
		}

		public void paint(Graphics g, JComponent c) {
			AbstractButton b = (AbstractButton) c;
			ButtonModel model = b.getModel();

			Dimension size = b.getSize();
			FontMetrics fm = g.getFontMetrics();

			Insets i = c.getInsets();
			
			Rectangle viewRect = new Rectangle(size);

			viewRect.x += i.left;
			//add 2 pixels
			viewRect.x += 2;
			viewRect.y += i.top;
			viewRect.width -= (i.right + viewRect.x);
			viewRect.height -= (i.bottom + viewRect.y);

			Rectangle iconRect = new Rectangle();
			Rectangle textRect = new Rectangle();
			//b.setBorder(new EtchedBorder());
			Font f = c.getFont();
			g.setFont(f);
			CardButton cb = (CardButton)b;
						
			if (cb.getCardType() != null) {
				if (cb.getCardType().equals(CardType.NOT_PRESENT)) {
					cb.setText("<HTML><font size=1 face=\"arial\" color=\"#6C6C6C\">Empty</font></html>");
				}
			} 
			
			// layout the text and icon
			String text = SwingUtilities.layoutCompoundLabel(
					c, fm, b.getText(), b.getIcon(),
					b.getVerticalAlignment(), b.getHorizontalAlignment(),
					b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
					viewRect, iconRect, textRect,
					b.getText() == null ? 0 : getDefaultTextIconGap(b));


			/* jb-01052008 */
			g.setColor( (new Color(153,153,153)).darker());
			
			
			if (model.isArmed() && model.isPressed() || model.isSelected()) {
				paintButtonPressed(g,b);
								
			} 
			

			// Paint the Icon
			if(b.getIcon() != null) { 
				paintIcon(g, b, iconRect);
			}

			// Draw the Text
			// if(text != null && !text.equals("")) {
			//   paintText(g, b, textRect, text);
			// }
			if (text != null && !text.equals("")){
				View v = (View) c.getClientProperty(BasicHTML.propertyKey);
				if (v != null) {
					v.paint(g, textRect);
				} else {
					paintText(g, c,textRect, text);
				}
			}

			// draw the dashed focus line.
			if (b.isFocusPainted() && b.hasFocus()) {
				paintFocus(g, b, viewRect, textRect, iconRect);
			}
		}
	}
	
	public void raiseFlag() {
		if (getCardState() != CardState.ALARM) {
			setIconState(CardState.EVENT);
		}
		mFlagRaised = true;
		
		
	}
	public void clearFlag() {
		clearFlags();
	}
	
}
