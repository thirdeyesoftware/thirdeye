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

import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.*;
import telesync.gui.icons.binders.*;

public class OC192TributaryPanel extends ButtonForm 
		implements TributaryEnabledListener, ClearFlagEventListener {

	RxStsNc192 mTributary;
	private JButton mEnableButton;
	private JButton mChangeButton;
	String mTitle;
	private PStateChangeConnector mChangeConnector;
	private PStateChangeListener mChangeListener;
	private boolean mSetupLock = false;
	
	private Hashtable mErrorEventListeners = new Hashtable();
	
	public OC192TributaryPanel(String title, STSLevel level, Mode mode,
			RxStsNc192 tributary) throws Exception {
		super(title + " Settings",
				new TributaryTypeSet(), false);
		mTitle = title;



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
				Pattern.getInstances()));
		addField("patternInvert", new BooleanBinder("Pattern Invert"),
			Binder.HIDE_EVENTS, Binder.HIDE_COMPONENT);

		addField("pointerError",
				bb = new HighlightBooleanBinder("Concat. Pointers", "pointerError",false,
				UIManager.getColor("pippin.colors.alertRed")),false);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("Error");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());

		addField("b3Errors",
				new FlagDecider("B3 Errors"),true);
		addField("patternErrs",
				new FlagDecider("Pattern Errors"),true);

		addField("patternLoss",
				bb = new HighlightBooleanBinder("Pattern", "patternLoss",false,
				UIManager.getColor("pippin.colors.alertRed")),false);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("Lost");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());

		addField("ptrace",
				new FixedStringBinder("Path Trace", 62),false);

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
				OC192TributaryChangePanel mPanel;
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change " + mTitle,
						mPanel = new OC192TributaryChangePanel(getTributary()));
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
					if (env.getElement("pattern") != null) {
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
		
		setEnvelope(penv);
		setTributaryEnabled(((Boolean)
				penv.getElement("rateEnabledUser")).booleanValue());

		getTributary().addStateChangeListener(mChangeConnector =
				new PStateChangeConnector(mChangeListener =
				new TributaryChangeListener(mTributary, this)));
	}


	public RxStsNc192 getTributary() {
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
		}
						
	}


	public void dispose() {
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
				setTributaryEnabled(((Boolean)
								env.getElement("rateEnabledUser")).booleanValue());
		
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
}