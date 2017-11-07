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

public class OC192TributaryChangePanel extends BasicForm {
	RxStsNc192 mTributary;

	protected OC192TributaryChangePanel(RxStsNc192 tributary) {
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
		addField("payloadType",
				new ComboBinder("Expected Payload Type", RxStsNc.PayloadType.class,
				RxStsNc.PayloadType.getInstances()),Binder.HIDE_EVENTS);
		addField("pattern",
				new ComboBinder("Pattern", Pattern.class,
				Pattern.getInstances()),Binder.HIDE_EVENTS);
		//addField("patternInvert", new BooleanBinder("Pattern Invert"));


		setEnvelope(env);
	}


	public RxStsNc192 getTributary() {
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
