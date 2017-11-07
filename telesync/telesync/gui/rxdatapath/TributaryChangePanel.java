////
//
// Telesync 5320 Project
//
//

package telesync.gui.rxdatapath;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;



public class TributaryChangePanel extends BasicForm {
	RxStsNc mTributary;

	protected TributaryChangePanel(RxStsNc tributary) {
		super(new TributaryTypeSet());

		mTributary = tributary;

		PEnvelope env = new PEnvelope(new TributaryTypeSet());
		try {
			env.mergePComponent(getTributary());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		/* jb-01062603 changed label - added "expected" */
		ComboBinder cb;
		addField("payloadType",
				cb = new ComboBinder("Expected Payload Type", RxStsNc.PayloadType.class,
				RxStsNc.PayloadType.getInstances()),Binder.HIDE_EVENTS);
		JComboBox combo = (JComboBox)cb.getFormComponent();
		combo.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					RxStsNc.PayloadType type = (RxStsNc.PayloadType)
							event.getItem();
					((DefaultBinder)getBinder("pattern")).setEnabled(
							(type.equals(RxStsNc.PayloadType.FIXED_PATTERN)));
				}
			}
		});
		addField("pattern",
						new ComboBinder("Pattern", Pattern.class,
						Pattern.getInstances()), Binder.HIDE_EVENTS);
						
		RxStsNc.PayloadType payloadType = (RxStsNc.PayloadType)env.getElement("payloadType");
		if (payloadType != null) {
			((DefaultBinder)getBinder("pattern")).setEnabled(
							(payloadType.equals(RxStsNc.PayloadType.FIXED_PATTERN)));
		}
		
		setEnvelope(env);
	}


	public RxStsNc getTributary() {
		return mTributary;
	}


	public void start() {
		// Start any needed listeners.
	}


	public void dispose() {
		super.dispose();

		// Disconnect any listeners from start().
	}
}
