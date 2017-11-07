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



public class OC192ChangePanel extends BasicForm {
	Oc192c_rx mOC192c;

	protected OC192ChangePanel(Oc192c_rx oc192c) {
		super(new OC192TypeSet());

		mOC192c = oc192c;

		PEnvelope env = new PEnvelope(new OC192TypeSet());
		try {
			env.mergePComponent(getOC192c());
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		addField("payloadType",
				new ComboBinder("Payload Type", Oc192c_rx.PayloadType.class,
				Oc192c_rx.PayloadType.getInstances()));
		addField("pattern",
				new ComboBinder("Pattern", Oc192c_rx.Pattern.class,
				Oc192c_rx.Pattern.getInstances()));
		//addField("patternInvert", new BooleanBinder("Pattern Invert"));

		setEnvelope(env);
	}


	public Oc192c_rx getOC192c() {
		return mOC192c;
	}


	public void start() {
		// Start any needed listeners.
	}


	public void dispose() {
		super.dispose();

		// Disconnect any listeners from start().
	}
}
