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



public class OC192Panel extends ButtonForm implements ClearFlagEventListener,
											TributaryEnabledListener {

	Oc192c mOC192c;
	private PStateChangeConnector mOC192ChangeConnector;
	private PStateChangeListener mOC192ChangeListener;
	private JButton mEnableButton;
	private JButton mChangeButton;
	private JButton mInjectButton;
	private JPopupMenu mPopupMenu;
	static Font cMenuFont = new Font("SansSerif", Font.PLAIN, 12);

	public OC192Panel(String title, Mode mode, Oc192c oc192c)
			throws Exception {

		super(title + " Settings",
				new OC192TypeSet(), false);


		mOC192c = oc192c;
		if (mOC192c == null) {
			throw new NullPointerException();
		}


		if (mode.equals(Mode.US_SONET)) {
			addField("rateEnabled",
					new BooleanBinder("OC192c Rate", "Enabled", "Disabled"),true);
		} else {
			addField("rateEnabled",
					new BooleanBinder("STM-16 Rate", "Enabled", "Disabled"),true);
		}
		addField("payloadType",
				new ComboBinder("Payload Type", Oc192c.PayloadType.class,
				Oc192c.PayloadType.getInstances()),true);
		addField("pattern",
				new ComboBinder("Pattern", Oc192c.Pattern.class,
				Oc192c.Pattern.getInstances()),true);
		addField("prbsInvert", new BooleanBinder("PRBS Invert"));
		addField("errorEnable",
				new BooleanBinder("Auto Error Insert"),true);
		addField("errorRate",
				new ComboBinder("Error Rate", Oc192c.ErrorRate.class,
				Oc192c.ErrorRate.getInstances()),true);
		addField("errorType",
				new ComboBinder("Error Type", Oc192c.ErrorType.class,
				Oc192c.ErrorType.getInstances()),true);
		//addField("errorButton", new IntegerBinder("Error Button"),true);

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


		mChangeButton = ButtonMaker.createPButton("Change");
		mChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OC192ChangePanel mPanel;
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change OC192C", mPanel = new OC192ChangePanel(getTributary()));
				PEnvelope pe = new PEnvelope(OC192TypeSet.class);
				try {
					pe.mergePComponent(getTributary());
					Boolean invert = (Boolean)pe.getElement("prbsInvert", false);
					if (invert != null) {
						if (invert.equals(Boolean.TRUE)) {
							Oc192c.Pattern pattern = (Oc192c.Pattern)pe.getElement("pattern");
							pe.putElement("pattern", Oc192c.Pattern.getInvertedFor(pattern));
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
						Oc192c.Pattern pattern = (Oc192c.Pattern)env.getElement("pattern");
						if (pattern.isInverted()) {
							env.putElement("prbsInvert",Boolean.TRUE);
							env.putElement("pattern",Oc192c.Pattern.getBasePatternFor(pattern));
							
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


		PEnvelope penv = new PEnvelope(new OC192TypeSet());
		penv.mergePComponent(getTributary());
		Boolean invert = (Boolean)penv.getElement("prbsInvert", false);
		if (invert != null) {
			if (invert.equals(Boolean.TRUE)) {
				Oc192c.Pattern pattern = (Oc192c.Pattern)penv.getElement("pattern");
				penv.putElement("pattern", Oc192c.Pattern.getInvertedFor(pattern));
			}
		}
		
		setEnvelope(penv);
		setTributaryEnabled(((Boolean)
				penv.getElement("rateEnabled")).booleanValue());


		getTributary().addStateChangeListener(mOC192ChangeConnector =
				new PStateChangeConnector(mOC192ChangeListener =
				new OC192ChangeListener(this)));
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
			mInjectButton.setEnabled(true);
		} else {
			mEnableButton.setEnabled(true);
			mChangeButton.setEnabled(false);
			mInjectButton.setEnabled(true);
		}
	}


	public Oc192c getTributary() {
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


	public void setTitle(String title) {
		super.setTitle(title + " Settings");
	}
	public void clearFlags() {
		super.clearBinderFlags();
	}
	
}
