////
//
// Telesync 5320 Project
//
//

package telesync.gui.toh;

import java.awt.*;
import javax.swing.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.ClearFlagEventListener;

public class OC192TOHPanel extends BasicForm implements ClearFlagEventListener {
	Mode mMode;

	public OC192TOHPanel(TOH toh, Mode mode) {
		super(new TOHTypeSet());

		mMode = mode;

		PEnvelope env = new PEnvelope(new TOHTypeSet());
		try {
			env.mergePComponent(toh);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		addField("A1", new OC192TOHBinder("A1"));//,LineSpeed.OC192));
		addField("A2", new OC192TOHBinder("A2"));//,LineSpeed.OC192));
		addField("J0", new OC192TOHBinder("J0"));//,LineSpeed.OC192));
		addField("B1", new OC192TOHBinder("B1"));//,LineSpeed.OC192));
		addField("E1", new OC192TOHBinder("E1"));//,LineSpeed.OC192));
		addField("F1", new OC192TOHBinder("F1"));//,LineSpeed.OC192));
		addField("D1", new OC192TOHBinder("D1"));//,LineSpeed.OC192));
		addField("D2", new OC192TOHBinder("D2"));//,LineSpeed.OC192));
		addField("D3", new OC192TOHBinder("D3"));//,LineSpeed.OC192));
		addField("H1", new OC192TOHBinder("H1"));//,LineSpeed.OC192));
		addField("H2", new OC192TOHBinder("H2"));//,LineSpeed.OC192));
		addField("H3", new OC192TOHBinder("H3"));//,LineSpeed.OC192));
		addField("B2", new OC192TOHBinder("B2"));//,LineSpeed.OC192));
		addField("K1", new OC192TOHBinder("K1"));//,LineSpeed.OC192));
		addField("K2", new OC192TOHBinder("K2"));//,LineSpeed.OC192));
		addField("D4", new OC192TOHBinder("D4"));//,LineSpeed.OC192));
		addField("D5", new OC192TOHBinder("D5"));//,LineSpeed.OC192));
		addField("D6", new OC192TOHBinder("D6"));//,LineSpeed.OC192));
		addField("D7", new OC192TOHBinder("D7"));//,LineSpeed.OC192));
		addField("D8", new OC192TOHBinder("D8"));//,LineSpeed.OC192));
		addField("D9", new OC192TOHBinder("D9"));//,LineSpeed.OC192));
		addField("Da", new OC192TOHBinder("Da"));//,LineSpeed.OC192));
		addField("Db", new OC192TOHBinder("Db"));//,LineSpeed.OC192));
		addField("Dc", new OC192TOHBinder("Dc"));//,LineSpeed.OC192));
		addField("S1", new OC192TOHBinder("S1"));//,LineSpeed.OC192));
		addField("Mx", new OC192TOHBinder("Mx"));//,LineSpeed.OC192));
		addField("E2", new OC192TOHBinder("E2"));//,LineSpeed.OC192));

		setEnvelope(env);
	}


	public void start() {
		// Start any needed listeners.
	}


	public void dispose() {
		super.dispose();

		// Disconnect any listeners from start().
	}


	public JComponent getColumnHeader() {
		JPanel hp = new TOHHeader(LineSpeed.OC192);
		// hp.setBackground(Color.green);

		JPanel np = new JPanel() {
			public Dimension getMaximumSize() {
				return new Dimension(super.getMaximumSize().width,
						super.getPreferredSize().height);
			}
		};
		np.setLayout(new BinderHeaderLayout(np, hp) {
			protected int getFormWidth() {
				return getMaxFormWidth();
			}
			protected int getTitleWidth() {
				return getMaxTitleWidth();
			}
			protected int getGapWidth() {
				return getLabelGap();
			}
		});
		// p.setBackground(Color.yellow);


		JPanel tp = new JPanel() {
			public Dimension getMaximumSize() {
				return new Dimension(super.getMaximumSize().width,
						super.getPreferredSize().height);
			}
		};

		String title;

		/*if (mMode.equals(Mode.US_SONET)) {
			title = "STS-3:";
		} else {
			title = "STM-1:";
		}
		*/
		title = "TOH:";
		

		tp.setLayout(new BinderHeaderLayout(tp, new JLabel(title)) {
			protected int getFormWidth() {
				return getMaxFormWidth();
			}
			protected int getTitleWidth() {
				return getMaxTitleWidth();
			}
			protected int getGapWidth() {
				return getLabelGap();
			}
		});

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(tp, BorderLayout.NORTH);
		p.add(np, BorderLayout.SOUTH);
		p.setOpaque(false);

		return p;
	}
	public void clearFlags() {
		super.clearBinderFlags();
	}
}
