////
//
// Telesync 5320 Project
//
//

package telesync.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.beans.*;
import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.txdatapath.*;
import telesync.gui.toh.*;
import telesync.gui.icons.binders.*;



public class OC48TransmitCardPanel extends CardPanel {
	private ModeButtonForm mMainForm;
	private TitleForm mHeatForm;
	private OC48CForm mOC48CForm;
	private POHForm mPOHForm;
	private TOHButtonForm mTOHForm;
	private DataPathPanel mDataPathPanel;
	private Tx48temperature mTemperature;
	private Oc48c mOC48c;
	private Tx48datapath mDataPath;
	private POH mPOH;
	private TOH mTOH;
	private ProgressBinder mInitProgressBinder;

	private PStateChangeConnector mHeatChangeConnector;
	private PStateChangeListener mHeatChangeListener;

	private PStateChangeConnector mOC48CChangeConnector;
	private PStateChangeListener mOC48CChangeListener;

	private PStateChangeConnector mPOHChangeConnector;
	private PStateChangeListener mPOHChangeListener;

	private PStateChangeConnector mTOHChangeConnector;
	private PStateChangeListener mTOHChangeListener;


	private JButton mInjectButton;
	private JButton mOC48EnableButton;
	private JButton mOC48ChangeButton;

	private Mode mMode;
	private JTabbedPane mTabbedPane;
		
	static private int cID = 0;
	private int mID = cID++;
	private ClientView mClientView;


	protected OC48TransmitCardPanel(ClientView clientView, Card card,
			int slotNumber) throws Exception {
		super(clientView, card, slotNumber);
		mClientView = clientView;
		System.out.println("OC48TransmitCardPanel.<init>: " + getInstanceID());
	}


	protected void createPeers() throws Exception {
		mTemperature = (Tx48temperature) getCard().getSubComponent("temperature");

		mDataPath = (Tx48datapath)
				getCard().getSubComponent("datapath");
		mOC48c = (Oc48c) mDataPath.getSubComponent("oc48c");

		setPOH((POH) getOC48C().getSubComponent("POH"));

		mTOH = (TOH)
				getOC48C().getSubComponent("TOH");
		
	}


	protected void createPeerListeners() throws Exception {
		mTemperature.addStateChangeListener(mHeatChangeConnector =
				new PStateChangeConnector(mHeatChangeListener =
				new HeatChangeListener()));

		mOC48c.addStateChangeListener(mOC48CChangeConnector =
				new PStateChangeConnector(mOC48CChangeListener =
				new OC48CChangeListener()));

		getPOH().addStateChangeListener(mPOHChangeConnector =
				new PStateChangeConnector(mPOHChangeListener =
				new POHChangeListener()));

		mTOH.addStateChangeListener(mTOHChangeConnector =
				new PStateChangeConnector(mTOHChangeListener =
				new TOHChangeListener()));
	}


