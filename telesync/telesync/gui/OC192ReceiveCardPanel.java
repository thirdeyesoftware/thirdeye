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
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.rxdatapath.*;
import telesync.gui.toh.*;
import telesync.gui.icons.binders.*;



public class OC192ReceiveCardPanel extends CardPanel {

	private ModeButtonForm mMainForm;
	private HeatForm mHeatForm;
	// private OC48CForm mOC48CForm;
	private OC192TOHButtonForm mTOHForm;
	private OC192DataPathPanel mDataPathPanel;
	private Rx192temperature mTemperature;
	private Oc192c_rx mOC192c;
	private Rx192datapath mDataPath;
	private TOH mTOH;
	private ProgressBinder mInitProgressBinder;
	private boolean mSetupLock = true;
	private IndicatorPanel b3Panel, patternPanel;
	private Vector mFlagListeners = new Vector();
	
	private PStateChangeConnector mHeatChangeConnector;
	private PStateChangeListener mHeatChangeListener;

	private PStateChangeConnector mDataPathChangeConnector;
	private PStateChangeListener mDataPathChangeListener;

	private JPanel mErrorPanel;
	private Hashtable mErrorEventListeners;
	

	// private PStateChangeConnector mOC48CChangeConnector;
	// private PStateChangeListener mOC48CChangeListener;

	private PStateChangeConnector mTOHChangeConnector;
	private PStateChangeListener mTOHChangeListener;


	// private JButton mOC48EnableButton;
	// private JButton mOC48ChangeButton;

	private Mode mMode;
	private JTabbedPane mTabbedPane;

	static private int cID = 0;
	private int mID = cID++;



	protected OC192ReceiveCardPanel(ClientView clientView, Card card,
			int slotNumber) throws Exception {
		super(clientView, card, slotNumber);

		//System.out.println("OC48ReceiveCardPanel.<init>: " + getInstanceID());
	}


	protected void createPeers() throws Exception {
		mTemperature = (Rx192temperature) getCard().getSubComponent("temperature");

		mDataPath = (Rx192datapath)
				getCard().getSubComponent("datapath");
		mOC192c = (Oc192c_rx) mDataPath.getSubComponent("oc192c");
		
		mTOH = (TOH)
			getOC192C().getSubComponent("TOH");
	  
	  
		
	}


	protected void createPeerListeners() throws Exception {
		mTemperature.addStateChangeListener(mHeatChangeConnector =
				new PStateChangeConnector(mHeatChangeListener =
				new HeatChangeListener()));
		mDataPath.addStateChangeListener(mDataPathChangeConnector =
				new PStateChangeConnector(mDataPathChangeListener =
				new DataPathChangeListener()));
		mTOH.addStateChangeListener(mTOHChangeConnector =
				new PStateChangeConnector(mTOHChangeListener =
				new TOHChangeListener()));
	}


