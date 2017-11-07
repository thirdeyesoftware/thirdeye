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
import java.util.Vector;

import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;



public class OC48ChangePanel extends BasicForm {
	Oc48c mOC48c;
	ComboBoxModel mRateModel;
	
	protected OC48ChangePanel(Oc48c oc48c) {
		super(new OC48TypeSet());

		mOC48c = oc48c;

		PEnvelope env = new PEnvelope(new OC48TypeSet());
		try {
			env.mergePComponent(getOC48c());
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		addField("payloadType",
				new ComboBinder("Payload Type", Oc48c.PayloadType.class,
				Oc48c.PayloadType.getInstances()), Binder.HIDE_EVENTS);
		addField("pattern",
				new ComboBinder("Pattern", Oc48c.Pattern.class,
				Oc48c.Pattern.getInstances()),Binder.HIDE_EVENTS);
		//addField("prbsInvert", new BooleanBinder("PRBS Invert"),Binder.HIDE_EVENTS);
		addField("errorEnable",
				new BooleanBinder("Auto Error Insert"),Binder.HIDE_EVENTS);
		
		ComboBinder cb = null;
		ComboBinder errorRateBinder = null;
		
		JComboBox combo;
		addField("errorRate",
				errorRateBinder = new ComboBinder("Error Rate", Oc48c.ErrorRate.class,
				Oc48c.ErrorRate.getInstances()),Binder.HIDE_EVENTS);
		combo =  (JComboBox)errorRateBinder.getFormComponent();
		
		
		mRateModel = combo.getModel();
		
		addField("errorType",
				cb = new ComboBinder("Error Type", Oc48c.ErrorType.class,
				Oc48c.ErrorType.getInstances()),Binder.HIDE_EVENTS);
		
		combo = (JComboBox)cb.getFormComponent();
		
		/**
		 * This functionality has been deprecated 10/28/2002 JSB
		 
		combo.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
							
					Oc48c.ErrorType item = 
						(Oc48c.ErrorType)event.getItem();
					ComboBinder cb = (ComboBinder)getBinder("errorRate");
					
					if (item.equals(Oc48c.ErrorType.B1_ERROR) ||
							item.equals(Oc48c.ErrorType.B2_ERROR)) {
						Vector v = Oc48c.ErrorRate.getInstances();
						v.removeElement(Oc48c.ErrorRate.ONE_E_3);
						v.removeElement(Oc48c.ErrorRate.ONE_E_4);
						cb.setChoices(v);
						
					}
					else {
						cb.setChoices(Oc48c.ErrorRate.getInstances());
						invalidateBinders();
					}
				}
			}
		});
		Oc48c.ErrorType item = 
				(Oc48c.ErrorType)combo.getSelectedItem();
		if (item != null) {
			if (item.equals(Oc48c.ErrorType.B1_ERROR) ||
					item.equals(Oc48c.ErrorType.B2_ERROR)) {
				Vector v = Oc48c.ErrorRate.getInstances();
				v.removeElement(Oc48c.ErrorRate.ONE_E_3);
				v.removeElement(Oc48c.ErrorRate.ONE_E_4);
				errorRateBinder.setChoices(v);

			}
			else {
				errorRateBinder.setChoices(Oc48c.ErrorRate.getInstances());
			}
		}	
		**/
		
		setEnvelope(env);
	}


	public Oc48c getOC48c() {
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
