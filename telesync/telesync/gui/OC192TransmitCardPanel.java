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
import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.txdatapath.*;
import telesync.gui.toh.*;
import telesync.gui.icons.binders.*;



public class OC192TransmitCardPanel extends CardPanel {
	private ModeButtonForm mMainForm;
	private TitleForm mHeatForm;
	private OC192CForm mOC192CForm;
	private ButtonForm mPOHForm;
	private OC192TOHButtonForm mTOHForm;
	private OC192DataPathPanel mDataPathPanel;
	private Tx192temperature mTemperature;
	private Oc192c mOC192c;
	private Tx192datapath mDataPath;
	private POH mPOH;
	private TOH mTOH;
	private ProgressBinder mInitProgressBinder;

	private PStateChangeConnector mHeatChangeConnector;
	private PStateChangeListener mHeatChangeListener;

	private PStateChangeConnector mOC192CChangeConnector;
	private PStateChangeListener mOC192CChangeListener;

	private PStateChangeConnector mPOHChangeConnector;
	private PStateChangeListener mPOHChangeListener;

	private PStateChangeConnector mTOHChangeConnector;
	private PStateChangeListener mTOHChangeListener;


	private JButton mInjectButton;
	private JButton mOC192EnableButton;
	private JButton mOC192ChangeButton;

	private Mode mMode;
	private JTabbedPane mTabbedPane;

	static private int cID = 0;
	private int mID = cID++;



	protected OC192TransmitCardPanel(ClientView clientView, Card card,
			int slotNumber) throws Exception {
		super(clientView, card, slotNumber);

		System.out.println("OC192TransmitCardPanel.<init>: " + getInstanceID());
	}


	protected void createPeers() throws Exception {
		mTemperature = (Tx192temperature) getCard().getSubComponent("temperature");

		mDataPath = (Tx192datapath)
				getCard().getSubComponent("datapath");
		mOC192c = (Oc192c) mDataPath.getSubComponent("oc192c");

		setPOH((POH) getOC192C().getSubComponent("POH"));

		mTOH = (TOH)
				getOC192C().getSubComponent("TOH");
	}


