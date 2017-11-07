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
import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;



public class OC48ChangePanel extends BasicForm {
	Oc48c_rx mOC48c;

	protected OC48ChangePanel(Oc48c_rx oc48c) {
		super(new OC48TypeSet());

		mOC48c = oc48c;

		PEnvelope env = new PEnvelope(new OC48TypeSet());
		try {
			env.mergePComponent(getOC48c());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		ComboBinder cb;
		addField("payloadType",
				cb = new ComboBinder("Payload Type", Oc48c_rx.PayloadType.class,
				Oc48c_rx.PayloadType.getInstances()),Binder.HIDE_EVENTS);
		JComboBox combo = (JComboBox)cb.getFormComponent();
		combo.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					Oc48c_rx.PayloadType type = (Oc48c_rx.PayloadType)
							event.getItem();
					((DefaultBinder)getBinder("pattern")).setEnabled(
							(type.equals(Oc48c_rx.PayloadType.FIXED_PATTERN)));
				}
			}
		});
		addField("pattern",
				new ComboBinder("Pattern", Oc48c_rx.Pattern.class,
				Oc48c_rx.Pattern.getInstances()), Binder.HIDE_EVENTS);	

		Oc48c_rx.PayloadType payloadType = (Oc48c_rx.PayloadType)env.getElement("payloadType");
		if (payloadType != null) {
			((DefaultBinder)getBinder("pattern")).setEnabled(
					(payloadType.equals(Oc48c_rx.PayloadType.FIXED_PATTERN)));
		}
		
		setEnvelope(env);
	}


	public Oc48c_rx getOC48c() {
		return mOC48c;
	}


	public void start() {
		// Start any needed listeners.
	}


	public void dispose() {
		super.dispose();

		// Disconnect any listeners from start().
	}
}
