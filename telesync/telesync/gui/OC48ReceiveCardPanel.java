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
import pippin.util.*;

import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.rxdatapath.*;
import telesync.gui.toh.*;
import telesync.gui.icons.binders.*;

public class OC48ReceiveCardPanel extends CardPanel 
		implements RefreshListener {

	private ModeButtonForm mMainForm;
	private HeatForm mHeatForm;
	private AlarmStatusForm mAlarmForm;
	
	// private OC48CForm mOC48CForm;
	private TOHButtonForm mTOHForm;
	private DataPathPanel mDataPathPanel;
	private Rx48temperature mTemperature;
	private Oc48c_rx mOC48c;
	private Rx48datapath mDataPath;
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
	
	private AlarmIndicatorPanel mLOLPanel, mLOSPanel, mLOFPanel,mLOPPanel; 

	// private PStateChangeConnector mOC48CChangeConnector;
	// private PStateChangeListener mOC48CChangeListener;

	private PStateChangeConnector mTOHChangeConnector;
	private PStateChangeListener mTOHChangeListener;

	private Vector mErrorRateBinders;
	private FlagDecider mB1ErrorBinder, mB2ErrorBinder, mFrmErrorBinder;
	
	private JButton mTestButton,mClearAlarmHistoryButton,mClearErrorHistoryButton;
	private boolean mIsTestRunning = false;
	private SecondsBinder mTestTimeBinder;
	
	// private JButton mOC48EnableButton;
	// private JButton mOC48ChangeButton;

	private Mode mMode;
	private JTabbedPane mTabbedPane;

	static private int cID = 0;
	private int mID = cID++;

	private OC48ReceiveCardPanel getRef() {
		return this;
	}
	
	protected OC48ReceiveCardPanel(ClientView clientView, Card card,
			int slotNumber) throws Exception {
		super(clientView, card, slotNumber);

		//System.out.println("OC48ReceiveCardPanel.<init>: " + getInstanceID());
	}


	protected void createPeers() throws Exception {
		mTemperature = (Rx48temperature) getCard().getSubComponent("temperature");

		mDataPath = (Rx48datapath)
				getCard().getSubComponent("datapath");
		mOC48c = (Oc48c_rx) mDataPath.getSubComponent("oc48c");
		
		mTOH = (TOH)
			getOC48C().getSubComponent("TOH");
	  
	  
		
	}
	
	
					
	/**
	 * synchronized accessors for peers.
	 */
	private synchronized TOH getTOH() {
		return mTOH;
	}
	private synchronized Rx48datapath getRx48datapath() {
		return mDataPath;
	}
	private synchronized Rx48temperature getRx48Temperature() {
		return mTemperature;
	}
	private synchronized Oc48c_rx getOC48C() {
		return mOC48c;
	}
	
	protected void createPeerListeners() throws Exception {
		getRx48Temperature().addStateChangeListener(mHeatChangeConnector =
				new PStateChangeConnector(mHeatChangeListener =
				new HeatChangeListener()));
		getRx48datapath().addStateChangeListener(mDataPathChangeConnector =
				new PStateChangeConnector(mDataPathChangeListener =
				new DataPathChangeListener()));
		getTOH().addStateChangeListener(mTOHChangeConnector =
				new PStateChangeConnector(mTOHChangeListener =
				new TOHChangeListener()));
	}

	protected JComponent createContentComponent() throws Exception {
		System.out.println("OC48ReceiveCardPanel.createContentComponent()");
		mTabbedPane = new JTabbedPane();
		JTabbedPane tp = mTabbedPane;
		JPanel containerPanel = new JPanel();

		// change layout to include error status panel.
		
		mTitleForm.add(Box.createVerticalStrut(5));
		mTitleForm.add(clearFlagsButton, BorderLayout.SOUTH);
		getTitlePanel().add(Box.createHorizontalGlue());
		
		mErrorPanel = new JPanel();
		mClearErrorHistoryButton = ButtonMaker.createPButton("Clear History", true);
			//ButtonMaker.createSmallPButton("Clear History", true);
			//btnHistory.setFont(UIManager.getFont("pippin.fonts.smaller"));			
		mClearErrorHistoryButton.setIcon(
			new ImageIcon( CardPanel.class.getResource("icons/cards/ErrorIcon.gif")) );
		mClearErrorHistoryButton.setHorizontalTextPosition(SwingConstants.LEFT);
		mClearErrorHistoryButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				clearErrorHistory();
			}
		});
		
		/*clearFlagsButton = ButtonMaker.makeSmaller(clearFlagsButton);
		clearFlagsButton.setFont(UIManager.getFont("pippin.fonts.smaller"));
		*/
		/*jb-01092001 - Move clear error button to error panel */
		JButton btnError = ButtonMaker.createPButton("Clear Errors", true); 
			//ButtonMaker.createSmallPButton("Clear Errors", true);
			//btnError.setFont(UIManager.getFont("pippin.fonts.smaller"));			
		btnError.setIcon( 
			new ImageIcon( CardPanel.class.getResource("icons/cards/indicator_error.gif")) );
		btnError.setHorizontalTextPosition(SwingConstants.LEFT);
		btnError.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				//do something here...
				try {
					resetErrorRates();
					((Rx48)getCard()).clearErrors( true );
									
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
		mErrorPanel.add( mClearErrorHistoryButton );
		mErrorPanel.add( Box.createHorizontalStrut(2) );
		
		
		
		
		
		mErrorPanel.setBorder( new EtchedBorder() );
		mErrorPanel.setLayout( new FlowLayout(FlowLayout.RIGHT, 1,1) );
		containerPanel.setLayout(new BorderLayout());
		
		//create errorPanel
		IndicatorPanel b1Panel, b2Panel;
		
				
		mLOLPanel = new AlarmIndicatorPanel("LOL", "Loss of Light", false, Rx48.Rx48AlarmType.ALARM_LOL);
		mLOLPanel.setInverted( false );
		
		mErrorEventListeners = new Hashtable();
		
		//mErrorEventListeners.put("loss_light", mLOLPanel);
		getCard().addAlarmEventListener(mLOLPanel);
		
		mLOSPanel = new AlarmIndicatorPanel("LOS", "Loss Of Signal", 
				false, Rx48.Rx48AlarmType.ALARM_LOS);
		mLOSPanel.setInverted( false);
		getCard().addAlarmEventListener(mLOSPanel);
		//mErrorEventListeners.put("loss_signal", losPanel);
		mLOFPanel = new AlarmIndicatorPanel("LOF", "Loss of Frame", 
				false, Rx48.Rx48AlarmType.ALARM_LOF);
		mLOFPanel.setInverted( false );
		getCard().addAlarmEventListener(mLOFPanel);
		
		//mErrorEventListeners.put("loss_framing", mLOFPanel);
		mLOPPanel = new AlarmIndicatorPanel("LOP", "Loss Of Pattern", 
				false, Rx48.Rx48AlarmType.ALARM_LOP);
		mLOPPanel.setInverted( false );
		getCard().addAlarmEventListener(mLOPPanel);
		
		b1Panel = new IndicatorPanel("B1", "B1 Errors",true);
		b1Panel.enableLatch( true );
		
		mErrorEventListeners.put("b1Errors", b1Panel);
		b2Panel = new IndicatorPanel("B2", "B2 Errors",true);
		b2Panel.enableLatch( true );
		mErrorEventListeners.put("b2Errors", b2Panel);
		b3Panel = new IndicatorPanel("B3", "B3 Errors",true);
		b3Panel.enableLatch( true );
		mErrorEventListeners.put("b3Errors", b3Panel);
		patternPanel = new IndicatorPanel("PAT","Pattern Errors",true);
		patternPanel.enableLatch( true );
		mErrorEventListeners.put("ptErrors", patternPanel);	
		
		mErrorPanel.add(mLOLPanel);
		mErrorPanel.add(Box.createHorizontalStrut(1));
		mErrorPanel.add(mLOSPanel);
		mErrorPanel.add(Box.createHorizontalStrut(1));
		mErrorPanel.add(mLOFPanel);
		mErrorPanel.add(Box.createHorizontalStrut(1));
		mErrorPanel.add(mLOPPanel);
		mErrorPanel.add( Box.createHorizontalStrut(2) );
		mErrorPanel.add(b1Panel);
		mErrorPanel.add(Box.createHorizontalStrut(1));
		mErrorPanel.add(b2Panel);
		mErrorPanel.add(Box.createHorizontalStrut(1));
		mErrorPanel.add(b3Panel);
		mErrorPanel.add(Box.createHorizontalStrut(2));
		mErrorPanel.add(patternPanel);
				
		containerPanel.add(mErrorPanel, BorderLayout.NORTH);
				
		
		// Main Form
		//
		mMainForm = new ModeButtonForm("OC-48 Receive Card Status",
				new MainTypeSet()) {
			public void setMode(Mode mode) {
				if (mode.equals(Mode.US_SONET)) {
					setTitle("OC-48 Receive Card Status");
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
				Mode.getInstances()), false);
						
				
		BooleanBinder boolBinder;
		
		// jb-01080302 implement autoscan
		//AutoScan == true equates to setuplock == false
		
		mMainForm.addField("setupLock", boolBinder = new BooleanBinder("AutoScan"), false);
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
				stepMax.intValue()), false);

		mMainForm.addField("b1Errors",
				mB1ErrorBinder = new FlagDecider("B1 Errors"),true);
		
		mB1ErrorBinder.setTributaryCount(48); //since this is an OC48
		mB1ErrorBinder.setCalculator(new ErrorRateCalculator() {
			public double calculateErrorRate(int numOfTribs, 
					long dT, long dE) {
				return (double)dE / (double)(numOfTribs * 90D * 9D * 8D * 8000D * dT);
				
			}
		});
		
		mErrorRateBinders = new Vector();
		
		mErrorRateBinders.addElement(mB1ErrorBinder);
		
		mMainForm.addField("b2Errors",
				mB2ErrorBinder = new FlagDecider("B2 Errors"),true);
		mB2ErrorBinder.setTributaryCount(48); //since this is an OC48
		mB2ErrorBinder.setCalculator(new ErrorRateCalculator() {
			public double calculateErrorRate(int numOfTribs, 
					long dT, long dE) {
				return (((double)dE / (double)(numOfTribs * (9D*87D + 3D*6D) * 8D * 8000D)) / (double)dT);
				
			}
		});
		
		mErrorRateBinders.addElement(mB2ErrorBinder);
		mMainForm.addField("frameErrors",
				mFrmErrorBinder = new FlagDecider("Frame Errors"),true);
		mFrmErrorBinder.setCalculator( new ErrorRateCalculator() {
			public double calculateErrorRate(int numOfTribs,
					long dT, long dE) {
				return (double)dE/dT;
			}
		});
		//mFrmErrorBinder.setUnits("E / s", "Errors per Second");
		
		mErrorRateBinders.addElement(mFrmErrorBinder);
		
		mMainForm.addField("testRunning", boolBinder = new BooleanBinder("Timed Test"));
		boolBinder.setIcon(BlackDot.getImageIcon());
		boolBinder.setOnText("Running");
		boolBinder.setOffText("Stopped");

		/*mMainForm.addField("timeIntoTest", mTestTimeBinder = new IntegerBinder("Time in Test"));
		mTestTimeBinder.setUnits("secs");
		*/
		mMainForm.addField("timeIntoTest", mTestTimeBinder = new SecondsBinder("Time in Test"));
		
		OpticalPowerBinder pb = new OpticalPowerBinder("Optical Power","optpwrDB");
		
		
		pb.setUnits("dbm");
		//mMainForm.addField("opticalPower", pb,false);
		mMainForm.addField("optpwrDB", pb, Binder.HIDE_EVENTS);
		
		mMainForm.addField("loss_light",
				bb = new HighlightBooleanBinder("Light", "loss_light", false,
				UIManager.getColor("pippin.colors.alertRed")),true);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("LOL");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());

		mMainForm.addField("loss_framing",
				bb = new HighlightBooleanBinder("Framing", "loss_framing", false,
				UIManager.getColor("pippin.colors.alertRed")),true);
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
		
		mMainForm.addField("serialNumber", new FormField("Serial Number", 150),Binder.HIDE_EVENTS);
		mMainForm.setEnabled(false);
		
		JButton b;
		// Reset Card Button removed for now 7/25/2002 - jb
		/*
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
		mMainForm.addButton(b);
		*/
				
		mTestButton = ButtonMaker.createPButton("Start Test");
		if (((Boolean)penv.getElement("testRunning")).booleanValue()) {
			
			setTestRunning(true);
		}
		else {
			setTestRunning(false);
			
		}
		
		mTestButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				PEnvelope env = new PEnvelope(mMainForm.getEnvelope());
				
				//action == true , start; else stop test
				boolean action = event.getActionCommand().equals("start");
				try {
					getCard().put("testRunning", new Boolean(action));
					setTestRunning(action);
											
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
							
				
					
			}
		});
		mTestButton.setEnabled(true);
		mMainForm.addButton(mTestButton);
		
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
						
						pe.applyToPComponent(getRx48datapath());

					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		
		mMainForm.addButton(b);

		penv.mergePComponent(getRx48datapath());
		
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
	
		// DataPath Panel
		//
		TitlePanel titlePanel = new TitlePanel("DataPath Composition",
				mDataPathPanel = new DataPathPanel(getClientView(), getRx48datapath(), (Rx48)getCard()), true, false);
		
		tp.add("DataPath", titlePanel);
		
		mDataPathPanel.setMode(mMode);
		
		/* include these as workaround for firmware blackhole (b3, pt errors) */
		//mDataPathPanel.addErrorEventListener("b3Errors", b3Panel);
		//mDataPathPanel.addErrorEventListener("patternErrs", patternPanel);
		
		
		if (penv.hasElement("setupLock")) {
			mDataPathPanel.setSetupLock(((Boolean)penv.getElement("setupLock")).booleanValue());
		} else mDataPathPanel.setSetupLock(false);
		
		addClearFlagEventListener(mDataPathPanel);
		
		
		// TOH Form
		//

		/* TOH isn't in the f/w yet.
		*/
		mTOHForm = new TOHButtonForm("Transport Overhead Settings",false);

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
		penv.mergePComponent(getRx48Temperature());
		mHeatForm.setEnvelope(penv);
		addClearFlagEventListener(mHeatForm);
		tp.add("Temp", mHeatForm);
		//
		// Heat Form

		// add Alarm Panel here...
		mAlarmForm = new AlarmStatusForm("Alarms",new AlarmStatusTypeSet());
		mAlarmForm.addField("BX-ERR", new IndicatorBinder("BX Chip"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("FX-ERR", new IndicatorBinder("FX Chip"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("PX-ERR", new IndicatorBinder("PX Chip"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("LOL", new IndicatorBinder("Loss of Light"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("LOS", new IndicatorBinder("Loss of Signal"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("LOF", new IndicatorBinder("Loss of Frame"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("LOP", new IndicatorBinder("Loss of Pointer"),Binder.HIDE_EVENTS);		
		mAlarmForm.addField("PL", new IndicatorBinder("Loss of Pattern"),Binder.HIDE_EVENTS);		
		mAlarmForm.addField("AIS-L", new IndicatorBinder("AIS-L"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("RDI-L", new IndicatorBinder("RDI-L"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("AIS-P", new IndicatorBinder("AIS-P"),Binder.HIDE_EVENTS);
		mAlarmForm.addField("RDI-P", new IndicatorBinder("RDI-P"),Binder.HIDE_EVENTS);
		
		mClearAlarmHistoryButton = ButtonMaker.createPButton("Clear History");
		mClearAlarmHistoryButton.setIcon(
			new ImageIcon( CardPanel.class.getResource("icons/cards/ErrorIcon.gif")) );
		mClearAlarmHistoryButton.setHorizontalTextPosition(SwingConstants.LEFT);			
		mClearAlarmHistoryButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				clearAlarmHistory();
				clearErrorHistory();
			}
		});


		mAlarmForm.addButton(mClearAlarmHistoryButton);
		getCard().addAlarmEventListener(new PAlarmEventListener() {
			public void alarmNotify(PAlarmEvent event) {
				Envelope env = new Envelope(new AlarmStatusTypeSet());
				if (getCard() == null) System.out.println("CARD IS NULL");
				
				env.mergeEnvelope(((Rx48)getCard()).getAlarmStatusEnvelope());
				
				Rx48.Rx48AlarmType type = Rx48.Rx48AlarmType.getInstanceFor(
						(Integer)event.getAttributes().getValue("id"));
				IndicatorStatus status = 
					new IndicatorStatus();
				status.setHistory(((Boolean)event.getAttributes().getValue("history")).booleanValue());
				status.setError(((Boolean)event.getAttributes().getValue("state")).booleanValue());
				
				env.putElement(type.getName(), status);
				mAlarmForm.setEnvelope(env);
				
			}
		});
		
				
		tp.add("Alarms",mAlarmForm);
		
		Envelope enve = new Envelope(new AlarmStatusTypeSet());
		enve.mergeEnvelope(((Rx48)getCard()).getAlarmStatusEnvelope());
		System.out.println("alarm envelope");	
		mAlarmForm.setEnvelope(enve);
		
		
		mLOLPanel.resetIndicators();
		mLOFPanel.resetIndicators();
		mLOSPanel.resetIndicators();
		mLOPPanel.resetIndicators();
		
		IndicatorStatus status = (IndicatorStatus)enve.getElement("LOL",false);
		mLOLPanel.setStatus(status);
		
		status = (IndicatorStatus)enve.getElement("LOF",false);
		mLOFPanel.setStatus(status);
		
		status = (IndicatorStatus)enve.getElement("LOS",false);
		mLOSPanel.setStatus(status);
		
		status = (IndicatorStatus)enve.getElement("LOP",false);
		mLOPPanel.setStatus(status);
		
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
	private void clearErrorHistory() {
		for (Enumeration e = mErrorEventListeners.elements(); e.hasMoreElements();) {
			IndicatorPanel panel = (IndicatorPanel)e.nextElement();
			panel.resetHistory();
		}
		clearAlarmHistory();
	}
	
	private void clearAlarmHistory() {
		((Rx48)getCard()).clearAlarms();
	}
	
	public void resetErrorRates() {
		if (mErrorRateBinders.size() > 0) {
			for (Iterator it = mErrorRateBinders.iterator();it.hasNext();) {
				((ErrorBinder)it.next()).resetErrorRate();
			}
		}
	}
	public void updateErrorSeconds() {
		Rx48 rx48 = (Rx48)getCard();
		mB1ErrorBinder.setErrorSeconds(rx48.getB1ErrorSecs());
		mB2ErrorBinder.setErrorSeconds(rx48.getB2ErrorSecs());
		mFrmErrorBinder.setErrorSeconds(rx48.getFrmErrorSecs());
	}
	
	
	public void showErrorPanel( boolean visible ) {
		if (mErrorPanel != null) mErrorPanel.setVisible( visible );
	}
	
	public void setTestRunning(boolean running) {
		
		if (running && !mIsTestRunning) {
			Refresher.getRefresher().addRefreshListener(OC48ReceiveCardPanel.this);
			mTestButton.setActionCommand("stop");
			mTestButton.setText("Stop Test");
		}
		else if (!running) {
			//resetErrorRates();
			//((Rx48)getCard()).resetErrorSeconds();
			updateErrorSeconds();
			Refresher.getRefresher().removeRefreshListener(OC48ReceiveCardPanel.this);
			mTestButton.setActionCommand("start");
			mTestButton.setText("Start Test");
		}
		mIsTestRunning = running;
		mTestButton.setToolTipText(mTestButton.getText());			

	}
	
	public void deactivate() {
		Refresher.getRefresher().removeRefreshListener(this);
		if (mDataPathPanel != null) mDataPathPanel.deactivate();
		
	}
	public void activate() {
		Envelope env = mMainForm.getEnvelope();
		if (env.hasElement("testRunning",false)) {
			boolean isInTest = ((Boolean)env.getElement("testRunning")).booleanValue();
			if (isInTest) {
				Refresher.getRefresher().addRefreshListener(this);
				if (mDataPathPanel != null) mDataPathPanel.activate();
			}
			
		}
		
	}
	
	public void doStateChange(PStateChangeEvent event) {
		PEnvelope env;

		if (event.getSource() == getCard()) {
			
			env = new PEnvelope(mMainForm.getEnvelope());
			PAttributeSet attributes = event.getAttributes();
						
			env.mergePAttributeList(attributes);
			
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
					getClientView().setTitle(getLabelText(false));
				}
				refreshTitleLabel();
				
			}
			
			if (env.hasElement("mode")) {
								
				Mode mode = (Mode) env.getElement("mode");
				titleLabel.setText(getLabelText(true));
				
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
			
			if (env.hasElement("testRunning", false)) {
				if (((Boolean)env.getElement("testRunning")).booleanValue()) {
					setTestRunning(true);
					
				} else {
					setTestRunning(false);
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
		/* handle history indicators LOL_HIST, LOP_HIST, LOF_HIST */
		/* these don't have indicator panels, per se.  we use their */
		/* attribute brethren LOL, LOP, LOS 											*/
		Boolean b;
		
		b = (Boolean)env.getElement("lol_hist",false);
		if (b != null)
			mLOLPanel.setHistoryIndicator(b.booleanValue());
		b = (Boolean)env.getElement("lop_hist",false);
		if (b != null)
			mLOPPanel.setHistoryIndicator(b.booleanValue());
		b = (Boolean)env.getElement("lof_hist",false);
				if (b != null)
		mLOFPanel.setHistoryIndicator(b.booleanValue());
		
	}
	
	private void updateErrorPanel( Envelope env ) {
		updateErrorPanel(env, false);
	}
	
	public void dispose() throws Exception {
		
		if (mDataPathPanel != null) {
			mDataPathPanel.deactivate();
		}
		
		Refresher.getRefresher().removeRefreshListener(this);
		
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
		mOC48c = null;

		if (mDataPath != null) {
			mDataPath.removeStateChangeListener(mDataPathChangeConnector);
		}
		mDataPath = null;
		mDataPathPanel.dispose();

	}

	/**
	 * @see RefreshListener
	 */
	public synchronized void refreshNotify() {
		//System.out.println("OC48ReceiveCardPanel.refreshNotify()");
		int time = ((Rx48)getCard()).getTestTime();
		if (time > 0) {
			for (Iterator it = mErrorRateBinders.iterator();it.hasNext();) {
				ErrorBinder eb = (ErrorBinder)it.next();
				eb.calculateErrorRate(time);
				
			}
			mTestTimeBinder.setBoundValue(new Moment(time));
			
			Envelope env = mMainForm.getEnvelope();
			updateErrorSeconds();
			
		}
				
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
				new TypeSetElement("optpwrDB",Integer.class),
				new TypeSetElement("loss_light", Boolean.class),
				new TypeSetElement("loss_framing", Boolean.class),
				new TypeSetElement("loss_pattern", Boolean.class),
				new TypeSetElement("mpll_los", Boolean.class),
				new TypeSetElement("b1Errors", Long.class),
				new TypeSetElement("b2Errors", Long.class),
				new TypeSetElement("frameErrors", Long.class),
				new TypeSetElement("lol_hist", Boolean.class),
				new TypeSetElement("lop_hist", Boolean.class),
				new TypeSetElement("lof_hist", Boolean.class),
				new TypeSetElement("setupLock", Boolean.class),
				new TypeSetElement("loss_signal", Boolean.class),
				//new TypeSetElement("clearErrors",Boolean.class),
				new TypeSetElement("name", String.class),
				new TypeSetElement("b3Errors", Boolean.class),
				new TypeSetElement("ptErrors", Boolean.class),
				new TypeSetElement("ptChange", Boolean.class),
				new TypeSetElement("testRunning", Boolean.class),
				new TypeSetElement("timeIntoTest", Moment.class),
				
				new TypeSetElement("b1ErrdSecs", Integer.class),
				new TypeSetElement("b2ErrdSecs", Integer.class),
				new TypeSetElement("FrmErrdSecs", Integer.class),
				
				
				
			});
			
			getElement("timeIntoTest").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return new Moment(((Integer) o).intValue());
				}
			});
			getElement("timeIntoTest").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return new Integer((int) ((Moment) o).getSeconds());
				}
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


	public static class OC48CTypeSet extends TypeSet {
		public OC48CTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("rateEnabled", Boolean.class),
				new TypeSetElement("payloadType", Oc48c_rx.PayloadType.class),
				new TypeSetElement("pattern", Oc48c_rx.Pattern.class),
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
					return ((Oc48c_rx.PayloadType) o).getValue();
				}
			});
			getElement("payloadType").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc48c_rx.PayloadType.getInstanceFor((Integer) o);
				}
			});

			getElement("pattern").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc48c_rx.Pattern) o).getValue();
				}
			});
			getElement("pattern").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc48c_rx.Pattern.getInstanceFor((Integer) o);
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
				env.mergePComponent(getRx48datapath());
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			addField("mode", new ComboBinder("Mode", Mode.class,
				Mode.getInstances()),false);
			
			BooleanBinder bb;
			
			addField("setupLock", bb = new BooleanBinder("AutoScan"), false);
			bb.setInverted(true);
			
			addField("name", new FormField("Card Name"));
			
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
				//System.out.println("datapath envelope " + env.toString());
				mMainForm.setEnvelope(env);
				Boolean bSetupLock = (Boolean)env.getElement("setupLock");
				if (mDataPathPanel != null && bSetupLock != null) 
					mDataPathPanel.setSetupLock( bSetupLock.booleanValue() );
				
				boolean b3Errors = env.hasElement("b3Errors") ? 
					((Boolean)env.getElement("b3Errors")).booleanValue() :
					false;
				boolean ptErrors = env.hasElement("ptErrors") ? 
					((Boolean)env.getElement("ptErrors")).booleanValue() :
					false;
				boolean patternError = env.hasElement("ptChange") ? 
					((Boolean)env.getElement("ptChange")).booleanValue() :
						false;
						
				if (b3Errors || ptErrors || patternError) {
					System.out.println("DatapathChangeListener.stateChanged():patternError = " + 
							patternError);
					for (Iterator it = mFlagListeners.iterator();it.hasNext();) {
						((FlagListener)it.next()).raiseFlag();
					}
				}
				
				updateErrorPanel(env);
				
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}




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


	class AlarmStatusForm extends ButtonForm
			implements  PAlarmEventListener {
		public AlarmStatusForm(String title, AlarmStatusTypeSet typeset) {
			super(title,typeset);
		}
		
		public void alarmNotify(PAlarmEvent event) {
			
		}
		public void setEnvelope(Envelope e) {
			if (e.getElement("RDI-P",false) != null) {
				System.out.println(e.getElement("RDI-P").toString());
			}
			super.setEnvelope(e);
		}
	}
	
	
	static abstract protected class ModeButtonForm extends ButtonForm implements ClearFlagEventListener{
		protected ModeButtonForm(String title, TypeSet typeSet) {
			super(title, typeSet);
		}

		abstract public void setMode(Mode mode);
		public void clearFlags() {
			super.clearBinderFlags();
		}
		
		
	}

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
	
	class AlarmIndicatorPanel extends IndicatorPanel 
			implements PAlarmEventListener {
		private Rx48.Rx48AlarmType mAlarmType;
		
		AlarmIndicatorPanel(String title, String desc, boolean doAutoHist, Rx48.Rx48AlarmType type) {
			super(title,desc,doAutoHist);
			mAlarmType = type;
		}
		Rx48.Rx48AlarmType getAlarmType() {
			return mAlarmType;
		}
		public void alarmNotify(PAlarmEvent event) {
			Rx48.Rx48AlarmType type = Rx48.Rx48AlarmType.getInstanceFor(
					(Integer)event.getAttributes().getValue("id"));
			if (!type.equals(getAlarmType())) return;
			IndicatorStatus status = 
				new IndicatorStatus();
			status.setHistory(((Boolean)event.getAttributes().getValue("history")).booleanValue());
			status.setError(((Boolean)event.getAttributes().getValue("state")).booleanValue());
			setStatus(status);
		}
		
		public void setStatus(IndicatorStatus status) {
			if (status == null) return;
			setErrorIndicator(status.getError());

			if (!status.getError()) {
				setHistoryIndicator(status.getHistory());
			}
			else {
				setHistoryIndicator(false);
			}
		}
		
	}
				
			
			
	
}
