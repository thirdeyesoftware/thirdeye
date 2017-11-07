////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import pippin.pui.*;
import telesync.pui.tsi5320.*;



public class BusPanel extends JPanel
		implements PStateChangeListener, ClientView.DisposeListener {

	private int mSlotCount;
	private Vector mCardButtons = new Vector();
	private Vector mSlots = new Vector();
	private ClientView mClientView;
	private Bus mBus = null;
	private PStateChangeConnector mStateChangeConnector;

	private JPopupMenu popupMenu;
	private ButtonGroup cardButtonGroup;
	private ResetFlagAction resetFlagAction;
	private ResetCardAction resetCardAction;
	private Vector mTitleLabels = new Vector();
	
	protected BusPanel(int slotCount) {
		mSlotCount = slotCount;

		try {
			init();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Error("impossible exception " + e.toString());
		}
	}


	public BusPanel(ClientView clientView) throws Exception {
		mClientView = clientView;
		mClientView.addDisposeListener(this);

		mBus = (Bus) getPClient().getRoot().getSubComponent("bus");
		mSlotCount = ((Integer) mBus.get("slotCount")).intValue() + 2;

		for (int i = 2; i < getSlotCount(); ++i) {
			Slot s = mBus.getSlotAt(i);
			mSlots.addElement(s);
		}

		init();

		// Why would we ever need this?
		mBus.addStateChangeListener(mStateChangeConnector =
				new PStateChangeConnector(this));
	}

	public void stateChanged(PStateChangeEvent event) {
		Integer cardMode = (Integer) event.getAttributes().getValue("mode", false);
		System.out.println("Card Mode = " + cardMode==null);
		
	}
	
	private void init() throws Exception {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel p0 = new JPanel();
		JPanel p1 = new JPanel();

		p0.setAlignmentY(0.0f);
		p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));
		p1.setAlignmentY(0.0f);
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

		add(p0);
		add(Box.createHorizontalStrut(5));
		add(p1);

		boolean firstRow = true;
		CardButton cb;
		
		/* jb-01052009 add CardButton border
		   Jeff Blau : 06/07/2001
		*/
		cardButtonGroup = new ButtonGroup();
		
		/* jb-01052007 add CardButton popup Menu 
		   Jeff Blau : 06/05/2001
		*/
		popupMenu = new JPopupMenu();
		
		resetFlagAction = new ResetFlagAction("Reset Flags",
			new ImageIcon(BusPanel.class.getResource("icons/cards/flag_small.gif")));
		
		resetCardAction = new ResetCardAction("Reset Card", 
			new ImageIcon(BusPanel.class.getResource("icons/cards/blank_small.gif")));
		
		JMenuItem item;
		
		popupMenu.add( item = new JMenuItem() );
				
		item.setAction( resetFlagAction  );
		item.setHorizontalTextPosition(JMenuItem.RIGHT);
		item.setBorderPainted( false );
		
		/*popupMenu.add( new JPopupMenu.Separator() );
		
		popupMenu.add(item = new JMenuItem() );
		item.setAction(resetCardAction);
		item.setHorizontalTextPosition(JMenuItem.RIGHT);
		item.setBorderPainted( false );
		*/		
		PopupListener popupListener = new PopupListener();
		JLabel titleLabel;
		
		for (int i = 0; i < mSlotCount; ++i) {
			
			cb = new CardButton(this, i);
			cb.setToolTipText("Right-Click for options");
			cb.setAlignmentX(0.0f);
			cb.addMouseListener( popupListener );
			/** Button Border Mod
			 * jb-010612-4
			 */
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout( new BorderLayout() );
			titleLabel = new JLabel(i+"");
			titleLabel.setFont(UIManager.getFont("pippin.fonts.smaller"));
			JPanel p = new JPanel();
			p.setLayout(new BorderLayout());
			p.add(titleLabel, BorderLayout.NORTH);
			//p.setBorder( new EtchedBorder());
			buttonPanel.add( p, BorderLayout.NORTH);
			mTitleLabels.addElement(titleLabel);
			buttonPanel.add(cb, BorderLayout.CENTER);
			buttonPanel.setMaximumSize(new Dimension((cb.WIDTH) + 5,70));
			
			buttonPanel.setBorder( new CompoundBorder( new LineBorder(buttonPanel.getBackground(),2), new EmptyBorder(1,1,1,1)));
			cb.setBorderPanel( buttonPanel );
			if (i % 2 == 0) {
				if (!firstRow) {
					p0.add(Box.createVerticalStrut(5));
				}
				p0.add(buttonPanel);
			} else {
				if (!firstRow) {
					p1.add(Box.createVerticalStrut(5));
				}
				firstRow = false;
				p1.add(buttonPanel);
			}
						
			mCardButtons.addElement(cb);
			cardButtonGroup.add( cb );
			
			
			
		}
		
		
	}


	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}


	public int getSlotCount() {
		return mSlotCount;
	}


	protected CardButton getCardButtonAt(int slot) {
		return (CardButton) mCardButtons.elementAt(slot - 1);
	}


	protected Slot getSlotAt(int slot) {
		slot -= 2;
		if (slot < 0) {
			return null;
		} else {
			return (Slot) mSlots.elementAt(slot);
		}
	}


	public CardType getCardTypeAt(int slot) {
		CardType ct = null;

		switch (slot) {
		case 0:
			ct = CardType.SYSTEM;
			break;
		case 1:
			ct = CardType.REF_CLOCK;
			break;
		default:
			try {
				ct = CardType.getTypeFor((Integer) getSlotAt(slot).get("cardType"));
				
			}
			catch (Exception e) {
				throw new RuntimeException(e.toString());
			}
			break;
		}

		return ct;
	}


	protected PClient getPClient() {
		return mClientView.getPClient();
	}


	protected GClient getGClient() {
		return mClientView.getGClient();
	}


	protected ClientView getClientView() {
		return mClientView;
	}
	
	public JLabel getTitleLabelAt(int index) {
		return (JLabel)mTitleLabels.elementAt(index);
	}
	

	/**
	* @see telesync.gui.ClientView.DisposeListener
	*/
	public void dispose() throws Exception {
		mBus.removeStateChangeListener( mStateChangeConnector );
		mBus = null;
		
	}


	protected void addDisposeListener(ClientView.DisposeListener listener) {
		mClientView.addDisposeListener(listener);
	}
	
	class PopupListener extends MouseAdapter {
			public void mousePressed(MouseEvent e) {
				isPopup(e);
			}
			public void mouseReleased( MouseEvent e ) {
				isPopup(e);
			}
			
			private void isPopup(MouseEvent e) {
				
				if (e.isPopupTrigger()) {
					if (((CardButton)e.getComponent()).getCardState().equals(CardState.ALARM)) return;
					resetFlagAction.setTargetButton((CardButton)e.getComponent());
					resetCardAction.setTargetButton((CardButton)e.getComponent());
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				} 
			}
  }
  
  /** 
   * ResetFlagAction Popup Menu Action
   *
   */
  class ResetFlagAction extends AbstractAction {
			private CardButton mTarget = null;
			
			public ResetFlagAction( String text, ImageIcon icon ) {
				super( text, icon );
			}
			public void actionPerformed( ActionEvent event ) {
				if (mTarget == null) return;
				else {
					
					mTarget.resetStatus();
				}
				
			}
			public void setTargetButton(CardButton cb) {
				mTarget = cb;
			}
			
	} 
	
	/**
	 * class ResetCardAction Menu Action
	 */
	class ResetCardAction extends AbstractAction {
		private CardButton mTarget = null;
		
		public ResetCardAction( String text, ImageIcon icon) {
			super(text, icon);
		}
		public void actionPerformed(ActionEvent event) {
			if (mTarget == null) return;
			else {
				if (getGClient().showConfirmDialog("Reset Card","Are you sure you want to reset this Card?")) {
					mTarget.resetCard();
				}
			}
		}
		public void setTargetButton(CardButton cb) {
			mTarget = cb;
		}
	}
				
			
}
