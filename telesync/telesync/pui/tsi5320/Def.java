package telesync.pui.tsi5320;

import pippin.pui.*;

public class Def extends PDef {

	private PComponent mRoot;

	public Def() {
	}

	public PComponent getRoot() {
		if (mRoot == null)
			mRoot = new Root(mClient,0);

		return mRoot;
	}


	public PApplianceException createPApplianceException(int exceptionType,
			String msg) {
		return new PApplianceException(msg);
	}


	public PApplianceError createPApplianceError(int exceptionType,
			String msg) {
		return new PApplianceError(msg);
	}
}
