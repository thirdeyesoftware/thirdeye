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



public class OC48TypeSet extends TypeSet {
	public OC48TypeSet() {
		super(new TypeSetElement[] {
			new TypeSetElement("rateEnabled", Boolean.class),
			new TypeSetElement("payloadType", Oc48c.PayloadType.class),
			new TypeSetElement("pattern", Oc48c.Pattern.class),
			new TypeSetElement("prbsInvert", Boolean.class),
			new TypeSetElement("errorEnable", Boolean.class),
			new TypeSetElement("errorRate", Oc48c.ErrorRate.class),
			new TypeSetElement("errorType", Oc48c.ErrorType.class),
			new TypeSetElement("errorSingle", Integer.class),
			new TypeSetElement("errorButton", Integer.class),
		});

		getElement("payloadType").setAccessConverter(new ElementConverter() {
			public Object convert(Object o) {
				return ((Oc48c.PayloadType) o).getValue();
			}
		});
		getElement("payloadType").setMutateConverter(new ElementConverter() {
			public Object convert(Object o) {
				return Oc48c.PayloadType.getInstanceFor((Integer) o);
			}
		});

		getElement("pattern").setAccessConverter(new ElementConverter() {
			public Object convert(Object o) {
				return ((Oc48c.Pattern) o).getValue();
			}
		});
		getElement("pattern").setMutateConverter(new ElementConverter() {
			public Object convert(Object o) {
				return Oc48c.Pattern.getInstanceFor((Integer) o);
			}
		});

		getElement("errorRate").setAccessConverter(new ElementConverter() {
			public Object convert(Object o) {
				return ((Oc48c.ErrorRate) o).getValue();
			}
		});
		getElement("errorRate").setMutateConverter(new ElementConverter() {
			public Object convert(Object o) {
				return Oc48c.ErrorRate.getInstanceFor((Integer) o);
			}
		});

		getElement("errorType").setAccessConverter(new ElementConverter() {
			public Object convert(Object o) {
				return ((Oc48c.ErrorType) o).getValue();
			}
		});
		getElement("errorType").setMutateConverter(new ElementConverter() {
			public Object convert(Object o) {
				return Oc48c.ErrorType.getInstanceFor((Integer) o);
			}
		});
	}
}
