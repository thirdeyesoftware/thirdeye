////
//
// Telesync 5320 Project
//
//

package telesync.gui.txdatapath;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.*;



public class TributaryPanel extends ButtonForm
		implements TributaryEnabledListener, ClearFlagEventListener {

	TxStsNc mTributary;
	private JButton mEnableButton;
	private JButton mChangeButton;
	String mTitle;
	private PStateChangeConnector mChangeConnector;
	private PStateChangeListener mChangeListener;

	public TributaryPanel(String title, STSLevel level, Mode mode,
			TxStsNc tributary) throws Exception {
		super(title + " Settings",
				new TributaryTypeSet(), false);
		mTitle = title;

		addField("rateEnabled",
				new BooleanBinder(level.toString(mode) + " Rate",
				"Enabled", "Disabled"),Binder.HIDE_EVENTS);
		addField("payloadType",
				new ComboBinder("Payload Type", PayloadType.class,
				PayloadType.getInstances()),Binder.HIDE_EVENTS);
		addField("pattern",
				new ComboBinder("Pattern", Pattern.class,
				Pattern.getInstances()),Binder.HIDE_EVENTS);
		addField("patternInvert", new BooleanBinder("Pattern Invert"),
			Binder.HIDE_EVENTS, Binder.HIDE_COMPONENT);

		setEnabled(false);

		mTributary = tributary;
		if (mTributary == null) {
			throw new NullPointerException();
		}

		mEnableButton = ButtonMaker.createPButton("Enable");
		mEnableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					getTributary().put("rateEnabled", Boolean.TRUE);
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
				penv.getElement("rateEnabled")).booleanValue());

		getTributary().addStateChangeListener(mChangeConnector =
				new PStateChangeConnector(mChangeListener =
				new TributaryChangeListener(this)));
	}


	public TxStsNc getTributary() {
		return mTributary;
	}


	public void setTributaryEnabled(boolean b) {
		if (b) {
			mEnableButton.setEnabled(false);
			mChangeButton.setEnabled(true);
		} else {
			mEnableButton.setEnabled(true);
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


	public void setTitle(String title) {
		super.setTitle(title + " Settings");
	}
	
	public void clearFlags() {
		super.clearBinderFlags();
	}
	
}
