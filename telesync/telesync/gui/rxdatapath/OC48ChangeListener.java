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
import telesync.gui.GClient;
import java.util.Enumeration;


public class OC48ChangeListener implements PStateChangeListener {
	BinderPanel mBinderPanel;
	PComponent mSource;
	
	public OC48ChangeListener(BinderPanel bp) {
		mBinderPanel = bp;
	}
	public OC48ChangeListener( PComponent source, BinderPanel bp) {
		this(bp);
		mSource = source;
	}
	
	public void stateChanged(PStateChangeEvent event) {
		try {
			PAttributeSet attributes = event.getAttributes();
			PEnvelope env = new PEnvelope(mBinderPanel.getEnvelope());
			env.mergePAttributeList(attributes);
			
			Oc48c_rx.PayloadType payloadType = (Oc48c_rx.PayloadType)env.getElement("payloadType");
			if (payloadType != null) {
				if (payloadType.equals(Oc48c_rx.PayloadType.UNKNOWN_PATTERN)) {
					mBinderPanel.setBinderVisible("pattern",false);
				}
				else 
					mBinderPanel.setBinderVisible("pattern", true);
				
			}
			
			
			
			Boolean	b = (Boolean)env.getElement("patternInvert", false);
			if (b != null) {
				
				Oc48c_rx.Pattern pattern = (Oc48c_rx.Pattern)env.getElement("pattern", false);
				if (pattern != null) {
					if (b.equals(Boolean.TRUE)) {
						Oc48c_rx.Pattern n = Oc48c_rx.Pattern.getInvertedFor(pattern);
						env.putElement("pattern", n);
					}
					else {
						Oc48c_rx.Pattern n = Oc48c_rx.Pattern.getBasePatternFor(pattern);
						env.putElement("pattern", n);
					}
				}
				
			}	
			/*
			if (attributes.getAttribute("timeIntoTest",false) != null) {
				int time = ((Integer)attributes.getValue("timeIntoTest")).intValue();
				
				if (attributes.getAttribute("b3Errors",false) != null) {
					((ErrorBinder)mBinderPanel.getBinder("b3Errors")).`(time);
				}
				if (attributes.getAttribute("patternErrs",false) != null) {
					((ErrorBinder)mBinderPanel.getBinder("patternErrs")).setSeconds(time);
				}
			}
			*/
			mBinderPanel.setEnvelope(env);
			((OC48Panel)mBinderPanel).setRefresher(
						((OC48Panel)mBinderPanel).isEnabled());
			
			if (attributes.getAttribute("rateEnabledUser",false) != null) {
							
				if (env.hasElement("rateEnabledUser")) {
					
					b = (Boolean) env.getElement("rateEnabledUser");
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
