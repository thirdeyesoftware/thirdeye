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

public class TributaryChangeListener implements PStateChangeListener {
	BinderPanel mBinderPanel;

	public TributaryChangeListener(BinderPanel bp) {
		mBinderPanel = bp;
	}

	public void stateChanged(PStateChangeEvent event) {
		try {
			synchronized (mBinderPanel) {
				
				
				PEnvelope env = new PEnvelope(mBinderPanel.getEnvelope());
				env.mergePAttributeList(event.getAttributes());
				
				Boolean b = (Boolean)env.getElement("patternInvert", false);
				if (b != null) {
					Pattern pattern = (Pattern)env.getElement("pattern");
					if (b.equals(Boolean.TRUE)) {

						env.putElement("pattern", Pattern.getInvertedFor(pattern));
					}
					else {
						env.putElement("pattern", Pattern.getBasePatternFor(pattern));
					}
				}	



				mBinderPanel.setEnvelope(env);

				if (env.hasElement("rateEnabled")) {
					b = (Boolean) env.getElement("rateEnabled");
					((TributaryEnabledListener) mBinderPanel).setTributaryEnabled(
							b.booleanValue());
				}
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
