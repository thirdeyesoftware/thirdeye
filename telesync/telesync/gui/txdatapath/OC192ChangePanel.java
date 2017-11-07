////
//
// Telesync 5320 Project
//
//

package telesync.gui.txdatapath;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;



public class OC192ChangePanel extends BasicForm {
	Oc192c mOC192c;

	protected OC192ChangePanel(Oc192c oc192c) {
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
				new ComboBinder("Payload Type", Oc192c.PayloadType.class,
				Oc192c.PayloadType.getInstances()), Binder.HIDE_EVENTS);
		addField("pattern",
				new ComboBinder("Pattern", Oc192c.Pattern.class,
				Oc192c.Pattern.getInstances()), Binder.HIDE_EVENTS);
		//addField("prbsInvert", new BooleanBinder("PRBS Invert"));
		addField("errorEnable",
				new BooleanBinder("Auto Error Insert"),Binder.HIDE_EVENTS);
		addField("errorRate",
				new ComboBinder("Error Rate", Oc192c.ErrorRate.class,
				Oc192c.ErrorRate.getInstances()), Binder.HIDE_EVENTS);
		addField("errorType",
				new ComboBinder("Error Type", Oc192c.ErrorType.class,
				Oc192c.ErrorType.getInstances()), Binder.HIDE_EVENTS);


		setEnvelope(env);
	}


	public Oc192c getOC192c() {
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
