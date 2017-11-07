////
//
// Telesync 5320 Project
//
//

package telesync.gui;


import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.beans.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.*;
import pippin.pui.*;
import pippin.util.*;
import pippin.binder.*;
import telesync.gui.icons.OrbitIcon;



public class GClient extends JFrame implements PConnectionListener {

	private static final String UNITS_FILENAME = "units.dbf";
	private static final int DEFAULT_HEIGHT_REDUCTION = 30;
	private static final int DEFAULT_WIDTH_REDUCTION = 30;
	
	private SystemMessages mSystemMessages = SystemMessages.getInstance();
	
	private JDesktopPane mDesktop;
	private boolean mConnected = false;
	private PClient mPClient = null;
	private ConnectionMenu mConnectionMenu;
	private ViewMenu mViewMenu;
	
	private WindowMenu mWindowMenu;
	protected HelpMenu mHelpMenu;
	private Vector mConnectListeners = new Vector();
	private String mBaseTitle = "";
	private String mClientName = "";
	private JToolBar mStatusBar;
	private StatusPanel mStatusPanel;
	
	private JPanel mContentPanel;
	
	private SnifferConsole sc;	
	
	private String mUnitName;
	private String mUnitAddress;
	private static String mVersion;
	private static boolean mDebug = false;
	int mCursorSuspendCount = 0;

	static GClient cGClient;
	static Font cMenuFont = new Font("SansSerif", Font.PLAIN, 12);

	public static GClient getClient() {
		return cGClient;
	}

	public Refresher getRefresher() {
		return Refresher.getRefresher();
	}
	

	public static GClient createClient(String name) {
		if (cGClient == null) {
			
			new GClient(name);
		} else {
			throw new Error("GClient already exists");
		}
		return cGClient;
	}


