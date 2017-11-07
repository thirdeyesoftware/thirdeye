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
import telesync.gui.GClient;
import java.util.Enumeration;



public class OC48ChangeListener implements PStateChangeListener {
	BinderPanel mBinderPanel;

	public OC48ChangeListener(BinderPanel bp) {
		mBinderPanel = bp;
	}

	public void stateChanged(PStateChangeEvent event) {
		try {
				//System.out.println(event.getAttributes());
				PEnvelope env = new PEnvelope(mBinderPanel.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				
				Boolean b = (Boolean)env.getElement("prbsInvert", false);
				if (b != null) {
					Oc48c.Pattern pattern = (Oc48c.Pattern)env.getElement("pattern");
					if (b.equals(Boolean.TRUE)) {
						env.putElement("pattern", Oc48c.Pattern.getInvertedFor(pattern));
					}
					else {
						env.putElement("pattern", Oc48c.Pattern.getBasePatternFor(pattern));
					}
				}	

				mBinderPanel.setEnvelope(env);

				if (env.hasElement("rateEnabled")) {
					b = (Boolean) env.getElement("rateEnabled");
					((TributaryEnabledListener) mBinderPanel).setTributaryEnabled(
							b.booleanValue());
				}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
