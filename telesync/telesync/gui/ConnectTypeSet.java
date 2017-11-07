////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import pippin.binder.*;



/**
*/
public class ConnectTypeSet extends TypeSet {

	public ConnectTypeSet() {
		super(new TypeSetElement[] {
				new TypeSetElement("unitName", String.class, true)
			});
	}
}
