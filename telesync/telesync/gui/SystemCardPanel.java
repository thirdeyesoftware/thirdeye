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
import pippin.util.*;
import telesync.pui.tsi5320.*;



public class SystemCardPanel extends CardPanel {
	private ButtonForm mMainForm;
	private ButtonForm mClockForm;
	private ButtonForm mFansForm;

	private Clock mClock;
	private Fans mFans;

	private PStateChangeConnector mFansChangeConnector;
	private PStateChangeListener mFansChangeListener;

	protected SystemCardPanel(ClientView clientView, Card card,
			int slotNumber) throws Exception {
		super(clientView, card, slotNumber);

		System.out.println("SytemCardPanel.<init>: " + getInstanceID());
	}


	protected void createPeers() throws Exception {
		System.out.println("SystemCardPanel.createPeers:");

		mClock = (Clock) getCard().getSubComponent("clock");
		if (mClock == null) {
			throw new Error("no clock");
		}

		mFans = (Fans) getCard().getSubComponent("fans");
		if (mFans == null) {
			throw new Error("no fans");
		}
	}

	protected void createPeerListeners() throws Exception {
		mFans.addStateChangeListener(mFansChangeConnector =
				new PStateChangeConnector(mFansChangeListener =
				new FansChangeListener()));
	}


	private Clock getClock() {
		return mClock;
	}

	private Fans getFans() {
		return mFans;
	}


	protected JComponent createContentComponent() throws Exception {
		JButton b;
		JTabbedPane tp = new JTabbedPane();

		mMainForm = new ButtonForm("System Status", new MainTypeSet());
		mMainForm.addField("softVersion",
				new FormField("Software Version", 220));
		
		//jb-01080301 expand "name" field to size of software version
		mMainForm.addField("name", new FormField("Name",220));
		mMainForm.addField("serialNumber", new FormField("Serial Number"));
		mMainForm.setEnabled(false);

		PEnvelope env = new PEnvelope(new MainTypeSet());
		env.mergePComponent(getCard());
		GClient.getClient().setName((String)env.getElement("name"));
		
		mMainForm.setEnvelope(env);

		b = ButtonMaker.createPButton("Change");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change Name", new NameChangePanel());
				d.setVisible(true);
				Envelope envelope = d.getEnvelope();
				if (envelope != null) {
					try {
						PEnvelope penv = new PEnvelope(envelope);
						penv.applyToPComponent(getCard());
						// getCard().put("name", (String) envelope.getElement("name"));
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		mMainForm.addButton(b);

		tp.add("Status", mMainForm);


		mClockForm = new ButtonForm("Real Time Clock", new ClockTypeSet());
		mClockForm.addField("seconds", new MomentBinder("Time"));
		mClockForm.setEnabled(false);

		env = new PEnvelope(ClockTypeSet.class);
		env.mergePComponent(getClock());
		mClockForm.setEnvelope(env);

		b = ButtonMaker.createPButton("Refresh");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					mClockForm.setBoundValue("seconds",
							new Moment(((Integer) mClock.get("seconds")).intValue()));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mClockForm.addButton(b);

		b = ButtonMaker.createPButton("Change");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Set Clock", new ClockChangePanel());
				d.setVisible(true);
				Envelope envelope = d.getEnvelope();
				if (envelope != null) {
					try {
						PEnvelope penv = new PEnvelope(envelope);
						penv.applyToPComponent(getClock());
						// getClock().put("seconds", new Integer(
						// 		(int)
						// 		((Moment) envelope.getElement("seconds")).getSeconds()));
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		mClockForm.addButton(b);

		tp.add("Clock", mClockForm);


		env = new PEnvelope(FansTypeSet.class);
		env.mergePComponent(getFans());
		int fanCount = 4;
		if (env.hasElement("fanCount")) {
			fanCount = ((Integer) env.getElement("fanCount")).intValue();
		}

		mFansForm = new ButtonForm("Chassis Fans", new FansTypeSet());

		for (int i = 1; i <= fanCount; ++i) {
			IntegerBinder ib = new IntegerBinder("Fan " + i + " RPM");
			ib.setPreferredFieldWidth("88888");
			mFansForm.addField("fan" + i, ib);
		}

		mFansForm.setEnabled(false);
		mFansForm.setEnvelope(env);

		tp.add("Fans", mFansForm);

		return tp;
	}


	public void doStateChange(PStateChangeEvent event) {
		
		PEnvelope env;
		
		if (event.getSource() == getCard()) {
			env = new PEnvelope(mMainForm.getEnvelope());
			env.mergePAttributeList(event.getAttributes());

			mMainForm.setEnvelope(env);

			if (env.hasElement("name")) {
				String name = (String)env.getElement("name");
				GClient.getClient().setName(name);
			}
		/*
		} else
		if (event.getSource() == mClock) {
			env = new Envelope(new ClockTypeSet());
			env.putElement("seconds",
					new Moment(((Integer)
					event.getAttribute("seconds").getValue()).intValue()));

			mClockForm.setEnvelope(env);
		*/
		}
		
		
	}


	public void dispose() throws Exception {
		super.dispose();
		mClock = null;
	}


	public static class MainTypeSet extends TypeSet {
		public MainTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("softVersion", String.class),
				new TypeSetElement("name", String.class),
				new TypeSetElement("serialNumber", String.class),
				new TypeSetElement("hdlcFail", Integer.class),
			});
		}
	}


	public static class ClockTypeSet extends TypeSet {
		public ClockTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("seconds", Moment.class),
				new TypeSetElement("msecs", Integer.class),
			});

			getElement("seconds").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return new Moment(((Integer) o).intValue());
				}
			});
			getElement("seconds").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return new Integer((int) ((Moment) o).getSeconds());
				}
			});
		}
	}


	public static class FansTypeSet extends TypeSet {
		public FansTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("fanCount", Integer.class),
				new TypeSetElement("fan1", Integer.class),
				new TypeSetElement("fan2", Integer.class),
				new TypeSetElement("fan3", Integer.class),
				new TypeSetElement("fan4", Integer.class),
			});
		}
	}


	protected class NameChangePanel extends BasicForm {
		protected NameChangePanel() {
			super(new MainTypeSet());

			PEnvelope env = new PEnvelope(new MainTypeSet());
			try {
				env.mergePComponent(getCard());
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			addField("name", new FormField("Name"));

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


	protected class ClockChangePanel extends BasicForm {
		protected ClockChangePanel() {
			super(new ClockTypeSet());

			PEnvelope env = new PEnvelope(new ClockTypeSet());
			try {
				// Moment m = new Moment(
				// 		((Integer) getClock().get("seconds")).intValue());
				// env.putElement("seconds", m);
				env.mergePComponent(getClock());
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			addField("seconds", new MomentBinder("Current Time"));

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


	protected class FansChangeListener
			implements PStateChangeListener {
		public void stateChanged(PStateChangeEvent event) {
			try {
				PEnvelope env = new PEnvelope(mFansForm.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				mFansForm.setEnvelope(env);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
