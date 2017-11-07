////
// //
// PippinSoft
//
//

package pippin.pui;

import java.util.*;

public abstract class PDef {
	protected PClient mClient;
	Hashtable mComponents = new Hashtable();



	public void setClient(PClient client) {
		mClient = client;
	}

	public abstract PComponent getRoot();


	public PComponent getComponent(int compID) {
		return (PComponent)mComponents.get(new Integer(compID));
	}

	protected void addComponent(PComponent comp,int id) {
		//System.out.println("PDef.addComponent-" + comp.getName() + "," + id);
		
		mComponents.put(new Integer(id),comp);
		
	}

	public abstract PApplianceException createPApplianceException(
			int exceptionType, String message);

	public abstract PApplianceError createPApplianceError(int exceptionType,
			String message);
}

