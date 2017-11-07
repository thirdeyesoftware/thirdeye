////
//
// Telesync 5320 Project
//
//

package telesync.gui;


import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;
import pippin.pui.*;
import telesync.gui.icons.OrbitIcon;



public class ClientView extends JInternalFrame
		implements InternalFrameListener {

	private static int DEFAULT_VSCROLL_SIZE = 50;
	
	GClient mGClient;
	boolean mDisposed = false;
	ConnectListener mConnectListener;
	boolean mConnected = false;
	private BusPanel mBusPanel = null;
	CardPanel mCardPanel = null;
	JPanel mCardButtonPanel = null;
	
	JScrollPane mCardPanelScrollPane;
	
	LegendPanel mLegendPanel = null;
	//ClockPanel mClockPanel = null;
	
	static Font cMenuFont = new Font("SansSerif", Font.PLAIN, 12);
	ViewMenu mViewMenu;
	
	/**
	* Set of entities to be disposed when this ClientView is disposed.
	*/
	Vector mDisposeListeners = new Vector();


	public ClientView(GClient gClient) throws Exception {
		System.out.println("ClientView.<init>");
		mGClient = gClient;
		getGClient().suspendCursor();
		addInternalFrameListener(this);
		setFrameIcon(OrbitIcon.getImageIcon());

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		//setSize(810, 600);
		setSize( 730, 520 );
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		
		// setDesktopIcon(new SFDesktopIcon(this));

		// Throws Exception
		// jb-01080401 add view menu.
		buildMenus();
		
		buildContents();
		

		//resetCardPanel();

		setVisible(true);
		this.toFront();
				
		PComponent sys = getPClient().getRoot().getSubComponent("system");
		
		getGClient().setName((String)sys.get("name"));
				
		getGClient().addConnectListener(
				mConnectListener = new ConnectListener() {
			public void connectNotify() {
				System.out.println("ClientView : connectNotify()");
				getGClient().suspendCursor();
				ClientView.this.connect();
				getGClient().resumeCursor();
			}

			public void disconnectNotify() {
				ClientView.this.disconnect();
			}
		});
		getGClient().resumeCursor();
		getGClient().setStatus(null);
		
		
		
	}

	public void addDisposeListener(DisposeListener d) {
		if (!mDisposeListeners.contains(d)) {
			mDisposeListeners.addElement(d);
		}
	}


	public void removeDisposeListener(DisposeListener d) {
		mDisposeListeners.removeElement(d);
	}

	private void buildMenus() {
		JMenuBar menuBar;
		JMenuItem menuItem;
		
		setJMenuBar( menuBar = new JMenuBar() );
		mViewMenu = new ViewMenu();
				
		menuBar.add( mViewMenu );
	}
	
	
	
	private void buildContents() throws Exception {
		// put the buspanel at NORTH.
		// put a cover panel in the content region
		// at CENTER.
		//
		// Content region should have a JScrollPane.

		Container c = getContentPane();
		JScrollPane sp;

		c.setLayout(new BorderLayout());
		mBusPanel = new BusPanel(this);
		sp = new JScrollPane(mBusPanel);
		
		/* jb-01052009 add Legend Panel 
		   Jeff Blau : 06/05/2001
		*/
		mCardButtonPanel = new JPanel();
		mCardButtonPanel.setOpaque( false );
		mCardButtonPanel.setLayout( new BorderLayout() );
		mCardButtonPanel.add( sp, BorderLayout.CENTER );
		
		sp.setViewportBorder(new EmptyBorder(5,5,5,5));
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		/**
		 * FIX- issue #17 - jsb 5/22/2002 
		 */
		sp.getVerticalScrollBar().setUnitIncrement(DEFAULT_VSCROLL_SIZE);
		c.add(mCardButtonPanel, BorderLayout.WEST);
		mLegendPanel = new LegendPanel();
		
		mLegendPanel.setMinimumSize(new Dimension( mCardButtonPanel.getWidth(), 50 ));
				
		mLegendPanel.setVisible( true );
		
		JPanel bottom = new JPanel();
		
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.add(mLegendPanel);
		mLegendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		/* jb-01071103 */
		/*mClockPanel = new ClockPanel();
			mClockPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			bottom.add(mClockPanel);
		*/	
		
		mCardButtonPanel.add(bottom, BorderLayout.SOUTH);
		
		//mClockPanel.setVisible(true);
		
		mCardPanelScrollPane = new JScrollPane();

		c.add(mCardPanelScrollPane, BorderLayout.CENTER);
		resetCardPanel();
	}


	public void dispose() {
		if (mDisposed) {
			return;
		} else {
			mDisposed = true;

			super.dispose();

			Vector disposeListeners = (Vector) mDisposeListeners.clone();

			int i = 0;
			for (Enumeration en = disposeListeners.elements();
					en.hasMoreElements(); ) {

				try {
					DisposeListener dl = (DisposeListener) en.nextElement();
					dl.dispose();
					removeDisposeListener(dl);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		getGClient().removeConnectListener(mConnectListener);
		
	}


	public GClient getGClient() {
		return mGClient;
	}


	public PClient getPClient() {
		return mGClient.getPClient();
	}


	protected void setCardPanel(CardPanel cardPanel) {
		if (cardPanel != null) {
			mCardPanel = cardPanel;
			
			updateViewMenu();
			
			mCardPanel.setViewport(mCardPanelScrollPane.getViewport());
			mCardPanelScrollPane.setViewportView(mCardPanel);

			mCardPanel.invalidate();
			mCardPanel.validate();
			mCardPanel.repaint();
			mCardPanelScrollPane.validate();

			setTitle(cardPanel.getTitle());
		} else {
			resetCardPanel();
		}
	}


	public boolean hasCardPanel(CardPanel cardPanel) {
		return mCardPanel == cardPanel;
	}


	private void updateViewMenu() {
		
		mViewMenu.showErrorItem(mCardPanel instanceof OC48ReceiveCardPanel);
		
	}
	
			
			
	protected void resetCardPanel() {
		setCardPanel(CardPanel.getCoverPanel());
	}


	protected BusPanel getBusPanel() {
		return mBusPanel;
	}


	public void internalFrameActivated(InternalFrameEvent e) {
		this.toFront();
	}


	public void internalFrameClosed(InternalFrameEvent e) {
		// This is redundant, but we have seen bugs in the swing
		// implementation that cause setDefaultCloseOperation(DISPOSE_ON_CLOSE)
		// to not work correctly.
		dispose();
	}


	public void internalFrameClosing(InternalFrameEvent e) {
	}


	public void internalFrameDeactivated(InternalFrameEvent e) {
	}


	public void internalFrameDeiconified(InternalFrameEvent e) {
		this.toFront();
	}


	public void internalFrameIconified(InternalFrameEvent e) {
	}


	public void internalFrameOpened(InternalFrameEvent e) {
		this.toFront();
	}


	public void connect() {
		if (!mConnected) {
			mConnected = true;
		} else {
			throw new Error("Attempt to connect a connected ClientView");
		}

		
		// ClientViews should already be connected when constructed.
		// We are mostly interested in disconnection, so all the
		// ClientViews can self-dispose at that time.
	}


	public void disconnect() {
		if (mConnected) {
			mConnected = false;

			dispose();
		} else {
			(new Throwable("Attempt to disconnect a disconnected SyncFrame")
					).printStackTrace();
		}
	}
	/* jb-01071102 */
	/*public void setClockVisible( boolean visible ) {
		mClockPanel.setVisible( visible );
	}
	*/
	public void setLegendVisible( boolean visible) {
		mLegendPanel.setVisible( visible );
	}
	public void setCardButtonPanelVisible( boolean visible ) {
		mCardButtonPanel.setVisible( visible );
	}
	
	public void setErrorPanelVisible( boolean visible ) {
		if (mCardPanel instanceof OC48ReceiveCardPanel) {
			((OC48ReceiveCardPanel)mCardPanel).showErrorPanel( visible );
		}
	}
	
	public void setTitle(String title) {
		super.setTitle(title);
		
		
		// Force the DesktopManager to reread the size
		// of this JInternalFrame's JDesktopIcon.
		//
		// This is very specific to swing-1.1.1 and Javasoft's
		// comments indicate that JInternalFrame.JDesktopIcon
		// could go away.
		//
		getGClient().getDesktopManager().setWasIcon(this, Boolean.FALSE);
	}


	/**
	* Interface for all entities that wish to be notified when this
	* ClientView is disposed.
	*/
	public static interface DisposeListener {
		public void dispose() throws Exception;
	}
	
	
	//menu classes
	class ViewMenu extends JMenu 
			implements ActionListener, ItemListener {
				
			JCheckBoxMenuItem mLegendItem;
			
			JCheckBoxMenuItem mCardButtonPanelItem;
			JMenuItem mExpertItem;
			JCheckBoxMenuItem mErrorItem;
			
			ViewMenu() {
				super("View");
				setFont(cMenuFont);
				
				// jb-01080203 add cardbutton panel view item
				mCardButtonPanelItem = new JCheckBoxMenuItem("Card Buttons", true);
				mCardButtonPanelItem.setFont(cMenuFont);
				mCardButtonPanelItem.addItemListener(this);
				mCardButtonPanelItem.setActionCommand("card");
				
				add(mCardButtonPanelItem);
				
				mLegendItem = new JCheckBoxMenuItem("Legend",true);
				mLegendItem.setFont(cMenuFont);
				mLegendItem.addItemListener(this);
				mLegendItem.setActionCommand("legend");
											
				add(mLegendItem);
				
				
				mErrorItem = new JCheckBoxMenuItem("Error Indicators", true);
				mErrorItem.setFont(cMenuFont);
				mErrorItem.addItemListener( this );
				mErrorItem.setActionCommand("error");
				add(mErrorItem);
								
				addSeparator();
				mExpertItem = new JMenuItem("Minimize View");
				mExpertItem.setActionCommand("minimize");
				mExpertItem.setFont(cMenuFont);
				mExpertItem.addActionListener( this );
				add(mExpertItem);
				
				
				
			}
			public void showErrorItem(boolean visible) {
				if (mErrorItem != null) mErrorItem.setVisible(visible);
			}
			
			public void itemStateChanged(ItemEvent event) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem)event.getSource();
				
				String command = item.getActionCommand();
								
				if (command.equals("card")) {
					_setCardButtonPanelVisible(item.isSelected());
				} else if (command.equals("error")) {
					_setErrorPanelVisible( item.isSelected() );
				} else {
					_setLegendVisible(item.isSelected());
				}
				
				
			}
			private void _setCardButtonPanelVisible( final boolean visible ) {
				if (SwingUtilities.isEventDispatchThread()) {
					setCardButtonPanelVisible( visible );
					
					mLegendItem.setEnabled(visible);
					
					

				} else {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							setCardButtonPanelVisible( visible );
							
							mLegendItem.setEnabled(visible);
						}
					});
				}
			}
			
			
			private void _setErrorPanelVisible( final boolean visible ) {
				if (SwingUtilities.isEventDispatchThread()) {
					setErrorPanelVisible( visible );

				} else {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							setErrorPanelVisible( visible );
						}
					});
				}
			}
			
			private void _setLegendVisible( final boolean visible ) {
				if (SwingUtilities.isEventDispatchThread()) {
					setLegendVisible( visible );
				
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							setLegendVisible( visible );
						}
					});
				}
			}
			private void _setExpertView( boolean expert ) {
				if (expert) {
					_setCardButtonPanelVisible( false );
					if (mCardPanel != null) mCardPanel.showTitlePanel( false );
					mExpertItem.setText("Maximize View");
					mExpertItem.setActionCommand("maximize");
					mCardButtonPanelItem.setEnabled(false);
					
				}
				else {
					_setCardButtonPanelVisible( true );
					if (mCardPanel != null) mCardPanel.showTitlePanel( true );
					mExpertItem.setText("Minimize View");
					mExpertItem.setActionCommand("minimize");
					mCardButtonPanelItem.setEnabled(true);

						
				}
				
			}
			
			
			
					
				
			public void actionPerformed( ActionEvent event ) {
					
				_setExpertView( event.getActionCommand().equals("minimize") );
							
			}
			
	}
}