	protected JComponent createContentComponent() throws Exception {
		mTabbedPane = new JTabbedPane();
		JTabbedPane tp = mTabbedPane;
		// change layout to include error status panel.
		
		mTitleForm.add(Box.createVerticalStrut(5));
		mTitleForm.add(clearFlagsButton, BorderLayout.SOUTH);
		getTitlePanel().add(Box.createHorizontalGlue());
		
		mErrorPanel = new JPanel();
		JButton btnHistory = ButtonMaker.createPButton("Clear History", true);
		
		btnHistory.setIcon(
					new ImageIcon( CardPanel.class.getResource("icons/cards/ErrorIcon.gif")) );
		btnHistory.setHorizontalTextPosition(SwingConstants.LEFT);
		
		btnHistory.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
						//do something here...
				for (Enumeration e = mErrorEventListeners.elements(); e.hasMoreElements();) {
					IndicatorPanel panel = (IndicatorPanel)e.nextElement();
					panel.resetHistory();
				}
			}
		});
		
		/*jb-01092001 - Move clear error button to error panel */
		JButton btnError = ButtonMaker.createPButton("Clear Errors", true); 
			
		btnError.setIcon( 
					new ImageIcon( CardPanel.class.getResource("icons/cards/indicator_error.gif")) );
		btnError.setHorizontalTextPosition(SwingConstants.LEFT);
		
		btnError.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				//do something here...
				try {
					((Rx192)getCard()).clearErrors( true );
									
					b3Panel.setErrorIndicator( false );
					patternPanel.setErrorIndicator( false );
					
				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog( e );
					e.printStackTrace();
				}
			}
		});

		//mMainForm.addButton( btnError );
		/* end task jb-2 */
		//mErrorPanel.add( Box.createHorizontalStrut(0));
		/*mErrorPanel.add( clearFlagsButton );
		mErrorPanel.add( Box.createHorizontalStrut(2));		
		*/
		mErrorPanel.add( btnError );
		mErrorPanel.add( Box.createHorizontalStrut(2));
		mErrorPanel.add( btnHistory );
		mErrorPanel.add( Box.createHorizontalStrut(2) );
		
		
		JPanel containerPanel = new JPanel();
		
		
		mErrorPanel.setBorder( new EtchedBorder() );
		mErrorPanel.setLayout( new FlowLayout(FlowLayout.RIGHT, 1,1) );
		containerPanel.setLayout(new BorderLayout());
		
		//create errorPanel
		IndicatorPanel lopPanel, losPanel, b1Panel, b2Panel;
		IndicatorPanel lolPanel, lofPanel; 
				
		lolPanel = new IndicatorPanel("LOL", "Loss of Light");
		lolPanel.setInverted( false );
		mErrorEventListeners = new Hashtable();
		
		mErrorEventListeners.put("loss_light", lolPanel);
				
		losPanel = new IndicatorPanel("LOS", "Loss Of Signal");
		losPanel.setInverted( false);
		mErrorEventListeners.put("loss_signal", losPanel);
		lofPanel = new IndicatorPanel("LOF", "Loss of Frame");
		lofPanel.setInverted( false );
		mErrorEventListeners.put("loss_framing", lofPanel);
		lopPanel = new IndicatorPanel("LOP", "Loss Of Pattern");
		lopPanel.setInverted( false );
		mErrorEventListeners.put("loss_pattern", lopPanel);
		b1Panel = new IndicatorPanel("B1", "B1 Errors");
		b1Panel.enableLatch( true );
		
		mErrorEventListeners.put("b1Errors", b1Panel);
		b2Panel = new IndicatorPanel("B2", "B2 Errors");
		b2Panel.enableLatch( true );
		mErrorEventListeners.put("b2Errors", b2Panel);
		b3Panel = new IndicatorPanel("B3", "B3 Errors");
		b3Panel.enableLatch( true );
		mErrorEventListeners.put("b3Errors", b3Panel);
		patternPanel = new IndicatorPanel("PAT","Pattern Errors");
		patternPanel.enableLatch( true );
		mErrorEventListeners.put("ptErrors", patternPanel);	
		
		mErrorPanel.add(lolPanel);
		mErrorPanel.add(losPanel);
		mErrorPanel.add(lofPanel);
		mErrorPanel.add(lopPanel);
		mErrorPanel.add( Box.createHorizontalStrut(1) );
		mErrorPanel.add(b1Panel);
		mErrorPanel.add(b2Panel);
		mErrorPanel.add(b3Panel);
		mErrorPanel.add(patternPanel);
		
		
		containerPanel.add(mErrorPanel, BorderLayout.NORTH);
				
				
		// Main Form
		//
		mMainForm = new ModeButtonForm("OC-192 Receive Card Status",
				new MainTypeSet()) {
			public void setMode(Mode mode) {
				if (mode.equals(Mode.US_SONET)) {
					setTitle("OC-192 Receive Card Status");
				} else {
					setTitle("STM-16 Receive Card Status");
				}
			}
			
		};


		PEnvelope penv = new PEnvelope(new MainTypeSet());
		Integer stepMax;

		penv.mergePComponent(getCard());
		if (penv.hasElement("mode")) {
			mMode = (Mode) penv.getElement("mode");
		}
		if (penv.hasElement("configMax")) {
			stepMax = (Integer) penv.getElement("configMax");
		} else {
			stepMax = new Integer(20);
		}

		updateErrorPanel(penv, true);
		HighlightBooleanBinder bb;
		ComboBinder cb;
		mMainForm.addField("mode",
				cb = new ComboBinder("Mode", Mode.class,
				Mode.getInstances()), Binder.HIDE_EVENTS);
						
		BooleanBinder boolBinder;
		// jb-01080302 implement autoscan
		//AutoScan == true equates to setuplock == false
		mMainForm.addField("setupLock", boolBinder = new BooleanBinder("AutoScan"), Binder.HIDE_EVENTS);
		boolBinder.setIcon(BlackDot.getImageIcon());
		boolBinder.setOnText("ON");
		boolBinder.setOffText("OFF");
		boolBinder.setInverted(true);
				
		mMainForm.addField("init_done",
				bb = new HighlightBooleanBinder("Initialization", "init_done",false,
				UIManager.getColor("pippin.colors.alertRed")), Binder.HIDE_EVENTS, Binder.HIDE_COMPONENT);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("Complete");
		bb.setOffText("In Progress");
		bb.setIcon(BlackDot.getImageIcon());
		
		
		
		mMainForm.addField("status",
				mInitProgressBinder =
				new ProgressBinder("Initialization Status",
				stepMax.intValue()), Binder.HIDE_EVENTS);

		mMainForm.addField("b1Errors",
				new FlagDecider("B1 Errors"),true);
		mMainForm.addField("b2Errors",
				new FlagDecider("B2 Errors"),true);
		mMainForm.addField("frameErrors",
				new FlagDecider("Frame Errors"),true);

		OpticalPowerBinder pb = new OpticalPowerBinder("Optical Power","optpwrDB");
		
		
		pb.setUnits("dbm");
		mMainForm.addField("optpwrDB", pb, Binder.HIDE_EVENTS);

		mMainForm.addField("loss_light",
				bb = new HighlightBooleanBinder("Light", "loss_light", false,
				UIManager.getColor("pippin.colors.alertRed")),Binder.RECEIVE_EVENTS);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("LOL");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());

		mMainForm.addField("loss_framing",
				bb = new HighlightBooleanBinder("Framing", "loss_framing", false,
				UIManager.getColor("pippin.colors.alertRed")),Binder.RECEIVE_EVENTS);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("Invalid");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());

		mMainForm.addField("loss_pattern",
				bb = new HighlightBooleanBinder("Pattern", "loss_pattern",false,
				UIManager.getColor("pippin.colors.alertRed")),true);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("Lost");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());

		mMainForm.addField("mpll_los",
				bb = new HighlightBooleanBinder("Mux PLL", "mpll_los",false,
				UIManager.getColor("pippin.colors.alertRed")));
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("No Sync");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());

		mMainForm.addField("serialNumber", new FormField("Serial Number", 150));

		mMainForm.setEnabled(false);

		
		
		JButton b;
		b = ButtonMaker.createPButton("Reset Card");
		b.setToolTipText("Reset Card in slot " + mSlotNumber);
		b.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (mClientView.getGClient().showConfirmDialog("Reset Card","Are you sure you want to reset this Card?")) {
					try {
						mClientView.getBusPanel().getSlotAt(mSlotNumber).reset();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		b.setEnabled(true);
		//mMainForm.addButton(b);

		b = ButtonMaker.createPButton("Change");
		
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change Main Settings", new MainChangePanel());
				d.setVisible(true);
				Envelope env = d.getEnvelope();
				/* jb-062401 Mode was not applied to component... 
				 * req'd to create 2 envelope instances.  
				 */
				Envelope envMode = d.getEnvelope();
				
				if (env != null) {
					try {
						
						PEnvelope pe = new PEnvelope(env);
						
						pe.removeElement("setupLock");
						
						pe.applyToPComponent(getCard());
						
						mMainForm.setMode(mMode);
						
						pe = new PEnvelope( envMode );
						pe.removeElement("mode");
						pe.removeElement("name");
						
						pe.applyToPComponent(mDataPath);

					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		
		mMainForm.addButton(b);

		penv.mergePComponent(mDataPath);
		mMainForm.setEnvelope(penv,true);
		if (penv.hasElement("configCount")) {
			mInitProgressBinder.setStepCount(
					((Integer) penv.getElement("configCount")).intValue());
		}
		mMainForm.setMode( mMode );
		
		ErrorEvent event;
		
		if (penv.hasElement("b3Errors")) {
			boolean err = ((Boolean)penv.getElement("b3Errors")).booleanValue();
			if (err) {
				event = new ErrorEvent(this, penv.getElement("b3Errors"));
				event.setIsInit( false );
				((ErrorEventListener)mErrorEventListeners.get("b3Errors")).errorEventNotify(event);	
			} 
				
		}
		if (penv.hasElement("ptErrors")) {
			boolean err = ((Boolean)penv.getElement("ptErrors")).booleanValue();
			if (err) {
				event = new ErrorEvent(this, penv.getElement("ptErrors"));
				event.setIsInit( false );
				((ErrorEventListener)mErrorEventListeners.get("ptErrors")).errorEventNotify(event);	
			}
			
		}
		
			
			
		addClearFlagEventListener(mMainForm);
		
		tp.add("Status", mMainForm);
		
		if (penv.hasElement("name")) {
			setCardName((String)penv.getElement("name"));
			refreshTitleLabel();
		}
		
		
		//
		// Main Form



		

		// DataPath Panel
		//
		TitlePanel titlePanel = new TitlePanel("DataPath Composition",
				mDataPathPanel = new OC192DataPathPanel(mDataPath), true, false);

		
		tp.add("DataPath", titlePanel);
		
		mDataPathPanel.setMode(mMode);
		
		/* include these as workaround for firmware blackhole (b3, pt errors) */
		//mDataPathPanel.addErrorEventListener("b3Errors", b3Panel);
		//mDataPathPanel.addErrorEventListener("patternErrs", patternPanel);
		
		
		if (penv.hasElement("setupLock")) {
			mDataPathPanel.setSetupLock(((Boolean)penv.getElement("setupLock")).booleanValue());
		} else mDataPathPanel.setSetupLock(false);
		
		addClearFlagEventListener(mDataPathPanel);
		
		
		//
		// DataPath Panel



		// TOH Form
		//

		/* TOH isn't in the f/w yet.
		*/
		mTOHForm = new OC192TOHButtonForm("Transport Overhead Settings");

		mTOHForm.setEnabled(false);
		addClearFlagEventListener(mTOHForm);
		/*b = ButtonMaker.createPButton("Change");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				TOHPanel mPanel;
				TOH toh;
				BinderDialog d;
				PEnvelope pe = new PEnvelope(TOHTypeSet.class);
				try {
					toh = getTOH();
					d = new BinderDialog(GClient.getClient(),
							"Change Transport Overhead",
							mPanel = new TOHPanel(getTOH(), getMode()));
					d.setColumnHeader(mPanel.getColumnHeader());
					pe.mergePComponent(toh);
					mPanel.setEnvelope(pe);
					d.setVisible(true);

					Envelope env = d.getEnvelope();
					if (env != null) {
						PEnvelope pen = new PEnvelope(env);
						pen.applyToPComponent(getTOH());
						
					}
				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog(e);
					e.printStackTrace();
				}
			}
		});
		mTOHForm.addButton(b);
		*/

		penv = new PEnvelope(new TOHTypeSet());
		penv.mergePComponent(getTOH());
		mTOHForm.setEnvelope(penv);
		mTOHForm.setMode(mMode);
		
		
		tp.add("Transport Overhead", mTOHForm);
		
		//
		// TOH Form
		/*
		*/



		// Heat Form
		//
		mHeatForm = new HeatForm("Thermometers",
				new HeatTypeSet());
		mHeatForm.addField("topTemp", new HeatBinder("Top Temperature"));
		mHeatForm.addField("botTemp",
				new HeatBinder("Bottom Temperature"));

		mHeatForm.setEnabled(false);


		penv = new PEnvelope(new HeatTypeSet());
		penv.mergePComponent(mTemperature);
		mHeatForm.setEnvelope(penv);
		addClearFlagEventListener(mHeatForm);
		tp.add("Temp", mHeatForm);
		//
		// Heat Form



		// Init the tabbed pane according to the card's initialization
		// state.  Do this after all the tabs are set up.
		penv = new PEnvelope(new MainTypeSet());
		penv.mergePComponent(getCard());
		if (penv.hasElement("init_done")) {
			setCardInitDone((Boolean) penv.getElement("init_done"));
		}
		
		containerPanel.add(tp, BorderLayout.CENTER);
		return containerPanel;
		
	}

	public void showErrorPanel( boolean visible ) {
		if (mErrorPanel != null) mErrorPanel.setVisible( visible );
	}
	
	public void doStateChange(PStateChangeEvent event) {
		PEnvelope env;

		if (event.getSource() == getCard()) {
			
			env = new PEnvelope(mMainForm.getEnvelope());
						
			env.mergePAttributeList(event.getAttributes());
			
			boolean initDone = false;
			if (env.hasElement("init_done")) {
				
				
				initDone = ((Boolean)env.getElement("init_done")).booleanValue();
				setCardInitDone((Boolean) env.getElement("init_done"));
				if (initDone) {
					mMainForm.setEnvelope(env);
				} else
					mMainForm.setEnvelope(env,true);
				
			}
			updateErrorPanel( env );
			
			if (env.hasElement("configCount")) {
				mInitProgressBinder.setStepCount(((Integer)
						env.getElement("configCount")).intValue());
			}
			if (env.hasElement("configMax")) {
				mInitProgressBinder.setStepMax(((Integer)
						env.getElement("configMax")).intValue());
			}
			if (env.hasElement("name")) {
				setCardName((String)env.getElement("name"));
				if (getClientView().hasCardPanel(this)) {
					getClientView().setTitle(getTitle());
				}
				refreshTitleLabel();
				
			}
			
			if (env.hasElement("mode")) {
								
				Mode mode = (Mode) env.getElement("mode");
				titleLabel.setText(getLabelText());
				
				mMode = mode;
				if (mDataPathPanel != null) {
					mDataPathPanel.setMode(mode);
				}
				if (mTOHForm != null) {
				 	mTOHForm.setMode(mode);
				}
				if (mMainForm != null) {
					mMainForm.setMode(mode);
				}
				
			}
					
										
		}
	}
	private void updateErrorPanel( Envelope env, boolean init ) {
								
				ErrorEvent event;
				for (Enumeration e = mErrorEventListeners.keys(); e.hasMoreElements();) {
					String key = (String)e.nextElement();
					if (env.hasElement(key)) {
						
						event = new ErrorEvent(this, env.getElement(key));
						event.setIsInit( init );
						((ErrorEventListener)mErrorEventListeners.get(key)).errorEventNotify(event);
						
					}
		}
	}
	
	private void updateErrorPanel( Envelope env ) {
		updateErrorPanel(env, false);
	}
	
	protected Oc192c_rx getOC192C() {
		return mOC192c;
	}


	protected TOH getTOH() {
		return mTOH;
	}


	public void dispose() throws Exception {
		super.dispose();
		if (mTemperature != null) {
			mTemperature.removeStateChangeListener(mHeatChangeConnector);
		}
		mTemperature = null;

		if (mTOH != null) {
			mTOH.removeStateChangeListener(mTOHChangeConnector);
		}
		mTOH = null;

		// if (mOC48c != null) {
		// 	mOC48c.removeStateChangeListener(mOC48CChangeConnector);
		// }
		mOC192c = null;

		if (mDataPath != null) {
			mDataPath.removeStateChangeListener(mDataPathChangeConnector);
		}
		mDataPath = null;
		mDataPathPanel.dispose();
	}



	public static class MainTypeSet extends TypeSet {
		public MainTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("serialNumber", String.class),
				new TypeSetElement("init_done", Boolean.class),
				new TypeSetElement("status", String.class),
				new TypeSetElement("configCount", Integer.class),
				new TypeSetElement("configMax", Integer.class),
				new TypeSetElement("mode", Mode.class),
				//new TypeSetElement("opticalPower", Integer.class),
				new TypeSetElement("optpwrDB", Integer.class),
				new TypeSetElement("loss_light", Boolean.class),
				new TypeSetElement("loss_framing", Boolean.class),
				new TypeSetElement("loss_pattern", Boolean.class),
				new TypeSetElement("mpll_los", Boolean.class),
				new TypeSetElement("b1Errors", Long.class),
				new TypeSetElement("b2Errors", Long.class),
				new TypeSetElement("frameErrors", Long.class),

				new TypeSetElement("setupLock", Boolean.class),
				new TypeSetElement("loss_signal", Boolean.class),
				//new TypeSetElement("clearErrors",Boolean.class),
				new TypeSetElement("name", String.class),
				new TypeSetElement("b3Errors", Boolean.class),
				new TypeSetElement("ptErrors", Boolean.class),
				
				new TypeSetElement("testRunning", Boolean.class),
				new TypeSetElement("timeIntoTest", Integer.class),
				
			});

			getElement("mode").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Mode) o).getValue();
				}
			});
			getElement("mode").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Mode.getInstanceFor((Integer) o);
				}
			});
		}
	}


	public static class HeatTypeSet extends TypeSet {
		public HeatTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("botTemp", Integer.class),
				new TypeSetElement("topTemp", Integer.class),
			});
		}
	}


	public static class OC192CTypeSet extends TypeSet {
		public OC192CTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("rateEnabled", Boolean.class),
				new TypeSetElement("payloadType", Oc192c_rx.PayloadType.class),
				new TypeSetElement("pattern", Oc192c_rx.Pattern.class),
				new TypeSetElement("patternInvert", Boolean.class),
				new TypeSetElement("pointerError", Boolean.class),
				new TypeSetElement("b3Errors", Long.class),
				new TypeSetElement("patterErrs", Long.class),
				new TypeSetElement("patternLoss", Boolean.class),
				new TypeSetElement("ptrace", String.class),
				new TypeSetElement("C2", Integer.class),
				new TypeSetElement("G1", Integer.class),
				new TypeSetElement("F2", Integer.class),
				new TypeSetElement("H4", Integer.class),
				new TypeSetElement("Z3", Integer.class),
				new TypeSetElement("Z4", Integer.class),
				new TypeSetElement("Z5", Integer.class),
			});

			getElement("payloadType").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc192c_rx.PayloadType) o).getValue();
				}
			});
			getElement("payloadType").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc192c_rx.PayloadType.getInstanceFor((Integer) o);
				}
			});

			getElement("pattern").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc192c_rx.Pattern) o).getValue();
				}
			});
			getElement("pattern").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc192c_rx.Pattern.getInstanceFor((Integer) o);
				}
			});
		}
	}


	public static class POHTypeSet extends TypeSet {
		public POHTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("ptrace", String.class),
				new TypeSetElement("C2", Integer.class),
				new TypeSetElement("G1", Integer.class),
				new TypeSetElement("F2", Integer.class),
				new TypeSetElement("H4", Integer.class),
				new TypeSetElement("Z3", Integer.class),
				new TypeSetElement("Z4", Integer.class),
				new TypeSetElement("Z5", Integer.class),
			});
		}
	}



	protected class MainChangePanel extends BasicForm {
		protected MainChangePanel() {
			super(new MainTypeSet());

			PEnvelope env = new PEnvelope(new MainTypeSet());
			try {
				env.mergePComponent(getCard());
				env.mergePComponent(mDataPath);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			addField("mode", new ComboBinder("Mode", Mode.class,
				Mode.getInstances()),Binder.HIDE_EVENTS);
			
			BooleanBinder bb;
			
			addField("setupLock", bb = new BooleanBinder("AutoScan"), Binder.HIDE_EVENTS);
			bb.setInverted(true);
			
			addField("name", new FormField("Card Name"),Binder.HIDE_EVENTS);
			
			setEnvelope(env);
		}


		public void start() {
			// Start any needed listeners.
		}


		public void dispose() {
			super.dispose();

			// Disconnect any listeners from start().
		}
	}


	protected class HeatChangeListener
			implements PStateChangeListener {
		public void stateChanged(PStateChangeEvent event) {
			try {
				PEnvelope env = new PEnvelope(mHeatForm.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				mHeatForm.setEnvelope(env);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	protected class DataPathChangeListener
			implements PStateChangeListener {
		public void stateChanged(PStateChangeEvent event) {
			try {
				
				PEnvelope env = new PEnvelope(mMainForm.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				mMainForm.setEnvelope(env);
				Boolean bSetupLock = (Boolean)env.getElement("setupLock");
				if (mDataPathPanel != null && bSetupLock != null) 
					mDataPathPanel.setSetupLock( bSetupLock.booleanValue() );
				
				//System.out.println(event.getAttributes());
				
				updateErrorPanel(env);
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	// protected class OC48CChangeListener
	// 		implements PStateChangeListener {
	// 	public void stateChanged(PStateChangeEvent event) {
	// 		try {
	// 			PEnvelope env = new PEnvelope(mOC48CForm.getEnvelope());
	// 			env.mergePAttributeList(event.getAttributes());
	// 			mOC48CForm.setEnvelope(env);

	// 			if (env.hasElement("rateEnabled")) {
	// 				Boolean b = (Boolean) env.getElement("rateEnabled");
	// 				setOC48Enabled(b.booleanValue());
	// 			}
	// 		}
	// 		catch (Exception e) {
	// 			e.printStackTrace();
	// 		}
	// 	}
	// }


	// private void setOC48Enabled(boolean enabled) {
	// 	if (enabled) {
	// 		mOC48EnableButton.setEnabled(false);
	// 		mOC48ChangeButton.setEnabled(true);
	// 	} else {
	// 		mOC48EnableButton.setEnabled(true);
	// 		mOC48ChangeButton.setEnabled(false);
	// 	}
	// }


	// Used by setCardInitDone()
	private Component mInitPendingComponent = null;

	private void setCardInitDone(Boolean initDone) {
		int si = mTabbedPane.indexOfTab("Status");

		if (initDone.booleanValue()) {
			mInitProgressBinder.setBoundValue("Complete");
			for (int i = 0; i < mTabbedPane.getTabCount(); ++i) {
				if (i != si) {
					mTabbedPane.setEnabledAt(i, true);
				}
			}

			if (mInitPendingComponent != null) {
				mTabbedPane.setSelectedComponent(mInitPendingComponent);
				mInitPendingComponent = null;
			}
		} else {
			if (mInitPendingComponent == null) {
				mInitPendingComponent = mTabbedPane.getSelectedComponent();
			}
			mTabbedPane.setSelectedIndex(si);

			for (int i = 0; i < mTabbedPane.getTabCount(); ++i) {
				if (i != si) {
					mTabbedPane.setEnabledAt(i, false);
				}
			}
		}
	}


	protected class TOHChangeListener
			implements PStateChangeListener {
		public void stateChanged(PStateChangeEvent event) {
			try {
				//System.out.println("Rx TOH Change Event");
				PEnvelope env = new PEnvelope(mTOHForm.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				mTOHForm.setEnvelope(env);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/*
	protected class OC48CChangePanel extends BasicForm {
		protected OC48CChangePanel() {
			super(new OC48CTypeSet());

			PEnvelope env = new PEnvelope(new OC48CTypeSet());
			try {
				env.mergePComponent(getOC48C());
			}
			catch (Exception e) {
				e.printStackTrace();
			}


			addField("payloadType",
					new ComboBinder("Payload Type", Oc48c.PayloadType.class,
					Oc48c.PayloadType.getInstances()));
			addField("pattern",
					new ComboBinder("Pattern", Oc48c.Pattern.class,
					Oc48c.Pattern.getInstances()));
			addField("prbsInvert", new BooleanBinder("PRBS Invert"));
			addField("errorEnable",
					new BooleanBinder("Automatic Error Insertion"));
			addField("errorRate",
					new ComboBinder("Error Rate", Oc48c.ErrorRate.class,
					Oc48c.ErrorRate.getInstances()));
			addField("errorType",
					new ComboBinder("Error Type", Oc48c.ErrorType.class,
					Oc48c.ErrorType.getInstances()));


			setEnvelope(env);
		}


		public void start() {
			// Start any needed listeners.
		}


		public void dispose() {
			super.dispose();

			// Disconnect any listeners from start().
		}
	}
	*/



	static abstract protected class ModeButtonForm extends ButtonForm implements ClearFlagEventListener{
		protected ModeButtonForm(String title, TypeSet typeSet) {
			super(title, typeSet);
		}

		abstract public void setMode(Mode mode);
		public void clearFlags() {
			super.clearBinderFlags();
		}
		
		
	}

  /*
	static private class OC48CForm extends ModeButtonForm {
		BooleanBinder mRateBinder;
		protected OC48CForm(String title, TypeSet typeSet) {
			super(title, typeSet);
		}

		public void setMode(Mode mode) {
			if (mode.equals(Mode.US_SONET)) {
				setTitle("OC-48C Test Settings");
				mRateBinder.setTitle("OC48c Rate");
			} else {
				setTitle("STM-16 Test Settings");
				mRateBinder.setTitle("STM-16 Rate");
			}
		}
	
			
	}
*/

	protected Mode getMode() {
		return mMode;
	}
	
	class HeatForm extends TitleForm implements ClearFlagEventListener {
		public HeatForm(String title, HeatTypeSet typeSet) {
			super(title,typeSet);
		}
		public void clearFlags() {
			super.clearBinderFlags();
		}
	}
	
	public void addFlagListener(FlagListener l) {
		mFlagListeners.add(l);
		if (mMainForm != null) mMainForm.addFlagListener(l);
		if (mTOHForm != null) mTOHForm.addFlagListener(l);
		if (mHeatForm != null) mHeatForm.addFlagListener(l);
		//if (mDataPathPanel != null) mDataPathPanel.addFlagListener(l);
		
		
	}
	
	
}
