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



public class OC48Panel extends ButtonForm
		implements TributaryEnabledListener, ClearFlagEventListener {

	Oc48c mOC48c;
	private PStateChangeConnector mOC48ChangeConnector;
	private PStateChangeListener mOC48ChangeListener;
	private JButton mEnableButton;
	private JButton mChangeButton;
	private JButton mInjectButton;
	private JPopupMenu mPopupMenu;
	
	static Font cMenuFont = new Font("SansSerif", Font.PLAIN, 12);

	public OC48Panel(String title, Mode mode, Oc48c oc48c)
			throws Exception {

		super(title + " Settings",
				new OC48TypeSet(), false);


		mOC48c = oc48c;
		if (mOC48c == null) {
			throw new NullPointerException();
		}

		// jb-01080201 change "oc48c rate" to "sts-48 rate"
		if (mode.equals(Mode.US_SONET)) {
			addField("rateEnabled",
					new BooleanBinder("STS-48 Rate", "Enabled", "Disabled"),false);
		} else {
			addField("rateEnabled",
					new BooleanBinder("STM-16 Rate", "Enabled", "Disabled"),false);
		}
		addField("payloadType",
				new ComboBinder("Payload Type", Oc48c.PayloadType.class,
				Oc48c.PayloadType.getInstances()),false);
		addField("pattern",
				new ComboBinder("Pattern", Oc48c.Pattern.class,
				Oc48c.Pattern.getInstances()),false);
		addField("prbsInvert", new BooleanBinder("PRBS Invert"),
				Binder.HIDE_EVENTS, Binder.HIDE_COMPONENT);
		addField("errorEnable",
				new BooleanBinder("Auto Error Insert"),false);
		addField("errorRate",
				new ComboBinder("Error Rate", Oc48c.ErrorRate.class,
				Oc48c.ErrorRate.getInstances()),false);
		addField("errorType",
				new ComboBinder("Error Type", Oc48c.ErrorType.class,
				Oc48c.ErrorType.getInstances()),false);
		//addField("errorButton", new IntegerBinder("Error Button"),false);

		setEnabled(false);
		
		// jb-01080601 added error popupMenu
		mPopupMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem();
		item.setAction(new ErrorItemAction("B1"));
		item.setFont(cMenuFont);
		mPopupMenu.add(item);
		item = new JMenuItem();
		item.setAction(new ErrorItemAction("B2"));
		item.setFont(cMenuFont);
		mPopupMenu.add(item);
		item = new JMenuItem();
		item.setAction(new ErrorItemAction("B3"));
		item.setFont(cMenuFont);
		mPopupMenu.add(item);
		
		mInjectButton = ButtonMaker.createPButton("B1 Error", true);
		//default action
		mInjectButton.setActionCommand("B1");
		
		// jb-01080601
		mInjectButton.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				doPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				doPopup(e);
			}
			private void doPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					mPopupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		mInjectButton.setToolTipText("Right-click to change Error Type");
		
		mInjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					// B1 Error is defined as 0x00
					// jb-01080601 added error popup
					String actionCommand = event.getActionCommand();
					Integer errorType = new Integer(0);
					if (actionCommand.equals("B2")) {
						errorType = new Integer(1);
					}
					else if (actionCommand.equals("B3")) {
						errorType = new Integer(2);
					}
					
					getTributary().insertError(getTributary().getErrorType(errorType));

				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog(e);
					e.printStackTrace();
				}
			}
		});
		
		addButton(mInjectButton);



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

		final Mode mMode = mode;
		
		mChangeButton = ButtonMaker.createPButton("Change");
		mChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OC48ChangePanel mPanel;
				String title = mMode.equals(Mode.US_SONET) ? "Change OC48C" : "Change STM-16";
				
				BinderDialog d = new BinderDialog(GClient.getClient(),
						title, mPanel = new OC48ChangePanel(getTributary()));
				PEnvelope pe = new PEnvelope(OC48TypeSet.class);
				
				
				try {
					pe.mergePComponent(getTributary());
					Boolean invert = (Boolean)pe.getElement("prbsInvert", false);
					if (invert != null) {
						if (invert.equals(Boolean.TRUE)) {
							Oc48c.Pattern pattern = (Oc48c.Pattern)pe.getElement("pattern");
							pe.putElement("pattern", Oc48c.Pattern.getInvertedFor(pattern));
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
						Oc48c.Pattern pattern = (Oc48c.Pattern)env.getElement("pattern");
						if (pattern.isInverted()) {
							env.putElement("prbsInvert",Boolean.TRUE);
							env.putElement("pattern",Oc48c.Pattern.getBasePatternFor(pattern));
							System.out.println("OC48Panel.change - sending prbsInvert = true");
						}
						else {
							env.putElement("pattern", pattern);
							env.putElement("prbsInvert",Boolean.FALSE);
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
		
		Boolean invert = (Boolean)penv.getElement("prbsInvert", false);
		if (invert != null) {
			if (invert.equals(Boolean.TRUE)) {
				Oc48c.Pattern pattern = (Oc48c.Pattern)penv.getElement("pattern");
				penv.putElement("pattern", Oc48c.Pattern.getInvertedFor(pattern));
			}
		}
		
		setEnvelope(penv);
		setTributaryEnabled(((Boolean)
				penv.getElement("rateEnabled")).booleanValue());


		getTributary().addStateChangeListener(mOC48ChangeConnector =
				new PStateChangeConnector(mOC48ChangeListener =
				new OC48ChangeListener(this)));
	}

	
	class ErrorItemAction extends AbstractAction {
		private String mText;	
		ErrorItemAction(String text) {
			super(text + " Error");
			mText = text;
		}
		public void actionPerformed(ActionEvent event) {
			mInjectButton.setText(mText + " Error");
			mInjectButton.setActionCommand(mText);
		}
	}
	
	

	public void setTributaryEnabled(boolean b) {
		if (b) {
			mEnableButton.setEnabled(false);
			mChangeButton.setEnabled(true);

			// should always be enabled
			mInjectButton.setEnabled(true);
		} else {
			mEnableButton.setEnabled(true);
			mChangeButton.setEnabled(false);

			// should always be enabled
			mInjectButton.setEnabled(true);
		}
	}


	public Oc48c getTributary() {
		return mOC48c;
	}


	public void dispose() {
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


	public void setTitle(String title) {
		super.setTitle(title + " Settings");
	}
	
	public void clearFlags() {
			super.clearBinderFlags();
	}
}
