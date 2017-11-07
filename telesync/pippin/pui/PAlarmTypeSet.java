////
//
// Telesync 5320 Project
//
//

package pippin.pui;



/**
*/
public class PAlarmTypeSet extends PTypeSet {
	private static String[] names = {"id","state","history","desc","opcode"};
	private static byte[] types = {PAttribute.TYPE_SHORT, 
													PAttribute.TYPE_BOOLEAN,
													PAttribute.TYPE_BOOLEAN,
													PAttribute.TYPE_STRING,
													PAttribute.TYPE_SHORT};
	
	public PAlarmTypeSet() {
		super(names,types);
	}
}
