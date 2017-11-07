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



public class OC192Panel extends ButtonForm
		implements TributaryEnabledListener, ClearFlagEventListener {
	/* clearflagEventListener - jb01070103 */
	/* binderflag impl - jb01070103 */
	
	Oc192c_rx mOC192c;
	private PStateChangeConnector mOC192ChangeConnector;
	private PStateChangeListener mOC192ChangeListener;
	private JButton mEnableButton;
	private JButton mChangeButton;
	private boolean mSetupLock = false;
	
	/* jb-0109232001 b3, pattern error history */
	private Hashtable mErrorEventListeners = new Hashtable();
	
	public OC192Panel(String title, Mode mode, Oc192c_rx oc192c)
			throws Exception {

		super(title + " Settings",
				new OC192TypeSet(), false);


		HighlightBooleanBinder bb;


		mOC192c = oc192c;
		if (mOC192c == null) {
			throw new NullPointerException();
		}

		BooleanBinder boolBinder;
		// jb-01080201 change "oc192c rate" to "sts-192 rate"
		if (mode.equals(Mode.US_SONET)) {
			addField("rateEnabled",
					boolBinder = new BooleanBinder("STS-192 Rate", "Enabled", "Disabled"),true);
				
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
		
		/* jb-06262001 highlightComboBinder */
		/* jb-01062603 changed label - added "expected" */
		HighlightComboBinder hcb;
		
		addField("payloadType",
				hcb = new HighlightComboBinder("Expected Payload Type", Oc192c_rx.PayloadType.class,
				Oc192c_rx.PayloadType.getInstances(), Oc192c_rx.PayloadType.UNKNOWN_PATTERN, 
				UIManager.getColor("pippin.colors.alertRed")),false);
		hcb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		
		addField("pattern",
				new ComboBinder("Pattern", Oc192c_rx.Pattern.class,
				Oc192c_rx.Pattern.getInstances()),false);
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

		addField("b3Errors",
				new FlagDecider("B3 Errors"),true);
		addField("patternErrs",
				new FlagDecider("Pattern Errors"),true);

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
				OC192ChangePanel mPanel;
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change OC192C", mPanel = new OC192ChangePanel(getTributary()));
				PEnvelope pe = new PEnvelope(OC192TypeSet.class);
				try {
					pe.mergePComponent(getTributary());
					Boolean invert = (Boolean)pe.getElement("patternInvert", false);
					if (invert != null) {
						if (invert.equals(Boolean.TRUE)) {
							Oc192c_rx.Pattern pattern = (Oc192c_rx.Pattern)pe.getElement("pattern");
							pe.putElement("pattern", Oc192c_rx.Pattern.getInvertedFor(pattern));
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
						Oc192c_rx.Pattern pattern = (Oc192c_rx.Pattern)env.getElement("pattern");
						if (pattern.isInverted()) {
							env.putElement("patternInvert",Boolean.TRUE);
							env.putElement("pattern",Oc192c_rx.Pattern.getBasePatternFor(pattern));
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


		PEnvelope penv = new PEnvelope(new OC192TypeSet());
		penv.mergePComponent(getTributary());
		Boolean invert = (Boolean)penv.getElement("patternInvert", false);
		if (invert != null) {
			if (invert.equals(Boolean.TRUE)) {
				Oc192c_rx.Pattern pattern = (Oc192c_rx.Pattern)penv.getElement("pattern");
				penv.putElement("pattern", Oc192c_rx.Pattern.getInvertedFor(pattern));
			}
		}
		
		setEnvelope(penv);
		setTributaryEnabled(((Boolean)
				penv.getElement("rateEnabledUser")).booleanValue());

				
		getTributary().addStateChangeListener(mOC192ChangeConnector =
				new PStateChangeConnector(mOC192ChangeListener =
				new TributaryChangeListener(mOC192c,this)));
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
		}
	}


	public Oc192c_rx getTributary() {
		return mOC192c;
	}


	public void dispose() {
		super.dispose();

		if (mOC192c != null) {
			try {
				mOC192c.removeStateChangeListener(mOC192ChangeConnector);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		mOC192c = null;
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
