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
import pippin.util.Moment;

import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;



public class TributaryTypeSet extends TypeSet {
	public TributaryTypeSet() {
		super(new TypeSetElement[] {
			new TypeSetElement("rateEnabled", Boolean.class),
			new TypeSetElement("payloadType", RxStsNc.PayloadType.class),
			new TypeSetElement("pattern", Pattern.class),
			new TypeSetElement("patternInvert", Boolean.class),
			new TypeSetElement("pointerError", Boolean.class),
			//new TypeSetElement("b3Errors", Long.class, false,new Long(0)),
			//new TypeSetElement("patternErrs", Long.class,false,new Long(0)),
			new TypeSetElement("b3Errors", Long.class),
			new TypeSetElement("patternErrs", Long.class),
			new TypeSetElement("patternLoss", Boolean.class),
			new TypeSetElement("ptrace", String.class),
			new TypeSetElement("C2", Integer.class),
			new TypeSetElement("G1", Integer.class),
			new TypeSetElement("F2", Integer.class),
			new TypeSetElement("H4", Integer.class),
			new TypeSetElement("Z3", Integer.class),
			new TypeSetElement("Z4", Integer.class),
			new TypeSetElement("Z5", Integer.class),
			new TypeSetElement("rateEnabledUser", Boolean.class),
			
			new TypeSetElement("timeIntoTest", Moment.class),
			
			new TypeSetElement("b3ErrdSecs", Integer.class),
			new TypeSetElement("pattnErrdSecs", Integer.class),
			
			
		});

		getElement("timeIntoTest").setMutateConverter(new ElementConverter() {
			public Object convert(Object o) {
				return new Moment(((Integer) o).intValue());
			}
		});
		getElement("timeIntoTest").setAccessConverter(new ElementConverter() {
			public Object convert(Object o) {
				return new Integer((int) ((Moment) o).getSeconds());
			}
		});
		
		getElement("payloadType").setAccessConverter(new ElementConverter() {
			public Object convert(Object o) {
				return ((RxStsNc.PayloadType) o).getValue();
			}
		});
		getElement("payloadType").setMutateConverter(new ElementConverter() {
			public Object convert(Object o) {
				return RxStsNc.PayloadType.getInstanceFor((Integer) o);
			}
		});

		getElement("pattern").setAccessConverter(new ElementConverter() {
			public Object convert(Object o) {
				return ((Pattern) o).getValue();
			}
		});
		getElement("pattern").setMutateConverter(new ElementConverter() {
			public Object convert(Object o) {
				return Pattern.getInstanceFor((Integer) o);
			}
		});
	}
}
