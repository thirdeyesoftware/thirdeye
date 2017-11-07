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



public class OC192TributaryChangePanel extends BasicForm {
	TxStsNc192 mTributary;

	protected OC192TributaryChangePanel(TxStsNc192 tributary) {
		super(new TributaryTypeSet());

		mTributary = tributary;

		PEnvelope env = new PEnvelope(new TributaryTypeSet());
		try {
			env.mergePComponent(getTributary());
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		addField("payloadType",
				new ComboBinder("Payload Type", PayloadType.class,
				PayloadType.getInstances()),Binder.HIDE_EVENTS);
		addField("pattern",
				new ComboBinder("Pattern", Pattern.class,
				Pattern.getInstances()),Binder.HIDE_EVENTS);
		//addField("patternInvert", new BooleanBinder("Pattern Invert"));


		setEnvelope(env);
	}


	public TxStsNc192 getTributary() {
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