	private GClient(String name) {
		super(name);
		cGClient = this;
		mBaseTitle = name;
		
		setIconImage(OrbitIcon.getImage(getToolkit()));

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		mDesktop = new JDesktopPane();
		mDesktop.setDesktopManager(new ClientDesktopManager());
		mDesktop.setBorder(new BevelBorder(BevelBorder.LOWERED));

		buildMenus();
		buildContents();

		setSize(getToolkit().getScreenSize().width - DEFAULT_WIDTH_REDUCTION, 
						getToolkit().getScreenSize().height - DEFAULT_HEIGHT_REDUCTION);
		setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Launch the connect window...
				((ConnectionMenu) mConnectionMenu).mConnectItem.doClick();
			}
		});
	}
		
		
	public String getUnitName() {
		return mUnitName;
	}
	
	private void buildMenus() {
		JMenuItem menuItem;
		JMenu menu;

		setJMenuBar(new JMenuBar());

		mConnectionMenu = new ConnectionMenu();
		getJMenuBar().add(mConnectionMenu);
				
		mViewMenu = new ViewMenu();
		getJMenuBar().add(mViewMenu);
				
		mWindowMenu = new WindowMenu();
		getJMenuBar().add(mWindowMenu);
				
		mHelpMenu = new HelpMenu();
		getJMenuBar().add(mHelpMenu);
		
	}

	
	private void buildContents() {
		getContentPane().setLayout(new BorderLayout());

		mContentPanel = new JPanel();
		mContentPanel.setLayout(new BorderLayout());
		mContentPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
		mContentPanel.add(mDesktop, BorderLayout.CENTER);
		
		mStatusBar = new JToolBar("Status");
		mStatusBar.setLayout(new BorderLayout());
		mStatusBar.setFloatable(true);
		mStatusBar.setMargin(new Insets(1,1,1,1)); //top,left,bottom,right
			
		/*ClockPanel clockPanel = new ClockPanel();
		clockPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mStatusBar.add(clockPanel, BorderLayout.EAST);
		clockPanel.setVisible(true);
		*/
		mStatusBar.add(Box.createHorizontalGlue());
		mStatusPanel = new StatusPanel();
		mStatusBar.add(mStatusPanel, BorderLayout.EAST);
		mStatusPanel.setVisible(true);
		mStatusBar.setVisible(true);
		//mStatusPanel.setStatus("Testing 1 2 3 - testing 1 2 3");
		
		mContentPanel.add(mStatusBar, BorderLayout.SOUTH);
		
		// getContentPane().add(new ButtonBar(this), BorderLayout.NORTH);
		getContentPane().add(mContentPanel, BorderLayout.CENTER);
	}

	private void setStatusBarVisible(boolean b) {
		
		mStatusBar.setVisible(b);
	
	}
	private void setPuiConsoleVisible(boolean b) {
		// PUI CONSOLE EXAMPLE 
		if (!b) {
			if (sc != null) sc.dispose();
			Sniffer.getSniffer().setEnabled(false);
		}
		else {
			Sniffer.getSniffer().setEnabled(true);
			sc = new SnifferConsole("PUI Console");
			sc.setSize(400,200);
						
			sc.setVisible(true);	

			getDesktop().add(sc);
			sc.setLocation(0,0);
			sc.show();
		}
			// PUI CONSOLE EXAMPLE

	}
	
		
	public void setStatus(final String s) {
		if (SwingUtilities.isEventDispatchThread()) {
			mStatusPanel.setStatus(s);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					setStatus(s);
				}
			});
		}
		
		
		
	}
	
	
	public void dispose() {
		Refresher.getRefresher().dispose();
		
		super.dispose();
		try {
			disconnect();
		}
		catch (Exception e) {
			System.out.println("GClient.dispose: " + e);
			e.printStackTrace();
		}

		System.exit(0);
	}

	/* setTitle( String name ) */
	/* sets device name in title bar */
	public void setName( String name ) {
		mClientName = name;	
		super.setTitle(mBaseTitle + " - " + name);
	
	}
	

	/**
	* @return
	* The PClient instance that this GClient uses for all
	* interaction with the PUI on a 5320.
	*/
	public PClient getPClient() {
		return mPClient;
	}
	public void setPClient(PClient pClient) {
		mPClient = pClient;
	}
	

	public JDesktopPane getDesktop() {
		return mDesktop;
	}


	protected synchronized void connect(String unitName)
			throws Exception {

		if (isConnected()) {
			throw new Error("Attempting to connect a connected GClient");
			
		}
		
		try {
			
			suspendCursor();
			ConnectForm connectForm = new ConnectForm(this, unitName);
			
			Dimension d = connectForm.getToolkit().getScreenSize();
			Dimension x = connectForm.getPreferredSize();
			Point location = new Point((d.width- x.width)/2, (d.height - x.height) / 2);
			
			connectForm.setLocation( location );
			connectForm.pack();
			connectForm.setVisible( true );  //this is modal.
			
			mPClient = connectForm.getPClient();
			if (mPClient != null) {
				
				mConnected = true;
				
				fireConnect(true);

				mPClient.addConnectionListener((PConnectionListener) this);
				connectForm.dispose();
				
				/*try {
					authenticateUser();
				}
				catch (UserSecurityException se) {
					System.out.println(se.toString());
					disconnect();
				}
			*/
	
				mUnitName = (String)mPClient.getRoot().getSubComponent("system").get("name");
				if (mUnitName == null || mUnitName.trim().equals("")) {
					mUnitName = mUnitAddress;
				}
				mConnectionMenu.updateHosts(mUnitName, mUnitAddress);
				
			}
			else {
				boolean userCanceled = connectForm.userCanceled();
				connectForm.dispose();
				if (userCanceled) 
					throw new UserInterruptedException("Connection Canceled");
				else 
					throw new ConnectException();
			}
			
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			resumeCursor();
		}

	}


	/**
	* @see pippin.pui.PConnectionListener
	*/
	public void connectionLost() {
		System.out.println("GClient.connectionLost:");
		try {
			disconnect();
			showAlertDialog("Disconnected", "Disconnected from " + mUnitName);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* One-shot disconnect.
	*/
	protected void disconnect() throws Exception {
		synchronized (this) {
			if (!isConnected()) {
				return;
			}
			// Do this before PClient.dispose().
			mConnected = false;
		}

		fireConnect(false);
		
		mPClient.dispose();
		mPClient = null;
	}


	protected boolean isConnected() {
		return mConnected;
	}


	/**
	* Will fire back a connectNotify() on the listener if
	* we are already connected.  Listeners should assume
	* we are not connected until told otherwise.
	*/
	public void addConnectListener(
			ConnectListener listener) {
		synchronized (mConnectListeners) {
			if (!mConnectListeners.contains(listener)) {
				mConnectListeners.addElement(listener);

				if (isConnected()) {
					SwingUtilities.invokeLater(new ParmRunnable(listener) {
						public void run() {
							((ConnectListener) mParm).connectNotify();
						}
					});
				}
			}
		}
	}


	public void removeConnectListener(
			ConnectListener listener) {
		mConnectListeners.removeElement(listener);
	}


	public void fireConnect(boolean connected) {
		Vector v = (Vector) mConnectListeners.clone();

		if (SwingUtilities.isEventDispatchThread()) {
			if (connected) {
				for (Enumeration en = v.elements(); en.hasMoreElements(); ) {
					((ConnectListener) en.nextElement()).connectNotify();
				}
			} else {
				for (Enumeration en = v.elements(); en.hasMoreElements(); ) {
					((ConnectListener) en.nextElement()).disconnectNotify();
				}
			}
		} else {
			SwingUtilities.invokeLater(new ParmRunnable(new Boolean(connected)) {
				public void run() {
					fireConnect(((Boolean) mParm).booleanValue());
				}
			});
		}
	}


	abstract class ParmRunnable implements Runnable {
		Object mParm;
		ParmRunnable(Object parm) {
			mParm = parm;
		}
	}


	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Message",
				JOptionPane.INFORMATION_MESSAGE);
	}


	public void showAlertDialog(Exception exception) {
		showAlertDialog(exception, "Alert");
	}


	public void showAlertDialog(Exception exception, String title) {
		String message = null;

		if (exception instanceof ExceptionMessage) {
			message = ((ExceptionMessage) exception).getExceptionMessage();
		}

		if (message == null) {
			message = exception.toString();
		}

		JOptionPane.showMessageDialog(this, message, title,
				JOptionPane.WARNING_MESSAGE);

		if (exception instanceof RuntimeException) {
			exception.printStackTrace();
		}
	}


	public void showAlertDialog(String title, String msg) {
		JOptionPane.showMessageDialog(this, msg, title,
				JOptionPane.WARNING_MESSAGE);
	}


	public void showErrorDialog(Exception exception) {
		String message = null;

		if (exception instanceof ExceptionMessage) {
			message = ((ExceptionMessage) exception).getExceptionMessage();
		}

		if (message == null) {
			message = exception.toString();
		}

		JOptionPane.showMessageDialog(this, message, "Error",
				JOptionPane.ERROR_MESSAGE);

		if (exception instanceof RuntimeException) {
			exception.printStackTrace();
		}
	}


	public void showErrorDialog(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}


	public boolean showConfirmDialog(String title, String message) {
		int r = JOptionPane.showConfirmDialog(this, message, title,
				JOptionPane.YES_NO_OPTION);

		if (r == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}


	public void suspendCursor() {
		Cursor cursor;
		JInternalFrame frames[];
		if (SwingUtilities.isEventDispatchThread()) {
			if (mCursorSuspendCount == 0) {
				cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
				frames = mDesktop.getAllFrames();
				for (int i = 0; i < frames.length; ++i) {
					frames[i].setCursor(cursor);
				}
				setCursor(cursor);
			}

			++mCursorSuspendCount;
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					suspendCursor();
				}
			});
		}
	}

	
	

	public void resumeCursor() {
		Cursor cursor;
		JInternalFrame frames[];

		if (SwingUtilities.isEventDispatchThread()) {
			if (mCursorSuspendCount > 0) {
				if (--mCursorSuspendCount == 0) {
					cursor = Cursor.getDefaultCursor();
					frames = mDesktop.getAllFrames();
					for (int i = 0; i < frames.length; ++i) {
						frames[i].setCursor(cursor);
					}
					setCursor(cursor);
				}
			}
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					resumeCursor();
				}
			});
		}
	}
