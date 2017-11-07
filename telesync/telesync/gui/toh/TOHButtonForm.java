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

/* jb-01092002 - add flag functinality to form */

public class TOHButtonForm extends ButtonForm implements ClearFlagEventListener  {
	Mode mMode = Mode.US_SONET;
	JComponent mColumnHeader = null;
	JLabel mColumnLabel = null;

	public TOHButtonForm(String title) {
			this(title, false);
	}
	
	/* jb-01092004 */	
	public TOHButtonForm(String title, boolean useFlags) {
		super(title, new TOHTypeSet());
				// jb-01073001 change Da,Db,Dc to D10,D11,D12
				addField("A1", new TOHBinder("A1"), useFlags);
				addField("A2", new TOHBinder("A2"), useFlags);
				addField("J0", new TOHBinder("J0"), useFlags);
				addField("B1", new TOHBinder("B1"), useFlags);
				addField("E1", new TOHBinder("E1"), useFlags);
				addField("F1", new TOHBinder("F1"), useFlags);
				addField("D1", new TOHBinder("D1"), useFlags);
				addField("D2", new TOHBinder("D2"), useFlags);
				addField("D3", new TOHBinder("D3"), useFlags);
				addField("H1", new TOHBinder("H1"), useFlags);
				addField("H2", new TOHBinder("H2"), useFlags);
				addField("H3", new TOHBinder("H3"), useFlags);
				addField("B2", new TOHBinder("B2"), useFlags);
				addField("K1", new TOHBinder("K1"), useFlags);
				addField("K2", new TOHBinder("K2"), useFlags);
				addField("D4", new TOHBinder("D4"), useFlags);
				addField("D5", new TOHBinder("D5"), useFlags);
				addField("D6", new TOHBinder("D6"), useFlags);
				addField("D7", new TOHBinder("D7"), useFlags);
				addField("D8", new TOHBinder("D8"), useFlags);
				addField("D9", new TOHBinder("D9"), useFlags);
				addField("Da", new TOHBinder("D10"), useFlags);
				addField("Db", new TOHBinder("D11"), useFlags);
				addField("Dc", new TOHBinder("D12"), useFlags);
				addField("S1", new TOHBinder("S1"), useFlags);
				addField("Mx", new TOHBinder("Mx"), useFlags);
				addField("E2", new TOHBinder("E2"), useFlags);
		
		setColumnHeader(getColumnHeader());
	}
	
	public void start() {
		// Start any needed listeners.
	}


	public void dispose() {
		super.dispose();

		// Disconnect any listeners from start().
	}


	public JComponent getColumnHeader() {
		if (mColumnHeader == null) {
			mColumnHeader = createColumnHeader();
		}

		return mColumnHeader;
	}


	private JComponent createColumnHeader() {
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

		/*if (mMode.equals(Tx48.Mode.US_SONET)) {
			title = "STS-3:";
		} else {
			title = "STM-1:";
		}*/
		title = "TOH:";
		
		tp.setLayout(new BinderHeaderLayout(tp,
				mColumnLabel = new JLabel(title)) {
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
		mColumnLabel.setOpaque(false);

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(tp, BorderLayout.NORTH);
		p.add(np, BorderLayout.SOUTH);
		np.setOpaque(false);
		tp.setOpaque(false);
		p.setOpaque(false);

		return p;
	}


	public void setMode(Mode mode) {
		mMode = mode;
		String title;

		/*if (mMode.equals(Mode.US_SONET)) {
			title = "STS-3:";
		} else {
			title = "STM-1:";
		}
		*/
		title = "TOH:";
		

		if (mColumnLabel != null) {
			mColumnLabel.setText(title);
		}
	}
	
	/* ClearFlagListener Methods */
	public void clearFlags() {
			super.clearBinderFlags();
	}
}
