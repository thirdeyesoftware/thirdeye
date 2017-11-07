////
//
// Telesync 5320 Project
//
//

package telesync.pui.tsi5320;



import pippin.binder.*;
import pippin.util.*;
	


/**
*/
public class AlarmStatusTypeSet extends TypeSet {

	public AlarmStatusTypeSet() {
		super(new TypeSetElement[] {
				new TypeSetElement("BX-ERR", IndicatorStatus.class, true),
				new TypeSetElement("FX-ERR", IndicatorStatus.class, true),
				new TypeSetElement("PX-ERR", IndicatorStatus.class, true),
				
				new TypeSetElement("LOL", IndicatorStatus.class, true),
				new TypeSetElement("LOF", IndicatorStatus.class, true),
				new TypeSetElement("LOS", IndicatorStatus.class, true),
				new TypeSetElement("LOP", IndicatorStatus.class, true),
				new TypeSetElement("AIS-L", IndicatorStatus.class, true),
				new TypeSetElement("RDI-L", IndicatorStatus.class, true),
				new TypeSetElement("AIS-P", IndicatorStatus.class, true),
				new TypeSetElement("RDI-P", IndicatorStatus.class, true),
				new TypeSetElement("PHASE-LOCK", IndicatorStatus.class, true),
				new TypeSetElement("UNKNOWN", IndicatorStatus.class, true),
				new TypeSetElement("PL", IndicatorStatus.class, true),
				
			});
	}
}
