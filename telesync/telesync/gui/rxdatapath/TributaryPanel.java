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
import java.util.*;

import pippin.pui.*;
import pippin.util.*;

import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.*;
import telesync.gui.icons.binders.*;

public class TributaryPanel extends ButtonForm 
		implements TributaryEnabledListener, ClearFlagEventListener, RefreshListener {

	RxStsNc mTributary;
	private JButton mEnableButton;
	private JButton mChangeButton;
	private JButton mTestButton;
	
	String mTitle;
	private PStateChangeConnector mChangeConnector;
	private PStateChangeListener mChangeListener;
	private PStateChangeConnector mCardChangeConnector;
	private PStateChangeListener mCardChangeListener;
	
	private AlarmIndicatorPanel mAISPanel, mRDIPanel;
	
	private boolean mSetupLock = false;
	private Rx48 mCard;
	
	private Hashtable mErrorEventListeners = new Hashtable();
	private Vector mErrorRateBinders;
	private FlagDecider mB3ErrorBinder, mPatternErrorBinder;
	private boolean mIsTestRunning = false;
	
	private SecondsBinder mTestTimeBinder;
	private STSLevel mSTSLevel;
	public TributaryPanel(String title, STSLevel level, Mode mode,
			RxStsNc tributary, Rx48 card) throws Exception {
		super(title + " Settings",
				new TributaryTypeSet(), false);
		mTitle = title;
		mCard = card;
		mSTSLevel = level;
		mErrorRateBinders = new Vector();
		
		HighlightBooleanBinder bb;
		BooleanBinder boolBinder;
		
		addField("rateEnabled",
				boolBinder = new BooleanBinder(level.toString(mode) + " Rate",
				"Enabled", "Disabled"),true);
		boolBinder.setOnText("Enabled");
		boolBinder.setOffText("Disabled");
		boolBinder.setIcon(BlackDot.getImageIcon());
		addField("rateEnabledUser", 
						boolBinder = new BooleanBinder("User Rate", "Enabled", "Disabled"),false);
		boolBinder.setOnText("Enabled");
		boolBinder.setOffText("Disabled");
		boolBinder.setIcon(BlackDot.getImageIcon());
		
		/* jb-06262001 highlightComboBinder */
		/* jb-01062603 changed label - added "expected" */
		HighlightComboBinder hcb;
		addField("payloadType",
				hcb = new HighlightComboBinder("Expected Payload Type", RxStsNc.PayloadType.class,
				RxStsNc.PayloadType.getInstances(), RxStsNc.PayloadType.UNKNOWN_PATTERN, 
				UIManager.getColor("pippin.colors.alertRed")),false);
		hcb.setRestColor( UIManager.getColor("pippin.colors.goGreen"));
		
		addField("pattern",
				new ComboBinder("Pattern", Pattern.class,
				Pattern.getInstances()),Binder.RECEIVE_EVENTS);
		addField("patternInvert", new BooleanBinder("Pattern Invert"),
			Binder.RECEIVE_EVENTS, Binder.HIDE_COMPONENT);

		addField("pointerError",
				bb = new HighlightBooleanBinder("Concat. Pointers", "pointerError",false,
				UIManager.getColor("pippin.colors.alertRed")),false);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("Error");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());
		FlagDecider fd;
		
		addField("b3Errors",
				mB3ErrorBinder = new FlagDecider("B3 Errors"),true);
		
		mB3ErrorBinder.setTributaryCount(mSTSLevel.getLevel()); //since this is an OC48c
		mB3ErrorBinder.setCalculator(new ErrorRateCalculator() {
			public double calculateErrorRate(int stsLevel, 
					long dT, long dE) {
				return (double) (dE / (stsLevel * 87D * 9D * 8D * 8000D * dT));
			}
		});
		
		mErrorRateBinders.addElement(mB3ErrorBinder);
		
		addField("patternErrs",
				mPatternErrorBinder = new FlagDecider("Pattern Errors"),true);
		mPatternErrorBinder.setTributaryCount(mSTSLevel.getLevel()); //since this is an OC48c
		mPatternErrorBinder.setCalculator(new ErrorRateCalculator() {
			public double calculateErrorRate(int stsLevel, 
					long dT, long dE) {
				if (stsLevel == 1) {
					return (double) ( dE / ( stsLevel * 86D * 9D * 8D * 8000D * dT));
				}
				else {
					return (double) ( dE / ( ((stsLevel * 86D) - ((stsLevel/3D)-1D)) * 9D * 8D * 8000D * dT));
				}
				
			}
		});
		mErrorRateBinders.addElement(mPatternErrorBinder);
		
		addField("timeIntoTest", mTestTimeBinder = new SecondsBinder("Time in Test"));
		//mTestTimeBinder.setUnits("secs");
		
		addField("patternLoss",
				bb = new HighlightBooleanBinder("Pattern Sync", "patternLoss",false,
				UIManager.getColor("pippin.colors.alertRed")),false);
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

		mTributary = tributary;
		if (mTributary == null) {
			throw new NullPointerException();
		}

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
				TributaryChangePanel mPanel;
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change " + mTitle,
						mPanel = new TributaryChangePanel(getTributary()));
				PEnvelope pe = new PEnvelope(TributaryTypeSet.class);
				try {
					pe.mergePComponent(getTributary());
					Boolean invert = (Boolean)pe.getElement("patternInvert", false);
					if (invert != null) {
						if (invert.equals(Boolean.TRUE)) {
							Pattern pattern = (Pattern)pe.getElement("pattern");
							pe.putElement("pattern", Pattern.getInvertedFor(pattern));
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
					if (env.getElement("pattern", false) != null) {
						Pattern pattern = (Pattern)env.getElement("pattern");
						if (pattern.isInverted()) {
							env.putElement("patternInvert",Boolean.TRUE);
							env.putElement("pattern",Pattern.getBasePatternFor(pattern));

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


		PEnvelope penv = new PEnvelope(new TributaryTypeSet());
		penv.mergePComponent(getTributary());
		Boolean invert = (Boolean)penv.getElement("patternInvert", false);
		if (invert != null) {
			if (invert.equals(Boolean.TRUE)) {
				Pattern pattern = (Pattern)penv.getElement("pattern");
				penv.putElement("pattern", Pattern.getInvertedFor(pattern));
			}
		}
		RxStsNc.PayloadType payloadType = (RxStsNc.PayloadType)penv.getElement("payloadType");
		if (payloadType != null) {
			if (payloadType.equals(RxStsNc.PayloadType.UNKNOWN_PATTERN)) {
				setBinderVisible("pattern",false);
			}
			else 
				setBinderVisible("pattern",true);
			
		}
		
		setEnvelope(penv);
		setTributaryEnabled(((Boolean)
				penv.getElement("rateEnabledUser")).booleanValue());

		getTributary().addStateChangeListener(mChangeConnector =
				new PStateChangeConnector(mChangeListener =
				new TributaryChangeListener(mTributary, this)));

		mCard.addStateChangeListener(mCardChangeConnector = 
			new PStateChangeConnector(mCardChangeListener = new PStateChangeListener() {
				public void stateChanged(PStateChangeEvent event) {
					if (event.getAttributes().contains("testRunning")) {
						if (((Boolean)event.getAttributes().getValue("testRunning")).booleanValue()) {
							setRefresher(isEnabled());
							setTestRunning(true);
						} else {
							setRefresher(false);
							setTestRunning(false);
						}
					}
				}
		}));

		mAISPanel = new AlarmIndicatorPanel("AIS-P", "AIS-P", false, RxStsNc.RxStsNcAlarmType.ALARM_AIS_P);
		mRDIPanel = new AlarmIndicatorPanel("RDI-P", "RDI-P", false, RxStsNc.RxStsNcAlarmType.ALARM_RDI_P);

		getTributary().addAlarmEventListener(mAISPanel);
		getTributary().addAlarmEventListener(mRDIPanel);

		addTitlePanel(mAISPanel);
		addTitlePanel(mRDIPanel);
	}


	public RxStsNc getTributary() {
		return mTributary;
	}


	public void setTributaryEnabled(boolean b) {

		if (b) {
			mEnableButton.setEnabled(false);
			if (mSetupLock) mChangeButton.setEnabled(true);
			
			else
				mChangeButton.setEnabled(false);
		} else {
									
			if (mSetupLock) mEnableButton.setEnabled(true);
			else mEnableButton.setEnabled( false );
			mChangeButton.setEnabled(false);
			//getTributary().resetErrorSeconds();
			//resetErrorRates();
			//updateErrorSeconds();
		}
		
				
	}
	
	public void setRefresher(boolean shouldRefresh) {
		if (shouldRefresh) {
			if (mCard.isTestRunning()) {
				Refresher.getRefresher().addRefreshListener(this);
			}
			else {
				Refresher.getRefresher().removeRefreshListener(this);
				resetErrorRates();
				mTributary.resetErrorSeconds();
				updateErrorSeconds();
			}
		}
		else {
			Refresher.getRefresher().removeRefreshListener(this);
			resetErrorRates();
			mTributary.resetErrorSeconds();
			updateErrorSeconds();
		}
	}
	
	public boolean isEnabled() {
		return mTributary.isRateEnabled() || mTributary.isUserEnabled();
		
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

		if (mTributary != null) {
			try {
				mTributary.removeStateChangeListener(mChangeConnector);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			mTributary = null;
		}
	}

	public void setSetupLock( boolean setupLock ) {
		mSetupLock = setupLock;
		Envelope env = getEnvelope();
		
		setBinderVisible("rateEnabledUser",mSetupLock);
		
		setTributaryEnabled(((Boolean)
								env.getElement("rateEnabledUser")).booleanValue());
		HighlightComboBinder payloadTypeBinder = (HighlightComboBinder)getBinder("payloadType");
		if (setupLock) {
			payloadTypeBinder.setTitle("Expected Payload Type");
		}
		else {
			payloadTypeBinder.setTitle("Payload Type");
		}
		payloadTypeBinder.setUseHighlight(!setupLock);		
		
	}
	public boolean getEnabled() {
			return mEnableButton.isEnabled();
	}
	
	
	public void setTitle(String title) {
		super.setTitle(title + " Settings");
	}
	public String getTitle() {
		return mTitle;
	}
	
	public void clearFlags() {
		super.clearBinderFlags();
	}
	
	public void setTestRunning(boolean running) {
		
		if (running && !mIsTestRunning) {
			Refresher.getRefresher().addRefreshListener(TributaryPanel.this);
			mTestButton.setActionCommand("stop");
			mTestButton.setText("Stop Test");
		}
		else if (!running) {
			//resetErrorRates();
			//((Rx48)getCard()).resetErrorSeconds();
			updateErrorSeconds();
			Refresher.getRefresher().removeRefreshListener(TributaryPanel.this);
			mTestButton.setActionCommand("start");
			mTestButton.setText("Start Test");
		}
		mIsTestRunning = running;
		mTestButton.setToolTipText(mTestButton.getText());			

	}
	
	public synchronized void refreshNotify() {
		//System.out.println("TributaryPanel.refreshNotify()");
		if (mCard.isTestRunning() && isEnabled()) {
			int time = mCard.getTestTime();
			//System.out.println("TributaryPanel.refreshNotify()-time=" + time);
			if (time > 0) {
				for (Iterator it = mErrorRateBinders.iterator();it.hasNext();) {
					((ErrorBinder)it.next()).calculateErrorRate(time);
				}
				mTestTimeBinder.setBoundValue(new Moment(time));
				updateErrorSeconds();
			}
			
		}
		
		
	}
	public void resetErrorRates() {
		System.out.println("resetErrorRates()");
		mB3ErrorBinder.setErrorRate(0);
		mPatternErrorBinder.setErrorRate(0);
	}
	
	public void updateErrorSeconds() {
		//System.out.println("updateErrorSecs():b3errsecs =" + mTributary.getB3ErrSecs());
		mB3ErrorBinder.setErrorSeconds(mTributary.getB3ErrSecs());
		mPatternErrorBinder.setErrorSeconds(mTributary.getPatternErrSecs());
		
	}
	
	class AlarmIndicatorPanel extends IndicatorPanel 
			implements PAlarmEventListener {
		private RxStsNc.RxStsNcAlarmType mAlarmType;

		AlarmIndicatorPanel(String title, String desc, boolean doAutoHist, RxStsNc.RxStsNcAlarmType type) {
			super(title,desc,doAutoHist);
			mAlarmType = type;
		}
		
		RxStsNc.RxStsNcAlarmType getAlarmType() {
			return mAlarmType;
		}
		public void alarmNotify(PAlarmEvent event) {
			RxStsNc.RxStsNcAlarmType type = RxStsNc.RxStsNcAlarmType.getInstanceFor(
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
