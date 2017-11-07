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




public class TOHPanel extends BasicForm {
	Mode mMode;

	public TOHPanel(TOH toh, Mode mode) {
		super(new TOHTypeSet());

		mMode = mode;

		PEnvelope env = new PEnvelope(new TOHTypeSet());
		try {
			env.mergePComponent(toh);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// jb-01073001 - change Da,Db,Dc labels to D10,D11,D12 
		
		addField("A1", new TOHBinder("A1"),Binder.HIDE_EVENTS);
		addField("A2", new TOHBinder("A2"),Binder.HIDE_EVENTS);
		addField("J0", new TOHBinder("J0"),Binder.HIDE_EVENTS);
		addField("B1", new TOHBinder("B1"),Binder.HIDE_EVENTS);
		addField("E1", new TOHBinder("E1"),Binder.HIDE_EVENTS);
		addField("F1", new TOHBinder("F1"),Binder.HIDE_EVENTS);
		addField("D1", new TOHBinder("D1"),Binder.HIDE_EVENTS);
		addField("D2", new TOHBinder("D2"),Binder.HIDE_EVENTS);
		addField("D3", new TOHBinder("D3"),Binder.HIDE_EVENTS);
		addField("H1", new TOHBinder("H1"),Binder.HIDE_EVENTS);
		addField("H2", new TOHBinder("H2"),Binder.HIDE_EVENTS);
		addField("H3", new TOHBinder("H3"),Binder.HIDE_EVENTS);
		addField("B2", new TOHBinder("B2"),Binder.HIDE_EVENTS);
		addField("K1", new TOHBinder("K1"),Binder.HIDE_EVENTS);
		addField("K2", new TOHBinder("K2"),Binder.HIDE_EVENTS);
		addField("D4", new TOHBinder("D4"),Binder.HIDE_EVENTS);
		addField("D5", new TOHBinder("D5"),Binder.HIDE_EVENTS);
		addField("D6", new TOHBinder("D6"),Binder.HIDE_EVENTS);
		addField("D7", new TOHBinder("D7"),Binder.HIDE_EVENTS);
		addField("D8", new TOHBinder("D8"),Binder.HIDE_EVENTS);
		addField("D9", new TOHBinder("D9"),Binder.HIDE_EVENTS);
		addField("Da", new TOHBinder("D10"),Binder.HIDE_EVENTS);
		addField("Db", new TOHBinder("D11"),Binder.HIDE_EVENTS);
		addField("Dc", new TOHBinder("D12"),Binder.HIDE_EVENTS);
		addField("S1", new TOHBinder("S1"),Binder.HIDE_EVENTS);
		addField("Mx", new TOHBinder("Mx"),Binder.HIDE_EVENTS);
		addField("E2", new TOHBinder("E2"),Binder.HIDE_EVENTS);

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
		JPanel hp = new TOHHeader();
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
		}*/
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
	
	
}