	protected JComponent createContentComponent() throws Exception {
		mTabbedPane = new JTabbedPane();
		JTabbedPane tp = mTabbedPane;
		
		mTitleForm.add(Box.createVerticalStrut(5));
		mTitleForm.add(clearFlagsButton, BorderLayout.SOUTH);
		getTitlePanel().add(Box.createHorizontalGlue());
		
		// Main Form
		//
		mMainForm = new ModeButtonForm("OC-48 Transmit Card Status",
				new MainTypeSet()) {
			public void setMode(Mode mode) {
				if (mode.equals(Mode.US_SONET)) {
					setTitle("OC-48 Transmit Card Status");
				} else {
					setTitle("STM-16 Transmit Card Status");
				}
			}
		};


		PEnvelope penv = new PEnvelope(new MainTypeSet());
		penv.mergePComponent(getCard());
		
		Integer stepMax;
			
		if (penv.hasElement("mode")) {
			mMode = (Mode) penv.getElement("mode");
		}
		if (penv.hasElement("configMax")) {
			stepMax = (Integer) penv.getElement("configMax");
		} else {
			stepMax = new Integer(20);
		}

		if (penv.hasElement("name")) {
			setCardName((String)penv.getElement("name"));
			refreshTitleLabel();
		}

		mMainForm.addField("mode",
				new ComboBinder("Mode", Mode.class,
				Mode.getInstances()),true);
		
		mMainForm.addField("laserCount", new IntegerBinder("Laser Count"),true);
		HighlightBooleanBinder bb;
		mMainForm.addField("lasersOn",
				bb = new HighlightBooleanBinder("Lasers", "lasersOn",false,
				UIManager.getColor("pippin.colors.alertRed")),Binder.RECEIVE_EVENTS);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setIcon(BlackDot.getImageIcon());
		mMainForm.addField("init_done",
				bb = new HighlightBooleanBinder("Initialization", "init_done",false,
				UIManager.getColor("pippin.colors.alertRed")),Binder.HIDE_EVENTS, Binder.HIDE_COMPONENT);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("Complete");
		bb.setOffText("In Progress");
		bb.setIcon(BlackDot.getImageIcon());
		mMainForm.addField("status",
				mInitProgressBinder =
				new ProgressBinder("Initialization Status",
				stepMax.intValue()),Binder.HIDE_EVENTS);
		mMainForm.addField("pll_los",
				bb = new HighlightBooleanBinder("PLL", "pll_loss",false,
				UIManager.getColor("pippin.colors.alertRed")),Binder.HIDE_EVENTS);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setOnText("OK");
		bb.setOffText("LOS");
		bb.setInverted(true);
		bb.setIcon(BlackDot.getImageIcon());
		mMainForm.addField("serialNumber", new FormField("Serial Number", 150),true);
		mMainForm.setEnabled(false);
		//jb-010612-2 jeff blau 06/12/2001
		
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
				
				Tx48 tx48 = (Tx48)getCard();
				
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change Main Settings", new MainChangePanel());
				d.setVisible(true);
				Envelope env = d.getEnvelope();
				if (env != null) {
					PEnvelope pe = null;
					
					try {
						if ( !env.getElement("lasersOn").equals(tx48.get("lasersOn")) ) {
							if (tx48.hasLaserChanged()) {
								//tx48 has unsolicited laser change
								String msg = "The laser has recently been changed by another user.  ";
								msg += "Apply changes anyway?";
								
								int response = JOptionPane.showConfirmDialog(null,
									msg,"Confirm Changes",JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
									
								switch (response) {
									case JOptionPane.YES_OPTION:
										// do nothing.
										tx48.setLaserWasChanged(false); //reset flag;
										break;
									case JOptionPane.NO_OPTION: 
										env.removeElement("lasersOn");
										break;
									case JOptionPane.CANCEL_OPTION: 
										return;	
									
								}
								
								
								
							}
							
						}
						pe = new PEnvelope(env);	
						pe.applyToPComponent(getCard());	
						mMainForm.setMode(mMode);
						
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		
		mMainForm.addButton(b);
	
		mMainForm.setEnvelope(penv, true);
		
		if (penv.hasElement("configCount")) {
			mInitProgressBinder.setStepCount(
					((Integer) penv.getElement("configCount")).intValue());
		}
		
		mMainForm.setMode(mMode);
		addClearFlagEventListener(mMainForm);
		
		tp.add("Status", mMainForm);
		//
		// Main Form



		// OC48C Form
		//
		mOC48CForm = new OC48CForm("OC-48C Test Settings",
				new OC48CTypeSet());
		mOC48CForm.addField("rateEnabled",
				mOC48CForm.mRateBinder =
				new BooleanBinder("OC48c Rate", "Enabled", "Disabled"));
		mOC48CForm.addField("payloadType",
				new ComboBinder("Payload Type", Oc48c.PayloadType.class,
				Oc48c.PayloadType.getInstances()));
		mOC48CForm.addField("pattern",
				new ComboBinder("Pattern", Oc48c.Pattern.class,
				Oc48c.Pattern.getInstances()));
		mOC48CForm.addField("prbsInvert", new BooleanBinder("PRBS Invert"));
		mOC48CForm.addField("errorEnable",
				new BooleanBinder("Automatic Error Insertion"));
		mOC48CForm.addField("errorRate",
				new ComboBinder("Error Rate", Oc48c.ErrorRate.class,
				Oc48c.ErrorRate.getInstances()));
		mOC48CForm.addField("errorType",
				new ComboBinder("Error Type", Oc48c.ErrorType.class,
				Oc48c.ErrorType.getInstances()));
		// mOC48CForm.addField("errorButton", new IntegerBinder("Error Button"));

		mOC48CForm.setEnabled(false);

		mOC48EnableButton = ButtonMaker.createPButton("Enable");
		mOC48EnableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					
					getOC48C().put("rateEnabled", Boolean.TRUE);
				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog(e);
					e.printStackTrace();
				}
			}
		});
		mOC48CForm.addButton(mOC48EnableButton);
		
		
		
		
		mInjectButton = ButtonMaker.createPButton("Insert B1 Error");
		mInjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					// B1 Error is defined as 0x00
					//getOC48C().put("errorSingle", new Integer(0));
					String actionCommand = event.getActionCommand();
					Integer errorType;
					if (actionCommand.equals("b2")) {
						errorType = new Integer(1);
					}
					else if (actionCommand.equals("b3")) {
						errorType = new Integer(2);
					}
					else {
						errorType = new Integer(3);
					}
					