//menu classes
	class ViewMenu extends JMenu 
			implements ActionListener, ItemListener {
				
		JCheckBoxMenuItem mStatusBarItem;
		JCheckBoxMenuItem mPuiConsoleItem;
		
		ViewMenu() {
			super("View");
			setFont(cMenuFont);

			// jb-01080203 add cardbutton panel view item
			mStatusBarItem = new JCheckBoxMenuItem("Status Bar", true);
			mStatusBarItem.setFont(cMenuFont);
			mStatusBarItem.addItemListener(this);
			mStatusBarItem.setActionCommand("status");

			add(mStatusBarItem);
			
			mPuiConsoleItem = new JCheckBoxMenuItem("Pui Console", false);
			mPuiConsoleItem.setFont(cMenuFont);
			mPuiConsoleItem.addItemListener(this);
			mPuiConsoleItem.setActionCommand("console");

			add(mPuiConsoleItem);

		}
		
		
		public void itemStateChanged(ItemEvent event) {
			JCheckBoxMenuItem item = (JCheckBoxMenuItem)event.getSource();

			String command = item.getActionCommand();

			if (command.equals("status")) {
				_setStatusBarVisible(item.isSelected());
			}
			else if (command.equals("console")) {
				_setPuiConsoleVisible(item.isSelected());
			}


		}
		private void _setPuiConsoleVisible( final boolean visible) {
			if (SwingUtilities.isEventDispatchThread()) {
				setPuiConsoleVisible( visible );

			} else {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setPuiConsoleVisible(visible);

					}
				});
			}
		}
		
		private void _setStatusBarVisible( final boolean visible ) {
			if (SwingUtilities.isEventDispatchThread()) {
				setStatusBarVisible( visible );

			} else {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setStatusBarVisible(visible);

					}
				});
			}
		}

		public void actionPerformed(ActionEvent event) {
		}
		
	

	}
			
	class ConnectionMenu extends JMenu
			implements ActionListener, ConnectListener {
		JMenuItem mConnectItem;
		JMenuItem mDisconnectItem;
		JMenuItem mDeleteItem;
		private Vector mUnitButtons = new Vector();
		private Vector mUnitNames = null;
		private Hashtable mUnits = null;
		


		ConnectionMenu() {
			super("Connect");
			setFont(cMenuFont);

			mConnectItem = new JMenuItem("Connect");
			mConnectItem.setFont(cMenuFont);
			mConnectItem.addActionListener(this);
			add(mConnectItem);

			mDisconnectItem = new JMenuItem("Disconnect");
			mDisconnectItem.setFont(cMenuFont);
			mDisconnectItem.setEnabled(false);
			mDisconnectItem.addActionListener(this);
			add(mDisconnectItem);

			mDeleteItem = new JMenuItem("Delete Unit");
			mDeleteItem.setFont(cMenuFont);
			mDeleteItem.setEnabled(false);
			mDeleteItem.addActionListener(this);
			add(mDeleteItem);

			getClient().addConnectListener((ConnectListener) this);

			loadUnitNames();
		}
		

		private void loadUnitNames() {
			File f = new File(UNITS_FILENAME);

			if (f.exists()) {
				try {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(f));
					//mUnitNames = (Vector) ois.readObject();
					mUnits = (Hashtable) ois.readObject();
				}
				catch (Throwable t) {
					//mUnitNames = new Vector();
					mUnits = new Hashtable();
				}
			} else {
				//mUnitNames = new Vector();
				mUnits = new Hashtable();
			}

			/*for (Enumeration en = mUnitNames.elements(); en.hasMoreElements(); ) {
				addUnitMenuItem((String) en.nextElement());
			}
			*/
			for (Enumeration en = mUnits.keys(); en.hasMoreElements(); ) {
							addUnitMenuItem((String) en.nextElement());
			}
			setUnitButtonsEnabled(true);
		}


		private void storeUnitNames() {
			File f = new File(UNITS_FILENAME);

			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(f));

				//oos.writeObject(mUnitNames);
				oos.writeObject(mUnits);
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}


		public void connectNotify() {
			mDisconnectItem.setEnabled(true);
			mConnectItem.setEnabled(false);

			setUnitButtonsEnabled(false);
		}

		public void disconnectNotify() {
			mDisconnectItem.setEnabled(false);
			mConnectItem.setEnabled(true);

			setUnitButtonsEnabled(true);
		}

		public void actionPerformed(ActionEvent event) {
			JMenuItem item = (JMenuItem) event.getSource();
			String unitName = null;

			if (mUnitButtons.contains(item)) {
				unitName = item.getText();
			}

			if (item == mConnectItem || unitName != null) {
				BasicForm connectForm = new BasicForm(new ConnectTypeSet());
				//JComboBox combo = new JComboBox(mUnitNames);
				JComboBox combo = new JComboBox(mUnits.keySet().toArray());
				
				combo.setEditable(true);
				connectForm.addField("unitName",
						new FormField("5320 Unit Name or IP Address", combo));
				BinderDialog connectDialog = new BinderDialog(GClient.this,
						"Connect", connectForm, "OK", "Cancel");

				if (unitName != null) {
					combo.setSelectedItem(unitName);
					// connectForm.setBoundValue("unitName", unitName);
				}

				connectDialog.setVisible(true);
				Envelope env = connectDialog.getEnvelope();

				if (env != null) {
					try {
						mUnitName = (String) env.getElement("unitName");
						if (mUnitName == null || mUnitName.trim().equals("")) {
							mUnitName = mUnitAddress;
						}
						System.out.println("ConnectionMenu.actionPerformed: " +
								mUnitName);
						/* getHostByName(mUnitName) */
						
						connect(mUnitAddress = getHostByName(mUnitName));
												
						addUnitName(mUnitName, mUnitAddress);

						showMessageDialog("Unit " + mUnitName +
								":  connection successful");
						
						mWindowMenu.autoStart();
					}
					catch (UnknownHostException uhe) {
						showAlertDialog("Connection Problems",
								"Unit '" + env.getElement("unitName") + "' is unknown");
						// Relaunch the login window...
						((ConnectionMenu) mConnectionMenu).mConnectItem.doClick();
					}
					catch (ConnectException ce) {
						showAlertDialog("Connection Problems",
								"Unit '" + env.getElement("unitName") +
								"' is not responding");
						// Relaunch the login window...
						((ConnectionMenu) mConnectionMenu).mConnectItem.doClick();
					}
					catch (UserInterruptedException uie) {
						((ConnectionMenu) mConnectionMenu).mConnectItem.doClick();
					} 
					catch (Exception e) {
						showAlertDialog(e, "Connection Problems");
						// Relaunch the login window...
						((ConnectionMenu) mConnectionMenu).mConnectItem.doClick();
					}
				}
			} else
			if (item == mDisconnectItem) {
				try {
					disconnect();
					// removeUnit();
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(GClient.this, e.toString(),
							"Disconnection Problems", JOptionPane.ERROR_MESSAGE);
				}
			} else
			if (item == mDeleteItem) {
				BasicForm deleteForm = new BasicForm(new ConnectTypeSet());
				//JComboBox combo = new JComboBox(mUnitNames);
				JComboBox combo = new JComboBox(mUnits.keySet().toArray());
				deleteForm.addField("unitName",
						new FormField("5320 Unit Name or IP Address", combo));
				BinderDialog deleteDialog = new BinderDialog(GClient.this,
						"Delete Unit", deleteForm, "OK", "Cancel");

				deleteDialog.setVisible(true);
				Envelope env = deleteDialog.getEnvelope();

				if (env != null) {
					deleteUnitName((String) env.getElement("unitName"));
				}
			}
		}
		
		/* TNS methods */
		public String getHostByName(String unitname) {
				if (mUnits.isEmpty()) return unitname;
				if (mUnits.containsKey(unitname)) {
					return (String)mUnits.get(unitname);
				} 
				else return unitname;
			}
			
			public void updateHosts(String unitname, String address) {
				System.out.println("GClient.updateHosts(),unitname = " + unitname + 
					", address = " + address);
					
				if (mUnits.isEmpty()) {
					addUnitName(unitname, address);
					
				} 
				else {
					if (mUnits.containsKey(unitname)) {
						System.out.println("contains key " + unitname);
						deleteUnitName(unitname);
						addUnitName(unitname, address);
					} else if (mUnits.contains(address)) {
						System.out.println("contains address = " + address);
						String key = null;
						for (Enumeration en = mUnits.keys();en.hasMoreElements();) {
							key = (String)en.nextElement();
							if (address.equals((String)mUnits.get(key))) break;
							else key = null;
						}
						if (key != null) deleteUnitName(key);
						addUnitName(unitname, address);
												
					}
				}
					
		}

		protected void addUnitName(String unitName, String addr) {
					
			if (mUnits != null && mUnits.containsKey(unitName)) {
				return;
			}
			if (addr == null) addr = mUnitName;
			
			System.out.println("GClient.addUnitName() unitName = " + unitName + 
				", address = " + addr);
				
				
			mUnits.put(unitName, addr);
			addUnitMenuItem(unitName);
			storeUnitNames();
			
			/* using hash table instead of vector. 5/23/2002 jsb
			
				if (mUnitNames != null && mUnitNames.contains(unitName)) {
					return;
				}

				mUnitNames.addElement(unitName);

				addUnitMenuItem(unitName);

				storeUnitNames();
			*/
			
		}


		protected void addUnitMenuItem(String unitName) {

			JMenuItem menuItem;

			if (mUnitButtons.isEmpty()) {
				addSeparator();
			}

			menuItem = new JMenuItem(unitName);
			menuItem.setFont(cMenuFont);
			menuItem.setEnabled(false);
			menuItem.addActionListener(this);

			add(menuItem);

			mUnitButtons.addElement(menuItem);

			mDeleteItem.setEnabled(true);
		}


		protected void deleteUnitName(String unitName) {
			if (mUnits.containsKey(unitName)) {
				mUnits.remove(unitName);
			}
			
			JMenuItem menuItem = null;
			
			for (Enumeration en = mUnitButtons.elements(); en.hasMoreElements();) {
				menuItem = (JMenuItem)en.nextElement();
				if (menuItem.getText().equals(unitName)) {
					remove(menuItem);
					break;
				}
			}
			if (menuItem != null) mUnitButtons.removeElement(menuItem);
			if (mUnits.isEmpty()) {
				mDeleteItem.setEnabled(false);
				remove(getItemCount() - 1);
			}
			
			storeUnitNames();
		}


		protected void setUnitButtonsEnabled(boolean enabled) {
			for (Enumeration en = mUnitButtons.elements();
					en.hasMoreElements(); ) {
				((JMenuItem) en.nextElement()).setEnabled(enabled);
			}
		}
	}


	class WindowMenu extends JMenu implements ActionListener, ConnectListener {
		JMenuItem mNewItem;
		Vector mClientViewItems = new Vector();
		JMenuItem mTileWindowItem;
		
		WindowMenu() {
			super("Windows");
			setFont(cMenuFont);

			mNewItem = new JMenuItem("New Window");
			mNewItem.setFont(cMenuFont);
			mNewItem.addActionListener(this);
			mNewItem.setEnabled(false);
			add(mNewItem);
			
			mTileWindowItem = new JMenuItem();
			mTileWindowItem.setFont(cMenuFont);
			mTileWindowItem.setAction( new TileAction( "Tile Windows", getDesktop() ) );
			mTileWindowItem.setEnabled(false);
			addSeparator();
			add(mTileWindowItem);
			
			getClient().addConnectListener((ConnectListener) this);
		}


		public void connectNotify() {
			mNewItem.setEnabled(true);
			mTileWindowItem.setEnabled(true);
		}


		public void disconnectNotify() {
			mNewItem.setEnabled(false);
			mTileWindowItem.setEnabled(true);
			
		}


		protected void autoStart() {
			Runnable ct = new Runnable() {
				public void run() {
					mHelpMenu.setEnabled(false);
					mWindowMenu.setEnabled(false);
					mViewMenu.setEnabled(false);
					
					createClientView();
					
					mHelpMenu.setEnabled(true);
					mWindowMenu.setEnabled(true);
					mViewMenu.setEnabled(true);					
				}
			};
			
			if (mClientViewItems.size() == 0) {
				setStatus(getMessage("busy.hardware"));
											
				Thread t = new Thread(ct, "DoubleBuffer");
				t.setDaemon(true);
				t.start();
				
			}
		}


		public void actionPerformed(ActionEvent event) {
			JMenuItem item = (JMenuItem) event.getSource();

			if (item == mNewItem) {
				createClientView();
			}
						
			if (item instanceof ClientViewItem) {
				((ClientViewItem) item).activate();
			}
		}
		
		
		private void createClientView() {
			
			System.out.println("GClient.createClientView() - current Thread = " + Thread.currentThread().getName());
			
			ClientView clientView;

			try {
				suspendCursor();
				System.out.println("GClient.createClientView()");
				clientView = new ClientView(GClient.this);
				Point pLocation = null;
				
				/* jb-01071101 */
				/* stagger internal frames */
				JInternalFrame[] frames = getDesktop().getAllFrames();
				for (int i = 0; i < frames.length; ++i) {
						if (frames[i].isSelected()) {
							pLocation = frames[i].getLocation();
							break;
						}
				}
				
				if (pLocation != null) {
					clientView.setLocation(new Point(pLocation.x+40, pLocation.y+40));
					
				}
				
						
				getDesktop().add(clientView);
				
				addViewItem(new ClientViewItem(clientView.getTitle(), clientView));
				clientView.show();
				resumeCursor();
				System.out.println("GClient.createClientView() - end.");				
			}
			catch (Exception e) {
				resumeCursor();
				showAlertDialog(e);
				
			}
			

		}

		void addViewItem(ClientViewItem item) {
			if (mClientViewItems.size() == 0) {
				addSeparator();
			}

			item.addActionListener(this);
			mClientViewItems.addElement(item);
			add(item);
		}

		void removeViewItem(ClientViewItem item) {
			mClientViewItems.removeElement(item);
			remove(item);
			if (mClientViewItems.size() == 0) {
				remove(getItemCount() - 1);
			}
		}
	}
	class TileAction extends AbstractAction {
		private JDesktopPane mDesktop;
		public TileAction(String label, JDesktopPane desktop) {
			super(label);
			mDesktop = desktop;
		}
		public void actionPerformed( ActionEvent event ) {
			/* jb-01083101 warn user if screen resulution falls below tolerance. (800/600) currently. */
			if (!isScreenSizeValid()) {
				String msg = "Your screen resolution is below the recommended size. ";
				msg += "Continue Anyway?";
				if (!showConfirmDialog("Invalid Screen Resolution", msg)) return;
			}
				
			JInternalFrame[] frames = mDesktop.getAllFrames();
			int count = frames.length;
			if (count==0) return;
			
			int square = (int)Math.sqrt(count);
			int rows = square;
			int cols = square;
			
			if (rows * cols < count) {
				++cols;
				if (rows * cols < count) {
					++rows;
				}
			}
			
			Dimension desktopSize = mDesktop.getSize();
			
			int newWidth = desktopSize.width / cols;
			int newHeight = desktopSize.height / rows;
			int newX = 0;
			int newY = 0;
			
			for (int i = 0; i < rows; ++i) {
				for (int j = 0; j < cols && ((i * cols) + j < count); ++j) {
					JInternalFrame frame = frames[(i * cols) + j];
					ClientView view = (ClientView)frame;
					
					/* jb-01090103 - hide buspanel when tiling windows */
					view.setCardButtonPanelVisible( false );
					
					if (frame.isIcon()) {
						try {
							frame.setIcon(false);
						}
						catch (PropertyVetoException pve) {
						}
						
					}
					mDesktop.getDesktopManager().resizeFrame(frame, newX, newY, newWidth, newHeight);
					newX += newWidth;
				}
				newY += newHeight; 
				newX = 0; //reset x position
			}
		}
		/* jb-01083101 - continued */
		private boolean isScreenSizeValid() {
			int MIN_WIDTH = 800;
			int MIN_HEIGHT = 600;
			Dimension currentResolution = Toolkit.getDefaultToolkit().getScreenSize();
			System.out.println(currentResolution);
			return (currentResolution.width > MIN_WIDTH && currentResolution.height > MIN_HEIGHT);
		}
		
	} //end action

	class ClientViewItem extends JMenuItem
			implements PropertyChangeListener, ClientView.DisposeListener {
		ClientView mClientView;

		ClientViewItem(String name, ClientView view) {
			super(name);
			setFont(cMenuFont);
			mClientView = view;
			view.addPropertyChangeListener(this);
			view.addDisposeListener(this);
		}

		ClientView getClientView() {
			return mClientView;
		}

		public void propertyChange(PropertyChangeEvent event) {
			if (event.getPropertyName().equals(JInternalFrame.TITLE_PROPERTY)) {
				setText(mClientView.getTitle());
			}
		}

		public void dispose() {
			mWindowMenu.removeViewItem(this);
		}

		void activate() {
			try {
				mClientView.setIcon(false);
				mClientView.show();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class HelpMenu extends JMenu implements ActionListener {
		JMenuItem mAboutItem;
		JMenuItem mHelpItem;
		JMenuItem mVersionItem;
		
		HelpDialog mHelpDialog = null;

		HelpMenu() {
			super("Help");
			setFont(cMenuFont);

			mHelpItem = new JMenuItem("TSI 5320 Help");
			mHelpItem.setFont(cMenuFont);
			mHelpItem.addActionListener(this);
			add(mHelpItem);
			mHelpItem.setEnabled(false);
			addSeparator();

			mAboutItem = new JMenuItem("About TSI 5320");
			mAboutItem.setFont(cMenuFont);
			mAboutItem.addActionListener(this);
			add(mAboutItem);
			mAboutItem.setEnabled(false);
			
			/* jb-01062501 add build and debug to menu*/
			mVersionItem = new JMenuItem("Version " + mVersion + ((mDebug==true) ? " Debug " : ""));
			mVersionItem.setFont(cMenuFont);
			mVersionItem.setEnabled( false );
			add( mVersionItem );
			
			
			
		}

		public void actionPerformed(ActionEvent event) {
			JMenuItem item = (JMenuItem) event.getSource();

			if (mHelpDialog == null) {
				suspendCursor();
				mHelpDialog = new HelpDialog(GClient.this);
				resumeCursor();
			} else {
				mHelpDialog.show();
			}

			if (item == mAboutItem) {
				mHelpDialog.getHelp().setCurrentID("about");
			}
		}
	}


	public static JWindow createSplashWindow() {
		ImageIcon icon = null;

		try {
			icon = new ImageIcon(
					GClient.class.getResource("icons/TSI5320Splash.gif"));
		}
		catch (Exception e) {
			throw new RuntimeException("cannot load icons/TSI5320Splash.gif");
		}
		JWindow splashWindow = new JWindow();
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new BevelBorder(BevelBorder.RAISED));
		java.awt.Container c = splashWindow.getContentPane();
		c.setLayout(new BorderLayout());
		JLabel l = new JLabel(icon);
		p.add(l, BorderLayout.CENTER);
		c.add(p, BorderLayout.CENTER);

		Dimension d = splashWindow.getPreferredSize();
		splashWindow.setSize(d);

		Dimension s = splashWindow.getToolkit().getScreenSize();
		Point loc = new Point((s.width - d.width) / 2, (s.height - d.height) / 2);
		splashWindow.setLocation(loc);

		return splashWindow;
	}
/*

	private void authenticateUser() throws UserSecurityException {
		if (!mConnected) throw new UserSecurityException("Must be connected before authenticating.");
		
		PComponent user = getPClient().getRoot().getSubComponent("user");
		Boolean enabled = (Boolean)user.get("authEnabled");
		if (enabled.equals(Boolean.FALSE)) {
			// this means Access Control is not required for this device.
			return;
		}
		
		
		
	}
	
*/


	public static void main(String[] args) {

		try {
					BinderUtilities.initProperties();
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		// Remove <cr> mapping for JTextFields.
		//
		// NOTE: this will have to be changed when we add javahelp,
		// since it blocks the <cr> ActionEvent on an important
		// stand-alone JTextField in the help search feature.
		//
		// See the 1569 gui for a better approach.
		//
		// BinderUtilities.initKeymaps();
		/* jb-01062701 Change disabled Binder foreground to a darker, more readable color. */
		UIManager.put("ComboBox.disabledForeground",
			UIManager.getColor("ComboBox.disabledForeground").darker());
			
		/* jb-01062501 Add Build Info to app */
		
		BuildInfo info = new BuildInfo();
		mDebug = info.isDebug();
		mVersion = info.getVersion();
		
		JWindow splash = createSplashWindow();
		splash.setVisible(true);

		createClient("TSI 5320");

		splash.dispose();
	}
	
	public String getMessage(String key) {
		if (mSystemMessages != null) return mSystemMessages.get(key);
		else return null;
	}
	

	ClientDesktopManager getDesktopManager() {
		return (ClientDesktopManager) mDesktop.getDesktopManager();
	}


	static class ClientDesktopManager extends DefaultDesktopManager {
		ClientDesktopManager() {
		}


		// Expand scope of this method so ClientView can
		// force DefaultDesktopManager to call manager.getBoundsForIcon()
		// and so call getPreferredSize() on the JInternalFrame.JDesktopIcon.
		//
		// See ClientView#setTitle
		//
		public void setWasIcon(JInternalFrame frame, Boolean value) {
			super.setWasIcon(frame, value);
		}
	}
	
	/** BuildInfo encapsulates the reading of the system-wide application properties file.
	  * PROP_FILE refers to {PROP_FILE}.properties.  ResourceBundle searches the class
	  * path for this file.
	  * author: jeff blau
	  * date:   6/25/2001
	  * jb-01062502
	  */
	static class BuildInfo {
		static boolean isDebug = false;
		static String mVersion = "Unknown";
		static final String PROP_FILE = "META-INF/tsi5320.properties";
		static int mRefreshRate;
		
		BuildInfo() {
			getProperties();
		}
		private void getProperties() {
			try {
				final Properties properties = new Properties();
				try {
				properties.load(getClass().getClassLoader().getResourceAsStream(PROP_FILE));
				}
				catch (Exception e) {
					throw new RuntimeException("cannot load " + PROP_FILE);
				}
				
				String version = properties.getProperty( "version" );
				if (version != null) mVersion = version;
				String debug = properties.getProperty( "debug" );
				if (debug != null) mDebug = (debug.equals("1")) ? true : false;
				String refreshRate = properties.getProperty("refreshRate");
				if (refreshRate != null) mRefreshRate = (new Integer(refreshRate)).intValue();
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		public int getRefreshRate() {
			return mRefreshRate;
		}
		
		public boolean isDebug() {
			return mDebug;
		}
		public String getVersion() {
			return mVersion;
		}
		
	} //end class BuildInfo
			
}
