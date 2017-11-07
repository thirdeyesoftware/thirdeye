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


public class TributaryChangeListener implements PStateChangeListener {
	BinderPanel mBinderPanel;
	PComponent mSource;
	static private int cInstanceID = 0;
	private int mInstanceID = getNextInstanceID();
	
	public TributaryChangeListener(BinderPanel bp) {
		mBinderPanel = bp;
	}
	public TributaryChangeListener( PComponent source, BinderPanel bp) {
		this(bp);
		mSource = source;
	}
	
	public void stateChanged(PStateChangeEvent event) {
		try {
			PAttributeSet attributes = event.getAttributes();
			PEnvelope env = new PEnvelope(mBinderPanel.getEnvelope());
			env.mergePAttributeList(attributes);

			RxStsNc.PayloadType payloadType = (RxStsNc.PayloadType)env.getElement("payloadType");
			if (payloadType != null) {
				if (payloadType.equals(RxStsNc.PayloadType.UNKNOWN_PATTERN)) {
					mBinderPanel.setBinderVisible("pattern",false);
				}
				else 
					mBinderPanel.setBinderVisible("pattern", true);

			}
			
			Boolean	b = (Boolean)env.getElement("patternInvert", false);
			if (b != null) {
				
				Pattern pattern = (Pattern)env.getElement("pattern", false);
				if (pattern != null) {
					if (pattern.isInverted() && b.equals(Boolean.TRUE)) {
						//System.out.println("patern is inverted.  inverted attribute is true.");
						//do nothing.
					}
					else if (b.equals(Boolean.TRUE)) {
						env.putElement("pattern", Pattern.getInvertedFor(pattern));
					}
					else {
						env.putElement("pattern", Pattern.getBasePatternFor(pattern));
					}
				}
			}	
			/*
			if (attributes.getAttribute("timeIntoTest",false) != null) {
				int time = ((Integer)attributes.getValue("timeIntoTest")).intValue();

				if (attributes.getAttribute("b3Errors",false) != null) {
					((ErrorBinder)mBinderPanel.getBinder("b3Errors")).setSeconds(time);
				}
				if (attributes.getAttribute("patternErrs",false) != null) {
					((ErrorBinder)mBinderPanel.getBinder("patternErrs")).setSeconds(time);
				}
			}
			*/
			
			mBinderPanel.setEnvelope(env);
			
			((TributaryPanel)mBinderPanel).setRefresher(
					((TributaryPanel)mBinderPanel).isEnabled());
									
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
	static synchronized private int getNextInstanceID() {
		System.out.println("CardPanel.getNextInstanceID: " + cInstanceID);
		return cInstanceID++;
	}


	protected int getInstanceID() {
		return mInstanceID;
	}
	
}
