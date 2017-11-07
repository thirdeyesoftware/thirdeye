////
//
// Telesync 5320 Project
//
//

package telesync.gui.rxdatapath;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Hashtable;
import java.util.*;
import pippin.util.*;
import pippin.pui.*;


import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.*;
import telesync.gui.icons.binders.*;



public class OC48Panel extends ButtonForm
		implements TributaryEnabledListener, ClearFlagEventListener, RefreshListener {
	/* clearflagEventListener - jb01070103 */
	/* binderflag impl - jb01070103 */
	
	Oc48c_rx mOC48c;
	Rx48 mCard;
	
	private PStateChangeConnector mOC48ChangeConnector;
	private PStateChangeListener mOC48ChangeListener;
	
	private PStateChangeConnector mCardChangeConnector;
	private PStateChangeListener mCardChangeListener;
	
	private JButton mEnableButton;
	private JButton mChangeButton;
	private JButton mTestButton;
	
	private boolean mSetupLock = false;
	private Vector mErrorRateBinders;
	private FlagDecider mB3ErrorBinder, mPatternErrorBinder;
	
	private SecondsBinder mTestTimeBinder;
	private boolean mIsTestRunning = false;
	private AlarmIndicatorPanel mAISPanel,mRDIPanel;
	
	/* jb-0109232001 b3, pattern error history */
	private Hashtable mErrorEventListeners = new Hashtable();
	private Vector mFlagListeners = new Vector();
	
	/* jb-11182002 rateEnabledUser state - used to fix rateEnabledUser state change
	  affecting resetErrorRates. */
	private boolean mRateEnabledUser;
	
	public OC48Panel(String title, Mode mode, Oc48c_rx oc48c, Rx48 card)
			throws Exception {

		super(title + " Settings",
				new OC48TypeSet(), false);


		HighlightBooleanBinder bb;

		mCard = card;
		
		mOC48c = oc48c;
		
		if (mOC48c == null) {
			throw new NullPointerException();
		}

		BooleanBinder boolBinder;
		// jb-01080201 change "oc48c rate" to "sts-48 rate"
		if (mode.equals(Mode.US_SONET)) {
			addField("rateEnabled",
					boolBinder = new BooleanBinder("STS-48 Rate", "Enabled", "Disabled"),true);
				
		} else {
			addField("rateEnabled",
					boolBinder = new BooleanBinder("STM-16 Rate", "Enabled", "Disabled"),true);
		}
		/* jb-01070101 */
		boolBinder.setOnText("Enabled");
		boolBinder.setOffText("Disabled");
		boolBinder.setIcon(BlackDot.getImageIcon());
		
		addField("rateEnabledUser", 
						boolBinder = new BooleanBinder("User Rate", "Enabled", "Disabled"),true);
		
		/* jb-01070101 */
		boolBinder.setOnText("Enabled");
		boolBinder.setOffText("Disabled");
		boolBinder.setIcon(BlackDot.getImageIcon());
		
		/*addField("setupLock", boolBinder = new BooleanBinder("AutoScan"), false);
		boolBinder.setIcon(BlackDot.getImageIcon());
		boolBinder.setOnText("ON");
		boolBinder.setOffText("OFF");
		boolBinder.setInverted(true);
		*/
		
		/* jb-06262001 highlightComboBinder */
		/* jb-01062603 changed label - added "expected" */
		HighlightComboBinder hcb;
		
		addField("payloadType",
				hcb = new HighlightComboBinder("Expected Payload Type", Oc48c_rx.PayloadType.class,
				Oc48c_rx.PayloadType.getInstances(), Oc48c_rx.PayloadType.UNKNOWN_PATTERN, 
				UIManager.getColor("pippin.colors.alertRed")),false);
		hcb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		
		addField("pattern",
				new ComboBinder("Pattern", Oc48c_rx.Pattern.class,
				Oc48c_rx.Pattern.getInstances()),Binder.RECEIVE_EVENTS);
		addField("patternInvert", new BooleanBinder("Pattern Invert"),
			Binder.HIDE_EVENTS, 
			Binder.HIDE_COMPONENT);

		addField("pointerError",
				bb = new HighlightBooleanBinder("Concat. Pointers", "pointerError",false,
				UIManager.getColor("pippin.colors.alertRed")),false);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("Error");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());
		
		mErrorRateBinders = new Vector();
		
		
		addField("b3Errors",
				mB3ErrorBinder = new FlagDecider("B3 Errors"),true);
				
		mB3ErrorBinder.setTributaryCount(STSLevel.STS_48.getLevel()); //since this is an OC48c
		mB3ErrorBinder.setCalculator(new ErrorRateCalculator() {
			public double calculateErrorRate(int stsLevel, 
					long dT, long dE) {
				return (double) ( dE / (stsLevel * 87D * 9D * 8D * 8000D * dT));
			}
		});
		
		mErrorRateBinders.addElement(mB3ErrorBinder);
		
		addField("patternErrs",
				mPatternErrorBinder = new FlagDecider("Pattern Errors"),true);
		mPatternErrorBinder.setTributaryCount(STSLevel.STS_48.getLevel()); //since this is an OC48c
		mPatternErrorBinder.setCalculator(new ErrorRateCalculator() {
			public double calculateErrorRate(int stsLevel, 
					long dT, long dE) {
				return (double) ( dE / ( ((stsLevel * 86D) - ((stsLevel/3D)-1D)) * 9D * 8D * 8000D * dT));
			}
		});
		
		mErrorRateBinders.addElement(mPatternErrorBinder);
		
		addField("timeIntoTest", mTestTimeBinder = new SecondsBinder("Time in Test"));
			//mTestTimeBinder.setUnits("secs");

		addField("patternLoss",
				bb = new HighlightBooleanBinder("Pattern", "patternLoss",false,
				UIManager.getColor("pippin.colors.alertRed")),true);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("Lost");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());

		addField("ptrace",
				new FixedStringBinder("Path Trace (J1)", 62),false);

		addField("C2", new ByteBinder("POH C2"),true);
		addField("G1", new ByteBinder("POH G1"),true);
		addField("F2", new ByteBinder("POH F2"),true);
		addField("H4", new ByteBinder("POH H4"),true);
		addField("Z3", new ByteBinder("POH Z3"),true);
		addField("Z4", new ByteBinder("POH Z4"),true);
		addField("Z5", new ByteBinder("POH Z5"),true);

		
		setEnabled(false);

		mTestButton = ButtonMaker.createPButton("Start Test");
		setTestRunning(mCard.isTestRunning());
		addButton(mTestButton);
		mTestButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				//action == true , start; else stop test
				boolean action = event.getActionCommand().equals("start");
				try {
					mCard.put("testRunning", new Boolean(action));
					setTestRunning(action);


				}
				catch (Exception e) {
					e.printStackTrace();
				}



			}
		});
		
		mEnableButton = ButtonMaker.createPButton("Enable");
		mEnableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					getTributary().put("rateEnabledUser", Boolean.TRUE);
				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog(e);
					e.printStackTrace();
				}
			}
		});
		addButton(mEnableButton);


		mChangeButton = ButtonMaker.createPButton("Change");
		mChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OC48ChangePanel mPanel;
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change OC48C", mPanel = new OC48ChangePanel(getTributary()));
				PEnvelope pe = new PEnvelope(OC48TypeSet.class);
				try {
					pe.mergePComponent(getTributary());
					Boolean invert = (Boolean)pe.getElement("patternInvert", false);
					if (invert != null) {
						if (invert.equals(Boolean.TRUE)) {
							Oc48c_rx.Pattern pattern = (Oc48c_rx.Pattern)pe.getElement("pattern");
							pe.putElement("pattern", Oc48c_rx.Pattern.getInvertedFor(pattern));
						}
					}
					
					mPanel.setEnvelope(pe);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				d.setVisible(true);

				Envelope env = d.getEnvelope();
				if (env != null) {
					if (env.getElement("pattern",false) != null) {
						Oc48c_rx.Pattern pattern = (Oc48c_rx.Pattern)env.getElement("pattern");
						if (pattern.isInverted()) {
							env.putElement("patternInvert",Boolean.TRUE);
							env.putElement("pattern",Oc48c_rx.Pattern.getBasePatternFor(pattern));
						}
						else {
							env.putElement("pattern", pattern);
							env.putElement("patternInvert",Boolean.FALSE);
						}
					}
					
					PEnvelope pen = new PEnvelope(env);
					try {
						pen.applyToPComponent(getTributary());
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		addButton(mChangeButton);


		PEnvelope penv = new PEnvelope(new OC48TypeSet());
		penv.mergePComponent(getTributary());
		Boolean invert = (Boolean)penv.getElement("patternInvert", false);
		if (invert != null) {
			if (invert.equals(Boolean.TRUE)) {
				Oc48c_rx.Pattern pattern = (Oc48c_rx.Pattern)penv.getElement("pattern");
				penv.putElement("pattern", Oc48c_rx.Pattern.getInvertedFor(pattern));
			}
		}
		Oc48c_rx.PayloadType payloadType = (Oc48c_rx.PayloadType)penv.getElement("payloadType");
		if (payloadType != null) {
			if (payloadType.equals(Oc48c_rx.PayloadType.UNKNOWN_PATTERN)) {
				//LIVE pattern.. .no need for pattern binder
				setBinderVisible("pattern",false);
			}
			else 
				setBinderVisible("pattern",true);
			
		}
		
		setEnvelope(penv);
		setTributaryEnabled(((Boolean)
				penv.getElement("rateEnabledUser")).booleanValue());
		
				
		getTributary().addStateChangeListener(mOC48ChangeConnector =
				new PStateChangeConnector(mOC48ChangeListener =
				new OC48ChangeListener(mOC48c,this)));
		
		
		mCard.addStateChangeListener(mCardChangeConnector = 
			new PStateChangeConnector(mCardChangeListener = new PStateChangeListener() {
				public void stateChanged(PStateChangeEvent event) {
					if (event.getAttributes().contains("testRunning")) {
						if (((Boolean)event.getAttributes().getValue("testRunning")).booleanValue()) {
							//System.out.println("mCard.stateChanged()");
							setRefresher(isEnabled());
							setTestRunning(true);
						} else {
							//System.out.println(event.getAttributes().toString());
							setRefresher(false);
							setTestRunning(false);
						}
					}
				}
		}));
		
		mAISPanel = new AlarmIndicatorPanel("AIS-P", "AIS-P", false, Oc48c_rx.Oc48c_RxAlarmType.ALARM_AIS_P);
		mRDIPanel = new AlarmIndicatorPanel("RDI-P", "RDI-P", false, Oc48c_rx.Oc48c_RxAlarmType.ALARM_RDI_P);
		
		getTributary().addAlarmEventListener(mAISPanel);
		getTributary().addAlarmEventListener(mRDIPanel);
		
		addTitlePanel(mAISPanel);
		addTitlePanel(mRDIPanel);
		
	}
	public void setRefresher(boolean shouldRefresh) {
		if (shouldRefresh) {
			if (mCard.isTestRunning()) {
				Refresher.getRefresher().addRefreshListener(this);
			}
			else {
				Refresher.getRefresher().removeRefreshListener(this);
				//System.out.println("setRefresher(true),notestrunning");
				resetErrorRates();
				getTributary().resetErrorSeconds();
				//System.out.println("setRefresher()");
				updateErrorSeconds();
			}
		}
		else {
			Refresher.getRefresher().removeRefreshListener(this);
			System.out.println("setRefresher(false)");
			resetErrorRates();
			//System.out.println("setRefresher()-refresh=false");
			getTributary().resetErrorSeconds();
			updateErrorSeconds();
		}
	}
	
	private void resetErrorRates() {
		mPatternErrorBinder.setErrorRate(0);
		mB3ErrorBinder.setErrorRate(0);
		
	}
	
	public void resetErrorSeconds() {
		mPatternErrorBinder.setErrorSeconds(0);
		mB3ErrorBinder.setErrorSeconds(0);
	}
	

	public void setTributaryEnabled(boolean b) {
		if (b) {
			mEnableButton.setEnabled(false);
			if (mSetupLock) mChangeButton.setEnabled(true);
			else 
				mChangeButton.setEnabled( false );
		} else {
			if (mSetupLock) mEnableButton.setEnabled(true);
			else 
				mEnableButton.setEnabled( false );
			mChangeButton.setEnabled(false);
			System.out.println("setTributaryEnabled(false)");
			//resetErrorRates();
			//System.out.println("setTribEnabled-false");
			//getTributary().resetErrorSeconds();
			//updateErrorSeconds();
		}
	}


	public Oc48c_rx getTributary() {
		return mOC48c;
	}


	public void dispose() {
		
		if (mCard != null) {
			try {
				mCard.removeStateChangeListener(mCardChangeConnector);
			}
			catch (Exception e) {
			}
		}
		
		
		super.dispose();

		if (mOC48c != null) {
			try {
				mOC48c.removeStateChangeListener(mOC48ChangeConnector);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		mOC48c = null;
	}

	public void setSetupLock( boolean setupLock ) {
		// remove "user rate" field if autoscan is on
		
		mSetupLock = setupLock;
		setBinderVisible("rateEnabledUser",mSetupLock);
		HighlightComboBinder payloadTypeBinder = (HighlightComboBinder) getBinder("payloadType");
		
		if (setupLock) {
			payloadTypeBinder.setTitle("Expected Payload Type");
		} else {
			payloadTypeBinder.setTitle("Payload Type");
		}
		payloadTypeBinder.setUseHighlight(!setupLock);		
		//System.out.println("setSetupLock()");
		//System.out.println(getEnvelope().toString());
		setTributaryEnabled(((Boolean)
			getEnvelope().getElement("rateEnabledUser")).booleanValue());
		
		
	}
	public boolean isEnabled() {
		if (mOC48c != null) {
			return mOC48c.isUserEnabled() || mOC48c.isRateEnabled();
		}
		else return false;
		
	}
	
	public void setTitle(String title) {
		super.setTitle(title + " Settings");
	}
	public void clearFlags() {
		
		super.clearBinderFlags();
	}
	
	/* jb-0109232001 b3, pattern error history */
	/*public void addErrorEventListener(String errorAttribute, ErrorEventListener listener) {
		mErrorEventListeners.put(errorAttribute, listener);
	}
	public void removeErrorEventListener(ErrorEventListener listener) {
		mErrorEventListeners.remove(listener);
	}
	public Hashtable getErrorEventListeners() {
		return mErrorEventListeners;
	}
	*/

	public void setTestRunning(boolean running) {
		
		if (running && !mIsTestRunning) {
			Refresher.getRefresher().addRefreshListener(OC48Panel.this);
			mTestButton.setActionCommand("stop");
			mTestButton.setText("Stop Test");
		}
		else if (!running) {
			//resetErrorRates();
			//((Oc48c_rx)getCard()).resetErrorSeconds();
			updateErrorSeconds();
			Refresher.getRefresher().removeRefreshListener(OC48Panel.this);
			mTestButton.setActionCommand("start");
			mTestButton.setText("Start Test");
		}
		mIsTestRunning = running;
		mTestButton.setToolTipText(mTestButton.getText());			

	}
	
	public synchronized void refreshNotify() {
		
		if (mCard.isTestRunning() && isEnabled()) {
			int time = mCard.getTestTime();
			if (time > 0) {
				for (Iterator it = mErrorRateBinders.iterator();it.hasNext();) {
					((ErrorBinder)it.next()).calculateErrorRate(time);
				}
				mTestTimeBinder.setBoundValue(new Moment(time));
			}
			updateErrorSeconds();
		}
		
	}
	
	public synchronized void updateErrorSeconds() {
		
		mB3ErrorBinder.setErrorSeconds(mOC48c.getB3ErrSecs());
		mPatternErrorBinder.setErrorSeconds(mOC48c.getPatternErrSecs());
	}
	
	class AlarmIndicatorPanel extends IndicatorPanel 
			implements PAlarmEventListener {
		private Oc48c_rx.Oc48c_RxAlarmType mAlarmType;

		AlarmIndicatorPanel(String title, String desc, boolean doAutoHist, Oc48c_rx.Oc48c_RxAlarmType type) {
			super(title,desc,doAutoHist);
			mAlarmType = type;
		}
		
		Oc48c_rx.Oc48c_RxAlarmType getAlarmType() {
			return mAlarmType;
		}
		public void alarmNotify(PAlarmEvent event) {
			Oc48c_rx.Oc48c_RxAlarmType type = Oc48c_rx.Oc48c_RxAlarmType.getInstanceFor(
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