	protected void createPeerListeners() throws Exception {
		mTemperature.addStateChangeListener(mHeatChangeConnector =
				new PStateChangeConnector(mHeatChangeListener =
				new HeatChangeListener()));

		mOC192c.addStateChangeListener(mOC192CChangeConnector =
				new PStateChangeConnector(mOC192CChangeListener =
				new OC192CChangeListener()));

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
		mMainForm = new ModeButtonForm("OC-192 Transmit Card Status",
				new MainTypeSet()) {
			public void setMode(Mode mode) {
				if (mode.equals(Mode.US_SONET)) {
					setTitle("OC-192 Transmit Card Status");
				} else {
					setTitle("STM-16 Transmit Card Status");
				}
			}
		};

		

		PEnvelope penv = new PEnvelope(new MainTypeSet());
		Integer stepMax;
		// penv.mergePComponent(getCard());
		penv.mergePComponent(getCard());
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
				Mode.getInstances()),Binder.HIDE_EVENTS);
		mMainForm.addField("laserCount", new IntegerBinder("Laser Count"));
		HighlightBooleanBinder bb;
		mMainForm.addField("lasersOn",
				bb = new HighlightBooleanBinder("Lasers", "lasersOn",false,
				UIManager.getColor("pippin.colors.alertRed")),Binder.RECEIVE_EVENTS);
		bb.setRestColor(UIManager.getColor("pippin.colors.goGreen"));
		bb.setIcon(BlackDot.getImageIcon());
		mMainForm.addField("init_done",
				bb = new HighlightBooleanBinder("Initialization", "init_done",false,
				UIManager.getColor("pippin.colors.alertRed")),Binder.HIDE_EVENTS,Binder.HIDE_COMPONENT);
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
		mMainForm.addField("serialNumber", new FormField("Serial Number", 150),Binder.HIDE_EVENTS);
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
		mMainForm.addButton(b);

		
		b = ButtonMaker.createPButton("Change");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change Main Settings", new MainChangePanel());
				d.setVisible(true);
				Envelope env = d.getEnvelope();
				if (env != null) {
					PEnvelope pe = new PEnvelope(env);
					try {
						pe.applyToPComponent(getCard());
						// getCard().put("lasersOn", (Boolean)
						// 		env.getElement("lasersOn"));
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		mMainForm.addButton(b);

		mMainForm.setEnvelope(penv);
		if (penv.hasElement("configCount")) {
			mInitProgressBinder.setStepCount(
					((Integer) penv.getElement("configCount")).intValue());
		}

		tp.add("Status", mMainForm);
		//
		// Main Form



		// OC192C Form
		//
		mOC192CForm = new OC192CForm("OC-192C Test Settings",
				new OC192CTypeSet());
		mOC192CForm.addField("rateEnabled",
				mOC192CForm.mRateBinder =
				new BooleanBinder("OC192c Rate", "Enabled", "Disabled"));
		mOC192CForm.addField("payloadType",
				new ComboBinder("Payload Type", Oc192c.PayloadType.class,
				Oc192c.PayloadType.getInstances()));
		mOC192CForm.addField("pattern",
				new ComboBinder("Pattern", Oc192c.Pattern.class,
				Oc192c.Pattern.getInstances()),Binder.HIDE_EVENTS);
		mOC192CForm.addField("prbsInvert", new BooleanBinder("PRBS Invert"));
		mOC192CForm.addField("errorEnable",
				new BooleanBinder("Automatic Error Insertion"));
		mOC192CForm.addField("errorRate",
				new ComboBinder("Error Rate", Oc192c.ErrorRate.class,
				Oc192c.ErrorRate.getInstances()));
		mOC192CForm.addField("errorType",
				new ComboBinder("Error Type", Oc192c.ErrorType.class,
				Oc192c.ErrorType.getInstances()));
		// mOC192CForm.addField("errorButton", new IntegerBinder("Error Button"));

		mOC192CForm.setEnabled(false);

		mOC192EnableButton = ButtonMaker.createPButton("Enable");
		mOC192EnableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					// B1 Error is defined as 0x00
					getOC192C().put("rateEnabled", Boolean.TRUE);
				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog(e);
					e.printStackTrace();
				}
			}
		});
		mOC192CForm.addButton(mOC192EnableButton);

		mInjectButton = ButtonMaker.createPButton("Insert B1 Error");
		mInjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					// B1 Error is defined as 0x00
					getOC192C().put("errorSingle", new Integer(0));
				}
				catch (Exception e) {
					GClient.getClient().showAlertDialog(e);
					e.printStackTrace();
				}
			}
		});
		mOC192CForm.addButton(mInjectButton);

		mOC192ChangeButton = ButtonMaker.createPButton("Change");
		mOC192ChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OC192CChangePanel mPanel;
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change OC192C", mPanel = new OC192CChangePanel());
				PEnvelope pe = new PEnvelope(OC192CTypeSet.class);
				try {
					pe.mergePComponent(getOC192C());
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
						pen.applyToPComponent(getOC192C());
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		mOC192CForm.addButton(mOC192ChangeButton);


		penv = new PEnvelope(new OC192CTypeSet());
		penv.mergePComponent(getOC192C());
		mOC192CForm.setEnvelope(penv);
		mOC192CForm.setMode(mMode);
		
		addClearFlagEventListener(mMainForm);
		
		setOC192Enabled(((Boolean)
				penv.getElement("rateEnabled")).booleanValue());

		// The OC192C panel has been obviated by the DataPath panel.
		//
		// if (mMode.equals(Mode.US_SONET)) {
		// 	tp.add("OC-192C", mOC192CForm);
		// } else {
		// 	tp.add("STM-16O", mOC192CForm);
		// }

		//
		// OC192C Form



		// DataPath Panel
		//
		TitlePanel titlePanel = new TitlePanel("DataPath Composition",
				mDataPathPanel = new OC192DataPathPanel(mDataPath), true, false);

		tp.add("DataPath", titlePanel);

		mDataPathPanel.setMode(mMode);
		addClearFlagEventListener( mDataPathPanel );
		
		//
		// DataPath Panel



		// POH Form
		//
		mPOHForm = new ButtonForm("Path Overhead Settings", new POHTypeSet());
		mPOHForm.addField("ptrace",
				new FixedStringBinder("Path Trace Message (J1)", 62));
		mPOHForm.addField("C2", new ByteBinder("C2"));
		mPOHForm.addField("G1", new ByteBinder("G1"));
		mPOHForm.addField("F2", new ByteBinder("F2"));
		mPOHForm.addField("H4", new ByteBinder("H4"));
		mPOHForm.addField("Z3", new ByteBinder("Z3"));
		mPOHForm.addField("Z4", new ByteBinder("Z4"));
		mPOHForm.addField("Z5", new ByteBinder("Z5"));

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

		tp.add("Path Overhead", mPOHForm);
		//
		// POH Form


		// TOH Form
		//
		mTOHForm = new OC192TOHButtonForm("Transport Overhead Settings");

		mTOHForm.setEnabled(false);

		b = ButtonMaker.createPButton("Change");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OC192TOHPanel mPanel;
				TOH toh;
				BinderDialog d;
				PEnvelope pe = new PEnvelope(TOHTypeSet.class);
				try {
					toh = getTOH();
					d = new BinderDialog(GClient.getClient(),
							"Change Transport Overhead",
							mPanel = new OC192TOHPanel(getTOH(), (Mode)getMode()));
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


		penv = new PEnvelope(new TOHTypeSet());
		penv.mergePComponent(getTOH());
		mTOHForm.setEnvelope(penv);
		mTOHForm.setMode(mMode);

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
			env = new PEnvelope(mMainForm.getEnvelope());
			env.mergePAttributeList(event.getAttributes());

			mMainForm.setEnvelope(env);

			if (env.hasElement("init_done")) {
				setCardInitDone((Boolean) env.getElement("init_done"));
				
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
				if (mDataPathPanel != null) {
					mDataPathPanel.setMode(mode);
				}
				if (mTOHForm != null) {
					mTOHForm.setMode(mode);
				}
				if (mMainForm != null) {
					mMainForm.setMode(mode);
				}
				if (mOC192CForm != null) {
					mOC192CForm.setMode(mode);
				}

				if (mMode.equals(Mode.US_SONET)) {
					if (mTabbedPane != null) {
						int ci = mTabbedPane.indexOfComponent(mOC192CForm);
						if (ci >= 0) {
							mTabbedPane.setTitleAt(ci, "OC-192C");
						}
					}
				} else {
					if (mTabbedPane != null) {
						int ci = mTabbedPane.indexOfComponent(mOC192CForm);
						if (ci >= 0) {
							mTabbedPane.setTitleAt(ci, "STM-16");
						}
					}
				}
			}
		}
	}


	protected Oc192c getOC192C() {
		return mOC192c;
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

		if (mOC192c != null) {
			mOC192c.removeStateChangeListener(mOC192CChangeConnector);
		}
		mOC192c = null;

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


	public static class OC192CTypeSet extends TypeSet {
		public OC192CTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("rateEnabled", Boolean.class),
				new TypeSetElement("payloadType", Oc192c.PayloadType.class),
				new TypeSetElement("pattern", Oc192c.Pattern.class),
				new TypeSetElement("prbsInvert", Boolean.class),
				new TypeSetElement("errorEnable", Boolean.class),
				new TypeSetElement("errorRate", Oc192c.ErrorRate.class),
				new TypeSetElement("errorType", Oc192c.ErrorType.class),
				new TypeSetElement("errorSingle", Integer.class),
				new TypeSetElement("errorButton", Integer.class),
			});

			getElement("payloadType").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc192c.PayloadType) o).getValue();
				}
			});
			getElement("payloadType").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc192c.PayloadType.getInstanceFor((Integer) o);
				}
			});

			getElement("pattern").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc192c.Pattern) o).getValue();
				}
			});
			getElement("pattern").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc192c.Pattern.getInstanceFor((Integer) o);
				}
			});

			getElement("errorRate").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc192c.ErrorRate) o).getValue();
				}
			});
			getElement("errorRate").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc192c.ErrorRate.getInstanceFor((Integer) o);
				}
			});

			getElement("errorType").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Oc192c.ErrorType) o).getValue();
				}
			});
			getElement("errorType").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Oc192c.ErrorType.getInstanceFor((Integer) o);
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
				Mode.getInstances()),false);
			addField("lasersOn", new BooleanBinder("Lasers"),false);
			addField("name", new FormField("Card Name"),false);
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


	protected class OC192CChangeListener
			implements PStateChangeListener {
		public void stateChanged(PStateChangeEvent event) {
			try {
				PEnvelope env = new PEnvelope(mOC192CForm.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				mOC192CForm.setEnvelope(env);
				
				if (env.hasElement("rateEnabled")) {
					Boolean b = (Boolean) env.getElement("rateEnabled");
					setOC192Enabled(b.booleanValue());
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void setOC192Enabled(boolean enabled) {
		if (enabled) {
			mOC192EnableButton.setEnabled(false);
			mInjectButton.setEnabled(true);
			mOC192ChangeButton.setEnabled(true);
		} else {
			mOC192EnableButton.setEnabled(true);
			mInjectButton.setEnabled(false);
			mOC192ChangeButton.setEnabled(false);
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


	protected class OC192CChangePanel extends BasicForm {
		protected OC192CChangePanel() {
			super(new OC192CTypeSet());

			PEnvelope env = new PEnvelope(new OC192CTypeSet());
			try {
				env.mergePComponent(getOC192C());
			}
			catch (Exception e) {
				e.printStackTrace();
			}


			addField("payloadType",
					new ComboBinder("Payload Type", Oc192c.PayloadType.class,
					Oc192c.PayloadType.getInstances()),false);
			addField("pattern",
					new ComboBinder("Pattern", Oc192c.Pattern.class,
					Oc192c.Pattern.getInstances()),Binder.HIDE_EVENTS);
			addField("prbsInvert", new BooleanBinder("PRBS Invert"),false);
			addField("errorEnable",
					new BooleanBinder("Automatic Error Insertion"),false);
			addField("errorRate",
					new ComboBinder("Error Rate", Oc192c.ErrorRate.class,
					Oc192c.ErrorRate.getInstances()),false);
			addField("errorType",
					new ComboBinder("Error Type", Oc192c.ErrorType.class,
					Oc192c.ErrorType.getInstances()));


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
					new FixedStringBinder("Path Trace Message (J1)", 62));
			addField("C2", new ByteBinder("C2"));
			addField("G1", new ByteBinder("G1"));
			addField("F2", new ByteBinder("F2"));
			addField("H4", new ByteBinder("H4"));
			addField("Z3", new ByteBinder("Z3"));
			addField("Z4", new ByteBinder("Z4"));
			addField("Z5", new ByteBinder("Z5"));

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


	static abstract protected class ModeButtonForm extends ButtonForm implements ClearFlagEventListener{
		protected ModeButtonForm(String title, TypeSet typeSet) {
			super(title, typeSet);
		}

		abstract public void setMode(Mode mode);
		public void clearFlags() {
			super.clearBinderFlags();
		}
		
	}


	static private class OC192CForm extends ModeButtonForm {
		BooleanBinder mRateBinder;
		protected OC192CForm(String title, TypeSet typeSet) {
			super(title, typeSet);
		}

		public void setMode(Mode mode) {
			if (mode.equals(Mode.US_SONET)) {
				setTitle("OC-192C Test Settings");
				mRateBinder.setTitle("OC192c Rate");
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
