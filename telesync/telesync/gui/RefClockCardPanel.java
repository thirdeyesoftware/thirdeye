////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.icons.binders.*;



public class RefClockCardPanel extends CardPanel {
	private ButtonForm mMainForm;

	protected RefClockCardPanel(ClientView clientView, Card card,
			int slotNumber) throws Exception {
		super(clientView, card, slotNumber);

		System.out.println("RefClockCardPanel.<init>: " + getInstanceID());
	}


	protected void createPeers() throws Exception {
	}


	protected void createPeerListeners() {
	}


	protected JComponent createContentComponent() throws Exception {
		JTabbedPane tp = new JTabbedPane();
		BooleanBinder bb;

		mMainForm = new ButtonForm("Reference Clock Status", new MainTypeSet());
		mMainForm.addField("source",
				new ComboBinder("Source", Refclk.ClockSource.class,
				Refclk.ClockSource.getClockSources()),Binder.HIDE_EVENTS);
		mMainForm.addField("tbitlos", new T1BitsLosBinder("T1 BITS","tbitlos"),Binder.HIDE_EVENTS);
		mMainForm.addField("ebitlos", new E1BitsLosBinder("E1 BITS","ebitlos"),Binder.HIDE_EVENTS);
		mMainForm.addField("pll_los",
				bb = new HighlightBooleanBinder("PLL", "pll_los",false,
				UIManager.getColor("pippin.colors.alertRed")),Binder.HIDE_EVENTS);
		bb.setInverted(true);
		bb.setOnText("OK");
		bb.setIcon(BlackDot.getImageIcon());
		bb.setOffText("LOS");
		mMainForm.setEnabled(false);

		JButton b = ButtonMaker.createPButton("Change");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				BinderDialog d = new BinderDialog(GClient.getClient(),
						"Change Clock Source", new SourceChangePanel());
				d.setVisible(true);
				Envelope env = d.getEnvelope();
				if (env != null) {
					PEnvelope pe = new PEnvelope(env);
					try {
						pe.applyToPComponent(getCard());
						// getCard().put("source", ((Refclk.ClockSource)
						// 		env.getElement("source")).getValue());
					}
					catch (Exception e) {
						GClient.getClient().showAlertDialog(e);
						e.printStackTrace();
					}
				}
			}
		});
		mMainForm.addButton(b);

		PEnvelope e = new PEnvelope(new MainTypeSet());
		e.mergePComponent(getCard());

		mMainForm.setEnvelope(e);

		tp.add("Status", mMainForm);

		return tp;
	}


	public void doStateChange(PStateChangeEvent event) {
		System.out.println("RefClockCardPanel.doStateChange: " + event);
		PEnvelope env = new PEnvelope(mMainForm.getEnvelope());
		env.mergePAttributeList(event.getAttributes());
		mMainForm.setEnvelope(env);
	}


	public static class MainTypeSet extends TypeSet {
		public MainTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("source", Refclk.ClockSource.class),
				new TypeSetElement("tbitlos", Boolean.class),
				new TypeSetElement("ebitlos", Boolean.class),
				new TypeSetElement("pll_los", Boolean.class),
			});

			getElement("source").setAccessConverter(new ElementConverter() {
				public Object convert(Object o) {
					return ((Refclk.ClockSource) o).getValue();
				}
			});
			getElement("source").setMutateConverter(new ElementConverter() {
				public Object convert(Object o) {
					return Refclk.ClockSource.getClockSourceFor((Integer) o);
				}
			});
		}
	}


	protected class SourceChangePanel extends BasicForm {
		protected SourceChangePanel() {
			super(new MainTypeSet());

			PEnvelope env = new PEnvelope(new MainTypeSet());
			try {
				env.mergePComponent(getCard());
				// env.putElement("source", ((Refclk) getCard()).getSource());
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			addField("source",
					new ComboBinder("Source", Refclk.ClockSource.class,
					Refclk.ClockSource.getClockSources()),Binder.HIDE_EVENTS);

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


	protected class T1BitsLosBinder extends HighlightBooleanBinder {
		protected T1BitsLosBinder(String title,String binderKey) {
			super(title, binderKey,false, UIManager.getColor("pippin.colors.alertRed"));
			setInverted(true);
			setOnText("OK");
			setOffText("LOS");
			setIcon(BlackDot.getImageIcon());
		}
	}


	protected class E1BitsLosBinder extends HighlightBooleanBinder {
		protected E1BitsLosBinder(String title, String binderKey) {
			super(title, binderKey,false, UIManager.getColor("pippin.colors.alertRed"));
			setInverted(true);
			setOnText("OK");
			setOffText("LOS");
			setIcon(BlackDot.getImageIcon());
		}
	}
}