					mOC48c.insertError(mOC48c.getErrorType(errorType));
					
				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog(e);
					e.printStackTrace();
				}
			}
		});
		mOC48CForm.addButton(mInjectButton);

		mOC48ChangeButton = ButtonMaker.createPButton("Change");
		mOC48ChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OC48CChangePanel mPanel;
				String title = (mMode.equals(Mode.US_SONET)) ? "Change OC48" : "Change STM-16";
				
				BinderDialog d = new BinderDialog(GClient.getClient(),
						title, mPanel = new OC48CChangePanel());
				PEnvelope pe = new PEnvelope(OC48CTypeSet.class);
				try {
					pe.mergePComponent(getOC48C());
					mPanel.setEnvelope(pe);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				d.setVisible(true);

				
				Envelope env = d.getEnvelope();
				
				if (env != null) {
					PEnvelope pen = new PEnvelope(env);
					try {
						pen.applyToPComponent(getOC48C());
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		mOC48CForm.addButton(mOC48ChangeButton);


		penv = new PEnvelope(new OC48CTypeSet());
		penv.mergePComponent(getOC48C());
		mOC48CForm.setEnvelope(penv);
		mOC48CForm.setMode(mMode);
		setOC48Enabled(((Boolean)
				penv.getElement("rateEnabled")).booleanValue());

		// The OC48C panel has been obviated by the DataPath panel.
		//
		// if (mMode.equals(Tx48.Mode.US_SONET)) {
		// 	tp.add("OC-48C", mOC48CForm);
		// } else {
		// 	tp.add("STM-16O", mOC48CForm);
		// }

		//
		// OC48C Form



		// DataPath Panel
		//
		TitlePanel titlePanel = new TitlePanel("DataPath Composition",
				mDataPathPanel = new DataPathPanel(mDataPath), true, false);
		addClearFlagEventListener( mDataPathPanel );
		tp.add("DataPath", titlePanel);

		mDataPathPanel.setMode(mMode);
		//
		// DataPath Panel



		// POH Form
		//
		mPOHForm = new POHForm("Path Overhead Settings", new POHTypeSet());
		mPOHForm.addField("ptrace",
				new FixedStringBinder("Path Trace Message (J1)", 62),true);
		mPOHForm.addField("C2", new ByteBinder("C2"),Binder.HIDE_EVENTS);
		mPOHForm.addField("G1", new ByteBinder("G1"),Binder.HIDE_EVENTS);
		mPOHForm.addField("F2", new ByteBinder("F2"),Binder.HIDE_EVENTS);
		mPOHForm.addField("H4", new ByteBinder("H4"),Binder.HIDE_EVENTS);
		mPOHForm.addField("Z3", new ByteBinder("Z3"),Binder.HIDE_EVENTS);
		mPOHForm.addField("Z4", new ByteBinder("Z4"),Binder.HIDE_EVENTS);
		mPOHForm.addField("Z5", new ByteBinder("Z5"),Binder.HIDE_EVENTS);

		mPOHForm.setEnabled(false);

		b = ButtonMaker.createPButton("Change");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				POHChangePanel mPanel;
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change Path Overhead", mPanel = new POHChangePanel());
				PEnvelope pe = new PEnvelope(POHTypeSet.class);
				try {
					pe.mergePComponent(getPOH());
					mPanel.setEnvelope(pe);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				d.setVisible(true);

				Envelope env = d.getEnvelope();
				if (env != null) {
					PEnvelope pen = new PEnvelope(env);
					try {
						pen.applyToPComponent(getPOH());
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		mPOHForm.addButton(b);


		penv = new PEnvelope(new POHTypeSet());
		penv.mergePComponent(getPOH());
		mPOHForm.setEnvelope(penv);
		addClearFlagEventListener(mPOHForm);
		tp.add("Path Overhead", mPOHForm);
		//
		// POH Form


		// TOH Form
		//
		mTOHForm = new TOHButtonForm("Transport Overhead Settings", false);

		mTOHForm.setEnabled(false);

		b = ButtonMaker.createPButton("Change");
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
						
						//consider dispatching this async...
						//jb-010612-1 - jeff blau 06/12/2001
						GClient.getClient().suspendCursor();
						pen.applyToPComponent(getTOH());
						GClient.getClient().resumeCursor();
						
					}
				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog(e);
					e.printStackTrace();
				}
			}
		});
		mTOHForm.addButton(b);


		penv = new PEnvelope(new TOHTypeSet());
		penv.mergePComponent(getTOH());
		mTOHForm.setEnvelope(penv);
		mTOHForm.setMode(mMode);
		addClearFlagEventListener(mTOHForm);
		tp.add("Transport Overhead", mTOHForm);
		//
		// TOH Form



		// Heat Form
		//
		mHeatForm = new TitleForm("Thermometers",
				new HeatTypeSet());
		mHeatForm.addField("topTemp", new HeatBinder("Top Temperature"));
		mHeatForm.addField("botTemp",
				new HeatBinder("Bottom Temperature"));

		mHeatForm.setEnabled(false);


		penv = new PEnvelope(new HeatTypeSet());
		penv.mergePComponent(mTemperature);
		mHeatForm.setEnvelope(penv);

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


		return tp;
	}
	
	public void doStateChange(PStateChangeEvent event) {
		PEnvelope env;
		if (event.getSource() == getCard()) {
			env = new PEnvelope(new MainTypeSet());
			try {
				env.mergePComponent(getCard());
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
						
			env.mergePAttributeList(event.getAttributes());
			
			if (env.hasElement("init_done")) {
				boolean initDone = ((Boolean)env.getElement("init_done")).booleanValue();
				setCardInitDone((Boolean) env.getElement("init_done"));
				if (initDone) {
					
					mMainForm.setEnvelope(env);
					mInitProgressBinder.setBoundValue("Complete");	
				}
				else {
					mMainForm.setEnvelope(env, true);
				}
			}
			
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
				mMode = mode;
				titleLabel.setText(getLabelText());
				if (mDataPathPanel != null) {
					mDataPathPanel.setMode(mode);
				}
				if (mTOHForm != null) {
					mTOHForm.setMode(mode);
				}
				if (mMainForm != null) {
					mMainForm.setMode(mode);
					
				}
				if (mOC48CForm != null) {
					mOC48CForm.setMode(mode);
				}

				if (mMode.equals(Mode.US_SONET)) {
					if (mTabbedPane != null) {
						int ci = mTabbedPane.indexOfComponent(mOC48CForm);
						if (ci >= 0) {
							mTabbedPane.setTitleAt(ci, "OC-48C");
						}
					}
				} else {
					if (mTabbedPane != null) {
						int ci = mTabbedPane.indexOfComponent(mOC48CForm);
						if (ci >= 0) {
							mTabbedPane.setTitleAt(ci, "STM-16");
						}
					}
				}
			}
		}
	}


	protected Oc48c getOC48C() {
		return mOC48c;
	}


	protected POH getPOH() {
		if (mPOH == null) {
			throw new Error("POH is null");
		}
		return mPOH;
	}

	private void setPOH(POH poh) {
		mPOH = poh;
		if (poh == null) {
			throw new NullPointerException("poh");
		}
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

		if (mOC48c != null) {
			mOC48c.removeStateChangeListener(mOC48CChangeConnector);
		}
		mOC48c = null;

		mDataPathPanel.dispose();
	}



	public static class MainTypeSet extends TypeSet {
		public MainTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("mode", Mode.class),
				new TypeSetElement("serialNumber", String.class),
				new TypeSetElement("laserCount", Integer.class),
				new TypeSetElement("lasersOn", Boolean.class),
				new TypeSetElement("pll_los", Boolean.class),
				new TypeSetElement("init_done", Boolean.class),
				new TypeSetElement("status", String.class),
				new TypeSetElement("configCount", Integer.class),
				new TypeSetElement("configMax", Integer.class),
				new TypeSetElement("name", String.class),
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
				new TypeSetElement("payloadType", Oc48c.PayloadType.class),
				new TypeSetElement("pattern", Oc48c.Pattern.class),
				new TypeSetElement("prbsInvert", Boolean.class),
				new TypeSetElement("errorEnable", Boolean.class),
				new TypeSetElement("errorRate", Oc48c.ErrorRate.class),
				new TypeSetElement("errorType", Oc48c.ErrorType.class),
				new TypeSetElement("errorSingle", Integer.class),
				new TypeSetElement("errorButton", Integer.class),
			});

			getElement("payloadType").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc48c.PayloadType) o).getValue();
				}
			});
			getElement("payloadType").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc48c.PayloadType.getInstanceFor((Integer) o);
				}
			});

			getElement("pattern").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc48c.Pattern) o).getValue();
				}
			});
			getElement("pattern").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc48c.Pattern.getInstanceFor((Integer) o);
				}
			});

			getElement("errorRate").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc48c.ErrorRate) o).getValue();
				}
			});
			getElement("errorRate").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc48c.ErrorRate.getInstanceFor((Integer) o);
				}
			});

			getElement("errorType").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc48c.ErrorType) o).getValue();
				}
			});
			getElement("errorType").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc48c.ErrorType.getInstanceFor((Integer) o);
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
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			addField("mode", new ComboBinder("Mode", Mode.class,
				Mode.getInstances()),Binder.HIDE_EVENTS);
			addField("lasersOn", new BooleanBinder("Lasers"),Binder.HIDE_EVENTS);
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


	protected class OC48CChangeListener
			implements PStateChangeListener {
		public void stateChanged(PStateChangeEvent event) {
			try {
				
				PEnvelope env = new PEnvelope(mOC48CForm.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				mOC48CForm.setEnvelope(env);

				if (env.hasElement("rateEnabled")) {
					Boolean b = (Boolean) env.getElement("rateEnabled");
					setOC48Enabled(b.booleanValue());
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void setOC48Enabled(boolean enabled) {
		if (enabled) {
			mOC48EnableButton.setEnabled(false);
			mInjectButton.setEnabled(true);
			mOC48ChangeButton.setEnabled(true);
		} else {
			mOC48EnableButton.setEnabled(true);
			mInjectButton.setEnabled(false);
			mOC48ChangeButton.setEnabled(false);
		}
	}


	// Used by setCardInitDone()
	private Component mInitPendingComponent = null;

	private void setCardInitDone(Boolean initDone) {
		int si = mTabbedPane.indexOfTab("Status");
		
		
		if (initDone.booleanValue()) {
			
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


	protected class POHChangeListener
			implements PStateChangeListener {
		public void stateChanged(PStateChangeEvent event) {
			try {
				PEnvelope env = new PEnvelope(mPOHForm.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				mPOHForm.setEnvelope(env);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	protected class TOHChangeListener
			implements PStateChangeListener {
		public void stateChanged(PStateChangeEvent event) {
			try {
				
				PEnvelope env = new PEnvelope(mTOHForm.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				mTOHForm.setEnvelope(env);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


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
			//addField("prbsInvert", new BooleanBinder("PRBS Invert"));
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


	protected class POHChangePanel extends BasicForm {
		protected POHChangePanel() {
			super(new POHTypeSet());

			PEnvelope env = new PEnvelope(new POHTypeSet());
			try {
				env.mergePComponent(getPOH());
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			addField("ptrace",
					new FixedStringBinder("Path Trace Message (J1)", 62),false);
			addField("C2", new ByteBinder("C2"),false);
			addField("G1", new ByteBinder("G1"),false);
			addField("F2", new ByteBinder("F2"),false);
			addField("H4", new ByteBinder("H4"),false);
			addField("Z3", new ByteBinder("Z3"),false);
			addField("Z4", new ByteBinder("Z4"),false);
			addField("Z5", new ByteBinder("Z5"),false);

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

	static private class POHForm extends ButtonForm implements ClearFlagEventListener {
		public POHForm(String title, TypeSet typeSet) {
			super(title,typeSet);
		}
		public void clearFlags() {
			super.clearBinderFlags();
		}
	}
	
	static abstract protected class ModeButtonForm extends ButtonForm implements ClearFlagEventListener {
		protected ModeButtonForm(String title, TypeSet typeSet) {
			super(title, typeSet);
		}

		abstract public void setMode(Mode mode);
		public void clearFlags() {
			super.clearBinderFlags();
		}
		
	}


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


	protected Mode getMode() {
		return mMode;
	}
}
