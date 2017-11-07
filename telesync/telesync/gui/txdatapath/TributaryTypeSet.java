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



public class TributaryTypeSet extends TypeSet {
	public TributaryTypeSet() {
		super(new TypeSetElement[] {
			new TypeSetElement("rateEnabled", Boolean.class),
			new TypeSetElement("payloadType", PayloadType.class),
			new TypeSetElement("pattern", Pattern.class),
			new TypeSetElement("patternInvert", Boolean.class),
		});

		getElement("payloadType").setAccessConverter(new ElementConverter() {
			public Object convert(Object o) {
				return ((PayloadType) o).getValue();
			}
		});
		getElement("payloadType").setMutateConverter(new ElementConverter() {
			public Object convert(Object o) {
				return PayloadType.getInstanceFor((Integer) o);
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
